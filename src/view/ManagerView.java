package view;

import java.util.List;
import model.User;

public interface ManagerView {

    void showMessage(String message);
    void loadUsersTable(List<User> users);
    String getUsername();
    String getPassword();
    String getRole();
    int getUserId();
    void clearFields();
}
