package com.example.store_everything.Controllers;

import com.example.store_everything.DAL.models.ApplicationUser;
import com.example.store_everything.DAL.models.StoredElement;
import com.example.store_everything.DAL.repositories.StoredElementRepository;
import com.example.store_everything.DAL.repositories.UserRepository;
import com.example.store_everything.config.UserDetailsImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.WebRequest;

import java.util.Optional;

@PreAuthorize("hasAuthority('USER')")
@Controller
public class SharedController {

    private final StoredElementRepository storedElementRepository;
    private final UserRepository userRepository;

    public SharedController(StoredElementRepository storedElementRepository, UserRepository userRepository){
        this.storedElementRepository = storedElementRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/shared/shared_with_me")
    public String showAllElements(WebRequest request, Model model){

        String username = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        Optional<ApplicationUser> user = this.userRepository.findByUsername(username);
        if(user.isEmpty()){
            return "redirect:/index";
        }

        model.addAttribute("storedElements", user.get().getStoredElements());

        return "shared";
    }

    @GetMapping("/shared_elements/{link}")
    public String showElement(@PathVariable("link") String elementLink, WebRequest request, Model model){

        Optional<StoredElement> storedElement = this.storedElementRepository.findBySharedLink("http://localhost:8080/shared_elements/" + elementLink);
        if(storedElement.isEmpty()){
            return "redirect:/index";
        }

        model.addAttribute("element", storedElement.get());

        return "shared_element";
    }

}
