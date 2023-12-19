package com.project.shopapp.services.impl;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Role;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.RoleRepository;
import com.project.shopapp.repositories.UserRepository;
import com.project.shopapp.services.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException {
        // kiểm tra số điện thoại đã tồn tại hay chưa'
        if (userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())){
            throw new DataIntegrityViolationException("Phone number already exists");
        }
//        User user = new User();
//        BeanUtils.copyProperties(userDTO,user);
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .password(userDTO.getPassword())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found!"));
        newUser.setRole(role);

        //Kiểm tra nếu có account Id không yêu cầu password
        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0){
            String password = userDTO.getPassword();
//            String endcodePassword = passwordEncode.encode(password);
//            newUser.setPassword(endcodePassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) {
        return null;
    }
}
