package com.doisproject.userpreview.ServiceLayer;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.doisproject.userpreview.model.Admin;
import com.doisproject.userpreview.model.User;
import com.doisproject.userpreview.repository.AdminRepository;
import com.doisproject.userpreview.repository.UserRepository;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
	
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AdminRepository adminRepository;

	@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    // Check User first
    User user = userRepository.findByEmail(username).orElse(null);
    if (user != null) {
        return user;
    }

    // Check Admin
    Admin admin = adminRepository.findByEmail(username).orElse(null);
    if (admin != null) {
        return admin;
    }

    throw new UsernameNotFoundException("User not found: " + username);
}

		
		
}
