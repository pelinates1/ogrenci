package bean;

import com.example.entity.AdvisorAssignment;
import com.example.entity.FreezeRequest;
import com.example.entity.Users;
import com.example.enums.FreezeStatusEnum;
import facadeLocal.AdvisorAssignmentFacadeLocal;
import facadeLocal.FreezeRequestFacadeLocal;
import facadeLocal.UserFacadeLocal;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Named("freezeBean")
@ViewScoped
public class FreezeBean implements Serializable {

    private FreezeRequest yeniTalep;
    private List<FreezeRequest> ogrenciTalepleri;
    private List<FreezeRequest> danismanTalepleri;
    private List<FreezeRequest> yoneticiTalepleri;

    @EJB
    private FreezeRequestFacadeLocal freezeRequestFacade;

    @EJB
    private AdvisorAssignmentFacadeLocal advisorAssignmentFacade;

    @EJB
    private UserFacadeLocal userFacade;

    private Users getCurrentUser() {
        return (Users) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
    }

    @PostConstruct
    public void init() {
        yeniTalep = new FreezeRequest();
        Users currentUser = getCurrentUser();
        if (currentUser != null) {
            switch (currentUser.getRol()) {
                case STUDENT:
                    ogrenciTalepleriGetir(currentUser);
                    break;
                case TEACHER:
                    danismanTalepleriGetir(currentUser);
                    break;
                case ADMIN:
                    yoneticiTalepleriGetir();
                    break;
            }
        }
    }

    // --- ÖĞRENCİ METOTLARI ---
    public void ogrenciTalepleriGetir(Users student) {
        ogrenciTalepleri = freezeRequestFacade.findByStudent(student);
    }

    public void talepOlustur() {
        Users student = getCurrentUser();
        if (student == null) return;

        // Öğrencinin danışmanı var mı kontrol et
        AdvisorAssignment advisor = advisorAssignmentFacade.findByDepartmentAndClass(student.getBolum(), student.getSinif());
        if (advisor == null) {
            mesajGoster("Hata", "Sistemde bölümünüz ve sınıfınız için atanmış bir danışman bulunmamaktadır. Lütfen yönetici ile iletişime geçin.");
            return;
        }

        // Zaten bekleyen veya onaylanan bir talebi var mı kontrol et
        if (ogrenciTalepleri != null) {
            for (FreezeRequest req : ogrenciTalepleri) {
                if (req.getStatus() == FreezeStatusEnum.PENDING_ADVISOR || req.getStatus() == FreezeStatusEnum.PENDING_ADMIN || req.getStatus() == FreezeStatusEnum.APPROVED) {
                    mesajGoster("Hata", "Zaten bekleyen veya onaylanmış bir dondurma talebiniz bulunmaktadır.");
                    return;
                }
            }
        }

        yeniTalep.setStudent(student);
        yeniTalep.setRequestDate(new Date());
        yeniTalep.setStatus(FreezeStatusEnum.PENDING_ADVISOR);

        try {
            freezeRequestFacade.create(yeniTalep);
            mesajGoster("Başarılı", "Dondurma talebiniz danışmanınıza iletilmiştir.");
            yeniTalep = new FreezeRequest();
            ogrenciTalepleriGetir(student);
        } catch (Exception e) {
            mesajGoster("Hata", "Talep oluşturulurken bir hata meydana geldi.");
        }
    }

    // --- DANIŞMAN (ÖĞRETMEN) METOTLARI ---
    public void danismanTalepleriGetir(Users teacher) {
        List<AdvisorAssignment> atamalar = advisorAssignmentFacade.findByTeacher(teacher);
        if (atamalar == null || atamalar.isEmpty()) {
            danismanTalepleri = new ArrayList<>();
            return;
        }
        
        List<String> depts = atamalar.stream().map(AdvisorAssignment::getDepartment).collect(Collectors.toList());
        List<String> classes = atamalar.stream().map(AdvisorAssignment::getStudentClass).collect(Collectors.toList());
        
        danismanTalepleri = freezeRequestFacade.findByDepartmentsAndClassesAndStatus(depts, classes, FreezeStatusEnum.PENDING_ADVISOR);
    }

    public void danismanOnayla(FreezeRequest req) {
        try {
            req.setStatus(FreezeStatusEnum.PENDING_ADMIN);
            freezeRequestFacade.edit(req);
            mesajGoster("Başarılı", "Talep yönetici onayına gönderildi.");
            danismanTalepleriGetir(getCurrentUser());
        } catch (Exception e) {
            mesajGoster("Hata", "İşlem başarısız.");
        }
    }

    public void danismanReddet(FreezeRequest req) {
        try {
            req.setStatus(FreezeStatusEnum.REJECTED);
            freezeRequestFacade.edit(req);
            mesajGoster("Başarılı", "Talep reddedildi.");
            danismanTalepleriGetir(getCurrentUser());
        } catch (Exception e) {
            mesajGoster("Hata", "İşlem başarısız.");
        }
    }

    // --- YÖNETİCİ METOTLARI ---
    public void yoneticiTalepleriGetir() {
        yoneticiTalepleri = freezeRequestFacade.findByStatus(FreezeStatusEnum.PENDING_ADMIN);
    }

    public void yoneticiOnayla(FreezeRequest req) {
        try {
            req.setStatus(FreezeStatusEnum.APPROVED);
            freezeRequestFacade.edit(req);
            
            // Öğrenciyi pasife al
            Users student = req.getStudent();
            student.setAktif(false);
            userFacade.editUser(student);
            
            mesajGoster("Başarılı", "Talep onaylandı ve öğrenci pasif duruma alındı.");
            yoneticiTalepleriGetir();
        } catch (Exception e) {
            mesajGoster("Hata", "İşlem başarısız.");
        }
    }

    public void yoneticiReddet(FreezeRequest req) {
        try {
            req.setStatus(FreezeStatusEnum.REJECTED);
            freezeRequestFacade.edit(req);
            mesajGoster("Başarılı", "Talep reddedildi.");
            yoneticiTalepleriGetir();
        } catch (Exception e) {
            mesajGoster("Hata", "İşlem başarısız.");
        }
    }

    private void mesajGoster(String baslik, String icerik) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, baslik, icerik));
    }

    // GETTER & SETTER
    public FreezeRequest getYeniTalep() { return yeniTalep; }
    public void setYeniTalep(FreezeRequest yeniTalep) { this.yeniTalep = yeniTalep; }
    public List<FreezeRequest> getOgrenciTalepleri() { return ogrenciTalepleri; }
    public void setOgrenciTalepleri(List<FreezeRequest> ogrenciTalepleri) { this.ogrenciTalepleri = ogrenciTalepleri; }
    public List<FreezeRequest> getDanismanTalepleri() { return danismanTalepleri; }
    public void setDanismanTalepleri(List<FreezeRequest> danismanTalepleri) { this.danismanTalepleri = danismanTalepleri; }
    public List<FreezeRequest> getYoneticiTalepleri() { return yoneticiTalepleri; }
    public void setYoneticiTalepleri(List<FreezeRequest> yoneticiTalepleri) { this.yoneticiTalepleri = yoneticiTalepleri; }
}
