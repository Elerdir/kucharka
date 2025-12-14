package cz.niderle.server.auth.service;

import cz.niderle.server.auth.dto.Users;
import cz.niderle.server.auth.model.UserEntity;
import cz.niderle.server.auth.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean registerUser(String username, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            return false;
        }
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(UserEntity.fromDto(user));
        return true;
    }

    public Users findByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username).orElse(null);

        assert userEntity != null;
        return Users.fromEntity(userEntity);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
