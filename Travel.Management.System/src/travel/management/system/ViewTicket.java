package travel.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewTicket extends JFrame implements ActionListener {
    private JTextField[] fields = new JTextField[9];
    private JButton firstBtn, nextBtn, prevBtn, lastBtn, cancelBtn;
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;

    public ViewTicket() {
        setTitle("Travel Management System - View Tickets");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        String[] labels = {"Customer ID", "Customer Name", "Destination", "Flight Name", "Price", "No. of Persons", "From Date", "To Date", "Total Price"};

        for (int i = 0; i < labels.length; i++) {
            formPanel.add(new JLabel(labels[i]));
            fields[i] = new JTextField();
            fields[i].setEditable(false);
            formPanel.add(fields[i]);
        }

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        firstBtn = new JButton("First");
        nextBtn = new JButton("Next");
        prevBtn = new JButton("Previous");
        lastBtn = new JButton("Last");
        cancelBtn = new JButton("Cancel");

        buttonPanel.add(firstBtn);
        buttonPanel.add(prevBtn);
        buttonPanel.add(nextBtn);
        buttonPanel.add(lastBtn);
        buttonPanel.add(cancelBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        firstBtn.addActionListener(this);
        nextBtn.addActionListener(this);
        prevBtn.addActionListener(this);
        lastBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        connectToDatabase();
        if (rs != null) {
            loadFirstRecord();
        } else {
            JOptionPane.showMessageDialog(this, "No records found in the database.");
        }
    }

    private void connectToDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tms", "root", "coder");
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery("SELECT * FROM tickets");

            if (!rs.isBeforeFirst()) {  // Check if the result set has records
                rs = null;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Connection Failed!\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadRecord() {
        try {
            if (rs != null) {
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setText(rs.getString(i + 1));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error Loading Data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadFirstRecord() {
        try {
            if (rs != null && rs.first()) {
                loadRecord();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadLastRecord() {
        try {
            if (rs != null && rs.last()) {
                loadRecord();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadNextRecord() {
        try {
            if (rs != null && !rs.isLast()) {
                rs.next();
                loadRecord();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPrevRecord() {
        try {
            if (rs != null && !rs.isFirst()) {
                rs.previous();
                loadRecord();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == firstBtn) {
            loadFirstRecord();
        } else if (ae.getSource() == nextBtn) {
            loadNextRecord();
        } else if (ae.getSource() == prevBtn) {
            loadPrevRecord();
        } else if (ae.getSource() == lastBtn) {
            loadLastRecord();
        } else if (ae.getSource() == cancelBtn) {
            dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ViewTicket::new);
    }
}
