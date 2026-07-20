package model;

public class Admin extends User {

    public Admin(String username, String password) {
        super(username, password, "Admin");
    }

    public void login() {
        System.out.println("Admin logged in");
    }

    public void manageUsers() {
        System.out.println("Managing users");
    }
}
