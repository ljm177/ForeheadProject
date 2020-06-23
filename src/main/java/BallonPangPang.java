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

class GameManager extends JPanel implements ActionListener {
    int money = 300;
    int level = 0;
    int time = 25;
    int score = 0;
    int life = 10;
    boolean playing = false;	//게임을 하는 중인가? -> 유닛을 만들면서 true로 바뀌고 Tread game이 시작됨
    Thread game = new Thread() {
        public void run() {
            for (int i = 0; i < 40; i++) { // level 40까지
                if (playing) {
                    level++; // 레벨 1씩 올라감
                    topPan.setLevelJL(level); // topPan의 레벨 증가시켜줌
                    for (int time = 25; time >= 0; time--) {
                        if (playing) {
                            topPan.setTimeJL(time);
                            if (i % 5 == 4) { // 5레벨마다 보스몹 -> 한마리만 등장시키려고 했는데 구현 못함..
                                try {
                                    if (time > 15) {
                                        makeUnit();
                                        Thread.sleep(1000);
                                    }
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                            } else {
                                try {

                                    if (time > 15) {
                                        makeUnit();
                                        Thread.sleep(1000); // 1초에 한마리씩
                                    } else {
                                        break;
                                    }
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                            }
                        }
                    }
                }
                // time의 범위를 나눠서 25초~15초 사이에는 1초에 한번씩 몬스터 생성 , 15초 밑부터는 시간만 줄어들도록 함

                for (int time = 15; time >= 0; time--) {
                    try {
                        topPan.setTimeJL(time);
                        Thread.sleep(1000);

                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
                money = money + 10 + i * 10;
            }
            time = 25;
            topPan.setTimeJL(time);
        }
    };
    BallonPangPang bp;
    Ground ground;
    TopPan topPan;
    LeftPan leftPan;
    RightPan rightPan;
    static Component select;
    Vector<AnimalTower> allTower = new Vector<AnimalTower>();
    String n;

    GameManager(BallonPangPang bp) {
        this.bp = bp;
        ground = new Ground(this);
        topPan = new TopPan(money);
        leftPan = new LeftPan(this);
        rightPan = new RightPan(ground);
        launchManager();	//게임 상태판, 동물타워 버튼 판, 게임 판 부착
    }

    public void launchManager() {
        setLayout(null);
        setBounds(10, 5, 750, 550);
        setBorder(new TitledBorder("BallonPangPang"));
        add(topPan);
        add(leftPan);
        add(rightPan);
    }

    public void buyTower(String name, int cost) {	//name은 구매할 동물 타워의 이름, cost은 구매할 타워의 가격
        AnimalTower tower = null;
        System.out.println(name);
        if (money >= cost) {
            if (name.equals("곰"))
                tower = new Bear(this);
            else if (name.equals("팬더"))
                tower = new Panda(this);
            else if (name.equals("개구리"))
                tower = new Frog(this);
            else if (name.equals("닭"))
                tower = new Chicken(this);
            else if (name.equals("돼지"))
                tower = new Pig(this);
            else
                System.out.println("만족하는 동물 타워가 없습니다.");
            tower.addMouseListener(tower);	//새로 만들어 지는 동물 타워에 마우스 이벤트 달기
            ground.bildTower(tower, cost);	//살 타워와 비용을 받음
        } else
            System.out.println("돈이부족해요");
    }

    public void buy(int cost) {
        money = money - cost;	//cost가 살 타워의 가격
        topPan.setMoneyJL(money);
    }

    public void upgrade(AnimalTower t, int cost) {
        if (money < cost) {
            System.out.println("업그레이드 비용이 부족합니다.");
        } else {
            money = money - cost;
            topPan.setMoneyJL(money);
            t.upgrade();
        }
    }

    public void addAllTower(AnimalTower t) {
        allTower.add(t);
    }

    public void repaintAllTower() {
        for (int i = 0; i < allTower.size(); i++) {
            AnimalTower t = allTower.get(i);
            t.repaint();
        }
    }

    public Ballon makeUnit() {
        Ballon unit = null;
        if (level % 5 == 1)
            unit = new RedUnit(0, ground.getLand(), this, level);
        else if (level % 5 == 2)
            unit = new YellowUnit(0, ground.getLand(), this, level);
        else if (level % 5 == 3)
            unit = new GreenUnit(0, ground.getLand(), this, level);
        else if (level % 5 == 4)
            unit = new BlueUnit(0, ground.getLand(), this, level);
        else if (level % 5 == 0)
            unit = new PurPleUnit(0, ground.getLand(), this, level);
        else
            System.out.println("맞는타입의 유닛이 없습니다.");

        unit.addMouseListener(unit);	//유닛에 마우스 이벤트 달기(마우스 누르면 유닛의 정보가 뜸)
        ground.addUnit(unit);
        ground.repaint();
        unit.moveStart();
        return unit;
    }

    public void actionPerformed(ActionEvent ae) {	//LeftPan의 Game Start 버튼의 이벤트 구현 메소드
        if (playing == false) {	//초기 playing은 false인 상태
            level = 0;
            playing = true;
            game.start();	//game 스레드 시작해서 유닛 나오는 시간 조절과 동시에 게임 상태 표시
            leftPan.setEnabledStartBtn(false);	//한번 누른 후 다시 눌리지 않게
        }
    }

    public void showInfor(JPanel jp) {
        leftPan.showInforLP(jp);
    }

    public void deadUnit(Ballon b) {
        ground.removeUnit(b);
        b = null;
        if (playing) {
            money = money + 1 + level / 2;
            topPan.setMoneyJL(money);
            score = score + 10;
            topPan.setScoreJL(score);
        }
    }

    public void exitUnit() { // 풍선이 길을 지나가버릴때 (라이프 -1)
        life -= 1;
        topPan.setLifeJL(life);
        if (life < 1) {
            if (playing) {
                playing = false;
                JOptionPane.showMessageDialog(new JFrame(), "Game Over", "알림", JOptionPane.PLAIN_MESSAGE);
                System.out.println("Game Over");
                bp.initGame();
                try {
                    FileWriter fw = new FileWriter("ranking.txt", true);
                    BufferedWriter bf = new BufferedWriter(fw);
                    bf.write(score + "\n");
                    bf.close();
                    new Rank(); //랭킹 sorting
                    bp.frame.dispose();
                    new RankingPan(); //랭킹 출력
                } catch (Exception aa) {

                    System.out.println(aa);
                }
            }
            leftPan.setEnabledStartBtn(true);
        }
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

class TopPan extends JPanel {
    JLabel levelJL, timeJL, moneyJL, scoreJL, lifeJL;

    TopPan(int m) {
        levelJL = new JLabel("Level : 1");
        timeJL = new JLabel("Time : 25");
        moneyJL = new JLabel("Money : " + String.valueOf(m));
        scoreJL = new JLabel("Score : 0");
        lifeJL = new JLabel("Life : 10");
        launchTopPan();
    }

    public void launchTopPan() {
        setBounds(10, 20, 720, 30);
        setBackground(Color.WHITE);
        setLayout(new FlowLayout(FlowLayout.CENTER, 50, 0));
        setBorder(new TitledBorder(""));
        add(lifeJL);
        add(levelJL);
        add(timeJL);
        add(moneyJL);
        add(scoreJL);
    }

    public void setLevelJL(int l) {
        levelJL.setText("Level : " + String.valueOf(l));
    }

    public void setTimeJL(int t) {
        timeJL.setText("Time : " + String.valueOf(t));
    }

    public void setMoneyJL(int m) {
        moneyJL.setText("Money : " + String.valueOf(m));
    }

    public void setScoreJL(int s) {
        scoreJL.setText("Score : " + String.valueOf(s));
    }

    public void setLifeJL(int l) {
        lifeJL.setText("Life : " + String.valueOf(l));
    }
}

class RightPan extends JPanel {
    Ground groundPan;
    JButton btns[] = { new JButton("play"), new JButton("stop"), new JButton("play again")};
    Clip clip;
    JPanel musicControl;

    RightPan(Ground gp) {
        groundPan = gp;
        musicControl = new JPanel();
        launchRightPan();
    }

    public void launchRightPan() {
        setBounds(230, 60, 500, 450);	//setBounds(x, y, w, h)
        setBackground(Color.WHITE);
        setLayout(null);
        setBorder(new TitledBorder(""));
        add(groundPan);
        musicControl.setBounds(10, 370, 480, 70);
        musicControl.setBackground(Color.WHITE);
        MyActionListener al = new MyActionListener();

        for(int i=0; i<btns.length; i++) {
            btns[i].setSize(50, 50);
            btns[i].setLocation(200 + 15 * i, 300);
            musicControl.add(btns[i]);
            btns[i].addActionListener(al);
        }
        setVisible(true);
        add(musicControl);
        loadAudio("audio/37.wav");
    }
    void loadAudio(String pathName) {
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

    class MyActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            switch(e.getActionCommand()) {
                case "play": clip.start(); // 오디오 재생 시작
                    break;
                case "stop": clip.stop(); // 오디오 재생 중단
                    break;
                case "play again":
                    clip.setFramePosition(0); // 재생 위치를 첫 프레임으로 변경
                    clip.start(); // 오디오 재생 시작
                    break;
            }
        }
    }
}

class LeftPan extends JPanel {
    JButton startBtn;
    CardLayout cardLayout = new CardLayout();
    JPanel inforPan, towerPan, informationPan;
    GameManager gm;

    LeftPan(GameManager gm) {
        this.gm = gm;
        startBtn = new JButton("Game Start");
        startBtn.addActionListener(gm);
        launchLeftPan();
    }

    public void launchLeftPan() {
        JPanel btnPan = new JPanel();	//start 버튼이 있는 Panel
        btnPan.setBorder(new TitledBorder(""));
        btnPan.add(startBtn);

        towerPan = new JPanel();
        towerPan.setBorder(new TitledBorder(""));
        AnimalTower baerTw = new Bear(gm);
        baerTw.addMouseListener(baerTw);
        AnimalTower pandaTw = new Panda(gm);
        pandaTw.addMouseListener(pandaTw);
        AnimalTower frogTw = new Frog(gm);
        frogTw.addMouseListener(frogTw);
        AnimalTower chickenTw = new Chicken(gm);
        chickenTw.addMouseListener(chickenTw);
        AnimalTower pigTw = new Pig(gm);
        pigTw.addMouseListener(pigTw);
        gm.addAllTower(baerTw);
        gm.addAllTower(pandaTw);
        gm.addAllTower(frogTw);
        gm.addAllTower(chickenTw);
        gm.addAllTower(pigTw);

        towerPan.add(baerTw);
        towerPan.add(pandaTw);
        towerPan.add(frogTw);
        towerPan.add(chickenTw);
        towerPan.add(pigTw);

        inforPan = new JPanel();
        inforPan.setBorder(new TitledBorder(""));
        inforPan.setLayout(cardLayout);
        informationPan = new JPanel();
        inforPan.add("defaultJP", new JPanel());
        inforPan.add("inforJP", informationPan);
        cardLayout.show(inforPan, "defaultJP");

        add(btnPan);
        add(towerPan);
        add(inforPan);
        setBounds(10, 60, 210, 450);
        setBackground(Color.WHITE);
        setBorder(new TitledBorder(""));
    }

    public void showInforLP(JPanel in) {
        informationPan.removeAll();
        informationPan.add(in);
        cardLayout.show(inforPan, "defaultJP");
        cardLayout.show(inforPan, "inforJP");
    }

    public void setEnabledStartBtn(boolean b) {
        startBtn.setEnabled(b);
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

