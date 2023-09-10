package com.pial.onlinebooklibraryapplication.service;

import com.pial.onlinebooklibraryapplication.dto.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto) throws Exception;
    UserDto getUser(String email);
    UserDto getUserByUserId(Long id) throws Exception;
}
