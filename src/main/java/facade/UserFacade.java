package facade;

import com.example.entity.Users;
import com.example.enums.RoleEnum;
import facadeLocal.UserFacadeLocal;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;

@Stateless
public class UserFacade extends AbstractFacade implements UserFacadeLocal {

    @Override
    public void createUser(Users u) {
        this.entityManager.persist(u); // INSERT INTO yerine geçer
        this.entityManager.flush();
    }

    @Override
    public Users editUser(Users entity) {
        this.entityManager.merge(entity); // UPDATE yerine geçer
        this.entityManager.flush();
        return entity;
    }

    @Override
    public void remove(Users entity) {
        Users merged = this.entityManager.merge(entity);
        this.entityManager.remove(merged); // DELETE yerine geçer
    }

    @Override
    public List<Users> usersList() {
        // Tüm kullanıcıları listeleme (SELECT * FROM Users)
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);
        CriteriaQuery<Users> all = cq.select(root);
        TypedQuery<Users> q = this.entityManager.createQuery(all);
        return q.getResultList();
    }

    @Override
    public Users login(String okulNo, String sifre) {
        // Okul No ve Şifreye göre veritabanında arama yapma
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);

        // WHERE okulNo = ? AND sifre = ?
        cq.where(
                cb.equal(root.get("okulNo"), okulNo),
                cb.equal(root.get("sifre"), sifre)
        );

        CriteriaQuery<Users> all = cq.select(root);
        TypedQuery<Users> q = this.entityManager.createQuery(all);
        List<Users> found = q.getResultList();

        // Eğer liste boşsa (kullanıcı yoksa) null dön, varsa ilk kullanıcıyı dön
        return found.isEmpty() ? null : found.get(0);
    }

    @Override
    public Users loginByRole(String okulNo, RoleEnum role) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);

        cq.where(
                cb.equal(root.get("okulNo"), okulNo),
                cb.equal(root.get("rol"), role)
        );

        TypedQuery<Users> q = this.entityManager.createQuery(cq.select(root));
        List<Users> found = q.getResultList();
        return found.isEmpty() ? null : found.get(0);
    }
}