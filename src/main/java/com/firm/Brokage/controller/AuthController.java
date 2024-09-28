package com.firm.Brokage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.firm.Brokage.entity.Customer;
import com.firm.Brokage.entity.JwtResponse;
import com.firm.Brokage.entity.LoginRequest;
import com.firm.Brokage.entity.SignupRequest;
import com.firm.Brokage.entity.UserTable;
import com.firm.Brokage.repository.CustomerRepository;
import com.firm.Brokage.repository.UserRepository;
import com.firm.Brokage.service.CustomUserDetailsService;
import com.firm.Brokage.util.JwtTokenUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        UserTable user = new UserTable();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setRole("CUSTOMER");

        userRepository.save(user);
        
        Customer customer = new Customer();
        customer.setName(signupRequest.getUsername());
        customer.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        customer.setEmail(signupRequest.getEmail());
        
        customerRepository.save(customer);
        
        
        return ResponseEntity.ok(customer);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
    	authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new JwtResponse(token));
    }
}
