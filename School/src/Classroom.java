import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class Classroom {
    
    private static int uid = 101;
    private int id;
    private Standard standard;
    private Section section;
    private Teacher classTeacher;
    private ArrayList<Student> students;
    private ArrayList<Subject> subjects;
    private HashMap<Subject,Teacher> subjectTeachers;
    
    public Classroom(Standard standard, Section section) {
        this.id = uid++;
        this.standard = standard;
        this.section = section;
        students = new ArrayList<>();
        subjects = new ArrayList<>();
        subjectTeachers = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public void setClassTeacher(Teacher teacher) {
        if(teacher != null){
            this.classTeacher = teacher;
            DBHelper.updateData(this);
        }
    }
    
    public Standard getStandard() {
        return standard;
    }

    public Section getSection() {
        return section;
    }

    public Teacher getClassTeacher() {
        return classTeacher;
    }

    public void addStudent(Student s){
        if(s != null){
            students.add(s);
            DBHelper.updateStudent(this,s);
        }
          
    }

    public void removeStudent(Student s){
        students.remove(s);
    }

    public void addSubject(Subject s){
        if(!subjectTeachers.containsKey(s)){
            subjects.add(s);
            subjectTeachers.put(s, null);
        }
    }

    public boolean setSubjectTeacher(Subject s, Teacher t){
        if(subjectTeachers.containsKey(s) && t != null){
            subjectTeachers.put(s, t);
            DBHelper.storeData(this,s,t);
            return true;
        }
        return false;
    }

    public boolean addSubjectTeacher(Subject s, Teacher t){
        if(s == null || t == null) return false;
        if(subjectTeachers.containsKey(s)){
            return false;
        }
        if(!DBHelper.storeData(this,s,t)){
            return false;
        }
        subjects.add(s);
        subjectTeachers.put(s, t);
        return true;
    }

    @Override
    public String toString() {
        return standard + " " + section;
    }

    public boolean showSubjects(){
        if(subjects.size() == 0) return false;
        for(int i=0 ; i<subjects.size() ; i++){
            System.out.println((i+1) + "->" + subjects.get(i));
        }
        return true;
    }

    public Subject getSubject(int n) {
        try {
            return subjects.get(n);
        } catch (Exception e) {
            return null;
        } 
    }

	public Iterator<Student> getStudents() {
        return Collections.unmodifiableList(students).iterator();
	}

}
