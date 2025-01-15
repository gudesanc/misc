package org.acme.domain;

public record MyEntityDTO(Long id ,String field) {
    public MyEntityDTO(String field) {
        this(null, field);
    }

}
