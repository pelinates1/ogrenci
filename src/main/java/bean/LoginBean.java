package bean;

import com.example.entity.Users;
import com.example.enums.RoleEnum;
import facadeLocal.UserFacadeLocal;
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

    @EJB
    private UserFacadeLocal userFacade;

    public LoginBean() {
        this.user = new Users();
    }

    private String loginByRole(RoleEnum role) {
        String okulNo = (this.user != null && this.user.getOkulNo() != null) ? this.user.getOkulNo().trim() : "";
        String sifre = (this.user != null) ? this.user.getSifre() : "";
        
        Users u = this.userFacade.login(okulNo, sifre);

        if (u != null && u.getRol() == role) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", u);
            switch (role) {
                case ADMIN:
                    return "/panel/index.xhtml?faces-redirect=true";
                case TEACHER:
                    return "/panel/teacher-index.xhtml?faces-redirect=true";
                case STUDENT:
                    return "/panel/student-index.xhtml?faces-redirect=true";
                default:
                    return "/login.xhtml?faces-redirect=true";
            }
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Kullanıcı adı veya şifre hatalı!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
    }

    public String loginStudent() {
        return loginByRole(RoleEnum.STUDENT);
    }

    public String loginTeacher() {
        return loginByRole(RoleEnum.TEACHER);
    }

    public String loginAdmin() {
        String okulNo = (this.user != null && this.user.getOkulNo() != null) ? this.user.getOkulNo().trim() : "";
        String sifre = (this.user != null) ? this.user.getSifre() : "";

        if ("admin".equals(okulNo) && "123".equals(sifre)) {
            Users adminUser = new Users();
            adminUser.setOkulNo("admin");
            adminUser.setAd("Sistem");
            adminUser.setSoyad("Yöneticisi");
            adminUser.setRol(RoleEnum.ADMIN);
            
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", adminUser);
            return "/panel/index.xhtml?faces-redirect=true";
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