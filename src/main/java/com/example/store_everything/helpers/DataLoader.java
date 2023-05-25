package com.example.store_everything.helpers;

import com.example.store_everything.DAL.models.AccessType;
import com.example.store_everything.DAL.models.Role;
import com.example.store_everything.DAL.repositories.AccessTypeRepository;
import com.example.store_everything.DAL.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final AccessTypeRepository accessTypeRepository;

    public DataLoader(RoleRepository roleRepository, AccessTypeRepository accessTypeRepository){
        this.roleRepository = roleRepository;
        this.accessTypeRepository = accessTypeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadRolesData();
        loadAccessTypesData();
    }

    private void loadRolesData(){
        if(roleRepository.count() > 0) return;

        roleRepository.save(new Role("USER"));
        roleRepository.save(new Role("FULL_USER"));
        roleRepository.save(new Role("ADMIN"));
    }

    private void loadAccessTypesData(){
        if(accessTypeRepository.count() > 0) return;

        accessTypeRepository.save(new AccessType("PRIVATE"));
        accessTypeRepository.save(new AccessType("PUBLIC"));
        accessTypeRepository.save(new AccessType("SHARED VIA LINK"));
        accessTypeRepository.save(new AccessType("SHARED WITH USERS"));
    }
}
