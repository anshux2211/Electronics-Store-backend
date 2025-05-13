package com.lcwd.electronic.store.services.implementations;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exception.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.ToPageableResponse;
import com.lcwd.electronic.store.repositories.RoleRepository;
import com.lcwd.electronic.store.repositories.UserRepositories;
import com.lcwd.electronic.store.services.UserServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserServices {

    @Autowired
    private UserRepositories user_repo;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository role_repo;


    @Value("${user.profile.image.path}")
    private String image_path;

    @Override
    public UserDto createUser(UserDto user_dto) {
        String userId= UUID.randomUUID().toString();
        user_dto.setUserId(userId);
        // Encoding Password
        user_dto.setPassword(passwordEncoder.encode(user_dto.getPassword()));
        User user=dto_to_user(user_dto);

        Role curr_role=role_repo.findByName("ROLE_NORMAL").orElseThrow(()-> new ResourceNotFoundException("Role Doesn't exist !!"));
        user.setRoleList(List.of(curr_role));

        User user1=user_repo.save(user);

        UserDto user_dto1=user_to_dto(user);
        return user_dto1;

    }

    @Override
    public UserDto updateUser(UserDto user_dto, String userId) {
        User user=dto_to_user(user_dto);

        User old_user=user_repo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User with provided user ID cannot be found"));
        old_user.setAbout(user.getAbout());
        old_user.setEmail(user.getEmail());
        old_user.setPhone(user.getPhone());
        old_user.setPassword(passwordEncoder.encode(user.getPassword()));
        old_user.setGender(user.getGender());
        old_user.setImageName(user.getImageName());
        old_user.setName(user.getName());

        User user1=user_repo.save(old_user);

        UserDto user_dto1=user_to_dto(user1);
        return user_dto1;

    }

    @Override
    public void deleteUser(String userId) {
        User user=user_repo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User with provided user ID cannot be found"));

        String image_name=user.getImageName();
        String full_Path=image_path+ File.separator+image_name;

        try {
            Path path = Paths.get(full_Path);
            Files.deleteIfExists(path);
        } catch (IOException ex){
            ex.printStackTrace();
        }

        user_repo.delete(user);
    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort=Sort.by(sortBy);
        if(sortDir.equalsIgnoreCase("DESC"))
            sort=sort.descending();

        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);

        Page<User> page=user_repo.findAll(pageable);

        PageableResponse<UserDto> resp= ToPageableResponse.getPageableResponse(page,UserDto.class);
        return resp;
    }

    @Override
    public UserDto getSingleUser(String userId) {
        User user=user_repo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User with provided user ID cannot be found"));

        UserDto userdto1=user_to_dto(user);
        return userdto1;
    }

    @Override
    public UserDto getUserByMail(String userMail) {
        User user=user_repo.findByEmail(userMail).orElseThrow(()->new ResourceNotFoundException("User with provided email cannot be found"));

        UserDto userdto1=user_to_dto(user);
        return userdto1;
    }

    @Override
    public UserDto getUserByPhone(String userPhone) {
        User user=user_repo.findByPhone(userPhone).orElseThrow(()->new ResourceNotFoundException("User with provided email cannot be found"));

        UserDto userdto1=user_to_dto(user);
        return userdto1;
    }

    @Override
    public PageableResponse<UserDto> searchUser(String name, int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort=Sort.by(sortBy);
        if(sortDir.equalsIgnoreCase("desc"))
                sort=sort.descending();

        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<User> page=user_repo.findByNameContaining(name,pageable);
        PageableResponse<UserDto> resp=ToPageableResponse.getPageableResponse(page,UserDto.class);
        return resp;
    }

    @Override
    public void addImageName(String userId, String imageName) {
        User user=user_repo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User with given Id is not present"));
        user.setImageName(imageName);
        user_repo.save(user);
    }


    User dto_to_user(UserDto userdto){
//        User user=User.builder()
//                .userId(userdto.getUserId())
//                .name(userdto.getName())
//                .about(userdto.getAbout())
//                .email(userdto.getEmail())
//                .password(userdto.getPassword())
//                .phone(userdto.getPhone())
//                .imageName(userdto.getImageName())
//                .gender(userdto.getGender())
//                .build();
        return mapper.map(userdto,User.class);
    }

        UserDto user_to_dto(User user){
//            UserDto userDto=UserDto.builder()
//                    .userId(user.getUserId())
//                    .about(user.getAbout())
//                    .email(user.getEmail())
//                    .phone(user.getPhone())
//                    .gender(user.getGender())
//                    .name(user.getName())
//                    .imageName(user.getImageName())
//                    .password(user.getPassword())
//                    .build();

            return mapper.map(user,UserDto.class);
        }

}
