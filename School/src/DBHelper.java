import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBHelper {
    
    private static String url;
    private static String username;
    private static String password;
    private static Connection con;
    private static PreparedStatement studentInsertStatement;
    private static PreparedStatement teacherInsertStatement;
    private static PreparedStatement subjectInsertStatement;
    private static PreparedStatement sectionInsertStatement;
    private static PreparedStatement standardInsertStatement;
    private static PreparedStatement classroomInsertStatement3;
    private static PreparedStatement classroomInsertStatement4;
    private static PreparedStatement classroomUpdateStatement;
    private static PreparedStatement classroomStudentUpdateStatement;
    private static PreparedStatement subjectTeacherInsertStatement;
    private static PreparedStatement examInsertStatement;
    private static PreparedStatement subjectTeacherSelectStatement;
    private static PreparedStatement markInsertStatement;

    static{
        url = "jdbc:postgresql://localhost:5432/studentdetails";
        username = "postgres";
        password = "1234";
        try {
            con = DriverManager.getConnection(url, username, password);
            studentInsertStatement = con.prepareStatement("INSERT INTO STUDENT VALUES(?,?,?,?,?)");
            teacherInsertStatement = con.prepareStatement("INSERT INTO TEACHER VALUES(?,?,?,?,?,?)");
            subjectInsertStatement = con.prepareStatement("INSERT INTO SUBJECT VALUES(?,?)");
            sectionInsertStatement = con.prepareStatement("INSERT INTO SECTION VALUES(?,?)");
            standardInsertStatement = con.prepareStatement("INSERT INTO STANDARD VALUES(?,?)");
            classroomInsertStatement3 = con.prepareStatement("INSERT INTO CLASSROOM VALUES(?,?,?)");
            classroomInsertStatement4 = con.prepareStatement("INSERT INTO CLASSROOM VALUES(?,?,?,?)");
            classroomUpdateStatement = con.prepareStatement("UPDATE CLASSROOM SET TEACHER_ID = ? WHERE id = ?");
            classroomStudentUpdateStatement = con.prepareStatement("INSERT INTO CLASSROOM_STUDENT VALUES(?,?)");
            subjectTeacherInsertStatement = con.prepareStatement("INSERT INTO SUBJECT_TEACHER(CLASSROOM_ID,SUBJECT_ID,TEACHER_ID) VALUES(?,?,?)");
            examInsertStatement = con.prepareStatement("INSERT INTO EXAMS VALUES(?,?,?,?)");
            markInsertStatement = con.prepareStatement("INSERT INTO MARKS(STUDENT_ID,EXAM_ID,SUBJECT_TEACHER_ID,MARKS) VALUES(?,?,?,?)");
            subjectTeacherSelectStatement = con.prepareStatement("SELECT ID FROM SUBJECT_TEACHER WHERE CLASSROOM_ID = ? AND SUBJECT_ID = ?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean storeData(Teacher t) {
        try {
            teacherInsertStatement.setInt(1, t.getId());
            teacherInsertStatement.setString(2, t.getName());
            teacherInsertStatement.setDate(3, t.getDob());
            teacherInsertStatement.setString(4, t.getGender().toString());
            teacherInsertStatement.setString(5, t.getBloodGroup().toString());
            teacherInsertStatement.setString(6, t.getDegree().toString());
            teacherInsertStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public static boolean storeData(Student s) {
        try {
            studentInsertStatement.setInt(1, s.getId());
            studentInsertStatement.setString(2, s.getName());
            studentInsertStatement.setDate(3, s.getDob());
            studentInsertStatement.setString(4, s.getGender().toString());
            studentInsertStatement.setString(5, s.getBloodGroup().toString());
            studentInsertStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }  
    }

    public static boolean storeData(Subject s){
        try {
            subjectInsertStatement.setInt(1, s.getId());
            subjectInsertStatement.setString(2, s.getName());
            subjectInsertStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public static void closeResource(){
        try {
            con.close();
        } catch (SQLException e) {}
    }

    public static boolean storeData(Section s) {
        try {
            sectionInsertStatement.setInt(1, s.getId());
            sectionInsertStatement.setString(2, s.getName()+"");
            sectionInsertStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean storeData(Classroom classroom) {
        if(classroom.getClassTeacher() != null){
            return storeData4(classroom);
        }
        return storeData3(classroom);
    }

    private static boolean storeData3(Classroom classroom) {
        try {
            classroomInsertStatement3.setInt(1, classroom.getId());
            classroomInsertStatement3.setInt(2, classroom.getStandard().ordinal());
            classroomInsertStatement3.setInt(3, classroom.getSection().getId());
            classroomInsertStatement3.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    private static boolean storeData4(Classroom classroom) {
        try {

            classroomInsertStatement4.setInt(1, classroom.getId());
            classroomInsertStatement4.setInt(2, classroom.getStandard().ordinal());
            classroomInsertStatement4.setInt(3, classroom.getSection().getId());
            classroomInsertStatement4.setInt(4, classroom.getClassTeacher().getId());
            classroomInsertStatement4.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public static boolean storeData(Standard standard) {
        try {
            standardInsertStatement.setInt(1, standard.ordinal());
            standardInsertStatement.setString(2, standard.toString()+"");
            standardInsertStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public static boolean updateData(Classroom classroom) {
        try {
            classroomUpdateStatement.setInt(1, classroom.getClassTeacher().getId());
            classroomUpdateStatement.setInt(2, classroom.getId());
            classroomUpdateStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public static boolean updateStudent(Classroom classroom, Student s) {
        try {
            classroomStudentUpdateStatement.setInt(1, s.getId());
            classroomStudentUpdateStatement.setInt(2, classroom.getId());
            classroomStudentUpdateStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public static boolean storeData(Classroom classroom, Subject s, Teacher t) {
        try {
            subjectTeacherInsertStatement.setInt(1, classroom.getId());
            subjectTeacherInsertStatement.setInt(2, s.getId());
            subjectTeacherInsertStatement.setInt(3, t.getId());
            subjectTeacherInsertStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public static boolean storeData(Exam exam) {
        try {
            examInsertStatement.setInt(1, exam.getId());
            examInsertStatement.setString(2, exam.getName());
            examInsertStatement.setString(3, exam.getMonth().toString());
            examInsertStatement.setInt(4, exam.getYear().getValue());
            examInsertStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }   
    }

    public static void storeData(Student s, Classroom classroom, Exam exam, Subject subject, int mark) {
        int id = getId(classroom,subject);
        try {
            markInsertStatement.setInt(1, s.getId());
            markInsertStatement.setInt(2, exam.getId());
            markInsertStatement.setInt(3, id);
            markInsertStatement.setInt(4, mark);
            markInsertStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private static int getId(Classroom classroom, Subject subject) {
        try{
            subjectTeacherSelectStatement.setInt(1, classroom.getId());
            subjectTeacherSelectStatement.setInt(2, subject.getId());
            ResultSet resultSet = subjectTeacherSelectStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt(1);
            }
        } catch(SQLException e){
            System.out.println(e);
        }
        return 0;
    }
}
