package com.example.service;


import com.example.model.Role;
import com.example.model.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    //    @Autowired
//    private UserDaoImpl userDao;
//
//    @Autowired
//    private RoleDao roleDao;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    @Override
    public void addUser(User user, List<String> rolesValues) {
        Set<Role> roles = new HashSet<>();
        for (String role: rolesValues) {
            if (roleRepository.countRoleByRole(role) > 0) {
                roles.add(roleRepository.getRole(role));
            } else {
                Role newRole = new Role();
                newRole.setRole(role);
                roleRepository.save(newRole);
                roles.add(newRole);
            }
        }
        user.setRoles(roles);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }



    @Transactional
    @Override
    public void removeUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public User getUserById(Long id) {
        return userRepository.getOne(id);
    }
    @Transactional(readOnly = true)
    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

}

