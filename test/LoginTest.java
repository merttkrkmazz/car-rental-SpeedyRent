import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import service.impl.AuthenticationImpl;
import dao.UserDAO;
import model.User;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class LoginTest {
    private AuthenticationImpl authentication;
    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = mock(UserDAO.class);
        authentication = new AuthenticationImpl(userDAO);
    }

    @Test
    void loginSucceedsWithValidCredentials() {
        String username = "validUser";
        String password = "validPass";
        User user = new User(username, password);

        when(userDAO.findByUsername(username)).thenReturn(user);

        assertTrue(authentication.login(username, password));
        verify(userDAO, times(1)).findByUsername(username);
    }

    @Test
    void loginFailsWithInvalidPassword() {
        String username = "validUser";
        String password = "invalidPass";
        User user = new User(username, "correctPass");

        when(userDAO.findByUsername(username)).thenReturn(user);

        assertFalse(authentication.login(username, password));
        verify(userDAO, times(1)).findByUsername(username);
    }

    @Test
    void loginFailsWhenUserDoesNotExist() {
        String username = "nonExistentUser";
        String password = "anyPass";

        when(userDAO.findByUsername(username)).thenReturn(null);

        assertFalse(authentication.login(username, password));
        verify(userDAO, times(1)).findByUsername(username);
    }

    @Test
    void loginFailsWhenPasswordIsNull() {
        String username = "validUser";
        String password = null;
        User user = new User(username, "correctPass");

        when(userDAO.findByUsername(username)).thenReturn(user);

        assertFalse(authentication.login(username, password));
        verify(userDAO, times(1)).findByUsername(username);
    }

    @Test
    void loginFailsWhenUsernameIsNull() {
        String username = null;
        String password = "validPass";

        assertFalse(authentication.login(username, password));
        verify(userDAO, never()).findByUsername(anyString());
    }

    @Test
    void loginFailsWhenBothUsernameAndPasswordAreNull() {
        String username = null;
        String password = null;

        assertFalse(authentication.login(username, password));
        verify(userDAO, never()).findByUsername(anyString());
    }

    @Test
    void loginFailsWhenUserHasNoPasswordSet() {
        String username = "validUser";
        String password = "validPass";
        User user = new User(username, null);

        when(userDAO.findByUsername(username)).thenReturn(user);

        assertFalse(authentication.login(username, password));
        verify(userDAO, times(1)).findByUsername(username);
    }
}