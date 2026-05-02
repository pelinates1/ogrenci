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

    private Users seciliKullanici; // Formda eklenecek/güncellenecek kullanıcı
    private List<Users> ogretmenListesi; // Tablodaki liste

    @EJB
    private UserFacadeLocal userFacade;

    // Sayfa ilk açıldığında bu metot çalışır ve listeyi doldurur
    @PostConstruct
    public void init() {
        seciliKullanici = new Users();
        ogretmenleriGetir();
    }

    // Sadece Öğretmenleri veritabanından çekip listeye atar
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

    // FORM: Yeni Kaydet veya Güncelle
    public void kaydet() {
        try {
            if (seciliKullanici.getId() == null) {
                // ID boşsa yeni kayıttır (CREATE)
                seciliKullanici.setRol(RoleEnum.TEACHER); // Otomatik öğretmen rolü ata
                userFacade.createUser(seciliKullanici);
                mesajGoster("Başarılı", "Öğretmen başarıyla eklendi.");
            } else {
                // ID doluysa güncellemedir (UPDATE)
                userFacade.editUser(seciliKullanici);
                mesajGoster("Başarılı", "Öğretmen başarıyla güncellendi.");
            }
            // İşlem bitince formu temizle ve listeyi yenile
            temizle();
            ogretmenleriGetir();
        } catch (Exception e) {
            mesajGoster("Hata", "İşlem sırasında bir hata oluştu!");
        }
    }

    // TABLO: Seçili kişiyi sil (DELETE)
    public void sil(Users u) {
        try {
            userFacade.remove(u);
            mesajGoster("Başarılı", "Öğretmen başarıyla silindi.");
            ogretmenleriGetir();
        } catch (Exception e) {
            mesajGoster("Hata", "Silme işlemi başarısız!");
        }
    }

    // TABLO: Güncelle butonuna basınca kişinin bilgilerini forma doldur
    public void formaGetir(Users u) {
        this.seciliKullanici = u;
    }

    // Formu sıfırla (İptal butonu için)
    public void temizle() {
        this.seciliKullanici = new Users();
    }

    // JSF Ekrana Bilgi Mesajı (Yeşil/Kırmızı Kutu) Çıkarma
    private void mesajGoster(String baslik, String icerik) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, baslik, icerik));
    }

    // --- GETTER & SETTER ---
    public Users getSeciliKullanici() { return seciliKullanici; }
    public void setSeciliKullanici(Users seciliKullanici) { this.seciliKullanici = seciliKullanici; }
    public List<Users> getOgretmenListesi() { return ogretmenListesi; }
    public void setOgretmenListesi(List<Users> ogretmenListesi) { this.ogretmenListesi = ogretmenListesi; }
}