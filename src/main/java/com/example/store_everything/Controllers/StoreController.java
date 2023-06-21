package com.example.store_everything.Controllers;

import com.example.store_everything.DAL.DTOs.StoredElementDTO;
import com.example.store_everything.DAL.models.AccessType;
import com.example.store_everything.DAL.models.ApplicationUser;
import com.example.store_everything.DAL.models.Category;
import com.example.store_everything.DAL.models.StoredElement;
import com.example.store_everything.DAL.repositories.AccessTypeRepository;
import com.example.store_everything.DAL.repositories.CategoryRepository;
import com.example.store_everything.DAL.repositories.StoredElementRepository;
import com.example.store_everything.DAL.repositories.UserRepository;
import com.example.store_everything.config.Filters;
import com.example.store_everything.config.UserDetailsImpl;
import jakarta.validation.Valid;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.*;
import java.util.stream.Collectors;

@PreAuthorize("hasAuthority('FULL_USER')")
@Controller
public class StoreController {

    private final StoredElementRepository storedElementRepository;
    private final UserRepository userRepository;
    private final AccessTypeRepository accessTypeRepository;
    private final CategoryRepository categoryRepository;

    @Value("${email.password}")
    private String emailPassword;

    public StoreController(StoredElementRepository storedElementRepository, UserRepository userRepository, AccessTypeRepository accessTypeRepository, CategoryRepository categoryRepository) {
        this.storedElementRepository = storedElementRepository;
        this.userRepository = userRepository;
        this.accessTypeRepository = accessTypeRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/store")
    public String showAllElements(@ModelAttribute("filters") Filters filters, WebRequest request, Model model) {

        String username = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();


        Optional<ApplicationUser> user = this.userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return "redirect:/index";
        }

        List<StoredElement> elements = user.get().getStoredByUser();

        if(filters == null){
            filters = new Filters();
        }

        if(filters.getCategoryFilter() != null && !filters.getCategoryFilter().equals("all")){
            Filters finalFilters = filters;
            elements = elements.stream().filter(element -> element.getCategory().getTitle().equals(finalFilters.getCategoryFilter())).collect(Collectors.toList());
        }

        if(filters.getDateFilter() != null && !filters.getDateFilter().equals("")){
            Filters finalFilters = filters;
            elements = elements.stream().filter(element -> element.getFormattedDate().equals(finalFilters.getDateFilter())).collect(Collectors.toList());
        }

        if(filters.getAlphabeticalSort() != null){
            if(filters.getAlphabeticalSort().equals("start")){
                elements.sort(Comparator.comparing(StoredElement::getTitle));
            }
            if(filters.getAlphabeticalSort().equals("end")){
                elements.sort((e1, e2) -> e2.getTitle().compareTo(e1.getTitle()));
            }
        }

        if(filters.getCategorySort() != null){
            if(filters.getCategorySort().equals("most")){
                elements.sort((e1, e2) -> e2.getCategory().getStoredElementList().size() - e1.getCategory().getStoredElementList().size());
            }else{
                elements.sort(Comparator.comparingInt(e -> e.getCategory().getStoredElementList().size()));
            }
        }

        model.addAttribute("categories", this.categoryRepository.findAll());
        model.addAttribute("storedElements", elements);
        model.addAttribute("Filters", filters);

        return "store";
    }

    @GetMapping("/store/new")
    public String showNewElementForm(WebRequest request, Model model) {

        StoredElementDTO storedElement = new StoredElementDTO();
        List<AccessType> accessTypeList = this.accessTypeRepository.findAll();

        model.addAttribute("storedElement", storedElement);
        model.addAttribute("accessTypes", accessTypeList);
        model.addAttribute("categories", this.categoryRepository.findAll());
        model.addAttribute("users", this.userRepository.findAll());

        return "new_element";
    }

