package com.lcwd.electronic.store.dtos;


import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.validate.ImageNameValid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDto {
    private String userId;

    @Size(min = 3,max = 25,message = "Invalid Name")
    private String name;

    @Pattern(regexp ="^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$",message = "Invalid email")
    private String email;

    @Size(min = 1,max = 10,message = "Invalid Password")
    private String password;

    @Size(min = 4,max = 6,message = "Invalid gender")
    private String gender;

    @Size(min = 6,max = 14,message = "Invalid phone number")
    private String phone;

    @NotBlank(message = "About section can't be blank")
    private String about;

    @ImageNameValid
    private String imageName;

    private List<RoleDto> roleList=new ArrayList<>();
}
