package org.gds.poc.orch.service.protocollo.protocollazione;

public record DtoCreaProtocollo(
        @NotBlank
        String oggetto, String mittente, String destinatario) {
}
