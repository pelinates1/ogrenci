package facadeLocal;

import com.example.entity.Course;
import com.example.entity.Enrollment;
import com.example.entity.Users;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface CourseFacadeLocal {
    // Ders İşlemleri
    void createCourse(Course c);
    void removeCourse(Course c);
    List<Course> courseList();

    // Kayıt (Enrollment) İşlemleri
    void enrollStudent(Enrollment e);
    void removeEnrollment(Enrollment e);

    // Bir hocanın verdiği dersleri getiren metot
    List<Course> getCoursesByTeacher(Users teacher);
}