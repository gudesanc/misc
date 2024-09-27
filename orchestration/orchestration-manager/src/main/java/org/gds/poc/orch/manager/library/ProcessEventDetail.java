package org.gds.poc.orch.manager.library;

import java.time.LocalDateTime;

public record ProcessEventDetail(LocalDateTime eventDate,
                                 BusinessState bsSource,
                                 BusinessState bsTarget,
                                 String ssmSource,
                                 String ssmTarget) {
}
