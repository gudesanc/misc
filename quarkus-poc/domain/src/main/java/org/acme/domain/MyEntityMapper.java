package org.acme.domain;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface MyEntityMapper {
    MyEntityDTO toDTO(MyEntity entity);
    MyEntity toEntity(MyEntityDTO dto);
    List<MyEntityDTO> toDTOList(List<MyEntity>   entity);
}
