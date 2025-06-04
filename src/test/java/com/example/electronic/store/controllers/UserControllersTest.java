package com.example.electronic.store.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.electronic.store.dtos.UserDto;
import com.example.electronic.store.services.UserServices;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllersTest {

    @MockitoBean
    private UserServices user_service;
    @InjectMocks
    private UserControllers user_controller;

    @Autowired
    private MockMvc mock_mvc;

    @Test
    public void createUserTest() throws Exception {
        UserDto userdto=UserDto.builder()
                .name("Anshu")
                .email("anshu@gmail.com")
                .password("anshu")
                .imageName("anshu.jpg")
                .gender("male")
                .phone("9122578632")
                .about("Sofware Engineer")
                .build();

        Mockito.when(user_service.createUser(Mockito.any())).thenReturn(userdto);

        this.mock_mvc.perform(
                MockMvcRequestBuilders
                        .post("/users/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ConvertObjectToJSON(userdto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists());

    }

    @Test
    public void updateUser() throws Exception {
        UserDto userdto1=UserDto.builder()
                .userId("anshu2211")
                .name("Anshu")
                .email("anshu@gmail.com")
                .password("anshu")
                .imageName("anshu.jpg")
                .gender("male")
                .phone("9122578632")
                .about("Sofware Engineer")
                .build();

        UserDto userdto2=UserDto.builder()
                .userId("anshu2211")
                .name("Akash")
                .email("akash@gmail.com")
                .password("akash")
                .imageName("akash.jpg")
                .gender("male")
                .phone("8122578632")
                .about("Data Analyst")
                .build();

        Mockito.when(user_service.updateUser(Mockito.any(),Mockito.any())).thenReturn(userdto2);
        this.mock_mvc.perform(
                MockMvcRequestBuilders.put("/users/update/"+"anshu2211")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhbnNodUBnbWFpbC5jb20iLCJpYXQiOjE3NDg1OTk4NjgsImV4cCI6MTc0ODYxNzg2OH0._PX640284evEubeglvCAaJtQrxCuNU01jewKkGDMzZ6whoSGJPRrsUH0E7Mz86RKZ9A9QI2iuIYJ2vbEeeZGSg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ConvertObjectToJSON(userdto1))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());
    }


    private String ConvertObjectToJSON(UserDto userdto) {
        try{
            return new ObjectMapper().writeValueAsString(userdto);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
