package com.example.store_everything;

import com.example.store_everything.DAL.models.ApplicationUser;
import com.example.store_everything.DAL.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserEntityRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void returnDefaultAdminUser() {
        Optional<ApplicationUser> admin = userRepository.findByUsername("admin");

        Assertions.assertTrue(admin.isPresent());
    }

    @Test
    void returnUserThatDoesntExist() {
        Optional<ApplicationUser> admin = userRepository.findByUsername("notExists");

        Assertions.assertFalse(admin.isPresent());
    }

    @Test
    void saveUserToDb() {
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setId(1L);
        applicationUser.setEmail("test@o2.pl");
        applicationUser.setName("Test");
        applicationUser.setPassword("$2a$12$31fmn4cF235a4W81//Dvm./YrbQXKaQhYpN/PfGAIBPGTNfBbAdDm");
        applicationUser.setUsername("test");
        applicationUser.setSurname("Test");
        applicationUser.setAge(19);
        userRepository.save(applicationUser);

        Optional<ApplicationUser> test = userRepository.findByUsername("test");


        Assertions.assertTrue(test.isPresent());
    }

    @Test
    void updateUser() {
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setId(1L);
        applicationUser.setEmail("test@o2.pl");
        applicationUser.setName("Test");
        applicationUser.setPassword("$2a$12$31fmn4cF235a4W81//Dvm./YrbQXKaQhYpN/PfGAIBPGTNfBbAdDm");
        applicationUser.setUsername("test");
        applicationUser.setSurname("Test");
        applicationUser.setAge(19);
        userRepository.save(applicationUser);

        applicationUser.setAge(30);
        userRepository.save(applicationUser);

        Optional<ApplicationUser> test = userRepository.findByUsername("test");
        Assertions.assertEquals(30, test.get().getAge());
    }

}
