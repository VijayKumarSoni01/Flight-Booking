package com.project.usermanagment.service.AdminService;

import java.util.List;
import java.util.Objects;

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

    public Page<User> getUsers(String search, Role role,
                               int page, int size,
                               String sortBy, String dir) {

        if (page < 0 || size <= 0 || size > 100) {
            throw new IllegalArgumentException("Invalid pagination values");
        }

        List<String> allowed = List.of("id", "email", "username");
        if (!allowed.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }

        Sort sort = dir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        return userRepository.searchUsers(search, role, PageRequest.of(page, size, sort));
    }

    @Transactional
    public void promoteToAdmin(Long id, String currentEmail) {

        User user = getUser(id);

        if (user.getRoles().contains(Role.ADMIN)) {
            throw new IllegalArgumentException("User is already an ADMIN");
        }

        user.getRoles().add(Role.ADMIN);

        // ✅ FIX: Don't assign save() return — avoids null conversion warning
        //    save() persists in-place; the managed entity is already updated.
        userRepository.save(user);

        log.info("Admin {} promoted user {} (email={})", currentEmail, id, user.getEmail());
    }

    @Transactional
    public void demoteToUser(Long id, String currentEmail) {

        User current = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found: " + currentEmail));

        // ✅ FIX line 127: Objects.equals() handles null getId() safely
        //    current.getId().equals(id) throws NPE if getId() returns null
        if (Objects.equals(current.getId(), id)) {
            throw new IllegalArgumentException("You cannot demote yourself");
        }

        User user = getUser(id);

        if (!user.getRoles().contains(Role.ADMIN)) {
            throw new IllegalArgumentException("User is not an ADMIN");
        }

        if (countAdmins() <= 1) {
            throw new IllegalArgumentException("Cannot demote: at least one ADMIN required");
        }

        user.getRoles().remove(Role.ADMIN);

        if (user.getRoles().isEmpty()) {
            user.getRoles().add(Role.USER);
        }

        userRepository.save(user);

        log.info("Admin {} demoted user {}", currentEmail, id);
    }

    @Transactional
    public void restoreUser(Long id) {

        User user = getUser(id);

        if (user.isActive()) {
            throw new IllegalArgumentException("User is already active");
        }

        user.setActive(true);
        user.setDeletedAt(null);
        user.setDeletedBy(null);

        userRepository.save(user);

        log.info("User {} restored", id);
    }

    @Transactional
    public void hardDeleteUser(Long id, String currentEmail) {

        User current = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found: " + currentEmail));

        // ✅ FIX line 131: same Objects.equals() fix
        if (Objects.equals(current.getId(), id)) {
            throw new IllegalArgumentException("You cannot delete yourself");
        }

        User user = getUser(id);

        userRepository.delete(user);

        log.info("Admin {} hard-deleted user {}", currentEmail, id);
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    private long countAdmins() {
        return userRepository.countAdmins();
    }
}