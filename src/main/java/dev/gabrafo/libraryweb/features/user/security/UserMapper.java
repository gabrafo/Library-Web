package dev.gabrafo.libraryweb.features.user.security;

import dev.gabrafo.libraryweb.features.user.User;
import dev.gabrafo.libraryweb.features.user.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public class UserMapper {
    public UserDTO toDTO(User user){
        return UserDTO.builder().user(user).build();
    }

    public User toEntity(UserDTO userDTO){
        return User.builder().dto(userDTO).build();
    }
}
