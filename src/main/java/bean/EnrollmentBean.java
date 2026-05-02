package bean;

import com.example.entity.Course;
import com.example.entity.Enrollment;
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

@Named("enrollmentBean")
@ViewScoped
public class EnrollmentBean implements Serializable {

    // Formdan gelecek seçili ID'ler
    private Long seciliDersId;
    private Long seciliOgrenciId;

    // Açılır menüleri (SelectBox) doldurmak için listeler
    private List<Course> tumDersler;
    private List<Users> tumOgrenciler;

    @EJB
    private CourseFacadeLocal courseFacade;

    @EJB
    private UserFacadeLocal userFacade;

    @PostConstruct
    public void init() {
        verileriGetir();
    }

    public void verileriGetir() {
        // Sistemdeki tüm dersleri getir
        tumDersler = courseFacade.courseList();

        // Sadece rolü STUDENT olan öğrencileri getir
        tumOgrenciler = new ArrayList<>();
        List<Users> users = userFacade.usersList();
        if (users != null) {
            for (Users u : users) {
                if (u.getRol() == RoleEnum.STUDENT) {
                    tumOgrenciler.add(u);
                }
            }
        }
    }

    // Yeni kayıt oluştur (Ders'e Öğrenci Ekle)
    public void kayitYap() {
        try {
            if (seciliDersId != null && seciliOgrenciId != null) {

                Course ders = new Course();
                ders.setId(seciliDersId);

                Users ogrenci = new Users();
                ogrenci.setId(seciliOgrenciId);

                Enrollment yeniKayit = new Enrollment();
                yeniKayit.setDers(ders);
                yeniKayit.setOgrenci(ogrenci);
                // Vize ve Final null kalacak (Daha sonra öğretmen girecek)

                courseFacade.enrollStudent(yeniKayit);

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Öğrenci derse başarıyla kaydedildi."));

                // Formu temizle
                seciliDersId = null;
                seciliOgrenciId = null;
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Lütfen Ders ve Öğrenci seçiniz!"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Kayıt sırasında bir sorun oluştu. Öğrenci zaten bu derse kayıtlı olabilir."));
        }
    }

    // GETTER VE SETTER'LAR
    public Long getSeciliDersId() { return seciliDersId; }
    public void setSeciliDersId(Long seciliDersId) { this.seciliDersId = seciliDersId; }
    public Long getSeciliOgrenciId() { return seciliOgrenciId; }
    public void setSeciliOgrenciId(Long seciliOgrenciId) { this.seciliOgrenciId = seciliOgrenciId; }
    public List<Course> getTumDersler() { return tumDersler; }
    public void setTumDersler(List<Course> tumDersler) { this.tumDersler = tumDersler; }
    public List<Users> getTumOgrenciler() { return tumOgrenciler; }
    public void setTumOgrenciler(List<Users> tumOgrenciler) { this.tumOgrenciler = tumOgrenciler; }
}