package org.gds.poc.orch.service.protocollo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
			title = "Protocollo Service",
			version = "1.0",
			description = "Servizio... protocllo"
		),
		servers = { @Server(
				description = "Server sviluppo",
				url = "http://localhost:8280/"
		)}
)
public class ProtocolloApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProtocolloApplication.class, args);
	}

}
