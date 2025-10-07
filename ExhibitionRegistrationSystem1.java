/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package exhibitionregistrationsystem1;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.util.regex.Pattern;
/**
 *
 * @author Magbo
 */public class ExhibitionRegistrationSystem1 extends JFrame {

    // Database URL
    static final String DB_URL = "jdbc:ucanaccess://C:\\Users\\Magbo\\Documents\\VUE_Exhibition.accdb";

    // GUI components
    private JTextField txtRegID, txtName, txtDept, txtPartner, txtContact, txtEmail;
    private JLabel lblImage;
    private String imagePath = null;

    public ExhibitionRegistrationSystem1() {
        setTitle("Exhibition Participant Registration");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Labels
        JLabel lblRegID = new JLabel("Registration ID:");
        lblRegID.setBounds(20, 20, 120, 25);
        add(lblRegID);
        
        JLabel lblName = new JLabel("Participant Name:");
        lblName.setBounds(20, 60, 150, 25);
        add(lblName);
        
        JLabel lblDept = new JLabel("Department:");
        lblDept.setBounds(20, 100, 150, 25);
        add(lblDept);
        
        JLabel lblPartner = new JLabel("Dancing Partner:");
        lblPartner.setBounds(20, 140, 150, 25);
        add(lblPartner);
        
        JLabel lblContact = new JLabel("Contact Number:");
        lblContact.setBounds(20, 180, 150, 25);
        add(lblContact);
        
        JLabel lblEmail = new JLabel("Email Address:");
        lblEmail.setBounds(20, 220, 150, 25);
        add(lblEmail);
        
        JLabel lblIDImage = new JLabel("ID Image:");
        lblIDImage.setBounds(20, 260, 150, 25);
        add(lblIDImage);

        // Text fields
        txtRegID = new JTextField();
        txtRegID.setBounds(180, 20, 150, 25);
        add(txtRegID);

        txtName = new JTextField();
        txtName.setBounds(180, 60, 200, 25);
        add(txtName);

        txtDept = new JTextField();
        txtDept.setBounds(180, 100, 200, 25);
        add(txtDept);

        txtPartner = new JTextField();
        txtPartner.setBounds(180, 140, 200, 25);
        add(txtPartner);

        txtContact = new JTextField();
        txtContact.setBounds(180, 180, 200, 25);
        add(txtContact);

        txtEmail = new JTextField();
        txtEmail.setBounds(180, 220, 200, 25);
        add(txtEmail);

        lblImage = new JLabel();
        lblImage.setBounds(180, 260, 150, 150);
        lblImage.setBorder(BorderFactory.createEtchedBorder());
        add(lblImage);

        // Buttons
        JButton btnUpload = new JButton("Upload Image");
        btnUpload.setBounds(350, 260, 150, 25);
        add(btnUpload);

        JButton btnRegister = new JButton("Register");
        btnRegister.setBounds(20, 430, 100, 25);
        add(btnRegister);

        JButton btnSearch = new JButton("Search");
        btnSearch.setBounds(130, 430, 100, 25);
        add(btnSearch);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(240, 430, 100, 25);
        add(btnUpdate);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setBounds(350, 430, 100, 25);
        add(btnDelete);

        JButton btnClear = new JButton("Clear");
        btnClear.setBounds(460, 430, 100, 25);
        add(btnClear);

        JButton btnExit = new JButton("Exit");
        btnExit.setBounds(460, 20, 100, 25);
        add(btnExit);

        // Button Actions
        btnUpload.addActionListener(e -> uploadImage());
        btnRegister.addActionListener(e -> registerParticipant());
        btnSearch.addActionListener(e -> searchParticipant());
        btnUpdate.addActionListener(e -> updateParticipant());
        btnDelete.addActionListener(e -> deleteParticipant());
        btnClear.addActionListener(e -> clearFields());
        btnExit.addActionListener(e -> System.exit(0));
    }

    // Upload Image
    private void uploadImage() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if(result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            imagePath = file.getAbsolutePath();
            lblImage.setIcon(new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(150,150,java.awt.Image.SCALE_SMOOTH)));
        }
    }

    // Input Validation
    private boolean validateInput() {
        if(txtName.getText().isEmpty() || txtDept.getText().isEmpty() || 
           txtPartner.getText().isEmpty() || txtContact.getText().isEmpty() || 
           txtEmail.getText().isEmpty() || imagePath == null) {
            JOptionPane.showMessageDialog(this, "All fields including ID Image must be filled!");
            return false;
        }

        // Simple email regex
        String emailPattern = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        if(!Pattern.matches(emailPattern, txtEmail.getText())) {
            JOptionPane.showMessageDialog(this, "Invalid Email format!");
            return false;
        }

        return true;
    }

    // Register Participant
    private void registerParticipant() {
        if(!validateInput()) return;

        try(Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "INSERT INTO Participants (ParticipantName, Department, DancingPartner, ContactNumber, EmailAddress, IDImagePath) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtName.getText());
            pst.setString(2, txtDept.getText());
            pst.setString(3, txtPartner.getText());
            pst.setString(4, txtContact.getText());
            pst.setString(5, txtEmail.getText());
            pst.setString(6, imagePath);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Participant Registered Successfully!");
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // Search Participant by ID
    private void searchParticipant() {
        if(txtRegID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Registration ID to search!");
            return;
        }
        try(Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT * FROM Participants WHERE RegistrationID=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(txtRegID.getText()));
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                txtName.setText(rs.getString("ParticipantName"));
                txtDept.setText(rs.getString("Department"));
                txtPartner.setText(rs.getString("DancingPartner"));
                txtContact.setText(rs.getString("ContactNumber"));
                txtEmail.setText(rs.getString("EmailAddress"));
                imagePath = rs.getString("IDImagePath");
                lblImage.setIcon(new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(150,150,java.awt.Image.SCALE_SMOOTH)));
            } else {
                JOptionPane.showMessageDialog(this, "Participant not found!");
            }
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // Update Participant
    private void updateParticipant() {
        if(txtRegID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Registration ID to update!");
            return;
        }
        if(!validateInput()) return;

        try(Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "UPDATE Participants SET ParticipantName=?, Department=?, DancingPartner=?, ContactNumber=?, EmailAddress=?, IDImagePath=? WHERE RegistrationID=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtName.getText());
            pst.setString(2, txtDept.getText());
            pst.setString(3, txtPartner.getText());
            pst.setString(4, txtContact.getText());
            pst.setString(5, txtEmail.getText());
            pst.setString(6, imagePath);
            pst.setInt(7, Integer.parseInt(txtRegID.getText()));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Participant Updated Successfully!");
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // Delete Participant
    private void deleteParticipant() {
        if(txtRegID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Registration ID to delete!");
            return;
        }
        try(Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "DELETE FROM Participants WHERE RegistrationID=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(txtRegID.getText()));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Participant Deleted Successfully!");
            clearFields();
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // Clear Fields
    private void clearFields() {
        txtRegID.setText("");
        txtName.setText("");
        txtDept.setText("");
        txtPartner.setText("");
        txtContact.setText("");
        txtEmail.setText("");
        lblImage.setIcon(null);
        imagePath = null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ExhibitionRegistrationSystem1().setVisible(true);
        });
    }
}