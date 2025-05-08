package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.exception.CustomException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {
    private final UserRepository userRep;

    @Transactional
    public ResponseEntity<?> createUser(UserDTO userDTO) {
        User user = new User(userDTO.name());
        userRep.save(user);
        return ResponseEntity.ok("Saved successfully");
    }

    @Transactional
    public ResponseEntity<?> getUser(Long id) {
        checkUserExist(id);
        User user = userRep.getReferenceById(id);
        return ResponseEntity.ok(new UserDTO(user.getUserName()));
    }

    @Transactional
    public ResponseEntity<?> updateUser(Long id, UserDTO userDTO) {
        checkUserExist(id);
        User user = userRep.getReferenceById(id);
        user.setUserName(userDTO.name());
        userRep.save(user);
        return ResponseEntity.ok("Changed successfully");
    }

    @Transactional
    public ResponseEntity<?> deleteUser(Long id) {
        userRep.deleteById(id);
        return ResponseEntity.ok("Deleted successfully");
    }

    private void checkUserExist(Long id) {
        if (!userRep.existsById(id))
            throw new CustomException("No user with that ID");
    }
}
