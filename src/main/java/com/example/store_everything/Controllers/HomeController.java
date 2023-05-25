package com.example.store_everything.Controllers;

import com.example.store_everything.DAL.models.StoredElement;
import com.example.store_everything.DAL.repositories.StoredElementRepository;
import com.example.store_everything.DAL.repositories.UserRepository;
import com.example.store_everything.config.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private final StoredElementRepository storedElementRepository;
    private final UserRepository userRepository;

    public HomeController(StoredElementRepository storedElementRepository, UserRepository userRepository){
        this.storedElementRepository = storedElementRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/index")
    public String homePage(WebRequest request, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getPrincipal().equals("anonymousUser")){

            List<StoredElement> storedElements = new ArrayList<>();
            storedElements.addAll(this.storedElementRepository.findByAccessType_Title("PUBLIC"));
            storedElements.addAll(this.userRepository.findByUsername(((UserDetailsImpl)authentication.getPrincipal()).getUsername()).get().getStoredElements());
            model.addAttribute("storedElements", storedElements);
        }
        return "index";
    }

}
