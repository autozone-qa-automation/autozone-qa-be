/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.az_qa.backend.dao.UsersDAO;
import com.az_qa.backend.exception.DuplicatedItemException;
import com.az_qa.backend.exception.ItemNotFoundException;
import com.az_qa.backend.exception.MissingRequiredFieldException;
import com.az_qa.backend.vo.UserVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTests {

    @Mock
    private UsersDAO usersDAO;

    @InjectMocks
    private UsersService usersService;

    private UserVO userInput;

    @BeforeEach
    void setUp() {
        userInput = new UserVO();
        userInput.setId(1L);
        userInput.setName("John");
        userInput.setLastName("Doe");
        userInput.setEmail("john.doe@example.com");
        userInput.setPassword("pass123");
        userInput.setIsActive(true);
        userInput.setRoleId(1L);
    }

    @Test
    @DisplayName("add: Must create user when email does not exist")
    void add_Success() {
        when(usersDAO.findByEmail(userInput.getEmail()))
                .thenThrow(new ItemNotFoundException("User with email {john.doe@example.com} not found."));
        when(usersDAO.add(any(UserVO.class))).thenReturn(userInput);

        UserVO result = usersService.add(userInput);

        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getEmail());
        verify(usersDAO).add(userInput);
    }

    @Test
    @DisplayName("add: Must throw DuplicatedItemException when email already exists")
    void add_ThrowsDuplicatedItemException_WhenEmailExists() {
        when(usersDAO.findByEmail(userInput.getEmail())).thenReturn(userInput);

        DuplicatedItemException exception = assertThrows(DuplicatedItemException.class,
                () -> usersService.add(userInput));

        assertEquals("User with email {john.doe@example.com} already exists.", exception.getMessage());
        verify(usersDAO, never()).add(any(UserVO.class));
    }

    @Test
    @DisplayName("add: Must throw MissingRequiredFieldException when roleId is null")
    void add_ThrowsMissingRequiredFieldException_WhenRoleIdIsNull() {
        userInput.setRoleId(null);

        MissingRequiredFieldException exception = assertThrows(MissingRequiredFieldException.class,
                () -> usersService.add(userInput));

        assertEquals("Producer id is required for user creation.", exception.getMessage());
    }

    @Test
    @DisplayName("add: Must throw MissingRequiredFieldException when email is null")
    void add_ThrowsMissingRequiredFieldException_WhenEmailIsNull() {
        userInput.setEmail(null);

        MissingRequiredFieldException exception = assertThrows(MissingRequiredFieldException.class,
                () -> usersService.add(userInput));

        assertEquals("Email is required for user creation.", exception.getMessage());
    }

    @Test
    @DisplayName("add: Must throw MissingRequiredFieldException when password is null")
    void add_ThrowsMissingRequiredFieldException_WhenPasswordIsNull() {
        userInput.setPassword(null);

        MissingRequiredFieldException exception = assertThrows(MissingRequiredFieldException.class,
                () -> usersService.add(userInput));

        assertEquals("Password is required for user creation.", exception.getMessage());
    }

    @Test
    @DisplayName("add: Must throw MissingRequiredFieldException when name is null")
    void add_ThrowsMissingRequiredFieldException_WhenNameIsNull() {
        userInput.setName(null);

        MissingRequiredFieldException exception = assertThrows(MissingRequiredFieldException.class,
                () -> usersService.add(userInput));

        assertEquals("Name is required for user creation.", exception.getMessage());
    }

    @Test
    @DisplayName("add: Must throw MissingRequiredFieldException when last name is null")
    void add_ThrowsMissingRequiredFieldException_WhenLastNameIsNull() {
        userInput.setLastName(null);

        MissingRequiredFieldException exception = assertThrows(MissingRequiredFieldException.class,
                () -> usersService.add(userInput));

        assertEquals("Last name is required for user creation.", exception.getMessage());
    }
}
