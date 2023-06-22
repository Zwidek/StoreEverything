package com.example.store_everything;

import com.example.store_everything.DAL.models.ApplicationUser;
import com.example.store_everything.DAL.repositories.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserEntityRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @ParameterizedTest
    @DisplayName("Invalid emails")
    @ValueSource(strings = {"example.example.com", "aa.pl", "a12domena.com.pl", "asonion"})
    void invalidEmails(String email) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setId(1L);
        applicationUser.setEmail(email);
        applicationUser.setName("Test");
        applicationUser.setPassword("$2a$12$31fmn4cF235a4W81//Dvm./YrbQXKaQhYpN/PfGAIBPGTNfBbAdDm");
        applicationUser.setUsername("test");
        applicationUser.setSurname("Test");
        applicationUser.setAge(20);

        Set<ConstraintViolation<ApplicationUser>> violations = validator.validate(applicationUser);

        assertEquals(1, violations.size(), "Expected one constraint violation");
        ConstraintViolation<ApplicationUser> violation = violations.iterator().next();
        assertEquals("email", violation.getPropertyPath().toString(), "Expected violation on 'email' property");
        System.out.println(violation);
        assertEquals("musi być poprawnie sformatowanym adresem e-mail", violation.getMessage(), "Expected violation message 'musi być poprawnie sformatowanym adresem e-mail'");
    }
    @Test
    @DisplayName("Should return admin user")
    void returnDefaultAdminUser() {
        Optional<ApplicationUser> admin = userRepository.findByUsername("admin");

        Assertions.assertTrue(admin.isPresent());
    }

    @Test
    @DisplayName("User doesnt exist")
    void userIsNotPresent() {
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
    @DisplayName("Invalid age")
    void invalidAge() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setId(1L);
        applicationUser.setEmail("test@o2.pl");
        applicationUser.setName("Test");
        applicationUser.setPassword("$2a$12$31fmn4cF235a4W81//Dvm./YrbQXKaQhYpN/PfGAIBPGTNfBbAdDm");
        applicationUser.setUsername("test");
        applicationUser.setSurname("Test");
        applicationUser.setAge(0);

        Set<ConstraintViolation<ApplicationUser>> violations = validator.validate(applicationUser);

        assertEquals(1, violations.size(), "Expected one constraint violation");
        ConstraintViolation<ApplicationUser> violation = violations.iterator().next();
        assertEquals("age", violation.getPropertyPath().toString(), "Expected violation on 'age' property");
        assertEquals("musi być równe lub większe od 18", violation.getMessage(), "Expected violation message 'musi być równe lub większe od 18'");
    }

    @Test
    @DisplayName("Should update user age from 19 to 30")
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
        assertEquals(30, test.get().getAge());
    }

}
