package JUnitTest;

import com.uet.libraryManagement.Controllers.LoginController;
import com.uet.libraryManagement.Controllers.MenuController;
import com.uet.libraryManagement.Managers.SceneManager;
import com.uet.libraryManagement.Managers.SessionManager;
import com.uet.libraryManagement.Repositories.UserRepository;
import com.uet.libraryManagement.User;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest extends ApplicationTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private TextField usernameTextField;

    @Mock
    private TextField passwordTextField;

    @Mock
    private Label messageLabel;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private SceneManager sceneManagerMock;

    @Mock
    private SessionManager sessionManagerMock;

    @BeforeEach
    public void setUp() {
        // Initialize JavaFX toolkit
        if (!Platform.isFxApplicationThread()) {
            Platform.startup(() -> {});
        }

        // Inject mock UI elements
        loginController.username_textfield = usernameTextField;
        loginController.password_textfield = passwordTextField;
        loginController.messageLabel = messageLabel;

    }

    @Override
    public void start(Stage stage) {
        try {
            // Chạy như bình thường
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHandleLogin_SuccessfulUserLogin() throws IOException {
        // Arrange
        String username = "validUser";
        String password = "validPassword";

        when(usernameTextField.getText()).thenReturn(username);
        when(passwordTextField.getText()).thenReturn(password);

        User mockUser = new User(username, password, "user@example.com", "user");
        when(userRepositoryMock.validateUser(username, password)).thenReturn(mockUser);

        // Act
        loginController.handleLogin();

        // Assert
        verify(sessionManagerMock).setUser(mockUser);
        verify(sceneManagerMock).setScene("FXML/UserMenu.fxml");
        verify(sceneManagerMock).setSubScene("FXML/UserDashboard.fxml");
        verify(messageLabel, never()).setText(anyString());
    }

    @Test
    public void testHandleLogin_InvalidCredentials() {
        // Arrange
        String username = "existingUser";
        String incorrectPassword = "wrongPassword";

        when(usernameTextField.getText()).thenReturn(username);
        when(passwordTextField.getText()).thenReturn(incorrectPassword);

        when(userRepositoryMock.validateUser(username, incorrectPassword)).thenReturn(null);

        // Act
        loginController.handleLogin();

        // Assert
        verify(messageLabel).setText(eq("Invalid username or password."));
        verifyNoInteractions(sessionManagerMock);
        verifyNoInteractions(sceneManagerMock);
    }

    @Test
    public void testHandleLogin_EmptyUsername() {
        // Arrange
        when(usernameTextField.getText()).thenReturn("");
        when(passwordTextField.getText()).thenReturn("anyPassword");

        // Act
        loginController.handleLogin();

        // Assert
        verify(messageLabel).setText(eq("Please enter both username and password."));
        verifyNoInteractions(userRepositoryMock);
        verifyNoInteractions(sessionManagerMock);
        verifyNoInteractions(sceneManagerMock);
    }

    @Test
    public void testHandleLogin_EmptyPassword() {
        // Arrange
        when(usernameTextField.getText()).thenReturn("someUser");
        when(passwordTextField.getText()).thenReturn("");

        // Act
        loginController.handleLogin();

        // Assert
        verify(messageLabel).setText(eq("Please enter both username and password."));
        verifyNoInteractions(userRepositoryMock);
        verifyNoInteractions(sessionManagerMock);
        verifyNoInteractions(sceneManagerMock);
    }

    @Test
    public void testHandleLogin_NullUsername() {
        // Arrange
        when(usernameTextField.getText()).thenReturn(null);
        when(passwordTextField.getText()).thenReturn("anyPassword");

        // Act
        loginController.handleLogin();

        // Assert
        verify(messageLabel).setText(eq("Please enter both username and password."));
        verifyNoInteractions(userRepositoryMock);
        verifyNoInteractions(sessionManagerMock);
        verifyNoInteractions(sceneManagerMock);
    }

    @Test
    public void testHandleLogin_ExcessivelyLongCredentials() {
        // Arrange
        String longUsername = "a".repeat(256); // Assuming a max length of 255 characters
        String longPassword = "b".repeat(256);

        when(usernameTextField.getText()).thenReturn(longUsername);
        when(passwordTextField.getText()).thenReturn(longPassword);

        // Act
        loginController.handleLogin();

        // Assert
        verify(messageLabel).setText(eq("Username or password is too long."));
        verifyNoInteractions(userRepositoryMock);
        verifyNoInteractions(sessionManagerMock);
        verifyNoInteractions(sceneManagerMock);
    }

}

class LoginControllerTestLauncher {
    public static void main(String[] args)
    {
        ConnectJDBCTest.main(args);
    }
}