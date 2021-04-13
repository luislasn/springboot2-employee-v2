package projectengeneeringv2.springboot2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import projectengeneeringv2.springboot2.repository.EmployeeUserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeUserDetailsService implements UserDetailsService {

    private final EmployeeUserRepository employeeUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return Optional.ofNullable(employeeUserRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("Employee User not found"));
    }
}
