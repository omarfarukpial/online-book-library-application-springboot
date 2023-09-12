package com.pial.onlinebooklibraryapplication.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

//    private long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String password;
    private String role;

    public void setAccessToken(String s) {
    }
}
