import java.util.ArrayList;

public class School {
    
    private String name;
    private ArrayList<Student> students;
    private ArrayList<Teacher> teachers;
    private ArrayList<Classroom> classrooms;
    private ArrayList<Section> sections;
    private ArrayList<Subject> subjects;
    private ArrayList<Exam> exams;

    public School(String name) {
        this.name = name;
        students = new ArrayList<>();
        teachers = new ArrayList<>();
        classrooms = new ArrayList<>();
        sections = new ArrayList<>();
        subjects = new ArrayList<>();
        exams = new ArrayList<>();
        initialize();
    }

    private void initialize(){
        addSection();addSection();
        this.addClassroom(Standard.PRE_K_G, sections.get(0));
        this.addClassroom(Standard.L_K_G, sections.get(0));
        this.addClassroom(Standard.U_K_G, sections.get(0));
        this.addClassroom(Standard.I, sections.get(0));
        this.addClassroom(Standard.II, sections.get(0));
        this.addClassroom(Standard.III, sections.get(0));
        this.addClassroom(Standard.IV, sections.get(0));
        this.addClassroom(Standard.V, sections.get(0));
        this.addClassroom(Standard.VI, sections.get(0));
        this.addClassroom(Standard.VII, sections.get(0));
        this.addClassroom(Standard.VIII, sections.get(0));
        this.addClassroom(Standard.IX, sections.get(0));
        this.addClassroom(Standard.X, sections.get(0));
        this.addClassroom(Standard.XI, sections.get(0));
        this.addClassroom(Standard.XII, sections.get(0));
        this.addSubject("Tamil");
        this.addSubject("English");
        this.addSubject("Maths");
        this.addSubject("Science");
        this.addSubject("Social");
        this.addSubject("Physics");
        this.addSubject("Chemistry");
        this.addSubject("Botany");
        this.addSubject("Zoology");
        this.addSubject("Computer Science");
        this.addSubject("Accounts");
        this.addSubject("Commerce");
        this.addSubject("Computer Appication");
        this.addSubject("English");
        this.addSubject("Economics");
    }

    public String getName() {
        return name;
    }

    public void addStudent(Student s){
        students.add(s);
        DBHelper.storeData(s);
    }

    public void addTeacher(Teacher t){
        teachers.add(t);
        DBHelper.storeData(t);
    }

    public boolean addClassroom(Standard standard, Section section){
        if(isClassroom(standard, section)){
            return false;
        }
        Classroom classroom = new Classroom(standard, section);
        classrooms.add(classroom);
        DBHelper.storeData(classroom);
        return true;
    }

    private boolean isClassroom(Standard standard, Section section){
        for(Classroom classroom : classrooms){
            if(classroom.getStandard() == standard && classroom.getSection() == section){
                return true;
            }
        }
        return false;
    }

    public void showSubjects() {
        for(int i=0 ; i<subjects.size() ; i++){
            System.out.println((i+1) + "->" + subjects.get(i));
        }
    }

    public Subject getSubject(int n) {
        try {
            return subjects.get(n);
        } catch (Exception e) {
            return null;
        }  
    }

    public void addSection(){
        Section s = Section.generateSection();
        sections.add(s);
        DBHelper.storeData(s);
    }

    public void addSubject(String name){
        Subject s = new Subject(name);
        subjects.add(s);
        DBHelper.storeData(s);
    }

    public void showSections() {
        for(int i=0 ; i<sections.size() ; i++){
            System.out.println((i+1) + "->" + sections.get(i));
        }
    }

    public void showClassrooms() {
        for(int i=0 ; i<classrooms.size() ; i++){
            System.out.println((i+1) + "->" + classrooms.get(i));
        }
    }

    public boolean showTeachers(){
        if(teachers.size() == 0) return false;
        for(int i=0 ; i<teachers.size() ; i++){
            System.out.println((i+1) + "->" + teachers.get(i));
        }
        return true;
    }

    public Classroom getClassroom(int n) {
        try {
            return classrooms.get(n);
        } catch (Exception e) {
            return null;
        }  
    }

    public Teacher getTeacher(int n) {
        try {
            return teachers.get(n);
        } catch (Exception e) {
            return null;
        }  
    }

    public Section getSection(int n) {
        try {
            return sections.get(n);
        } catch (Exception e) {
            return null;
        }  
    }

    public void addExam(Exam exam){
        if(exam != null){
            exams.add(exam);
            DBHelper.storeData(exam);
        }
    }

    public boolean showExams() {
        if(exams.size() == 0) return false;
        for(int i=0 ; i<exams.size() ; i++){
            System.out.println((i+1) + "->" + exams.get(i));
        }
        return true;
    }

    public Exam getExam(int i) {
        try {
            return exams.get(i);
        } catch (Exception e) {
            return null;
        }  
    }

    @Override
    public String toString() {
        return name;
    }

}