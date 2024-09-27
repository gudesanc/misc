package org.gds.poc.orch.manager.library;

import java.time.LocalDateTime;
import java.util.List;

public record ProcessDetailResponse(String uuid,
                                    BusinessState businessState,
                                    String currentState,
                                    String ctx,
                                    String result,
                                    LocalDateTime processStart,
                                    LocalDateTime processEnd,
                                    List<ProcessEventDetail> events) {
}
