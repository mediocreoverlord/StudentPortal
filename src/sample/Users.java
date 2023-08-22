package sample;

public class Users {

    int courseId, studentId;
    String courseName, professorName, startTime, endTime, roomNum;
    String firstName, lastName, address, phoneNum, email, dob;


    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getCourseId() {
        return courseId;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getProfessorName() {
        return professorName;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public String getDob() {
        return dob;
    }


    public Users(int courseId, String courseName, String professorName, String startTime, String endTime, String roomNum) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.professorName = professorName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomNum = roomNum;
    }
}

//    public Users(String firstName){
//        this.firstName = firstName;
//    }}
//}
