package facade;

import com.example.entity.AdvisorAssignment;
import com.example.entity.Users;
import facadeLocal.AdvisorAssignmentFacadeLocal;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class AdvisorAssignmentFacade implements AdvisorAssignmentFacadeLocal {

    @PersistenceContext(unitName = "obsPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void create(AdvisorAssignment advisorAssignment) {
        em.persist(advisorAssignment);
    }

    @Override
    public void edit(AdvisorAssignment advisorAssignment) {
        em.merge(advisorAssignment);
    }

    @Override
    public void remove(AdvisorAssignment advisorAssignment) {
        em.remove(em.merge(advisorAssignment));
    }

    @Override
    public AdvisorAssignment find(Object id) {
        return em.find(AdvisorAssignment.class, id);
    }

    @Override
    public List<AdvisorAssignment> findAll() {
        return em.createQuery("SELECT a FROM AdvisorAssignment a", AdvisorAssignment.class).getResultList();
    }

    @Override
    public AdvisorAssignment findByDepartmentAndClass(String department, String studentClass) {
        try {
            return em.createQuery("SELECT a FROM AdvisorAssignment a WHERE a.department = :dept AND a.studentClass = :cls", AdvisorAssignment.class)
                    .setParameter("dept", department)
                    .setParameter("cls", studentClass)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<AdvisorAssignment> findByTeacher(Users teacher) {
        return em.createQuery("SELECT a FROM AdvisorAssignment a WHERE a.teacher = :teacher", AdvisorAssignment.class)
                .setParameter("teacher", teacher)
                .getResultList();
    }
}
