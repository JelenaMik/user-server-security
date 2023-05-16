package com.example.security.mapper.impl;

import com.example.security.exceptions.UserNotFoundException;
import com.example.security.mapper.UserMapper;
import com.example.security.model.UserDto;
import com.example.security.repository.UserRepository;
import com.example.security.repository.model.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {
    private final UserRepository repository;
    @Override
    public UserDto userTouserDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public User userDtoToUser(UserDto userDto){
        return repository.findById(userDto.getId()).orElseThrow(UserNotFoundException::new);
    }
}
