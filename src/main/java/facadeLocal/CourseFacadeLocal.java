package facadeLocal;

import com.example.entity.Course;
import com.example.entity.Enrollment;
import com.example.entity.Users;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface CourseFacadeLocal {
    void create(Course c);
    void edit(Course c);
    void remove(Course c);
    List<Course> courseList();
    List<Users> teacherList(); // Eksik olan hoca listesi metodu

    void enrollStudent(Enrollment e);
    void removeEnrollment(Enrollment e);
    List<Course> getCoursesByTeacher(Users teacher);
}