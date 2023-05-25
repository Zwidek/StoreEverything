package com.example.store_everything.Controllers;

import com.example.store_everything.DAL.DTOs.CategoryDTO;
import com.example.store_everything.DAL.models.Category;
import com.example.store_everything.DAL.repositories.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    @PreAuthorize("hasAuthority('FULL_USER')")
    @GetMapping("/categories")
    public String showCategoriesTable(WebRequest request, Model model){
        List<Category> categoryList = categoryRepository.findAll();
        model.addAttribute("categories", categoryList);
        return "categories";
    }

    @PreAuthorize("hasAuthority('FULL_USER')")
    @GetMapping("/categories/new")
    public String showNewCategoryForm(WebRequest request, Model model){
        Category category = new Category();
        model.addAttribute("category", category);
        return "new_category";
    }

    @PreAuthorize("hasAuthority('FULL_USER')")
    @PostMapping("/categories/new")
    public String addNewCategory(@Valid @ModelAttribute("category") Category category,
                                 BindingResult result,
                                 Model model){
        if(result.hasErrors()){
            return "new_category";
        }

        Optional<Category> oldCategory = this.categoryRepository.findByTitle(category.getTitle().toLowerCase());

        if(oldCategory.isPresent()){
            model.addAttribute("categoryError", "Category with same title already exists");
            return "new_category";
        }

        category.setTitle(category.getTitle().toLowerCase());

        this.categoryRepository.save(category);

        return "redirect:/categories";
    }

    @PreAuthorize("hasAuthority('FULL_USER')")
    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") String id) {
        Optional<Category> category = this.categoryRepository.findById(UUID.fromString(id));
        category.ifPresent(this.categoryRepository::delete);
        return "redirect:/categories";
    }

    @PreAuthorize("hasAuthority('FULL_USER')")
    @GetMapping("/categories/update/{id}")
    public String showEditCategoryForm(@PathVariable("id") String id, Model model) {
        Optional<Category> oldCategory = this.categoryRepository.findById(UUID.fromString(id));

        if (oldCategory.isEmpty()) {
            return "redirect:/categories";
        }

        Category category = oldCategory.get();

        CategoryDTO categoryDTO = new CategoryDTO();

        categoryDTO.setId(category.getId());
        categoryDTO.setTitle(category.getTitle());

        model.addAttribute("category", categoryDTO);

        return "category_update";
    }

    @PreAuthorize("hasAuthority('FULL_USER')")
    @PostMapping("/categories/update")
    public String editCategory(@Valid @ModelAttribute("category") CategoryDTO categoryDTO,
                               BindingResult result,
                               WebRequest request,
                               Model model)
    {
        if(result.hasErrors()){
            return "category_update";
        }
        Optional<Category> oldCategory = this.categoryRepository.findById(categoryDTO.getId());

        if (oldCategory.isEmpty()) {
            return "redirect:/categories";
        }

        Category category = oldCategory.get();

        category.setTitle(categoryDTO.getTitle());

        this.categoryRepository.save(category);

        return "redirect:/categories";
    }

}
