package facadeLocal;

import com.example.entity.FreezeRequest;
import com.example.entity.Users;
import com.example.enums.FreezeStatusEnum;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface FreezeRequestFacadeLocal {
    void create(FreezeRequest freezeRequest);
    void edit(FreezeRequest freezeRequest);
    void remove(FreezeRequest freezeRequest);
    FreezeRequest find(Object id);
    List<FreezeRequest> findAll();
    List<FreezeRequest> findByStudent(Users student);
    List<FreezeRequest> findByStatus(FreezeStatusEnum status);
    List<FreezeRequest> findByDepartmentsAndClassesAndStatus(List<String> departments, List<String> classes, FreezeStatusEnum status);
}
