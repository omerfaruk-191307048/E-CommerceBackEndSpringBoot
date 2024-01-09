package com.securityVideoProject.security.mapper;

import com.securityVideoProject.security.dto.request.OrderRequestDto;
import com.securityVideoProject.security.dto.request.UserRequestDto;
import com.securityVideoProject.security.dto.response.OrderResponseDto;
import com.securityVideoProject.security.dto.response.UserResponseDto;
import com.securityVideoProject.security.entities.order.Order;
import com.securityVideoProject.security.entities.user.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequestDto userRequestDto);

    UserResponseDto toDto(User user);

    List<UserResponseDto> toDtoList(List<User> users);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User update(UserRequestDto userRequestDto, @MappingTarget User user);
}
