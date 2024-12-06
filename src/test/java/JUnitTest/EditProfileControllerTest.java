package JUnitTest;

import com.uet.libraryManagement.APIService.ImgurUpload;
import com.uet.libraryManagement.Controllers.EditProfileController;
import com.uet.libraryManagement.Managers.SessionManager;
import com.uet.libraryManagement.Repositories.UserRepository;
import com.uet.libraryManagement.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EditProfileControllerTest {

    @InjectMocks
    private EditProfileController editProfileController;

    @Mock
    private ImageView avatarImage;
    @Mock
    private Label usernameLabel;
    @Mock
    private TextField phoneField;
    @Mock
    private TextField emailField;
    @Mock
    private TextField nameField;
    @Mock
    private DatePicker birthdayField;
    @Mock
    private FileChooser fileChooser;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SessionManager sessionManager;

    private User currentUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up the mock user
        currentUser = mock(User.class);
        when(sessionManager.getInstance().getUser()).thenReturn(currentUser);

        // Set up the controller
        editProfileController.usernameLabel = usernameLabel;
        editProfileController.avatarImage = avatarImage;
        editProfileController.phoneField = phoneField;
        editProfileController.emailField = emailField;
        editProfileController.nameField = nameField;
        editProfileController.birthdayField = birthdayField;
    }

    @Test
    public void testLoadUserInfo() {
        // Arrange
        String username = "testUser";
        String email = "test@example.com";
        String phone = "123456789";
        String name = "John Doe";
        String birthday = "1990-01-01";
        String avatar = "http://example.com/avatar.png";

        // Mocking the User data
        when(currentUser.getUsername()).thenReturn(username);
        when(currentUser.getEmail()).thenReturn(email);
        when(currentUser.getPhone()).thenReturn(phone);
        when(currentUser.getFullName()).thenReturn(name);
        when(currentUser.getBirthday()).thenReturn(birthday);
        when(currentUser.getAvatar()).thenReturn(avatar);

        // Act
        editProfileController.loadUserInfo();

        // Assert
        verify(usernameLabel).setText(username);
        verify(nameField).setText(name);
        verify(phoneField).setText(phone);
        verify(emailField).setText(email);
        verify(birthdayField).setValue(LocalDate.parse(birthday));

        // Instead of mocking ImageView, verify the image URL.
        assertEquals(avatar, avatarImage.getImage().getUrl());  // This checks if the image URL was set correctly
    }


    @Test
    public void testSaveChanges_EmptyPhoneOrEmail() throws IOException {
        // Arrange
        when(phoneField.getText()).thenReturn("");
        when(emailField.getText()).thenReturn("");
        when(nameField.getText()).thenReturn("John");
        when(birthdayField.getValue()).thenReturn(LocalDate.of(1990, 1, 1));

        // Act
        editProfileController.saveChanges(mock(ActionEvent.class));

        // Assert
        verify(editProfileController).showAlert("Please fill in all required fields.");
        verifyNoInteractions(userRepository);
    }

    @Test
    public void testSaveChanges_SuccessfulUpdate() throws IOException {
        // Arrange
        String phone = "987654321";
        String email = "newemail@example.com";
        String name = "Jane Doe";
        String birthday = "1992-02-02";
        String imgUrl = "http://example.com/updated_avatar.png";

        when(phoneField.getText()).thenReturn(phone);
        when(emailField.getText()).thenReturn(email);
        when(nameField.getText()).thenReturn(name);
        when(birthdayField.getValue()).thenReturn(LocalDate.parse(birthday));
        when(currentUser.getBirthday()).thenReturn(birthday);

        // Mock Imgur image upload success
        when(currentUser.getAvatar()).thenReturn(null); // Initial avatar is null
        when(ImgurUpload.uploadImage(any(File.class))).thenReturn(imgUrl);

        // Act
        editProfileController.saveChanges(mock(ActionEvent.class));

        // Assert
        verify(currentUser).setFullName(name);
        verify(currentUser).setPhone(phone);
        verify(currentUser).setEmail(email);
        verify(currentUser).setBirthday(birthday);
        verify(currentUser).setAvatar(imgUrl);
        verify(userRepository).updateProfile(currentUser);
        verify(editProfileController).showAlert("Profile changed successfully.");
    }

    @Test
    public void testInsertImg() {
        // Arrange
        File file = mock(File.class);
        when(file.getAbsolutePath()).thenReturn("path/to/image.jpg");
        when(fileChooser.showOpenDialog(any())).thenReturn(file);

        // Act
        editProfileController.insertImg();

        // Assert
        verify(avatarImage).setImage(any());
        assertEquals("path/to/image.jpg", editProfileController.avatarPath);
    }

    @Test
    public void testCloseForm() {
        // Arrange
        Stage stage = mock(Stage.class);
        when(usernameLabel.getScene().getWindow()).thenReturn(stage);

        // Act
        editProfileController.closeForm();

        // Assert
        verify(stage).close();
    }

    @Test
    public void testShowAlert() {
        // Arrange
        String message = "Test Alert Message";

        // Act
        editProfileController.showAlert(message);

        // Assert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        assertNotNull(alert.getContentText());
    }
}

class EditProfileControllerTestLauncher {
    public static void main(String[] args)
    {
        ConnectJDBCTest.main(args);
    }
}
