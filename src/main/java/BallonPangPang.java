import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.*;

public class BallonPangPang {
    JPanel loginP, mainP;
    JPanel p1, p2, p3;
    CardLayout card;
    JLabel lbName;
    TextField tfName;
    JButton btLogin, btReset, btExit;

    public static void main(String[] args) {
        new Login();
    }
}
class Login extends JFrame implements ActionListener {

    public Login() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}