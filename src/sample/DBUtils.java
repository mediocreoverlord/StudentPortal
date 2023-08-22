package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;
import java.util.Observable;

public class DBUtils {

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String studentName, int studentId) {
        Parent root = null;
        Connection connection = null;
        PreparedStatement psModify = null;
        PreparedStatement psModify2 = null;
        ResultSet resultSet = null;

        if (studentName != null && studentId != 0) {
            try {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();

                // new
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdb", "root", "DBMprojectspr2023");
                psModify = connection.prepareStatement("SELECT first_name, last_name, address, phone_number, email, date_of_birth FROM student WHERE student_id = ?");
                psModify.setInt(1, studentId);
                resultSet = psModify.executeQuery();
                if (resultSet.next()) {

                    LogInController logInController = loader.getController();
                    logInController.setUserInformation(resultSet.getString("first_name"), resultSet.getString("last_name"), resultSet.getString("address"), resultSet.getString("phone_number"),
                            resultSet.getString("email"), resultSet.getString("date_of_birth"), studentId);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } else {
            try {
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public static void registerUser(ActionEvent event, String username, String password, int studentId) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExist = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdb", "root", "DBMprojectspr2023");
            psCheckUserExist = connection.prepareStatement("SELECT * FROM registration WHERE username = ?");
            psCheckUserExist.setString(1, username);
            resultSet = psCheckUserExist.executeQuery();

            if (resultSet.isBeforeFirst()) {
                System.out.println("User already exists!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You can not use this username.");
                alert.show();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO registration (username, password, student_student_id) VALUES (?, ?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.setInt(3, studentId);
                psInsert.executeUpdate();

                changeScene(event, "log-in.fxml", "Welcome", username, studentId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psCheckUserExist != null) {
                try {
                    psCheckUserExist.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void loginUser(ActionEvent event, String username, String password, int studentId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {                                                   //"jdbc:mysql://localhost:3306/studentdb", "root", "DBMprojectspr2023"
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdb", "root", "DBMprojectspr2023");
            preparedStatement = connection.prepareStatement("SELECT password, student_student_id FROM registration WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();


            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found in the database");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Incorrect credentials");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String retrievedPassword = resultSet.getString("password");
                    int retrievedStudID = resultSet.getInt("student_student_id");
                    if (retrievedPassword.equals(password) && retrievedStudID == studentId) {
                        changeScene(event, "log-in.fxml", "Welcome", username, studentId);
                    } else {
                        System.out.println("Passwords did not match");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Incorrect credentials");
                        alert.show();
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static ObservableList<Users> getDatausesrs(int studentId) throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdb", "root", "DBMprojectspr2023");
        ObservableList<Users> list = FXCollections.observableArrayList();
        try {
            ps = connection.prepareStatement("select course.course_id, course.course_name, course.instructor_name, schedule.start_time, schedule.end_time, schedule.room_number\n" +
                    "from schedule\n" +
                    "join course\n" +
                    "on schedule.course_id = course.course_id\n" +
                    "where student_id = ?");
            ps.setInt(1, studentId);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Users(Integer.parseInt(rs.getString("course_id")), rs.getString("course_name"),
                        rs.getString("instructor_name"), rs.getString("start_time"),
                        rs.getString("end_time"), rs.getString("room_number")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


}

