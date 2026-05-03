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

@Named("teacherBean")
@ViewScoped
public class TeacherBean implements Serializable {

    private Users seciliKullanici; 
    private List<Users> ogretmenListesi; 
    private String aramaMetni; // Arama özelliği için eklendi

    @EJB
    private UserFacadeLocal userFacade;

    @PostConstruct
    public void init() {
        seciliKullanici = new Users();
        ogretmenleriGetir();
    }

    public void ogretmenleriGetir() {
        ogretmenListesi = new ArrayList<>();
        List<Users> tumKullanicilar = userFacade.usersList();
        if (tumKullanicilar != null) {
            for (Users u : tumKullanicilar) {
                // Sadece öğretmen rolünde olanları al
                if (u.getRol() == RoleEnum.TEACHER) {
                    // Arama metni boşsa hepsini al, doluysa ad-soyad-okulNo kontrolü yap
                    if (aramaMetni == null || aramaMetni.isEmpty() ||
                        u.getAd().toLowerCase().contains(aramaMetni.toLowerCase()) ||
                        u.getSoyad().toLowerCase().contains(aramaMetni.toLowerCase()) ||
                        u.getOkulNo().contains(aramaMetni)) {
                        ogretmenListesi.add(u);
                    }
                }
            }
        }
    }

    public void kaydet() {
        try {
            if (seciliKullanici.getId() == null) {
                seciliKullanici.setRol(RoleEnum.TEACHER);
                seciliKullanici.setAktif(true); // Yeni öğretmen varsayılan aktif
                userFacade.createUser(seciliKullanici);
                mesajGoster("Başarılı", "Öğretmen başarıyla eklendi.");
            } else {
                userFacade.editUser(seciliKullanici);
                mesajGoster("Başarılı", "Öğretmen başarıyla güncellendi.");
            }
            temizle();
            ogretmenleriGetir();
        } catch (Exception e) {
            mesajGoster("Hata", "İşlem sırasında bir hata oluştu!");
        }
    }

    public void sil(Users u) {
        try {
            userFacade.remove(u);
            mesajGoster("Başarılı", "Öğretmen başarıyla silindi.");
            ogretmenleriGetir();
        } catch (Exception e) {
            mesajGoster("Hata", "Silme işlemi başarısız!");
        }
    }

    // YENİ: Durum Değiştir (Aktif/Pasif)
    public void durumDegistir(Users u) {
        u.setAktif(!u.isAktif());
        userFacade.editUser(u);
        ogretmenleriGetir();
    }

    // YENİ: Şifre Sıfırla (123456)
    public void sifreSifirla(Users u) {
        u.setSifre("123456");
        userFacade.editUser(u);
        mesajGoster("Bilgi", u.getAd() + " şifresi '123456' olarak sıfırlandı.");
    }

    public void formaGetir(Users u) {
        this.seciliKullanici = u;
    }

    public void temizle() {
        this.seciliKullanici = new Users();
        this.aramaMetni = "";
    }

    private void mesajGoster(String baslik, String icerik) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, baslik, icerik));
    }

    // --- GETTER & SETTER ---
    public Users getSeciliKullanici() { return seciliKullanici; }
    public void setSeciliKullanici(Users seciliKullanici) { this.seciliKullanici = seciliKullanici; }
    public List<Users> getOgretmenListesi() { return ogretmenListesi; }
    public void setOgretmenListesi(List<Users> ogretmenListesi) { this.ogretmenListesi = ogretmenListesi; }
    public String getAramaMetni() { return aramaMetni; }
    public void setAramaMetni(String aramaMetni) { this.aramaMetni = aramaMetni; }
}