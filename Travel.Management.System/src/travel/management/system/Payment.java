package travel.management.system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Payment extends JFrame {

    public Payment() {
        setTitle("Payment Options");
        setBounds(600, 220, 800, 600);
        setLayout(null);

        JLabel label = new JLabel("Pay using Paytm");
        label.setFont(new Font("Raleway", Font.BOLD, 30));
        label.setBounds(50, 20, 350, 45);
        add(label);

        // Fix: Correct Image Path
        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("travel/management/system/icons/paytm.jpeg"));
        Image i8 = i7.getImage().getScaledInstance(800, 450, Image.SCALE_DEFAULT);
        ImageIcon i9 = new ImageIcon(i8);
        JLabel l4 = new JLabel(i9);
        l4.setBounds(0, 100, 800, 450);
        add(l4);

        // Pay Button
        JButton pay = new JButton("Pay");
        pay.setBounds(420, 20, 80, 40);
        pay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new Paytm().setVisible(true);
            }
        });
        add(pay);

        // Back Button
        JButton back = new JButton("Back");
        back.setBounds(510, 20, 80, 40);
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        add(back);

        getContentPane().setBackground(Color.WHITE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Payment().setVisible(true);
    }
}
