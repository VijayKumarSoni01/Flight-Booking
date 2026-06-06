package com.project.usermanagment;

import com.project.usermanagment.dtos.UserDTO.registrationORlogin.RegistrationRequest;
import com.project.usermanagment.dtos.UserDTO.securitydto.UserAuthResponse;
import com.project.usermanagment.enumFolder.Gender;
import com.project.usermanagment.enumFolder.Title;
import com.project.usermanagment.service.UserService.PublicUserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class RegLogTest {

    @Autowired
    private PublicUserService userService;

    @Test
    void testRegisterAndLogin() {

        System.out.println("===== TEST STARTED =====");

        // ✅ Create request
        RegistrationRequest request = new RegistrationRequest();
        request.setTitle(Title.MR);
        request.setFirstName("Vijay");
        request.setMiddleName("Kumar");
        request.setLastName("Soni");

        // ✅ UNIQUE EMAIL (VERY IMPORTANT)
        String email = "test" + System.currentTimeMillis() + "@gmail.com";
        request.setEmail(email);

        request.setPassword("Password123");
        request.setUsername("user" + System.currentTimeMillis());
        request.setPhoneNumber("9876543210");
        request.setAlternatePhone("9123456789");
        request.setGender(Gender.MALE);
        request.setDateOfBirth(LocalDate.of(2000, 5, 15));
        request.setNationality("Indian");

        request.setAddressLine1("House No. 45");
        request.setAddressLine2("Near Market");
        request.setCity("Patna");
        request.setState("Bihar");
        request.setCountry("India");
        request.setPinCode("800001");

        UserAuthResponse registerResponse = userService.register(request);
        System.out.println("REGISTER SUCCESS: " + registerResponse);

        UserAuthResponse loginResponse =
                userService.login("9876543210", "Password123");

        System.out.println("LOGIN SUCCESS: " + loginResponse);

        System.out.println("===== TEST COMPLETED =====");
    }
}