    @PostMapping("/store/new")
    public String storeNewElement(@Valid @ModelAttribute("storedElement") StoredElementDTO storedElementDTO,
                                  BindingResult result,
                                  WebRequest request,
                                  Model model) throws EmailException {
        if(result.hasErrors()){
            model.addAttribute("categories", this.categoryRepository.findAll());
            model.addAttribute("accessTypes", this.accessTypeRepository.findAll());
            return "new_element";
        }
        AccessType accessType = this.accessTypeRepository.findByTitle(storedElementDTO.getAccessType()).get();
        Category category = this.categoryRepository.findByTitle(storedElementDTO.getCategory()).get();

        List<ApplicationUser> users = new ArrayList<>();
        for (String username : storedElementDTO.getSharedWithUsers()) {
            ApplicationUser user = this.userRepository.findByUsername(username).get();
            users.add(user);
            Email email = new SimpleEmail();
            email.setHostName("smtp.office365.com");
            email.setSmtpPort(587);
            email.setStartTLSEnabled(true);
            email.setAuthenticator(new DefaultAuthenticator("71033@student.pb.edu.pl", emailPassword));
            email.setFrom("71033@student.pb.edu.pl");
            email.setSubject(username + " Zaprosił Cie do obejrzenia nowej notatki w aplikacji store everything!");
            email.setMsg("Message is waiting from: " + username + "\nhttp://localhost:8080/shared/shared_with_me");
            email.addTo(user.getEmail());
            email.send();
        }

        if (storedElementDTO.getAccessType().equals("SHARED VIA LINK")) {
            storedElementDTO.setSharedLink("http://localhost:8080/shared_elements/" + UUID.randomUUID());
        }

        StoredElement storedElement = new StoredElement();
        storedElement.setTitle(storedElementDTO.getTitle());
        storedElement.setDescription(storedElementDTO.getDescription());
        storedElement.setLink(storedElementDTO.getLink());
        storedElement.setSharedLink(storedElementDTO.getSharedLink());
        storedElement.setAccessType(accessType);
        storedElement.setSharedWithUsers(users);
        storedElement.setCategory(category);
        storedElement.setStoredByUser(this.userRepository.findByUsername(((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).get());
        storedElement.setCreationDate(new Date());

        this.storedElementRepository.save(storedElement);

        return "redirect:/store";
    }

    @GetMapping("/store/delete/{id}")
    public String deleteStoredElement(@PathVariable("id") String id) {
        Optional<StoredElement> storedElement = this.storedElementRepository.findById(UUID.fromString(id));
        storedElement.ifPresent(this.storedElementRepository::delete);
        return "redirect:/store";
    }

    @GetMapping("/store/edit/{id}")
    public String showEditStoredElementForm(@PathVariable("id") String id, Model model) {
        Optional<StoredElement> oldStoredElement = this.storedElementRepository.findById(UUID.fromString(id));

        if (oldStoredElement.isEmpty()) {
            return "redirect:/store";
        }

        StoredElement storedElement = oldStoredElement.get();

        StoredElementDTO storedElementDTO = new StoredElementDTO();

        storedElementDTO.setId(storedElement.getId());
        storedElementDTO.setTitle(storedElement.getTitle());
        storedElementDTO.setDescription(storedElement.getDescription());
        storedElementDTO.setCategory(storedElement.getCategory().getTitle());
        storedElementDTO.setLink(storedElement.getLink());
        storedElementDTO.setAccessType(storedElement.getAccessType().getTitle());

        List<String> userUsernames = new ArrayList<>();

        for(ApplicationUser applicationUser : storedElement.getSharedWithUsers()){
            userUsernames.add(applicationUser.getUsername());
        }

        storedElementDTO.setSharedWithUsers(userUsernames);

        model.addAttribute("storedElement", storedElementDTO);
        model.addAttribute("accessTypes", this.accessTypeRepository.findAll());
        model.addAttribute("categories", this.categoryRepository.findAll());
        model.addAttribute("users", this.userRepository.findAll());

        return "store_edit";
    }

    @PostMapping("/store/edit")
    public String editStoredElement(@Valid @ModelAttribute("storedElement") StoredElementDTO storedElementDTO,
                                    BindingResult result,
                                    WebRequest request,
                                    Model model) {
        if(result.hasErrors()){
            return "store_edit";
        }
        Optional<StoredElement> oldStoredElement = this.storedElementRepository.findById(storedElementDTO.getId());

        if (oldStoredElement.isEmpty()) {
            return "redirect:/store";
        }

        AccessType accessType = this.accessTypeRepository.findByTitle(storedElementDTO.getAccessType()).get();
        Category category = this.categoryRepository.findByTitle(storedElementDTO.getCategory()).get();

        StoredElement storedElement = oldStoredElement.get();

        storedElement.setTitle(storedElementDTO.getTitle());
        storedElement.setDescription(storedElementDTO.getDescription());
        storedElement.setCategory(category);
        storedElement.setLink(storedElementDTO.getLink());
        storedElement.setAccessType(accessType);

        List<ApplicationUser> users = new ArrayList<>();

        for(String username : storedElementDTO.getSharedWithUsers()){
            users.add(this.userRepository.findByUsername(username).get());
        }

        storedElement.setSharedWithUsers(users);

        if (storedElementDTO.getAccessType().equals("SHARED VIA LINK")) {
            storedElement.setSharedLink("http://localhost:8080/shared_elements/" + UUID.randomUUID());
        }

        this.storedElementRepository.save(storedElement);

        return "redirect:/store";
    }
}
