package travel.management.system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
public class Paytm extends JFrame {

    JTextField accountField, nameField;
    JPasswordField passwordField;
    JComboBox<String> paymentMethodBox;
    JButton saveButton;

    Paytm() {
        setTitle("Choose a Payment Method");
        setSize(800, 600);
        setLocationRelativeTo(null); // Centers the window on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Web Page Viewer
        JEditorPane jEditorPane = new JEditorPane();
        jEditorPane.setEditable(false);
        try {
            jEditorPane.setPage("https://paytm.com/electricity-bill-payment");
        } catch (Exception e) {
            jEditorPane.setContentType("text/html");
            jEditorPane.setText("<html>Could not load</html>");
        }
        JScrollPane scrollPane = new JScrollPane(jEditorPane);
        scrollPane.setPreferredSize(new Dimension(800, 200)); // Responsive size

        // Payment Options Panel
        JPanel paymentPanel = new JPanel(new GridLayout(2, 2, 20, 20)); // 2 rows, 2 columns
        paymentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding

        // Load Images for Payment Buttons
        JButton paytmButton = createButton("Pay via Paytm", loadImage("/icons/paytm.png", "Paytm"), e -> showMessage("Redirecting to Paytm..."));
        JButton googlePayButton = createButton("Google Pay", loadImage("/icons/googlepay.png", "Google Pay"), e -> showMessage("Redirecting to Google Pay..."));
        JButton phonePeButton = createButton("PhonePe", loadImage("/icons/phonepe.png", "PhonePe"), e -> showMessage("Redirecting to PhonePe..."));
        JButton amazonPayButton = createButton("Amazon Pay", loadImage("/icons/amazonpay.png", "Amazon Pay"), e -> showMessage("Redirecting to Amazon Pay..."));

        // Add buttons to grid panel
        paymentPanel.add(paytmButton);
        paymentPanel.add(googlePayButton);
        paymentPanel.add(phonePeButton);
        paymentPanel.add(amazonPayButton);

        // Account Information Panel (Using BoxLayout for proper alignment)
        JPanel accountPanel = new JPanel();
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.Y_AXIS));
        accountPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding

        // Labels and Input Fields
        JLabel accountLabel = new JLabel("UPI ID:"); // Changed label from "Account Number" to "UPI ID"
        accountField = new JTextField();
        JLabel nameLabel = new JLabel("Account Holder Name:");
        nameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JLabel paymentMethodLabel = new JLabel("Payment Method:");
        paymentMethodBox = new JComboBox<>(new String[]{"Paytm", "Google Pay", "PhonePe", "Amazon Pay"});

        // Set font for labels
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        accountLabel.setFont(labelFont);
        nameLabel.setFont(labelFont);
        passwordLabel.setFont(labelFont);
        paymentMethodLabel.setFont(labelFont);

        // Add labels and fields in order
        accountPanel.add(accountLabel);
        accountPanel.add(accountField);
        accountPanel.add(Box.createVerticalStrut(10)); // Adds spacing

        accountPanel.add(nameLabel);
        accountPanel.add(nameField);
        accountPanel.add(Box.createVerticalStrut(10));

        accountPanel.add(passwordLabel);
        accountPanel.add(passwordField);
        accountPanel.add(Box.createVerticalStrut(10));

        accountPanel.add(paymentMethodLabel);
        accountPanel.add(paymentMethodBox);
        accountPanel.add(Box.createVerticalStrut(10));

        // Back and Save Button Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton back = new JButton("Back");
        back.setFont(new Font("Arial", Font.BOLD, 14));
        back.addActionListener(e -> {
            setVisible(false);
            new Payment().setVisible(true);
        });
        saveButton = new JButton("Save");
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.addActionListener(e -> saveData());

        bottomPanel.add(back);
        bottomPanel.add(saveButton);

        // Main Layout
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.NORTH);
        add(paymentPanel, BorderLayout.CENTER);
        add(accountPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Button Factory Method (Flexible for Future)
    private JButton createButton(String text, ImageIcon icon, ActionListener action) {
        JButton button = new JButton(text, icon);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setHorizontalAlignment(SwingConstants.LEFT); // Align text with icon
        button.setPreferredSize(new Dimension(200, 50)); // Standard button size
        button.addActionListener(action);
        return button;
    }

    // Load Image with Default Placeholder
    private ImageIcon loadImage(String path, String name) {
        try {
            URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                Image img = new ImageIcon(imgURL).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            } else {
                return new ImageIcon(new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB)); // Blank image
            }
        } catch (Exception e) {
            return new ImageIcon(new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB)); // Blank image
        }
    }

    // Show Message Helper
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    // Save Data to Database with Validation
    private void saveData() {
        String accountNumber = accountField.getText().trim();
        String accountHolderName = nameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String paymentMethod = (String) paymentMethodBox.getSelectedItem();

        if (accountNumber.isEmpty() || accountHolderName.isEmpty() || password.isEmpty()) {
            showMessage("Error: All fields must be filled!");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tms", "root", "coder");
            String sql = "INSERT INTO payments (account_number, account_holder_name, password, payment_method) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, accountNumber);
            pstmt.setString(2, accountHolderName);
            pstmt.setString(3, password);
            pstmt.setString(4, paymentMethod);
            pstmt.executeUpdate();
            conn.close();
            showMessage("Data saved successfully!");
        } catch (Exception e) {
            showMessage("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Paytm();
    }
}
