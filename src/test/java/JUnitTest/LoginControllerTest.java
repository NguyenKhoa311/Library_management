package JUnitTest;

import com.uet.libraryManagement.Controllers.LoginController;
import com.uet.libraryManagement.Managers.SceneManager;
import com.uet.libraryManagement.Managers.SessionManager;
import com.uet.libraryManagement.Repositories.UserRepository;
import com.uet.libraryManagement.User;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UserRepository.class, SceneManager.class, SessionManager.class})
public class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private TextField usernameTextField;

    @Mock
    private TextField passwordTextField;

    @Mock
    private Label messageLabel;

    private UserRepository userRepositoryMock;

    private SceneManager sceneManagerMock;

    private SessionManager sessionManagerMock;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock singleton instances
        userRepositoryMock = PowerMockito.mock(UserRepository.class);
        sceneManagerMock = PowerMockito.mock(SceneManager.class);
        sessionManagerMock = PowerMockito.mock(SessionManager.class);

        PowerMockito.mockStatic(UserRepository.class);
        PowerMockito.mockStatic(SceneManager.class);
        PowerMockito.mockStatic(SessionManager.class);

        PowerMockito.when(UserRepository.getInstance()).thenReturn(userRepositoryMock);
        PowerMockito.when(SceneManager.getInstance()).thenReturn(sceneManagerMock);
        PowerMockito.when(SessionManager.getInstance()).thenReturn(sessionManagerMock);

        // Inject mock UI elements
        loginController.username_textfield = usernameTextField;
        loginController.password_textfield = passwordTextField;
        loginController.messageLabel = messageLabel;
    }

    @Test
    public void testHandleLogin_SuccessfulUserLogin() throws Exception {
        // Arrange
        when(usernameTextField.getText()).thenReturn("user1");
        when(passwordTextField.getText()).thenReturn("password1");

        User mockUser = new User("user1", "password1", "user@example.com", "user");
        when(userRepositoryMock.validateUser("user1", "password1")).thenReturn(mockUser);

        // Act
        loginController.handleLogin();

        // Assert
        verify(sessionManagerMock).setUser(mockUser);
        verify(sceneManagerMock).setScene("FXML/UserMenu.fxml");
        verify(sceneManagerMock).setSubScene("FXML/UserDashboard.fxml");
        verify(messageLabel, never()).setText("Invalid username or password.");
    }

    @Test
    public void testHandleLogin_InvalidLogin() {
        // Arrange
        when(usernameTextField.getText()).thenReturn("user1");
        when(passwordTextField.getText()).thenReturn("wrongpassword");

        when(userRepositoryMock.validateUser("user1", "wrongpassword")).thenReturn(null);

        // Act
        loginController.handleLogin();

        // Assert
        verify(messageLabel).setText("Invalid username or password.");
        verifyNoInteractions(sessionManagerMock);
        verifyNoInteractions(sceneManagerMock);
    }

    @Test
    public void testHandleLogin_EmptyFields() {
        // Arrange
        when(usernameTextField.getText()).thenReturn("");
        when(passwordTextField.getText()).thenReturn("");

        // Act
        loginController.handleLogin();

        // Assert
        verify(messageLabel).setText("Please enter both username and password.");
        verifyNoInteractions(userRepositoryMock);
        verifyNoInteractions(sessionManagerMock);
        verifyNoInteractions(sceneManagerMock);
    }
}
