package JUnitTest;

import com.uet.libraryManagement.ConnectJDBC;
import com.uet.libraryManagement.Controllers.MenuController;
import com.uet.libraryManagement.Managers.SceneManager;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;

import java.sql.Connection;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ConnectJDBCTest extends ApplicationTest {

    @Override
    public void start(Stage stage) {
        try {
            SceneManager.getInstance().setStage(stage);
            SceneManager.getInstance().setLoginScene("FXML/Login.fxml");

            stage.setOnCloseRequest(event -> {
                event.consume();
                MenuController.close(stage);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStaticConnectionInitialization() throws SQLException {
        try (MockedStatic<DriverManager> mockedDriverManager = Mockito.mockStatic(DriverManager.class)) {
            Connection mockConnection = mock(Connection.class);
            mockedDriverManager.when(() -> DriverManager.getConnection(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                    .thenReturn(mockConnection);

            // Accessing the static block indirectly by calling a ConnectJDBC method
            ResultSet mockResultSet = mock(ResultSet.class);
            Statement mockStatement = mock(Statement.class);
            when(mockConnection.createStatement()).thenReturn(mockStatement);
            when(mockStatement.executeQuery("SELECT 1")).thenReturn(mockResultSet);

            ConnectJDBC.executeQuery("SELECT 1");

            mockedDriverManager.verify(() -> DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library_db",
                    "root",
                    ""
            ));
        }
    }

    @Test
    public void testExecuteQuery() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery("SELECT * FROM books")).thenReturn(mockResultSet);

        try (MockedStatic<DriverManager> mockedDriverManager = Mockito.mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                    .thenReturn(mockConnection);

            ResultSet resultSet = ConnectJDBC.executeQuery("SELECT * FROM books");
            assertNotNull(resultSet, "ResultSet should not be null");
            assertEquals(mockResultSet, resultSet, "ResultSet should match the mocked result");
        }
    }

    @Test
    public void testExecuteQueryWithParams() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement("SELECT * FROM books WHERE id = ?")).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        try (MockedStatic<DriverManager> mockedDriverManager = Mockito.mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                    .thenReturn(mockConnection);

            ResultSet resultSet = ConnectJDBC.executeQueryWithParams("SELECT * FROM books WHERE id = ?", 1);
            verify(mockPreparedStatement).setObject(1, 1);
            assertNotNull(resultSet, "ResultSet should not be null");
            assertEquals(mockResultSet, resultSet, "ResultSet should match the mocked result");
        }
    }

    @Test
    public void testExecuteUpdate() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement("UPDATE books SET title = ? WHERE id = ?")).thenReturn(mockPreparedStatement);

        try (MockedStatic<DriverManager> mockedDriverManager = Mockito.mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                    .thenReturn(mockConnection);

            ConnectJDBC.executeUpdate("UPDATE books SET title = ? WHERE id = ?", "New Title", 1);
            verify(mockPreparedStatement).setObject(1, "New Title");
            verify(mockPreparedStatement).setObject(2, 1);
            verify(mockPreparedStatement).executeUpdate();
        }
    }



    static void main(String[] args) {
    }
}

class ConnectJDBCTestLauncher {
    public static void main(String[] args)
    {
        ConnectJDBCTest.main(args);
    }
}
