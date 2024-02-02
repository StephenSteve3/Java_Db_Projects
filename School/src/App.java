import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.Year;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

public class App {

    static Scanner in = new Scanner(System.in);
    static SimpleDateFormat sdf= new SimpleDateFormat("dd-MM-yyyy");

    public static void main(String[] args) {
        School school = new School("ABC School");
        System.out.println(school);
        while(true){
            try {
                System.out.println("---------------------------------------------------------");
                System.out.println("\n1.Add Student \n2.Add Teacher \n3.Add Subject \n4.Add Section ");
                System.out.println("5.Add Classroom \n6.Set Class teacher \n7.Set Subject teachers ");
                System.out.println("8.Add Exam \n9.Add Marks \n10.Exit ");
                System.out.println("\nEnter your choice:");
                int ch = in.nextInt();
                System.out.println("---------------------------------------------------------");
                switch(ch){
                    case 1:
                        Student s = createStudent();
                        school.addStudent(s);
                        Classroom classroom = selectClassroom(school);
                        classroom.addStudent(s);
                        System.out.println("Student Added!");
                        break;
                    
                    case 2:
                        Teacher t = createTeacher();
                        school.addTeacher(t);
                        System.out.println("Teacher Added!");
                        break;

                    case 3:
                        String name = getName();
                        school.addSubject(name);
                        System.out.println("Subject Added!");
                        break;

                    case 4:
                        school.addSection();
                        System.out.println("Section Added!");
                        break;

                    case 5:
                        if(school.addClassroom(selectStandard(), selectSection(school))){
                            System.out.println("Classroom Added!");
                        } else{
                            System.out.println("Classroom not created!");
                        }
                        break;

                    case 6:
                        Classroom classroom1 = selectClassroom(school);
                        Teacher tt = selectTeacher(school);
                        classroom1.setClassTeacher(tt);
                        System.out.println("Class Teacher Updated!");
                        break;

                    case 7: 
                        Classroom classroom2 = selectClassroom(school);
                        addSubjectTeacher(school,classroom2);
                        break;

                    case 8:
                        Exam exam = createExam();
                        school.addExam(exam);
                        System.out.println("Exam Added!");
                        break;

                    case 9:
                        getMarks(school);
                        System.out.println("Marks Added!");
                        break;

                    case 10:
                        DBHelper.closeResource();
                        System.exit(0);

                    default:
                        System.out.println("Invalid Input!");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private static void getMarks(School school) {
        Classroom classroom = selectClassroom(school);
        Exam exam = selectExam(school);
        if(exam == null) return;
        Subject subject = selectSubject(classroom);
        if(subject == null) return;
        Iterator<Student> sIterator = classroom.getStudents();
        while(sIterator.hasNext()){
            Student s = sIterator.next();
            System.out.println(s);
            System.out.println("Enter Mark (-1 for absent):");
            int mark = -2;
            while(mark>100 || mark<-1){
                mark = in.nextInt();
            }
            DBHelper.storeData(s,classroom,exam,subject,mark);
        }
    }

    private static Exam selectExam(School school) {
        if(!school.showExams()){
            System.out.println("No Exams available!");
            return null;
        }
        Exam exam = null;
        while(exam == null){
            System.out.println("Select Exam :");
            int n = in.nextInt();
            exam = school.getExam(n-1);
        }
        return exam;
    }

    private static Subject selectSubject(Classroom classroom) {
        if(!classroom.showSubjects()){
            System.out.println("No Subjects available!");
            return null;
        }
        Subject subject = null;
        while (subject == null) {
            System.out.println("Select Subject :");
            int n = in.nextInt();
            subject = classroom.getSubject(n-1);
        }
        return subject;
    }

    private static Exam createExam() {
        String name = getName();
        System.out.println("Enter Month no(1 - 12):");
        int n;
        Month m = null;
        while(m == null){
            try{
                n = in.nextInt();
                m = Month.of(n);
            }
            catch(Exception e){
                System.out.println("Invalid Month number! \nTry Again!");
            }
        }
        System.out.println("Enter Year:");
        Year y = null;
        while(y == null){
            try{
                n = in.nextInt();
                if((n+"").length()!=4) throw new Exception();
                y = Year.of(n);
            }
            catch(Exception e){
                System.out.println("Invalid Year! \nTry Again!");
            }
        }
        return new Exam(name, m, y);
    }

    private static void addSubjectTeacher(School school, Classroom classroom) {
        while(true){
            Subject subject = selectSubject(school);
            Teacher teacher = selectTeacher(school);
            if(teacher == null) return;
            if(classroom.addSubjectTeacher(subject, teacher)) System.out.println("Subject teacher added!");
            else  System.out.println("Not Added!");
            System.out.println("Press 0 to STOP and any number to CONTINUE:");
            int s = in.nextInt();
            if(s == 0){
                break;
            }
        }
    }

    private static Subject selectSubject(School school) {
        school.showSubjects();
        Subject subject = null;
        while(subject == null){
            System.out.println("Select Subject:");
            int ch = in.nextInt();
            subject = school.getSubject(ch-1);
        }
        return subject;
    }

    private static Classroom selectClassroom(School school) {
        school.showClassrooms();
        Classroom classroom = null;
        while(classroom == null){
            System.out.println("Select Classroom:");
            int ch = in.nextInt();
            classroom = school.getClassroom(ch-1);
        }
        return classroom;
    }

    private static Teacher selectTeacher(School school) {
        if(!school.showTeachers()){
            System.out.println("No Teachers Here!");
            return null;
        } 
        Teacher teacher = null;
        while(teacher == null){
            System.out.println("Select Teacher:");
            int ch = in.nextInt();
            teacher = school.getTeacher(ch-1);
        }
        return teacher;
    }

    private static Section selectSection(School school) {
        school.showSections();
        Section section = null;
        while(section == null){
            System.out.println("Select Section:");
            int ch = in.nextInt();
            section = school.getSection(ch-1);
        }
        return section;
    }

    private static Standard selectStandard() {
        Standard[] standards = Standard.values();
        for(int i=0 ; i<standards.length ; i++){
            System.out.println((i+1) +" -> " + standards[i]);
        }
        while (true) {
            System.out.println("Select Standard : ");
            int ch = in.nextInt();
            if(ch > 0 && ch < standards.length)
                return standards[ch-1];
        }
    }

    private static String getName() {
        in.nextLine();
        System.out.println("Enter name : ");
        String name =  null;
        while (name == null || name.equals("")){
            name = in.nextLine();
        }
        return name;
    }

    private static Teacher createTeacher() {
        in.nextLine();
        System.out.println("Enter Teacher name : ");
        String name = in.nextLine();
        System.out.println("Enter Date of Birth(dd-MM-yyyy): ");
        String dobs;
        Date dob = null;
        while(dob == null){
            try {
                dobs = in.nextLine();
                dob = sdf.parse(dobs);
            } catch (ParseException e) {
                System.out.println("Invalid Date");
            }
        }
        Gender g = selectGender();
        BloodGroup bg = selectBloodGroup();
        Degree dg = selectDegree();
        return new Teacher(name, dob, g, bg, dg);
    }

    private static Degree selectDegree() {
        System.out.println();
        Degree[] degrees = Degree.values();
        for(int i=0 ; i<degrees.length ; i++){
            System.out.println((i+1) +" -> " + degrees[i]);
        }
        while (true) {
            System.out.println("Select Degree : ");
            int ch = in.nextInt();
            if(ch > 0 && ch < degrees.length)
                return degrees[ch-1];
        }
    }


    private static Student createStudent() {
        in.nextLine();
        System.out.println("Enter Student name : ");
        String name = in.nextLine();
        System.out.println("Enter Date of Birth(dd-MM-yyyy): ");
        String dobs;
        Date dob = null;
        while(dob == null){
            try {
                dobs = in.nextLine();
                dob = sdf.parse(dobs);
            } catch (ParseException e) {
                System.out.println("Invalid Date");
            }
        }
        Gender g = selectGender();
        BloodGroup bg = selectBloodGroup();
        return new Student(name, dob, g, bg);
    }

    private static BloodGroup selectBloodGroup() {
        BloodGroup[] bGroups = BloodGroup.values();
        System.out.println();
        for(int i=0 ; i<bGroups.length ; i++){
            System.out.println((i+1) +" -> " + bGroups[i]);
        }
        while (true) {
            System.out.println("Select Blood Group : ");
            int ch = in.nextInt();
            if(ch > 0 && ch < bGroups.length)
                return bGroups[ch-1];
        }
    }

    private static Gender selectGender() {
        System.out.println("\n1 -> Male \n2 -> Female");
        while(true){
            System.out.println("Select gender : ");
            int ch = in.nextInt();
            switch(ch){
                case 1 : return Gender.MALE;
                case 2 : return Gender.FEMALE;
                default : break;
            }
        }
    }

}