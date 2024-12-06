package JUnitTest;

import com.uet.libraryManagement.Controllers.RegisterController;
import com.uet.libraryManagement.Managers.SceneManager;
import com.uet.libraryManagement.Repositories.UserRepository;
import com.uet.libraryManagement.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterControllerTest {

    @InjectMocks
    private RegisterController registerController;

    @Mock
    private TextField usernameField;

    @Mock
    private PasswordField passwordField;

    @Mock
    private PasswordField cf_passwordField;

    @Mock
    private TextField emailField;

    @Mock
    private Label messageLabel;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SceneManager sceneManager;

    @BeforeEach
    public void setUp() {
        // Initialize JavaFX toolkit if not already initialized
        if (!Platform.isFxApplicationThread()) {
            Platform.startup(() -> {});
        }

        // Inject mock UI elements
        registerController.usernameField = usernameField;
        registerController.passwordField = passwordField;
        registerController.cf_passwordField = cf_passwordField;
        registerController.emailField = emailField;
        registerController.messageLabel = messageLabel;
    }

    @Test
    public void testSubmitRegister_AllFieldsFilled_PasswordsMatch_ValidEmail_SuccessfulRegistration() {
        // Arrange
        String username = "validUser";
        String password = "validPassword";
        String email = "valid.email@example.com";

        when(usernameField.getText()).thenReturn(username);
        when(passwordField.getText()).thenReturn(password);
        when(cf_passwordField.getText()).thenReturn(password);
        when(emailField.getText()).thenReturn(email);
        when(userRepository.create(any(User.class))).thenReturn(true);

        // Act
        Platform.runLater(() -> registerController.submitRegister());

        // Assert
        verify(userRepository).create(any(User.class));
        verify(messageLabel).setText("Registration successful!");
    }

    @Test
    public void testSubmitRegister_EmptyFields_ShowErrorMessage() {
        // Arrange
        when(usernameField.getText()).thenReturn("");
        when(passwordField.getText()).thenReturn("");
        when(cf_passwordField.getText()).thenReturn("");
        when(emailField.getText()).thenReturn("");

        // Act
        Platform.runLater(() -> registerController.submitRegister());

        // Assert
        verify(messageLabel).setText("Please fill all fields.");
        verifyNoInteractions(userRepository);
    }

    @Test
    public void testSubmitRegister_PasswordsDoNotMatch_ShowErrorMessage() {
        // Arrange
        when(usernameField.getText()).thenReturn("validUser");
        when(passwordField.getText()).thenReturn("password1");
        when(cf_passwordField.getText()).thenReturn("password2");
        when(emailField.getText()).thenReturn("valid.email@example.com");

        // Act
        Platform.runLater(() -> registerController.submitRegister());

        // Assert
        verify(messageLabel).setText("Passwords do not match.");
        verifyNoInteractions(userRepository);
    }

    @ParameterizedTest
    @CsvSource({
            "invalid.email",
            "invalid@email",
            "@invalid.email",
            "invalid@.com",
            "  @example.com"
    })
    public void testSubmitRegister_InvalidEmail_ShowErrorMessage(String invalidEmail) {
        // Arrange
        when(usernameField.getText()).thenReturn("validUser");
        when(passwordField.getText()).thenReturn("validPassword");
        when(cf_passwordField.getText()).thenReturn("validPassword");
        when(emailField.getText()).thenReturn(invalidEmail);

        // Act
        Platform.runLater(() -> registerController.submitRegister());

        // Assert
        verify(messageLabel).setText("Invalid email address.");
        verifyNoInteractions(userRepository);
    }

    @Test
    public void testCancelRegister_NavigatesToLoginScene() throws IOException {
        // Arrange
        ActionEvent mockEvent = mock(ActionEvent.class);

        // Act
        Platform.runLater(() -> {
            try {
                registerController.cancelRegister(mockEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Assert
        verify(sceneManager).setLoginScene("FXML/Login.fxml");
    }

    @Test
    public void testIsValidEmail_ValidEmails() {
        // Valid email test cases
        String[] validEmails = {
                "user@example.com",
                "user.name@example.com",
                "user+tag@example.com",
                "user123@example.co.uk",
                "user-name@example.org"
        };

        for (String email : validEmails) {
            assertTrue(registerController.isValidEmail(email),
                    "Email should be valid: " + email);
        }
    }

    @Test
    public void testIsValidEmail_InvalidEmails() {
        // Invalid email test cases
        String[] invalidEmails = {
                "invalid.email",
                "invalid@email",
                "@invalid.email",
                "user@.com",
                "user@example",
                "user@example.",
                ""
        };

        for (String email : invalidEmails) {
            assertFalse(registerController.isValidEmail(email),
                    "Email should be invalid: " + email);
        }
    }
}

class RegisterControllerTestLauncher {
    public static void main(String[] args)
    {
        ConnectJDBCTest.main(args);
    }
}
