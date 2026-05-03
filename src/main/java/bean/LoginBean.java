package bean;

import com.example.entity.Users;
import com.example.enums.RoleEnum;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("loginBean")
@ViewScoped
public class LoginBean implements Serializable {

    private Users user;

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

        if (targetUser.equals(okulNo) && targetPass.equals(sifre)) {
            Users u = new Users();
            u.setOkulNo(targetUser);
            u.setAd(name);
            u.setSoyad(surname);
            u.setRol(role);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", u);
            return redirectPath + "?faces-redirect=true";
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Giriş Başarısız", "Hatalı giriş yaptınız!");
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

    public Users getUser() {
        if (this.user == null) {
            this.user = new Users();
        }
        return this.user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}