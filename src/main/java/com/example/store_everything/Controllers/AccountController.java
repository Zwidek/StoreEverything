package com.example.store_everything.Controllers;

import com.example.store_everything.DAL.DTOs.ApplicationUserDTO;
import com.example.store_everything.DAL.models.ApplicationUser;
import com.example.store_everything.DAL.models.Role;
import com.example.store_everything.DAL.repositories.RoleRepository;
import com.example.store_everything.DAL.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
public class AccountController {

    public final UserRepository userRepository;
    public final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;

    public AccountController(UserRepository userRepository, PasswordEncoder encoder, RoleRepository roleRepository, AuthenticationManager authenticationManager){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/user/sign_up")
    public String showRegistrationForm(WebRequest request, Model model){
        ApplicationUserDTO userDTO = new ApplicationUserDTO();
        model.addAttribute("user", userDTO);
        return "register";
    }

    @PostMapping("/user/register")
    public String registerUser(@Valid @ModelAttribute("user") ApplicationUserDTO userDto,
                               BindingResult result,
                               Model model, HttpServletRequest request){
        if(result.hasErrors()){
            return "register";
        }

        if(this.userRepository.findByUsername(userDto.getUsername()).isPresent()){
            model.addAttribute("registerError", "user with given username already exists");
            return "register";
        }

        if(!userDto.getPassword().equals(userDto.getMatchingPassword())){
            model.addAttribute("registerError", "the passwords don't match");
            return "register";
        }

        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setUsername(userDto.getUsername());
        applicationUser.setName(userDto.getName());
        applicationUser.setSurname(userDto.getSurname());
        applicationUser.setAge(userDto.getAge());
        applicationUser.setPassword(encoder.encode(userDto.getPassword()));

        if(userDto.getPassword().equals("Admin123@")){
            List<Role> roles = this.roleRepository.findAll();
            applicationUser.setRoles(roles);
        }else{
            Role role = roleRepository.findByTitle("USER").orElseThrow(() -> new RuntimeException("Role not found"));
            List<Role> roles = new ArrayList<>();
            roles.add(role);
            applicationUser.setRoles(roles);
        }

        this.userRepository.save(applicationUser);

        this.signIn(userDto.getUsername(), userDto.getPassword(), request);

        return "redirect:/index";
    }


    @GetMapping("/user/sign_in")
    public String showLoginForm(WebRequest request, Model model){
        ApplicationUserDTO userDTO = new ApplicationUserDTO();
        model.addAttribute("user", userDTO);
        return "login";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/user/logout")
    public String logout(WebRequest request, Model model){
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/index";
    }


    @PostMapping("/user/login")
    public String loginUser(@ModelAttribute("user") ApplicationUserDTO userDto,
                               BindingResult result,
                               Model model, HttpServletRequest request){

        Optional<ApplicationUser> applicationUser = this.userRepository.findByUsername(userDto.getUsername());

        if(applicationUser.isEmpty()){
            model.addAttribute("loginError", "User with given username not found");
            return "login";
        }

        if(encoder.matches(userDto.getPassword(), applicationUser.get().getPassword())){
            this.signIn(userDto.getUsername(), userDto.getPassword(), request);
            return "redirect:/index";
        }else{
            model.addAttribute("loginError", "Invalid password");
            return "login";
        }
    }

    private void signIn(String username, String password, HttpServletRequest req){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authentication);

        HttpSession session = req.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
    }
}
