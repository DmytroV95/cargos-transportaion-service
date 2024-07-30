//package com.varukha.userservice.security;
//
//import com.varukha.onlinebookstore.repository.user.UserRepository;
//import java.util.NoSuchElementException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//    private final UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username)
//            throws UsernameNotFoundException {
//        return userRepository.findUserByEmail(username)
//                .orElseThrow(() -> new NoSuchElementException("Can't find user by email"));
//    }
//}
