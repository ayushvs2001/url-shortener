package com.ayush.short_url_service.mapper;

import com.ayush.short_url_service.dto.UserDto;
import com.ayush.short_url_service.entities.User;
import org.springframework.stereotype.Controller;

@Controller
public class UserMapper {
    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto(user.getId(), user.getName());
        return userDto;
    }
}
