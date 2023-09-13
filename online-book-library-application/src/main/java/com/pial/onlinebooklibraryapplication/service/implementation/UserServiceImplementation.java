package com.pial.onlinebooklibraryapplication.service.implementation;

import com.pial.onlinebooklibraryapplication.constants.AppConstants;
import com.pial.onlinebooklibraryapplication.dto.UserDto;
import com.pial.onlinebooklibraryapplication.entity.UserEntity;
import com.pial.onlinebooklibraryapplication.exception.EmailAlreadyExistsException;
import com.pial.onlinebooklibraryapplication.exception.UserIdNotFoundException;
import com.pial.onlinebooklibraryapplication.repository.UserRepository;
import com.pial.onlinebooklibraryapplication.service.UserService;
import com.pial.onlinebooklibraryapplication.utils.JWTUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Transactional

public class UserServiceImplementation implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public UserServiceImplementation(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    @Override
    public UserDto createUser(UserDto user) throws Exception {
        if(userRepository.findByEmail(user.getEmail()).isPresent())
            throw new EmailAlreadyExistsException(AppConstants.EMAIL_DUPLICATE);
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setAddress(user.getAddress());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setRole(user.getRole());
        UserEntity storedUserDetails = userRepository.save(userEntity);
        UserDto returnedValue = new ModelMapper().map(storedUserDetails,UserDto.class);
        String accessToken = JWTUtils.generateToken(userEntity.getEmail());
        returnedValue.setAccessToken(AppConstants.TOKEN_PREFIX + accessToken);
        return returnedValue;
    }
    @Override
    public UserDto getUser(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository
                .findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException(AppConstants.USER_NOTFOUND));
        return new ModelMapper().map(userEntity, UserDto.class);
    }
    @Override
    public UserDto getUserByUserId(Long userId) throws Exception {
        UserEntity userEntity = userRepository
                .findByUserId(userId)
                .orElseThrow(()->new UserIdNotFoundException(AppConstants.USER_NOTFOUND));
        return new ModelMapper().map(userEntity, UserDto.class);
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(AppConstants.USER_NOTFOUND));
        return new User(userEntity.getEmail(),userEntity.getPassword(),
                true,true,true,true,new ArrayList<>());
    }
}
