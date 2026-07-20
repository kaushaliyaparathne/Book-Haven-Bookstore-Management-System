package dbms;

import model.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    
    public boolean addBook(Book b) {
        String sql = "INSERT INTO books (book_Id, title, author, category, price, stock) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, b.getBookId());
            pst.setString(2, b.getTitle());
            pst.setString(3, b.getAuthor());
            pst.setString(4, b.getCategory());
            pst.setDouble(5, b.getPrice());
            pst.setInt(6, b.getStock());

            pst.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Book b = new Book(
                        rs.getInt("book_Id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                );
                books.add(b);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public int addBookAndReturnId(Book book) {

        int bookId = 0;

        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO books(title, author, category, price, stock) VALUES (?,?,?,?,?)";

            PreparedStatement pst = con.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS
            );

            pst.setString(1, book.getTitle());
            pst.setString(2, book.getAuthor());
            pst.setString(3, book.getCategory());
            pst.setDouble(4, book.getPrice());
            pst.setInt(5, book.getStock());

            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                bookId = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookId;
    }
}
