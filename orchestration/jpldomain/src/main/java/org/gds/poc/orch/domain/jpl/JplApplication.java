package org.gds.poc.orch.domain.jpl;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
			title = "jPL Domain",
			version = "1.0",
			description = "Dominio... jpn"
		),
		servers = { @Server(
				description = "Server sviluppo",
				url = "http://localhost:8180/"
		)}
)
public class JplApplication {

	public static void main(String[] args) {
		SpringApplication.run(JplApplication.class, args);
	}

}
