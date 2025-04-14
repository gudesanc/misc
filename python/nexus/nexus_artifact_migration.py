import requests
import os

def get_user_input(prompt, default=None, is_sensitive=False):
    """Helper function to get input with an optional default value."""
    if default:
        prompt = f"{prompt} [Default: {default}]: "
    else:
        prompt = f"{prompt}: "
    
    user_input = input(prompt)
    if is_sensitive and not user_input:
        return default
    return user_input.strip() or default

def get_artifacts_from_source(source_url, source_repo, source_username, source_password, group_id):
    print("Fetching artifacts from source Nexus...")

    search_url = f"{source_url}/service/rest/v1/search"
    params = {
        "repository": source_repo,
        "group": group_id,
    }
    response = requests.get(search_url, auth=(source_username, source_password), params=params)
    response.raise_for_status()

    artifacts = response.json().get("items", [])
    return artifacts

def download_artifact(artifact, source_username, source_password, local_download_dir, dry_run):
    print(f"Downloading artifact: {artifact['name']} version: {artifact['version']}")

    if dry_run:
        print(f"[DRY-RUN] Skipping download for artifact: {artifact['name']} version: {artifact['version']}")
        return None

    download_url = artifact["assets"][0]["downloadUrl"]
    local_file_path = os.path.join(local_download_dir, artifact["name"])

    os.makedirs(os.path.dirname(local_file_path), exist_ok=True)

    response = requests.get(download_url, auth=(source_username, source_password), stream=True)
    response.raise_for_status()

    with open(local_file_path, "wb") as file:
        for chunk in response.iter_content(chunk_size=8192):
            file.write(chunk)

    return local_file_path

def upload_artifact_to_target(file_path, artifact, target_url, target_repo, target_username, target_password, group_id, dry_run):
    print(f"Uploading artifact: {artifact['name']} version: {artifact['version']} to target Nexus...")

    if dry_run:
        print(f"[DRY-RUN] Skipping upload for artifact: {artifact['name']} version: {artifact['version']}")
        return

    upload_url = f"{target_url}/service/rest/v1/components?repository={target_repo}"
    files = {
        "raw.asset1": open(file_path, "rb"),
        "raw.asset1.filename": (None, artifact["name"]),
        "raw.directory": (None, f"{group_id.replace('.', '/')}/{artifact['name']}"),
    }

    response = requests.post(upload_url, auth=(target_username, target_password), files=files)
    response.raise_for_status()

def main():
    # Prompt the user for input parameters
    print("=== Nexus Artifact Migrator ===")
    source_url = get_user_input("Enter the source Nexus base URL")
    source_repo = get_user_input("Enter the source Nexus repository name")
    source_username = get_user_input("Enter the source Nexus username")
    source_password = get_user_input("Enter the source Nexus password (will not be displayed)", is_sensitive=True)
    target_url = get_user_input("Enter the target Nexus base URL")
    target_repo = get_user_input("Enter the target Nexus repository name")
    target_username = get_user_input("Enter the target Nexus username")
    target_password = get_user_input("Enter the target Nexus password (will not be displayed)", is_sensitive=True)
    group_id = get_user_input("Enter the group ID to migrate")
    local_download_dir = get_user_input("Enter the local directory to temporarily store artifacts", default="artifacts")
    dry_run_input = get_user_input("Enable dry-run mode? (yes/no)", default="no")
    dry_run = dry_run_input.lower() in ["yes", "y", "true"]

    try:
        artifacts = get_artifacts_from_source(
            source_url=source_url,
            source_repo=source_repo,
            source_username=source_username,
            source_password=source_password,
            group_id=group_id,
        )
        if not artifacts:
            print("No artifacts found.")
            return

        for artifact in artifacts:
            try:
                file_path = download_artifact(
                    artifact=artifact,
                    source_username=source_username,
                    source_password=source_password,
                    local_download_dir=local_download_dir,
                    dry_run=dry_run,
                )
                if file_path or dry_run:
                    upload_artifact_to_target(
                        file_path=file_path,
                        artifact=artifact,
                        target_url=target_url,
                        target_repo=target_repo,
                        target_username=target_username,
                        target_password=target_password,
                        group_id=group_id,
                        dry_run=dry_run,
                    )
            except Exception as e:
                print(f"Error processing artifact {artifact['name']} version {artifact['version']}: {e}")
            finally:
                # Cleanup local files after upload if not in dry-run mode
                if file_path and os.path.exists(file_path) and not dry_run:
                    os.remove(file_path)

        print("Artifact migration completed.")
    except Exception as e:
        print(f"An error occurred: {e}")

if __name__ == "__main__":
    main()