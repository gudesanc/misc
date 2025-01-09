package org.gds.poc.orch.service.protocollo.protocollazione;

import jakarta.validation.constraints.NotBlank;

public record DtoCreaProtocollo(
        @NotBlank
        String oggetto, String mittente, String destinatario) {
}
