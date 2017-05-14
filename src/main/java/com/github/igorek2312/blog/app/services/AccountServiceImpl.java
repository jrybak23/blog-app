package com.github.igorek2312.blog.app.services;

import com.github.igorek2312.blog.app.error.NotFoundException;
import com.github.igorek2312.blog.app.model.Role;
import com.github.igorek2312.blog.app.model.User;
import com.github.igorek2312.blog.app.repositories.RoleRepository;
import com.github.igorek2312.blog.app.repositories.UserRepository;
import com.github.igorek2312.blog.app.transfer.SignUpForm;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Igor Rybak
 */
@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;

    @Autowired
    public AccountServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            ModelMapper modelMapper
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public User signUp(SignUpForm form) {
        Role role = roleRepository.findByName("ROLE_USER");
        User user = modelMapper.map(form, User.class);
        user.encodePassword(passwordEncoder::encode);
        user.setActivationKey(UUID.randomUUID().toString());
        user.getRoles().add(role);
        return userRepository.saveAndFlush(user);
    }

    @Override
    public void activate(String activationKey) {
        User user = userRepository.findByActivationKey(activationKey)
                .orElseThrow(() -> new NotFoundException("error.no.user.with.such.activation.key"));
        user.setActivated(true);
        userRepository.saveAndFlush(user);
    }

    @Override
    public Optional<User> getUser(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void setResetKey(User user) {
        user.setResetKey(UUID.randomUUID().toString());
        userRepository.saveAndFlush(user);
    }

    @Override
    public void resetPassword(String resetKey, String password) {
        User user = userRepository.findByResetKey(resetKey)
                .orElseThrow(() -> new NotFoundException("error.no.user.with.such.reset.key"));
        user.setPassword(password);
        user.encodePassword(passwordEncoder::encode);
        userRepository.saveAndFlush(user);
    }
}