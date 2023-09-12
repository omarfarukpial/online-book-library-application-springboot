package com.pial.onlinebooklibraryapplication.service;

import com.pial.onlinebooklibraryapplication.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    UserDto createUser(UserDto userDto) throws Exception;
    UserDto getUserByUserId(Long id) throws Exception;
    UserDto getUser(String email) throws Exception;
    UserDetails loadUserByUsername(String email) throws Exception;
}
