package facade;

import com.example.entity.Announcement;
import com.example.entity.CalendarEvent;
import com.example.entity.Exam;
import facadeLocal.AcademicFacadeLocal;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class AcademicFacade implements AcademicFacadeLocal {

    @PersistenceContext(unitName = "obsPU")
    private EntityManager em;

    @Override
    public void saveEvent(CalendarEvent event) {
        if (event.getId() == null) {
            em.persist(event);
        } else {
            em.merge(event);
        }
    }

    @Override
    public void deleteEvent(CalendarEvent event) {
        em.remove(em.merge(event));
    }

    @Override
    public List<CalendarEvent> eventList() {
        return em.createQuery("SELECT e FROM CalendarEvent e ORDER BY e.eventDate ASC", CalendarEvent.class).getResultList();
    }

    @Override
    public void saveExam(Exam exam) {
        if (exam.getId() == null) {
            em.persist(exam);
        } else {
            em.merge(exam);
        }
    }

    @Override
    public void deleteExam(Exam exam) {
        em.remove(em.merge(exam));
    }

    @Override
    public List<Exam> examList() {
        return em.createQuery("SELECT e FROM Exam e ORDER BY e.examDateTime ASC", Exam.class).getResultList();
    }

    @Override
    public void saveAnnouncement(Announcement announcement) {
        if (announcement.getId() == null) {
            em.persist(announcement);
        } else {
            em.merge(announcement);
        }
    }

    @Override
    public void deleteAnnouncement(Announcement announcement) {
        em.remove(em.merge(announcement));
    }

    @Override
    public List<Announcement> announcementList() {
        return em.createQuery("SELECT a FROM Announcement a ORDER BY a.publishDate DESC", Announcement.class).getResultList();
    }
}
