package controller;

import dbms.StockDAO;
import dbms.UserDAO;
import javax.swing.JOptionPane;
import model.User;
import view.ManagerViewForm;
import java.sql.*;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Stock;

public class ManagerController {

    private ManagerViewForm view;
    private UserDAO userDAO;
    private Connection con; 
    private StockDAO stockDAO;

    public ManagerController(ManagerViewForm view, Connection conn) {
        this.view = view;
        this.userDAO = new UserDAO(conn);
        this.con = conn; 
        this.stockDAO = new StockDAO();

        loadStockTable();
        initController();
        loadUsersTable();
        loadMonitoringTable(); 
    }

    private void initController() {
        view.getBtnAddUser().addActionListener(e -> addUser());
        view.getBtnUpdateUser().addActionListener(e -> updateUser());
        view.getBtnDeleteUser().addActionListener(e -> deleteUser());
        view.getTableUsers().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableRowSelected();
            }
        });
    }

   
    public void addUser() {
        try {
            String username = view.getTxtUsername().getText();
            String password = view.getTxtPassword().getText();
            String role = view.getCmdRole().getSelectedItem().toString();

            User user = new User(username, password, role);
            if (userDAO.addUser(user)) {
                JOptionPane.showMessageDialog(view, "User added!");
                loadUsersTable();
                view.clearFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Fill all fields correctly!");
        }
    }

    public void updateUser() {
        try {
            int id = Integer.parseInt(view.getTxtId().getText());
            String username = view.getTxtUsername().getText();
            String password = view.getTxtPassword().getText();
            String role = view.getCmdRole().getSelectedItem().toString();

            User user = new User(id, username, password, role);
            if (userDAO.updateUser(user)) {
                JOptionPane.showMessageDialog(view, "User updated!");
                loadUsersTable();
                view.clearFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Select a valid user!");
        }
    }

    public void deleteUser() {
        try {
            int id = Integer.parseInt(view.getTxtId().getText());
            int confirm = JOptionPane.showConfirmDialog(view, "Are you sure?");
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            if (userDAO.deleteUser(id)) {
                JOptionPane.showMessageDialog(view, "User deleted!");
                loadUsersTable();
                view.clearFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Select a valid user!");
        }
    }

    private void tableRowSelected() {
        int selectedRow = view.getTableUsers().getSelectedRow();
        if (selectedRow >= 0) {
            view.getTxtId().setText(view.getTableModel().getValueAt(selectedRow, 0).toString());
            view.getTxtUsername().setText(view.getTableModel().getValueAt(selectedRow, 1).toString());
            view.getTxtPassword().setText(view.getTableModel().getValueAt(selectedRow, 2).toString());
            view.getCmdRole().setSelectedItem(view.getTableModel().getValueAt(selectedRow, 3).toString());
        }
    }

    private void loadUsersTable() {
        List<User> users = userDAO.getAllUsers();
        view.loadUsersTable(users);
    }

   
    
    
    private void loadMonitoringTable() {
        try {
            String sql = "SELECT book_id, title, stock FROM books";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            JTable monitoringTable = view.getMonitoringTable(); 
            DefaultTableModel model = (DefaultTableModel) monitoringTable.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String title = rs.getString("title");
                int stock = rs.getInt("stock");
                String status = (stock <= 5) ? "Low" : "Available";

                model.addRow(new Object[]{bookId, title, stock, status});
            }

            monitoringTable.getColumnModel().getColumn(3).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
                @Override
                public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {

                    javax.swing.JLabel label = (javax.swing.JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    if (value != null) {
                        String status = value.toString();

                        
                        label.setText(status);

                        
                        int size = 30;
                        java.awt.Color color = status.equalsIgnoreCase("Low") ? java.awt.Color.RED : java.awt.Color.GREEN;
                        javax.swing.Icon icon = new javax.swing.ImageIcon(new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_RGB));
                        java.awt.Graphics g = ((java.awt.image.BufferedImage) ((javax.swing.ImageIcon) icon).getImage()).getGraphics();
                        g.setColor(color);
                        g.fillRect(0, 0, size, size);
                        g.dispose();

                        label.setIcon(icon);
                        label.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT); 
                    }

                    return label;
                }
            });

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error loading monitoring table: " + e.getMessage());
        }
    }

    private void loadStockTable() {
        List<Stock> stocks = stockDAO.getAllStocks();
        DefaultTableModel model
                = (DefaultTableModel) view.getMonitoringTable().getModel();

        model.setRowCount(0);

        for (Stock s : stocks) {
            model.addRow(new Object[]{
                s.getBookId(),
                s.getTitle(),
                s.getStock(),
                s.getStatus()
            });
        }
    }
    
}
