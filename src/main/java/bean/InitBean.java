package bean;

import com.example.entity.Users;
import com.example.enums.RoleEnum;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Singleton
@Startup
public class InitBean {

    @PersistenceContext(unitName = "obsPU")
    private EntityManager em;

    @PostConstruct
    @Transactional
    public void init() {
        // Check if Admin exists
        if (em.createQuery("SELECT u FROM Users u WHERE u.okulNo = 'admin'").getResultList().isEmpty()) {
            Users admin = new Users();
            admin.setOkulNo("admin");
            admin.setSifre("admin123");
            admin.setAd("Sistem");
            admin.setSoyad("Yöneticisi");
            admin.setRol(RoleEnum.ADMIN);
            em.persist(admin);
        }

        // Check if Teacher exists
        if (em.createQuery("SELECT u FROM Users u WHERE u.okulNo = 'teacher'").getResultList().isEmpty()) {
            Users teacher = new Users();
            teacher.setOkulNo("teacher");
            teacher.setSifre("teacher123");
            teacher.setAd("Ahmet");
            teacher.setSoyad("Hoca");
            teacher.setRol(RoleEnum.TEACHER);
            em.persist(teacher);
        }

        // Check if Student exists
        if (em.createQuery("SELECT u FROM Users u WHERE u.okulNo = 'student'").getResultList().isEmpty()) {
            Users student = new Users();
            student.setOkulNo("student");
            student.setSifre("student123");
            student.setAd("Ali");
            student.setSoyad("Öğrenci");
            student.setRol(RoleEnum.STUDENT);
            em.persist(student);
        }
    }
}
