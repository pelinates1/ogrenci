package facade;

import com.example.entity.Course;
import com.example.entity.Enrollment;
import com.example.entity.Users;
import facadeLocal.CourseFacadeLocal;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

@Stateless
public class CourseFacade extends AbstractFacade implements CourseFacadeLocal {

    @Override
    public void createCourse(Course c) {
        this.entityManager.persist(c);
        this.entityManager.flush();
    }

    @Override
    public void removeCourse(Course c) {
        Course merged = this.entityManager.merge(c);
        this.entityManager.remove(merged);
    }

    @Override
    public List<Course> courseList() {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> cq = cb.createQuery(Course.class);
        Root<Course> root = cq.from(Course.class);
        CriteriaQuery<Course> all = cq.select(root);
        TypedQuery<Course> q = this.entityManager.createQuery(all);
        return q.getResultList();
    }

    @Override
    public void enrollStudent(Enrollment e) {
        this.entityManager.persist(e);
        this.entityManager.flush();
    }

    @Override
    public void removeEnrollment(Enrollment e) {
        Enrollment merged = this.entityManager.merge(e);
        this.entityManager.remove(merged);
    }

    @Override
    public List<Course> getCoursesByTeacher(Users teacher) {
        // Öğretmene göre dersleri listele
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> cq = cb.createQuery(Course.class);
        Root<Course> root = cq.from(Course.class);

        cq.where(cb.equal(root.get("ogretmen"), teacher));

        TypedQuery<Course> q = this.entityManager.createQuery(cq);
        return q.getResultList();
    }
}
