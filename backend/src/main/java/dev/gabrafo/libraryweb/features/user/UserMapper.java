package dev.gabrafo.libraryweb.features.user;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserRequestDTO toRequestDTO(User user);

    UserResponseDTO toResponseDTO(User user);

    User fromRequestToEntity(UserRequestDTO userRequestDTO);

    User fromResponseToEntity(UserResponseDTO userResponseDTO);
}