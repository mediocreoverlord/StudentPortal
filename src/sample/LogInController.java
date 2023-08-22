package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.control.TextField;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static sample.DBUtils.getDatausesrs;

public class LogInController implements Initializable {
    @FXML
    private TextField tf_input;

    @FXML
    private Button button_updateinfo;
    @FXML
    private Button button_addcourse;
    @FXML
    private Button button_deletecourse;
    @FXML
    private Button button_logout;
    @FXML
    private Button button_deleteacc;
    @FXML
    private ListView list_studinfo;
    @FXML
    private Label label_welcome;
    @FXML
    private Label label_studentid;

    @FXML
    private TableView<Users> table;
    @FXML
    private TableColumn<Users, Integer> table_courseid;
    @FXML
    private TableColumn<Users, String> table_coursename;
    @FXML
    private TableColumn<Users, String> table_prof;
    @FXML
    private TableColumn<Users, String> table_starttime;
    @FXML
    private TableColumn<Users, String> table_endtime;
    @FXML
    private TableColumn<Users, String> table_room;


    ObservableList<Users> listM;
    private int index = -1;
    private List<String> studInfo = new ArrayList<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "sample.fxml", "Log in", null, 0);
            }

        });


    }


    public void setUserInformation(String firstname, String lastname, String address, String phonenum, String email, String dob, int studentId) throws Exception {
        label_welcome.setText("Welcome " + firstname);
        label_studentid.setText(String.valueOf(studentId));

        String[] studInfo = {firstname, lastname, address, phonenum, email, dob};

        list_studinfo.getItems().addAll(studInfo);


        table_courseid.setCellValueFactory(new PropertyValueFactory<Users, Integer>("courseId"));
        table_coursename.setCellValueFactory(new PropertyValueFactory<Users, String>("courseName"));
        table_prof.setCellValueFactory(new PropertyValueFactory<Users, String>("professorName"));
        table_starttime.setCellValueFactory(new PropertyValueFactory<Users, String>("startTime"));
        table_endtime.setCellValueFactory(new PropertyValueFactory<Users, String>("endTime"));
        table_room.setCellValueFactory(new PropertyValueFactory<Users, String>("roomNum"));

        listM = getDatausesrs(studentId);
        table.setItems(listM);


    }


    public void addCourse(ActionEvent actionEvent) {
        Connection connection = null;
        PreparedStatement psCheckCourseExist;
        PreparedStatement psInsert = null;
        PreparedStatement psUpdate = null;
        PreparedStatement psInsert2 = null;
        ResultSet rs1 = null;

        try {
            if (tf_input.getText().trim().isEmpty()) {
                System.out.println("Please fill in field");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please fill in field");
                alert.show();
            } else {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdb", "root", "REDbloodyjust^ce");
                psCheckCourseExist = connection.prepareStatement("SELECT * FROM enrollment WHERE student_id = ? AND course_id = ?" );
                psCheckCourseExist.setInt(1, Integer.parseInt(label_studentid.getText()));
                psCheckCourseExist.setInt(2, Integer.parseInt(tf_input.getText()));
                rs1 = psCheckCourseExist.executeQuery();


                if (rs1.isBeforeFirst()) {
                    System.out.println("Course already in schedule!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("This course is already in schedule");
                    alert.show();
                } else {
                        psInsert = connection.prepareStatement("insert into enrollment (student_id, course_id, grade) values(?, ?, \"n/a\")");
                        psInsert2 = connection.prepareStatement("insert into schedule (student_id, course_id) values(?, ?)");
                        psInsert.setInt(1, Integer.parseInt(label_studentid.getText()));
                        psInsert.setInt(2, Integer.parseInt(tf_input.getText()));
                        psInsert2.setInt(1, Integer.parseInt(label_studentid.getText()));
                        psInsert2.setInt(2, Integer.parseInt(tf_input.getText()));
                        psUpdate = connection.prepareStatement("Update schedule\n" +
                                "join course\n" +
                                "on course.course_id = schedule.course_id\n" +
                                "set schedule.start_time = course.start_time, schedule.end_time = course.end_time,\n" +
                                " schedule.room_number = course.room_number\n" +
                                "where course.course_id = ?");


                        psUpdate.setString(1, tf_input.getText());
                        psInsert.executeUpdate();
                        psInsert2.executeUpdate();
                        psUpdate.executeUpdate();
                        Alert alert;
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Class added");
                        alert.show();

                        listM = getDatausesrs(Integer.parseInt(label_studentid.getText()));
                        table.setItems(listM);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteCourse(ActionEvent actionEvent) {
        Connection connection = null;
        PreparedStatement psCheckCourseExist;
        PreparedStatement psRemove = null;
        PreparedStatement psRemove2 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;

        try {
            if (tf_input.getText().trim().isEmpty()) {
                System.out.println("Please fill in field");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please fill in field");
                alert.show();
            } else {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdb", "root", "REDbloodyjust^ce");
                psRemove = connection.prepareStatement("delete from schedule\n" +
                        "where student_id = ? AND course_id = ?");
                psRemove.setInt(1, Integer.parseInt(label_studentid.getText()));
                psRemove.setInt(2, Integer.parseInt(tf_input.getText()));
                psRemove2 = connection.prepareStatement("delete from enrollment\n" +
                        "where student_id = ? AND course_id = ?");
                psRemove2.setInt(1, Integer.parseInt(label_studentid.getText()));
                psRemove2.setInt(2, Integer.parseInt(tf_input.getText()));

                psRemove.executeUpdate();
                psRemove2.executeUpdate();
                Alert alert;
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Class removed");
                alert.show();

                listM = getDatausesrs(Integer.parseInt(label_studentid.getText()));
                table.setItems(listM);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateInfo(ActionEvent actionEvent) {
        Connection connection = null;
        PreparedStatement psUpdate = null;
        ResultSet rs1 = null;

        int selectedItem = (list_studinfo.getSelectionModel().getSelectedIndex());
        String input = tf_input.getText();


        try {
            if (tf_input.getText().trim().isEmpty()) {
                System.out.println("Please fill in field");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please fill in field");
                alert.show();
            } else {

                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdb", "root", "REDbloodyjust^ce");
                switch(selectedItem){
                    case 0 :
                        input = ("student.first_name"); break;
                    case 1:
                        input = "student.last_name"; break;
                    case 2:
                        input = "student.address"; break;
                    case 3:
                        input = "student.phone_number"; break;
                    case 4:
                        input = "student.email"; break;
                    case 5:
                        input ="student.date_of_birth"; break;

                }
                String sql = String.format("UPDATE student\n" +
                        "JOIN registration\n" +
                        "ON student.student_id = registration.student_student_id SET %s = ? WHERE student_id = ?", input);
                psUpdate = connection.prepareStatement(sql);

                psUpdate.setString(1,tf_input.getText());
                psUpdate.setInt(2, Integer.parseInt(label_studentid.getText()));

                psUpdate.executeUpdate();

                list_studinfo.getItems().set(selectedItem, tf_input.getText());
                //String[] studInfo = {firstname, lastname, address, phonenum, email, dob};
                // list_studinfo.getItems().addAll(studInfo);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAcc(ActionEvent event){
        Connection connection = null;
        PreparedStatement ps = null;
        PreparedStatement psInsert = null;
        PreparedStatement psUpdate = null;
        ResultSet rs1 = null;

        try {

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdb", "root", "REDbloodyjust^ce");
            ps = connection.prepareStatement("delete from registration\n" +
                    "where student_student_id = ?;");
            ps.setInt(1, Integer.parseInt(label_studentid.getText()));
            ps.executeUpdate();

            Alert alert;
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Acc deleted");
            alert.show();

            DBUtils.changeScene(event, "sample.fxml", "Log-in", null, 0);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}




