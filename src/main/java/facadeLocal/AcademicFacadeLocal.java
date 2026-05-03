package facadeLocal;

import com.example.entity.Announcement;
import com.example.entity.CalendarEvent;
import com.example.entity.Exam;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface AcademicFacadeLocal {
    
    // Takvim İşlemleri
    void saveEvent(CalendarEvent event);
    void deleteEvent(CalendarEvent event);
    List<CalendarEvent> eventList();

    // Sınav İşlemleri
    void saveExam(Exam exam);
    void deleteExam(Exam exam);
    List<Exam> examList();

    // Duyuru İşlemleri
    void saveAnnouncement(Announcement announcement);
    void deleteAnnouncement(Announcement announcement);
    List<Announcement> announcementList();
}
