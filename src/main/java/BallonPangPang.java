import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.*;

public class BallonPangPang {
    JFrame frame;	//전체 프레임
    Container con;
    GameManager gm;

    public BallonPangPang() {
        frame = new JFrame("풍선이 팡팡");
        con = frame.getContentPane();
        launchFrame();
    }

    public void launchFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gm = new GameManager(this);
        con.add(gm);	//게임 메니저를 붙붙
        frame.setSize(750, 550);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Login();
    }

    public void initGame() {	//게임이 종료되고 게임을 초기화 하는 메소드
        gm.setVisible(false);
        con.remove(gm);
        gm = new GameManager(this);
        con.add(gm);
        con.repaint();
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

        setTitle("::풍선이 팡팡::");

        card = new CardLayout();
        setLayout(card); // 카드레이아웃으로 설정
        loginP = new JPanel();

        add(loginP, "loginP");

        p1 = new JPanel(new BorderLayout());
        loginP.add(p1);
        p2 = new JPanel(new GridLayout(1, 1));
        p3 = new JPanel(new GridLayout(1, 1));
        p1.add(p2, "West");
        p1.add(p3, "Center");
        p2.add(lbName = new JLabel("이   름  :  "));
        p3.add(tfName = new TextField(20));

        Panel p4 = new Panel(); // 버튼 부착
        p1.add(p4, "South");
        p4.add(btLogin = new JButton("Login"));
        p4.add(btReset = new JButton("Reset"));
        p4.add(btExit = new JButton("Exit"));

        // 버튼 이벤트 리스너 등록
        btLogin.addActionListener(this);
        btReset.addActionListener(this);
        btExit.addActionListener(this);

        setSize(300, 130);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        HashMap<String, Integer> hm = new HashMap<String, Integer>();
        GameManager gm = new GameManager(null);

        try {
            FileWriter fw = new FileWriter("ranking.txt", true);
            BufferedWriter bf = new BufferedWriter(fw);

            //score가 GameManager에서 넘어오긴 하는데 게임 플레이 후 +가 안됨......
            if (o == btLogin) {
                String name = tfName.getText();
                int score = gm.totalScore;
                hm.put(name, score);
                bf.write(name + " ");
                bf.write(score + " ");
                JOptionPane.showMessageDialog(btReset, "로그인을 환영합니다.");
                new BallonPangPang();
                bf.close();
            } else if (o == btReset) {
                tfName.setText(" ");
                tfName.requestFocus();
            }
        } catch (Exception ae) {
            System.out.println(ae);
        }
    }
}