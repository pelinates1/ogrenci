package bean;

import com.example.entity.Course;
import com.example.entity.Users;
import facadeLocal.CourseFacadeLocal;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class CourseBean implements Serializable {

    private Course seciliDers = new Course();
    private List<Course> dersListesi;
    private List<Users> ogretmenListesi;
    private Long seciliOgretmenId;
    private String aramaMetni;

    @EJB
    private CourseFacadeLocal courseFacade;

    @PostConstruct
    public void init() {
        dersleriGetir();
        ogretmenListesi = courseFacade.teacherList();
    }

    public void dersleriGetir() {
        List<Course> allCourses = courseFacade.courseList();
        if (aramaMetni != null && !aramaMetni.isEmpty()) {
            dersListesi = allCourses.stream()
                    .filter(c -> (c.getDersAdi() != null && c.getDersAdi().toLowerCase().contains(aramaMetni.toLowerCase())) || 
                                 (c.getDersKodu() != null && c.getDersKodu().toLowerCase().contains(aramaMetni.toLowerCase())))
                    .collect(Collectors.toList());
        } else {
            dersListesi = allCourses;
        }
    }

    public void kaydet() {
        try {
            if (seciliOgretmenId != null) {
                Users ogretmen = new Users();
                ogretmen.setId(seciliOgretmenId);
                seciliDers.setOgretmen(ogretmen);
            }

            if (seciliDers.getId() == null) {
                courseFacade.create(seciliDers);
            } else {
                courseFacade.edit(seciliDers);
            }
            seciliDers = new Course();
            seciliOgretmenId = null;
            dersleriGetir();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sil(Course c) {
        courseFacade.remove(c);
        dersleriGetir();
    }

    public void formaGetir(Course c) {
        this.seciliDers = c;
        if (c.getOgretmen() != null) {
            this.seciliOgretmenId = c.getOgretmen().getId();
        }
    }

    public void temizle() {
        seciliDers = new Course();
        seciliOgretmenId = null;
    }

    // --- Getter & Setter ---
    public Course getSeciliDers() { return seciliDers; }
    public void setSeciliDers(Course seciliDers) { this.seciliDers = seciliDers; }
    public List<Course> getDersListesi() { return dersListesi; }
    public void setDersListesi(List<Course> dersListesi) { this.dersListesi = dersListesi; }
    public List<Users> getOgretmenListesi() { return ogretmenListesi; }
    public void setOgretmenListesi(List<Users> ogretmenListesi) { this.ogretmenListesi = ogretmenListesi; }
    public Long getSeciliOgretmenId() { return seciliOgretmenId; }
    public void setSeciliOgretmenId(Long seciliOgretmenId) { this.seciliOgretmenId = seciliOgretmenId; }
    public String getAramaMetni() { return aramaMetni; }
    public void setAramaMetni(String aramaMetni) { this.aramaMetni = aramaMetni; }
}