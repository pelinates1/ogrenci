package bean;

import com.example.entity.Course;
import com.example.entity.Users;
import com.example.enums.RoleEnum;
import facadeLocal.CourseFacadeLocal;
import facadeLocal.UserFacadeLocal;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("courseBean")
@ViewScoped
public class CourseBean implements Serializable {

    private Course seciliDers;
    private List<Course> dersListesi;
    private List<Users> ogretmenListesi; // Formdaki açılır menü (SelectBox) için lazım

    // Hangi öğretmenin seçildiğini tutacak (ID olarak)
    private Long seciliOgretmenId;

    @EJB
    private CourseFacadeLocal courseFacade;

    @EJB
    private UserFacadeLocal userFacade;

    @PostConstruct
    public void init() {
        seciliDers = new Course();
        dersleriGetir();
        ogretmenleriGetir();
    }

    public void dersleriGetir() {
        dersListesi = courseFacade.courseList();
    }

    public void ogretmenleriGetir() {
        ogretmenListesi = new ArrayList<>();
        List<Users> tumKullanicilar = userFacade.usersList();
        if (tumKullanicilar != null) {
            for (Users u : tumKullanicilar) {
                if (u.getRol() == RoleEnum.TEACHER) {
                    ogretmenListesi.add(u);
                }
            }
        }
    }

    public void kaydet() {
        try {
            // Açılır menüden seçilen ID'ye göre öğretmeni bul ve derse ekle
            if (seciliOgretmenId != null) {
                Users ogretmen = new Users();
                ogretmen.setId(seciliOgretmenId);
                seciliDers.setOgretmen(ogretmen);
            }

            courseFacade.createCourse(seciliDers);
            mesajGoster("Başarılı", "Ders sisteme başarıyla eklendi.");
            temizle();
            dersleriGetir();

        } catch (Exception e) {
            mesajGoster("Hata", "Ders eklenirken bir hata oluştu!");
        }
    }

    public void sil(Course c) {
        try {
            courseFacade.removeCourse(c);
            mesajGoster("Başarılı", "Ders silindi.");
            dersleriGetir();
        } catch (Exception e) {
            mesajGoster("Hata", "Silme işlemi başarısız!");
        }
    }

    public void temizle() {
        this.seciliDers = new Course();
        this.seciliOgretmenId = null;
    }

    private void mesajGoster(String baslik, String icerik) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, baslik, icerik));
    }

    // --- GETTER & SETTER ---
    public Course getSeciliDers() { return seciliDers; }
    public void setSeciliDers(Course seciliDers) { this.seciliDers = seciliDers; }
    public List<Course> getDersListesi() { return dersListesi; }
    public void setDersListesi(List<Course> dersListesi) { this.dersListesi = dersListesi; }
    public List<Users> getOgretmenListesi() { return ogretmenListesi; }
    public void setOgretmenListesi(List<Users> ogretmenListesi) { this.ogretmenListesi = ogretmenListesi; }
    public Long getSeciliOgretmenId() { return seciliOgretmenId; }
    public void setSeciliOgretmenId(Long seciliOgretmenId) { this.seciliOgretmenId = seciliOgretmenId; }
}