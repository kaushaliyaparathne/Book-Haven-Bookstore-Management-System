package controller;

import model.User;
import javax.swing.JOptionPane;
import view.CashierViewForm;
import view.LoginViewForm;
import view.ManagerViewForm;
import dbms.DBConnection;
import java.sql.Connection;

public class LoginController {

    User admin = new User("admin", "123", "Admin");
    User seller = new User("cashier", "123", "Cashier");

    public void login(String username, String password, LoginViewForm loginForm) {

        if (username.equals(admin.username) && password.equals(admin.password)) {
            ManagerViewForm managerView = new ManagerViewForm();
            Connection con = DBConnection.getConnection();
            new ManagerController(managerView, con);
            managerView.setVisible(true);
            loginForm.dispose();

        } else if (username.equals(seller.username) && password.equals(seller.password)) {
            CashierViewForm cashierView = new CashierViewForm();

            CashierController cashier = new CashierController(cashierView);
            cashierView.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(loginForm, "Invalid username or password!");
        }
    }

}
