package facadeLocal;

import com.example.entity.Users;
import com.example.enums.RoleEnum;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface UserFacadeLocal {
    void createUser(Users u);
    Users editUser(Users u);
    void remove(Users u);
    List<Users> usersList();
    Users login(String okulNo, String sifre); // Giriş yapma fonksiyonumuz
    Users loginByRole(String okulNo, RoleEnum role);
}