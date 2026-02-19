package com.delivery.food.user.mapper;

import com.delivery.food.user.domain.User;
import com.delivery.food.user.dto.UserCreateDto;
import com.delivery.food.user.dto.UserResponseDto;
import com.delivery.food.user.dto.UserUpdateDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserResponseDto toResponseDto(User user);

    User toEntity(UserCreateDto dto);

    // For update – only non-null fields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UserUpdateDto dto, @MappingTarget User user);

}
