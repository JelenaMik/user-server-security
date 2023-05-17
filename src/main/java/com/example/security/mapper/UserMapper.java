package com.example.security.mapper;

import com.example.security.mapper.impl.UserMapperImpl;
import com.example.security.model.UserDto;
import com.example.security.repository.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserMapper {
    public UserDto  userTouserDto(User user);
    public User userDtoToUser(UserDto userDto);
}
