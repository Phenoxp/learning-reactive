package com.phenoxp.reactive.userservice.mapper;

import com.phenoxp.reactive.userservice.dto.UserDto;
import com.phenoxp.reactive.userservice.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto map(User user);
    User map(UserDto userDto);
}
