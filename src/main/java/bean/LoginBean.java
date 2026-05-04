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

@Named("loginBean")
@ViewScoped
public class LoginBean implements Serializable {

    private Users user;
    private String eskiSifre;
    private String yeniSifre;
    private String yeniSifreTekrar;
    private String danismanAdi;

    @EJB
    private UserFacadeLocal userFacade;

    public LoginBean() {
        this.user = new Users();
    }



    public String loginStudent() {
        return handleStaticLogin("ogrenci", "123", RoleEnum.STUDENT, "Öğrenci", "Kullanıcısı", "/panel/student-index.xhtml");
    }

    public String loginTeacher() {
        return handleStaticLogin("ogretmen", "123", RoleEnum.TEACHER, "Öğretmen", "Kullanıcısı", "/panel/teacher-index.xhtml");
    }

    public String loginAdmin() {
        return handleStaticLogin("admin", "123", RoleEnum.ADMIN, "Sistem", "Yöneticisi", "/panel/index.xhtml");
    }

    private String handleStaticLogin(String targetUser, String targetPass, RoleEnum role, String name, String surname, String redirectPath) {
        String okulNo = (this.user != null && this.user.getOkulNo() != null) ? this.user.getOkulNo().trim() : "";
        String sifre = (this.user != null) ? this.user.getSifre() : "";

        // Önce veritabanından giriş yapmayı dene
        Users authenticatedUser = userFacade.login(okulNo, sifre);
        
        if (authenticatedUser != null && authenticatedUser.getRol() == role) {
            // Eksik akademik bilgileri tamamla (eğer boşsa)
            if (authenticatedUser.getBolum() == null || authenticatedUser.getBolum().isEmpty()) {
                authenticatedUser.setBolum("Bilgisayar Mühendisliği");
                authenticatedUser.setSinif("1");
                userFacade.editUser(authenticatedUser);
            }
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", authenticatedUser);
            return redirectPath + "?faces-redirect=true";
        } else if (targetUser.equals(okulNo) && targetPass.equals(sifre)) {
            // Eğer statik bilgilerle eşleşiyorsa ama login olamadıysa (Muhtemelen DB'de var ama şifre farklı)
            Users existingUser = userFacade.loginByRole(okulNo, role);
            if (existingUser != null) {
                // Şifreyi statik olanla güncelle ve girişe izin ver
                existingUser.setSifre(targetPass);
                userFacade.editUser(existingUser);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", existingUser);
                return redirectPath + "?faces-redirect=true";
            } else {
                // Veritabanında hiç yoksa oluştur
                Users u = new Users();
                u.setOkulNo(targetUser);
                u.setAd(name);
                u.setSoyad(surname);
                u.setRol(role);
                u.setSifre(targetPass);
                u.setBolum("Sistem Yönetimi");
                u.setSinif("1");
                userFacade.createUser(u);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", u);
                return redirectPath + "?faces-redirect=true";
            }
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Giriş Başarısız", "Hatalı okul numarası veya şifre!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    public void checkLogin() {
        try {
            Object u = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
            if (u == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("../login.xhtml");
            }
        } catch (Exception e) {
            // ignore
        }
    }

    public void sifreGuncelle() {
        Users currentUser = (Users) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        if (currentUser == null) return;

        // Statik şifre kontrolü (Şu an tüm hesaplar '123' kullanıyor)
        if (!"123".equals(eskiSifre)) {
            mesajGoster("Hata", "Mevcut şifreniz hatalı!");
            return;
        }

        if (yeniSifre == null || yeniSifre.length() < 6) {
            mesajGoster("Hata", "Yeni şifre en az 6 karakter olmalıdır!");
            return;
        }

        if (!yeniSifre.equals(yeniSifreTekrar)) {
            mesajGoster("Hata", "Yeni şifreler eşleşmiyor!");
            return;
        }

        // Başarılı
        currentUser.setSifre(yeniSifre);
        try {
            userFacade.editUser(currentUser); // Veritabanında güncelle
            mesajGoster("Başarılı", "Şifreniz kalıcı olarak güncellendi. Artık yeni şifrenizle giriş yapabilirsiniz.");
        } catch (Exception e) {
            mesajGoster("Hata", "Şifre güncellenirken veritabanı hatası oluştu!");
        }
        eskiSifre = ""; yeniSifre = ""; yeniSifreTekrar = "";
    }

    private void mesajGoster(String baslik, String icerik) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(baslik.equals("Hata") ? FacesMessage.SEVERITY_ERROR : FacesMessage.SEVERITY_INFO, baslik, icerik));
    }

    public Users getUser() {
        if (this.user == null) {
            this.user = new Users();
        }
        return this.user;
    }

    public void setUser(Users user) { this.user = user; }

    public String getEskiSifre() { return eskiSifre; }
    public void setEskiSifre(String eskiSifre) { this.eskiSifre = eskiSifre; }

    public String getYeniSifre() { return yeniSifre; }
    public void setYeniSifre(String yeniSifre) { this.yeniSifre = yeniSifre; }

    public String getYeniSifreTekrar() { return yeniSifreTekrar; }
    public void setYeniSifreTekrar(String yeniSifreTekrar) { this.yeniSifreTekrar = yeniSifreTekrar; }

    public String getDanismanAdi() { return danismanAdi; }
    public void setDanismanAdi(String danismanAdi) { this.danismanAdi = danismanAdi; }
}