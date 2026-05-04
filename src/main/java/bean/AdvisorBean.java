package bean;

import com.example.entity.AdvisorAssignment;
import com.example.entity.Users;
import com.example.enums.RoleEnum;
import facadeLocal.AdvisorAssignmentFacadeLocal;
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

@Named("advisorBean")
@ViewScoped
public class AdvisorBean implements Serializable {

    private AdvisorAssignment seciliAtama;
    private List<AdvisorAssignment> atamaListesi;
    private List<Users> ogretmenListesi;

    private Long seciliOgretmenId;

    @EJB
    private AdvisorAssignmentFacadeLocal advisorAssignmentFacade;

    @EJB
    private UserFacadeLocal userFacade;

    @PostConstruct
    public void init() {
        seciliAtama = new AdvisorAssignment();
        atamalariGetir();
        ogretmenleriGetir();
    }

    public void atamalariGetir() {
        atamaListesi = advisorAssignmentFacade.findAll();
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
            if (seciliOgretmenId == null) {
                mesajGoster("Hata", "Lütfen bir öğretmen seçin.");
                return;
            } else {
                Users ogretmen = new Users();
                ogretmen.setId(seciliOgretmenId);
                seciliAtama.setTeacher(ogretmen);
            }
            if (seciliAtama.getDepartment() == null || seciliAtama.getDepartment().trim().isEmpty()) {
                mesajGoster("Hata", "Lütfen bölüm adı girin.");
                return;
            }
            if (seciliAtama.getStudentClass() == null || seciliAtama.getStudentClass().trim().isEmpty()) {
                mesajGoster("Hata", "Lütfen sınıf girin.");
                return;
            }

            // Ayni bolum ve sinif icin atama var mi kontrol et
            AdvisorAssignment mevcut = advisorAssignmentFacade.findByDepartmentAndClass(seciliAtama.getDepartment(), seciliAtama.getStudentClass());
            if (mevcut != null && (seciliAtama.getId() == null || !mevcut.getId().equals(seciliAtama.getId()))) {
                mesajGoster("Hata", "Bu bölüm ve sınıf için zaten bir danışman atanmış.");
                return;
            }

            if (seciliAtama.getId() == null) {
                advisorAssignmentFacade.create(seciliAtama);
                mesajGoster("Başarılı", "Danışman ataması eklendi.");
            } else {
                advisorAssignmentFacade.edit(seciliAtama);
                mesajGoster("Başarılı", "Danışman ataması güncellendi.");
            }
            temizle();
            atamalariGetir();
        } catch (Exception e) {
            mesajGoster("Hata", "İşlem sırasında bir hata oluştu!");
        }
    }

    public void sil(AdvisorAssignment a) {
        try {
            advisorAssignmentFacade.remove(a);
            mesajGoster("Başarılı", "Atama silindi.");
            atamalariGetir();
        } catch (Exception e) {
            mesajGoster("Hata", "Silme başarısız!");
        }
    }

    public void formaGetir(AdvisorAssignment a) {
        this.seciliAtama = a;
        if (a.getTeacher() != null) {
            this.seciliOgretmenId = a.getTeacher().getId();
        }
    }

    public void temizle() {
        this.seciliAtama = new AdvisorAssignment();
        this.seciliOgretmenId = null;
    }

    private void mesajGoster(String baslik, String icerik) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, baslik, icerik));
    }

    public AdvisorAssignment getSeciliAtama() { return seciliAtama; }
    public void setSeciliAtama(AdvisorAssignment seciliAtama) { this.seciliAtama = seciliAtama; }
    public List<AdvisorAssignment> getAtamaListesi() { return atamaListesi; }
    public void setAtamaListesi(List<AdvisorAssignment> atamaListesi) { this.atamaListesi = atamaListesi; }
    public List<Users> getOgretmenListesi() { return ogretmenListesi; }
    public void setOgretmenListesi(List<Users> ogretmenListesi) { this.ogretmenListesi = ogretmenListesi; }
    public Long getSeciliOgretmenId() { return seciliOgretmenId; }
    public void setSeciliOgretmenId(Long seciliOgretmenId) { this.seciliOgretmenId = seciliOgretmenId; }
}
