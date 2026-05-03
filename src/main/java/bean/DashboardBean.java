package bean;

import com.example.entity.CalendarEvent;
import com.example.entity.Exam;
import com.example.entity.Users;
import com.example.enums.RoleEnum;
import facadeLocal.AcademicFacadeLocal;
import facadeLocal.CourseFacadeLocal;
import facadeLocal.UserFacadeLocal;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Named("dashboardBean")
@ViewScoped
public class DashboardBean implements Serializable {

    private long ogrenciSayisi;
    private long ogretmenSayisi;
    private long dersSayisi;
    
    private List<Exam> yaklasanSinavlar;
    private List<CalendarEvent> yaklasanEtkinlikler;

    @EJB
    private UserFacadeLocal userFacade;

    @EJB
    private CourseFacadeLocal courseFacade;
    
    @EJB
    private AcademicFacadeLocal academicFacade;

    @PostConstruct
    public void init() {
        sayaclariGuncelle();
        raporlariHazirla();
    }

    public void sayaclariGuncelle() {
        List<Users> tumKullanicilar = userFacade.usersList();
        if (tumKullanicilar != null) {
            ogrenciSayisi = tumKullanicilar.stream().filter(u -> u.getRol() == RoleEnum.STUDENT).count();
            ogretmenSayisi = tumKullanicilar.stream().filter(u -> u.getRol() == RoleEnum.TEACHER).count();
        }
        dersSayisi = courseFacade.courseList().size();
    }

    public void raporlariHazirla() {
        // Son 5 sınavı ve takvimi getir
        List<Exam> tumSinavlar = academicFacade.examList();
        if (tumSinavlar != null) {
            yaklasanSinavlar = tumSinavlar.stream().limit(5).collect(Collectors.toList());
        }

        List<CalendarEvent> tumTakvim = academicFacade.eventList();
        if (tumTakvim != null) {
            yaklasanEtkinlikler = tumTakvim.stream().limit(5).collect(Collectors.toList());
        }
    }

    // Getter & Setter
    public long getOgrenciSayisi() { return ogrenciSayisi; }
    public long getOgretmenSayisi() { return ogretmenSayisi; }
    public long getDersSayisi() { return dersSayisi; }
    public List<Exam> getYaklasanSinavlar() { return yaklasanSinavlar; }
    public List<CalendarEvent> getYaklasanEtkinlikler() { return yaklasanEtkinlikler; }
}
