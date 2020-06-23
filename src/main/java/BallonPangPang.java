import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.*;

public class BallonPangPang {
    JFrame frame;	//전체 프레임
    Container con;
    GameManager gm;
    private Clip clip;

    BallonPangPang() {
        frame = new JFrame("풍선이 팡팡");
        con = frame.getContentPane();
        launchFrame();
        Dimension frameSize = frame.getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((screenSize.width-frameSize.width)/2 , (screenSize.height-frameSize.height)/2);
    }

    public void launchFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gm = new GameManager(this);
        con.add(gm);	//게임 메니저를 붙붙
        frame.setSize(750, 550);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Login();
    }

    public void initGame() {	//게임이 종료되고 게임을 초기화 하는 메소드
        gm.setVisible(false);
        con.remove(gm);
        gm = new GameManager(this);
        con.add(gm);
        con.repaint();
    }
    private void loadAudio(String pathName) {
        try {
            clip = AudioSystem.getClip(); // 비어있는 오디오 클립 만들기
            File audioFile = new File(pathName); // 오디오 파일의 경로명
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile); // 오디오 파일로부터
            clip.open(audioStream); // 재생할 오디오 스트림 열기
        }
        catch (LineUnavailableException e) { e.printStackTrace(); }
        catch (UnsupportedAudioFileException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }

    }
}

class RankingPan extends JFrame implements ActionListener{
    JFrame frame = new JFrame();
    TextArea ranking = new TextArea();
    JButton btRestart, btExit;
    //랭킹 출력해주는 판
    public RankingPan() {

        try {
            File file = new File("ranking.txt");
            FileReader filereader = new FileReader(file);
            BufferedReader bufReader = new BufferedReader(filereader);
            String line = "";
            ranking.append("          ========= RANKING ========= ");
            ranking.append("\n\n");
            int i=1;
            while((line = bufReader.readLine()) != null){

                ranking.append(i+"위  >> ");
                ranking.append(line);
                ranking.append("점");
                ranking.append("\n\n");
                i+=1;
            }
            bufReader.close();
        } catch (FileNotFoundException e) { }
        catch(IOException e) {System.out.println(e);}
        frame.setTitle("Ranking");
        frame.setSize(300,300);

        JPanel p1 = new JPanel(new BorderLayout());
        JPanel p4 = new JPanel(); // 버튼 부착
        p1.add(p4, "South");

        p4.add(btRestart = new JButton("Restart"));
        p4.add(btExit = new JButton("Exit"));

        frame.add(p1);
        p1.add(ranking);

        ranking.setSize(300,300);

        btExit.addActionListener(this);
        btRestart.addActionListener(this);

        frame.setVisible(true);
        frame.setResizable(false);


    }

    public void actionPerformed(ActionEvent a) {
        Object o = a.getSource();
        if (o == btExit) {
            System.exit(0); // exit 누르면 종료
        }else if(o == btRestart) {
            frame.dispose();
        }


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

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == btLogin) {
            String name = tfName.getText();
            JOptionPane.showMessageDialog(btReset, name + "님 로그인을 환영합니다.");
            try {
                FileWriter fw = new FileWriter("ranking.txt", true);
                BufferedWriter bf = new BufferedWriter(fw);
                bf.write(tfName.getText() + ":");
                bf.close();
                tfName.setText("");
            } catch (Exception aa) {
                System.out.println(aa);
            }
            new BallonPangPang();

        } else if (o == btReset) {
            tfName.setText(" ");
            tfName.requestFocus();
        } else {

            System.exit(0); // exit 누르면 종료
        }
    }
}
class Rank { //추가한거! 랭킹순으로 배열해주는거
    public static int k = 0;
    public static String[][] Rank = new String[2][1000];

    public static void bubble() {
        String tmp = "";
        int a1, b1;
        while (Rank[0][k] != null)
            k++;
        for (int i = 0; i < k; i++) {
            for (int j = 1; j < k; j++) {
                a1 = Integer.parseInt(Rank[1][j - 1]);
                b1 = Integer.parseInt(Rank[1][j]);
                if (a1 < b1) {
                    tmp = Rank[0][j - 1];
                    Rank[0][j - 1] = Rank[0][j];
                    Rank[0][j] = tmp;
                    tmp = Rank[1][j - 1];
                    Rank[1][j - 1] = Rank[1][j];
                    Rank[1][j] = tmp;
                }
            }
        }
    }
    Rank() {
        try {
            File file = new File("ranking.txt");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String lst = "";

            int i = 0;
            while ((lst = br.readLine()) != null) {
                Rank[0][i] = new String(lst.split(":")[0]);
                Rank[1][i] = new String(lst.split(":")[1]);
                i++;
            }
            bubble(); // 섞기
            FileWriter fw = new FileWriter(file, false); // 메모장 다 지우고 다시 저장!
            for (i = 0; i < k; i++) {
                fw.write(Rank[0][i] + ":" + Rank[1][i] + "\r\n");
            }
            fw.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            System.out.print(e);
        }
    }
}

