package facade;

import com.example.entity.Course;
import com.example.entity.Enrollment;
import com.example.entity.Users;
import facadeLocal.CourseFacadeLocal;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class CourseFacade implements CourseFacadeLocal {

    @PersistenceContext(unitName = "obsPU")
    private EntityManager em;

    @Override
    public void create(Course c) {
        em.persist(c);
    }

    @Override
    public void edit(Course c) {
        em.merge(c);
    }

    @Override
    public void remove(Course c) {
        em.remove(em.merge(c));
    }

    @Override
    public List<Course> courseList() {
        return em.createQuery("SELECT c FROM Course c", Course.class).getResultList();
    }

    @Override
    public List<Users> teacherList() {
        return em.createQuery("SELECT u FROM Users u WHERE u.rol = com.example.enums.RoleEnum.TEACHER", Users.class).getResultList();
    }

    @Override
    public void enrollStudent(Enrollment e) {
        em.persist(e);
    }

    @Override
    public void removeEnrollment(Enrollment e) {
        em.remove(em.merge(e));
    }

    @Override
    public List<Course> getCoursesByTeacher(Users teacher) {
        return em.createQuery("SELECT c FROM Course c WHERE c.ogretmen = :teacher", Course.class)
                 .setParameter("teacher", teacher)
                 .getResultList();
    }
}
