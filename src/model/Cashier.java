package model;

public class Cashier extends User {

    public Cashier(String username, String password) {
        super(username, password, "Cashier");
    }

    public void login() {
        System.out.println("Cashier logged in");
    }

    public void handleSales() {
        System.out.println("Handling sales");
    }
}
