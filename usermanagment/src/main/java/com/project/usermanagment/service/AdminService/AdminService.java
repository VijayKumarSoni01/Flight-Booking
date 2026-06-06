package com.project.usermanagment.service.AdminService;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.usermanagment.entity.User;
import com.project.usermanagment.enumFolder.Role;
import com.project.usermanagment.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final UserRepository userRepository;

    public Page<User> getUsers(String search, Role role, int page, int size, String sortBy, String dir) {

        if (page < 0 || size <= 0 || size > 100) {
            throw new IllegalArgumentException("Invalid pagination values");
        }

        List<String> allowed = List.of("id", "email", "username");
        if (!allowed.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field");
        }

        Sort sort = dir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        PageRequest pageable = PageRequest.of(page, size, sort);

        return userRepository.searchUsers(search, role, pageable);
    }

    @Transactional
    public void promoteToAdmin(Long id, String currentEmail) {

        User user = getUser(id);

        if (user.getRoles().contains(Role.ADMIN)) {
            throw new IllegalArgumentException("User already ADMIN");
        }

        log.info("Admin {} promoted user {} (email={})",
                currentEmail, id, user.getEmail());

        user.getRoles().add(Role.ADMIN);

        userRepository.save(user);
    }

    @Transactional
    public void demoteToUser(Long id, String currentEmail) {

        User current = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (current.getId().equals(id)) {
            throw new IllegalArgumentException("You cannot demote yourself");
        }

        User user = getUser(id);

        if (!user.getRoles().contains(Role.ADMIN)) {
            throw new IllegalArgumentException("User is not ADMIN");
        }

        if (countAdmins() == 1) {
            throw new IllegalArgumentException("At least one ADMIN required");
        }

        log.info("Admin {} is demoting user {}", currentEmail, id);

        user.getRoles().remove(Role.ADMIN);

        if (user.getRoles().isEmpty()) {
            user.getRoles().add(Role.USER);
        }

        userRepository.save(user);
    }

    private long countAdmins() {
        return userRepository.countAdmins();
    }

    @Transactional
    public void restoreUser(Long id) {

        User user = getUser(id);

        if (user.isActive()) {
            throw new IllegalArgumentException("User already active");
        }

        user.setActive(true);
        user.setDeletedAt(null);
        user.setDeletedBy(null);

        userRepository.save(user);
    }

    @Transactional
    public void hardDeleteUser(Long id, String currentEmail) {

        User current = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (current.getId().equals(id)) {
            throw new IllegalArgumentException("You cannot delete yourself");
        }

        log.info("Admin {} deleted user {}", currentEmail, id);

        User user = getUser(id);

        userRepository.delete(user);
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}