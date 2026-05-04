package facade;

import com.example.entity.FreezeRequest;
import com.example.entity.Users;
import com.example.enums.FreezeStatusEnum;
import facadeLocal.FreezeRequestFacadeLocal;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class FreezeRequestFacade implements FreezeRequestFacadeLocal {

    @PersistenceContext(unitName = "obsPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void create(FreezeRequest freezeRequest) {
        em.persist(freezeRequest);
    }

    @Override
    public void edit(FreezeRequest freezeRequest) {
        em.merge(freezeRequest);
    }

    @Override
    public void remove(FreezeRequest freezeRequest) {
        em.remove(em.merge(freezeRequest));
    }

    @Override
    public FreezeRequest find(Object id) {
        return em.find(FreezeRequest.class, id);
    }

    @Override
    public List<FreezeRequest> findAll() {
        return em.createQuery("SELECT f FROM FreezeRequest f", FreezeRequest.class).getResultList();
    }

    @Override
    public List<FreezeRequest> findByStudent(Users student) {
        return em.createQuery("SELECT f FROM FreezeRequest f WHERE f.student = :student", FreezeRequest.class)
                .setParameter("student", student)
                .getResultList();
    }

    @Override
    public List<FreezeRequest> findByStatus(FreezeStatusEnum status) {
        return em.createQuery("SELECT f FROM FreezeRequest f WHERE f.status = :status", FreezeRequest.class)
                .setParameter("status", status)
                .getResultList();
    }

    @Override
    public List<FreezeRequest> findByDepartmentsAndClassesAndStatus(List<String> departments, List<String> classes, FreezeStatusEnum status) {
        List<FreezeRequest> allByStatus = findByStatus(status);
        if (departments == null || departments.isEmpty() || classes == null || classes.isEmpty()) {
            return List.of();
        }
        return allByStatus.stream()
                .filter(f -> departments.contains(f.getStudent().getBolum()) && classes.contains(f.getStudent().getSinif()))
                .collect(Collectors.toList());
    }
}
