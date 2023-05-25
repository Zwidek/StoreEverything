package com.example.store_everything.Controllers;

import com.example.store_everything.DAL.models.ApplicationUser;
import com.example.store_everything.DAL.repositories.RoleRepository;
import com.example.store_everything.DAL.repositories.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class UsersController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UsersController(UserRepository userRepository, RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    public String showUsersTable(WebRequest request, Model model){
        List<ApplicationUser> userList = userRepository.findAll();
        model.addAttribute("users", userList);
        return "users";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users/edit/{user_id}")
    public String showEditUserForm(@PathVariable(value="user_id") String id, WebRequest request, Model model){

        Optional<ApplicationUser> user = this.userRepository.findById(UUID.fromString(id));

        if(user.isEmpty()){
            return "users";
        }

        model.addAttribute("user", user.get());
        model.addAttribute("roles", this.roleRepository.findAll());
        return "edit_user";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/users/edit")
    public String saveUser(@ModelAttribute("user") ApplicationUser applicationUser){

        Optional<ApplicationUser> oldUser = this.userRepository.findByUsername(applicationUser.getUsername());
        if(oldUser.isEmpty()){
            return "redirect:/users/edit/" + applicationUser.getId();
        }

        if(applicationUser.getRoles().size() == 0){
            return "redirect:/users/edit/" + applicationUser.getId();
        }

        oldUser.get().setRoles(applicationUser.getRoles());
        this.userRepository.save(oldUser.get());

        return "redirect:/users";
    }

}
