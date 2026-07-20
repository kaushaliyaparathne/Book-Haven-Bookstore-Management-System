package controller;

import model.Book;
import view.CashierViewForm;
import dbms.BookDAO;
import dbms.StockDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.util.List;
import java.util.ArrayList;
import model.Stock;

public class CashierController {

    private JTable tblBooks;
    private CashierViewForm view;
    private BookDAO bookDAO;
    private List<Book> bookList; 
    private StockDAO stockDAO;

    public CashierController(CashierViewForm view) {
        this.view = view;
        this.tblBooks = view.getBookTable(); 
        this.bookDAO = new BookDAO();
        this.stockDAO = new StockDAO();

        this.bookList = bookDAO.getAllBooks();

        initController();
        showBooks(bookList); 
    }

    private void initController() {
        view.getAddButton().addActionListener(e -> addBook());
        view.getBtnClear().addActionListener(e -> clearForm());
        view.getBtnSearch().addActionListener(e -> searchBook());
        view.getBtnCancel().addActionListener(e -> cancelSearch());
        view.getBtnLogout().addActionListener(e -> logout());
    }

    private void addBook() {
        try {
            String title = view.getTitleField().getText();
            String author = view.getAuthorField().getText();
            String category = (String) view.getCategoryField().getSelectedItem();
            double price = Double.parseDouble(view.getPriceField().getText());
            int stock = Integer.parseInt(view.getStockField().getText());

           
            Book book = new Book(0, title, author, category, price, stock);

           
            int generatedBookId = bookDAO.addBookAndReturnId(book);

            if (generatedBookId > 0) {
                
                boolean stockAdded = stockDAO.addStock(generatedBookId, title, stock);

                if (!stockAdded) {
                    JOptionPane.showMessageDialog(view,
                            "Book added, but stock insert failed!");
                } else {
                    JOptionPane.showMessageDialog(view,
                            "Book & Stock added successfully!");
                }

                bookList = bookDAO.getAllBooks();
                showBooks(bookList); 

                
                clearForm();

            } else {
                JOptionPane.showMessageDialog(view, "Error adding book to DB!");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view,
                    "Invalid input. Please check your fields.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Unexpected error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void showBooks(List<Book> books) {
        DefaultTableModel model = (DefaultTableModel) tblBooks.getModel();
        model.setRowCount(0);

        for (Book b : books) {
            model.addRow(new Object[]{
                b.getBookId(),
                b.getTitle(),
                b.getAuthor(),
                b.getCategory(),
                b.getPrice(),
                b.getStock()
            });

        }

    }

    private void clearForm() {
        view.getBookId().setText("");
        view.getTitleField().setText("");
        view.getAuthorField().setText("");
        view.getPriceField().setText("");
        view.getStockField().setText("");
        view.getCategoryField().setSelectedIndex(0);
        view.getTxtSearch().setText("");
        showBooks(bookList);
    }

    private void searchBook() {
        String keyword = view.getTxtSearch().getText().trim();
        if (keyword.isEmpty()) {
            cancelSearch();
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tblBooks.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tblBooks.setRowSorter(sorter);

        RowFilter<DefaultTableModel, Object> idFilter = RowFilter.regexFilter("^" + keyword + "$", 0);
        RowFilter<DefaultTableModel, Object> titleFilter = RowFilter.regexFilter("(?i)" + keyword, 1);

        List<RowFilter<DefaultTableModel, Object>> filters = new ArrayList<>();
        filters.add(idFilter);
        filters.add(titleFilter);

        sorter.setRowFilter(RowFilter.orFilter(filters));
    }

    private void cancelSearch() {
        view.getTxtSearch().setText("");
        TableRowSorter<?> sorter = (TableRowSorter<?>) tblBooks.getRowSorter();
        if (sorter != null) {
            sorter.setRowFilter(null);
        }
        ((DefaultTableModel) tblBooks.getModel()).fireTableDataChanged();
    }

    private void logout() {
        view.dispose();
        JOptionPane.showMessageDialog(null, "Logged out successfully!");
    }

}
