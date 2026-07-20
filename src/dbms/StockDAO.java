package dbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import model.Stock;

public class StockDAO {

    public List<Stock> getAllStocks() {
        List<Stock> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement("SELECT book_id, title, stock, status FROM stocks"); java.sql.ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                list.add(new Stock(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getInt("stock"),
                        rs.getString("status")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addStock(int bookId, String title, int stock) {
        String status = (stock <= 5) ? "LOW" : "OK"; // Low stock threshold
        String sql = "INSERT INTO stocks(book_id, title, stock, status) VALUES (?,?,?,?)";

        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, bookId);
            pst.setString(2, title);
            pst.setInt(3, stock);
            pst.setString(4, status);

            int rows = pst.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
