package facade;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public abstract class AbstractFacade {

    @PersistenceContext(unitName = "obsPU")
    protected EntityManager entityManager;

}