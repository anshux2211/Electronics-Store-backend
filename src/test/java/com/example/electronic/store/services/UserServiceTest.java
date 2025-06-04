package com.example.electronic.store.services;

import com.example.electronic.store.dtos.PageableResponse;
import com.example.electronic.store.dtos.UserDto;
import com.example.electronic.store.entities.Role;
import com.example.electronic.store.entities.User;
import com.example.electronic.store.repositories.RoleRepository;
import com.example.electronic.store.repositories.UserRepositories;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @MockitoBean
    private UserRepositories userRepositories;
    @MockitoBean
    private RoleRepository roleRepository;


    @Autowired
    private UserServices user_service;

    @Autowired
    private ModelMapper mapper;

    User user;
    Role role;
    String roleName;

    @BeforeEach
    public void init(){
        role=Role.builder().id("abc").name("GUEST").build();

        user=User.builder()
                .name("Anshu")
                .email("anshu@gmail.com")
                .password("anshu")
                .imageName("anshu.jpg")
                .gender("male")
                .phone("6988778122")
                .about("Software Developer")
                .roleList(List.of(role))
                .build();

        roleName="GUEST";
    }

    @Test
    public void createUserTest(){
        Mockito.when(userRepositories.save(Mockito.any())).thenReturn(user);
        Mockito.when(roleRepository.findByName(Mockito.any())).thenReturn(Optional.of(role));

        UserDto userDto=user_service.createUser(mapper.map(user,UserDto.class));
        System.out.println(userDto.getName());

        Assertions.assertNotNull(userDto);

    }

    @Test
    public void updateUser_Test(){
        UserDto userDto=UserDto.builder()
                .name("Anshu Kuamr Mehta")
                .email("anshu.mehta@gmail.com")
                .gender("male")
                .phone("9190122536")
                .about("B.Tech Student")
                .imageName("mehta.jpg")
                .build();

        Mockito.when(userRepositories.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(userRepositories.save(Mockito.any())).thenReturn(user);

        UserDto updaterUser=user_service.updateUser(userDto,"anshu");
        System.out.println(updaterUser.getName());
        Assertions.assertEquals(userDto.getUserId(),updaterUser.getUserId());
        Assertions.assertEquals(userDto.getName(),updaterUser.getName());
        Assertions.assertEquals(userDto.getEmail(),updaterUser.getEmail());
        Assertions.assertEquals(userDto.getAbout(),updaterUser.getAbout());
        Assertions.assertEquals(userDto.getPassword(),updaterUser.getPassword());
    }

    @Test
    public void deleterUser_Test(){
        String userId="abchd";
        Mockito.when(userRepositories.findById(userId)).thenReturn(Optional.of(user));
        user_service.deleteUser(userId);
        Mockito.verify(userRepositories,Mockito.times(1)).delete(user);
    }

    @Test
    public void getAllUser_Test(){
        User user1=User.builder()
                .name("Rocky")
                .email("rocky@gmail.com")
                .password("rocky")
                .imageName("rocky.jpg")
                .gender("male")
                .phone("69856778122")
                .about("Software Developer")
                .roleList(List.of(role))
                .build();
        User user2=User.builder()
                .name("Harsh")
                .email("harsh@gmail.com")
                .password("harsh")
                .imageName("harsh.jpg")
                .gender("male")
                .phone("6988678122")
                .about("Software Developer")
                .roleList(List.of(role))
                .build();


        List<User> lst= Arrays.asList(user,user1,user2);
        Page<User> page=new PageImpl<>(lst);
        Mockito.when(userRepositories.findAll((Pageable) Mockito.any())).thenReturn(page);

        PageableResponse<UserDto> response=user_service.getAllUser(1,10,"name","ASC");

        System.out.println(response.getContent());
        Assertions.assertEquals(3, response.getContent().size());
    }

    @Test
    public void getSingleUser_Test(){
        Mockito.when(userRepositories.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        UserDto resp=user_service.getSingleUser("abcd");
        Assertions.assertEquals(user.getName(),resp.getName());
        Assertions.assertEquals(user.getEmail(),resp.getEmail());
    }

    @Test
    public void getUserByEmail_Test(){
        Mockito.when(userRepositories.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
        UserDto resp=user_service.getUserByMail("abcd");
        Assertions.assertEquals(user.getName(),resp.getName());
        Assertions.assertEquals(user.getEmail(),resp.getEmail());
    }

    @Test
    public void getUserByPhone_Test(){
        Mockito.when(userRepositories.findByPhone(Mockito.anyString())).thenReturn(Optional.of(user));
        UserDto resp=user_service.getUserByPhone("abcd");
        Assertions.assertEquals(user.getName(),resp.getName());
        Assertions.assertEquals(user.getEmail(),resp.getEmail());
    }

    @Test
    public void searchUser_Test(){
        List<User> lst= Arrays.asList(user);
        Page<User> page=new PageImpl<>(lst);
        Mockito.when(userRepositories.findByNameContaining(Mockito.anyString(),(Pageable) Mockito.any())).thenReturn(page);

        PageableResponse<UserDto> response=user_service.searchUser("Anshu",1,10,"name","ASC");

        System.out.println(response.getContent());
        Assertions.assertEquals(1, response.getContent().size());
    }

    @Test
    public void addImageName_Test(){
        Mockito.when(userRepositories.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        user_service.addImageName("abs","new_image.png");
        Mockito.verify(userRepositories,Mockito.times(1)).save(user);
        System.out.println(user.getImageName());
    }


}
