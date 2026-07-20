package model;

public class Stock {

    private int bookId;
    private String title;
    private int stock;
    private String status;

    public Stock(int bookId, String title, int stock, String status) {
        this.bookId = bookId;
        this.title = title;
        this.stock = stock;
        this.status = status;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public int getStock() {
        return stock;
    }

    public String getStatus() {
        return status;
    }
}
