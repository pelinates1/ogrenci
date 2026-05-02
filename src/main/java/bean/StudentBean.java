package bean;

import com.example.entity.Users;
import com.example.enums.RoleEnum;
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

@Named("studentBean")
@ViewScoped
public class StudentBean implements Serializable {

    private Users seciliOgrenci;
    private List<Users> ogrenciListesi;

    @EJB
    private UserFacadeLocal userFacade;

    @PostConstruct
    public void init() {
        seciliOgrenci = new Users();
        ogrencileriGetir();
    }

    public void ogrencileriGetir() {
        ogrenciListesi = new ArrayList<>();
        List<Users> tumKullanicilar = userFacade.usersList();
        if (tumKullanicilar != null) {
            for (Users u : tumKullanicilar) {
                // Veritabanındaki rolü STUDENT olanları alıp listeye koyuyoruz
                if (u.getRol() == RoleEnum.STUDENT) {
                    ogrenciListesi.add(u);
                }
            }
        }
    }

    public void kaydet() {
        try {
            if (seciliOgrenci.getId() == null) {
                seciliOgrenci.setRol(RoleEnum.STUDENT);
                userFacade.createUser(seciliOgrenci);
                mesajGoster("Başarılı", "Öğrenci sisteme başarıyla eklendi.");
            } else {
                userFacade.editUser(seciliOgrenci);
                mesajGoster("Başarılı", "Öğrenci bilgileri güncellendi.");
            }
            temizle();
            ogrencileriGetir();
        } catch (Exception e) {
            mesajGoster("Hata", "İşlem sırasında bir hata oluştu!");
        }
    }

    public void sil(Users u) {
        try {
            userFacade.remove(u);
            mesajGoster("Başarılı", "Öğrenci sistemden silindi.");
            ogrencileriGetir();
        } catch (Exception e) {
            mesajGoster("Hata", "Silme işlemi başarısız!");
        }
    }

    public void formaGetir(Users u) {
        this.seciliOgrenci = u;
    }

    public void temizle() {
        this.seciliOgrenci = new Users();
    }

    private void mesajGoster(String baslik, String icerik) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, baslik, icerik));
    }

    // --- GETTER & SETTER ---
    public Users getSeciliOgrenci() { return seciliOgrenci; }
    public void setSeciliOgrenci(Users seciliOgrenci) { this.seciliOgrenci = seciliOgrenci; }
    public List<Users> getOgrenciListesi() { return ogrenciListesi; }
    public void setOgrenciListesi(List<Users> ogrenciListesi) { this.ogrenciListesi = ogrenciListesi; }
}