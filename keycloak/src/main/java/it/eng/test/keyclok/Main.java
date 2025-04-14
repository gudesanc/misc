package it.eng.test.keyclok;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
//        String url = args[0];
//        String clientId = args[1];
//        String clientSecret = args[2];
//        String realm = args[3];
//        String cf = args[4];
        String serverUrl = "https://muniiam.test.municipia.eng.it/realms/realm-poc-jente2/protocol/openid-connect/auth";
        String clientId = "test-impersonate";
        String clientSecret = "TWUiV6IF35ishVZktBU2Gc1SldGnWflv";
        String realm ="realm-poc-jente2";
        String cf = "DSNGTV";
        // Obtain a Keycloak instance
        try(
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType("client_credentials")
                .build();
        ) {
            RealmResource realmInfo = keycloak.realm(realm);

            System.out.println("Realm: "+realmInfo);
            System.out.println("Clients: "+realmInfo.clients());
//            System.out.println("Client: "+realmInfo.clients().findByClientId(clientId));
            List<UserRepresentation> users = realmInfo.users().list();
            System.out.println("Users: "+users);
            // Find the user to impersonate
            users = keycloak.realm(realm).users().searchByAttributes("codiceFiscale:" + cf);
            System.out.println(users);
                UserRepresentation user = keycloak.realm(realm).users().searchByAttributes("codiceFiscale:"+cf).get(0);

                // Impersonate the user
                Map<String, Object> impersonationToken = keycloak.realm("myrealm").users().get(user.getId()).impersonate();

            // Use the impersonation token as needed
            System.out.println("Impersonation Token: " + impersonationToken);
        }
    }
}
