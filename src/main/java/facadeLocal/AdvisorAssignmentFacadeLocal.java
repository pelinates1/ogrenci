package facadeLocal;

import com.example.entity.AdvisorAssignment;
import com.example.entity.Users;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface AdvisorAssignmentFacadeLocal {
    void create(AdvisorAssignment advisorAssignment);
    void edit(AdvisorAssignment advisorAssignment);
    void remove(AdvisorAssignment advisorAssignment);
    AdvisorAssignment find(Object id);
    List<AdvisorAssignment> findAll();
    AdvisorAssignment findByDepartmentAndClass(String department, String studentClass);
    List<AdvisorAssignment> findByTeacher(Users teacher);
}
