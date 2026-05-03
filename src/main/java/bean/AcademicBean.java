package bean;

import com.example.entity.Announcement;
import com.example.entity.CalendarEvent;
import com.example.entity.Course;
import com.example.entity.Exam;
import facadeLocal.AcademicFacadeLocal;
import facadeLocal.CourseFacadeLocal;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("academicBean")
@ViewScoped
public class AcademicBean implements Serializable {

    private CalendarEvent seciliEtkinlik;
    private List<CalendarEvent> etkinlikListesi;

    private Exam seciliSinav;
    private List<Exam> sinavListesi;
    private List<Course> tumDersler;
    private Long seciliDersId;

    private Announcement seciliDuyuru;
    private List<Announcement> duyuruListesi;

    private String duyuruAramaMetni;
    private String sinavAramaMetni;
    private String etkinlikAramaMetni;

    @EJB
    private AcademicFacadeLocal academicFacade;

    @EJB
    private CourseFacadeLocal courseFacade;

    @PostConstruct
    public void init() {
        seciliEtkinlik = new CalendarEvent();
        seciliSinav = new Exam();
        seciliDuyuru = new Announcement();
        verileriGetir();
    }

    public void verileriGetir() {
        // Duyuru Filtreleme
        List<Announcement> allAnnouncements = academicFacade.announcementList();
        if (duyuruAramaMetni != null && !duyuruAramaMetni.isEmpty()) {
            duyuruListesi = allAnnouncements.stream()
                    .filter(a -> a.getTitle().toLowerCase().contains(duyuruAramaMetni.toLowerCase()) || 
                                 a.getContent().toLowerCase().contains(duyuruAramaMetni.toLowerCase()))
                    .collect(java.util.stream.Collectors.toList());
        } else {
            duyuruListesi = allAnnouncements;
        }

        // Sınav Filtreleme
        List<Exam> allExams = academicFacade.examList();
        if (sinavAramaMetni != null && !sinavAramaMetni.isEmpty()) {
            sinavListesi = allExams.stream()
                    .filter(e -> e.getCourse().getDersAdi().toLowerCase().contains(sinavAramaMetni.toLowerCase()) || 
                                 e.getExamType().toLowerCase().contains(sinavAramaMetni.toLowerCase()) ||
                                 e.getClassroom().toLowerCase().contains(sinavAramaMetni.toLowerCase()))
                    .collect(java.util.stream.Collectors.toList());
        } else {
            sinavListesi = allExams;
        }

        // Etkinlik Filtreleme
        List<CalendarEvent> allEvents = academicFacade.eventList();
        if (etkinlikAramaMetni != null && !etkinlikAramaMetni.isEmpty()) {
            etkinlikListesi = allEvents.stream()
                    .filter(e -> e.getTitle().toLowerCase().contains(etkinlikAramaMetni.toLowerCase()) || 
                                 e.getType().toLowerCase().contains(etkinlikAramaMetni.toLowerCase()))
                    .collect(java.util.stream.Collectors.toList());
        } else {
            etkinlikListesi = allEvents;
        }

        tumDersler = courseFacade.courseList();
    }

    // --- DUYURU İŞLEMLERİ ---
    public void duyuruKaydet() {
        academicFacade.saveAnnouncement(seciliDuyuru);
        mesajGoster("Başarılı", "Duyuru kaydedildi.");
        duyuruTemizle();
        duyuruListesi = academicFacade.announcementList();
    }

    public void duyuruSil(Announcement a) {
        academicFacade.deleteAnnouncement(a);
        duyuruListesi = academicFacade.announcementList();
    }

    public void duyuruFormaGetir(Announcement a) {
        this.seciliDuyuru = a;
    }

    public void duyuruTemizle() {
        this.seciliDuyuru = new Announcement();
    }

