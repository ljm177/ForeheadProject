import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.*;

public class BallonPangPang {

    public static void main(String[] args) {
        new Login();
    }
}
class Login extends JFrame implements ActionListener {
    JPanel loginP, mainP;
    JPanel p1, p2, p3;
    CardLayout card;
    JLabel lbName;
    TextField tfName;
    JButton btLogin, btReset, btExit;

    public Login() {

        setTitle("::풍선이 팡팡:");

        card = new CardLayout();
        setLayout(card); // 카드레이아웃으로 설정
        loginP = new JPanel();

        add(loginP, "loginP");

        p1 = new JPanel(new BorderLayout());
        loginP.add(p1);
        p2 = new JPanel(new GridLayout(1, 1));
        p3 = new JPanel(new GridLayout(1, 1));
        p1.add(p2, "West");
        p1.add(p3, "Center");
        p2.add(lbName = new JLabel("이   름  :  "));
        p3.add(tfName = new TextField(20));

        Panel p4 = new Panel(); // 버튼 부착
        p1.add(p4, "South");
        p4.add(btLogin = new JButton("Login"));
        p4.add(btReset = new JButton("Reset"));
        p4.add(btExit = new JButton("Exit"));

        // 버튼 이벤트 리스너 등록
        btLogin.addActionListener(this);
        btReset.addActionListener(this);
        btExit.addActionListener(this);

        setSize(300, 130);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}