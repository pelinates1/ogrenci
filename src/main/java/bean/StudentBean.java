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
    private String aramaMetni; // Arama için yeni alan

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
                if (u.getRol() == RoleEnum.STUDENT) {
                    // Arama filtresi uygula
                    if (aramaMetni == null || aramaMetni.isEmpty() || 
                        u.getAd().toLowerCase().contains(aramaMetni.toLowerCase()) || 
                        u.getSoyad().toLowerCase().contains(aramaMetni.toLowerCase()) ||
                        u.getOkulNo().contains(aramaMetni)) {
                        ogrenciListesi.add(u);
                    }
                }
            }
        }
    }

    public void kaydet() {
        try {
            if (seciliOgrenci.getId() == null) {
                seciliOgrenci.setRol(RoleEnum.STUDENT);
                seciliOgrenci.setAktif(true); // Varsayılan aktif
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

    public void sifreSifirla(Users u) {
        try {
            u.setSifre("123456");
            userFacade.editUser(u);
            mesajGoster("Başarılı", u.getAd() + " " + u.getSoyad() + " şifresi '123456' olarak sıfırlandı.");
        } catch (Exception e) {
            mesajGoster("Hata", "Şifre sıfırlama başarısız!");
        }
    }

    public void durumDegistir(Users u) {
        try {
            u.setAktif(!u.isAktif());
            userFacade.editUser(u);
            String durum = u.isAktif() ? "Aktif" : "Pasif";
            mesajGoster("Durum Güncellendi", "Öğrenci durumu " + durum + " yapıldı.");
            ogrencileriGetir();
        } catch (Exception e) {
            mesajGoster("Hata", "Durum değiştirme başarısız!");
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
    public String getAramaMetni() { return aramaMetni; }
    public void setAramaMetni(String aramaMetni) { this.aramaMetni = aramaMetni; }
}