    // --- TAKVİM İŞLEMLERİ ---
    public void etkinlikKaydet() {
        academicFacade.saveEvent(seciliEtkinlik);
        mesajGoster("Başarılı", "Takvim etkinliği kaydedildi.");
        etkinlikTemizle();
        etkinlikListesi = academicFacade.eventList();
    }

    public void etkinlikSil(CalendarEvent e) {
        academicFacade.deleteEvent(e);
        etkinlikListesi = academicFacade.eventList();
    }

    public void etkinlikFormaGetir(CalendarEvent e) {
        this.seciliEtkinlik = e;
    }

    public void etkinlikTemizle() {
        this.seciliEtkinlik = new CalendarEvent();
    }

    // --- SINAV İŞLEMLERİ ---
    public void sinavKaydet() {
        try {
            if (seciliDersId == null) {
                mesajGoster("Hata", "Lütfen bir ders seçiniz!");
                return;
            }

            // Dersi listeden bulup gerçek nesneyi ata
            boolean dersBulundu = false;
            for (Course c : tumDersler) {
                if (c.getId().equals(seciliDersId)) {
                    seciliSinav.setCourse(c);
                    dersBulundu = true;
                    break;
                }
            }

            if (!dersBulundu) {
                mesajGoster("Hata", "Seçilen ders bulunamadı!");
                return;
            }

            academicFacade.saveExam(seciliSinav);
            mesajGoster("Başarılı", "Sınav programı başarıyla güncellendi.");
            sinavTemizle();
            verileriGetir(); // Listeyi en baştan çekerek garantile
        } catch (Exception e) {
            mesajGoster("Hata", "Sınav kaydedilirken teknik bir sorun oluştu: " + e.getMessage());
        }
    }

    public void sinavSil(Exam e) {
        academicFacade.deleteExam(e);
        sinavListesi = academicFacade.examList();
    }

    public void sinavFormaGetir(Exam e) {
        this.seciliSinav = e;
        if (e.getCourse() != null) {
            this.seciliDersId = e.getCourse().getId();
        }
    }

    public void sinavTemizle() {
        this.seciliSinav = new Exam();
        this.seciliDersId = null;
    }

    private void mesajGoster(String baslik, String icerik) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, baslik, icerik));
    }

    // --- GETTER & SETTER ---
    public CalendarEvent getSeciliEtkinlik() { return seciliEtkinlik; }
    public void setSeciliEtkinlik(CalendarEvent seciliEtkinlik) { this.seciliEtkinlik = seciliEtkinlik; }
    public List<CalendarEvent> getEtkinlikListesi() { return etkinlikListesi; }
    public Exam getSeciliSinav() { return seciliSinav; }
    public void setSeciliSinav(Exam seciliSinav) { this.seciliSinav = seciliSinav; }
    public List<Exam> getSinavListesi() { return sinavListesi; }
    public List<Course> getTumDersler() { return tumDersler; }
    public Long getSeciliDersId() { return seciliDersId; }
    public void setSeciliDersId(Long seciliDersId) { this.seciliDersId = seciliDersId; }
    
    public Announcement getSeciliDuyuru() { return seciliDuyuru; }
    public void setSeciliDuyuru(Announcement seciliDuyuru) { this.seciliDuyuru = seciliDuyuru; }
    public List<Announcement> getDuyuruListesi() { return duyuruListesi; }

    public String getDuyuruAramaMetni() { return duyuruAramaMetni; }
    public void setDuyuruAramaMetni(String duyuruAramaMetni) { this.duyuruAramaMetni = duyuruAramaMetni; }
    public String getSinavAramaMetni() { return sinavAramaMetni; }
    public void setSinavAramaMetni(String sinavAramaMetni) { this.sinavAramaMetni = sinavAramaMetni; }
    public String getEtkinlikAramaMetni() { return etkinlikAramaMetni; }
    public void setEtkinlikAramaMetni(String etkinlikAramaMetni) { this.etkinlikAramaMetni = etkinlikAramaMetni; }
}
