import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.*;
import javax.swing.Timer;

public class Ballon extends Canvas implements ActionListener, MouseListener {
    String name;	//풍선 이름
    JLabel lifeJL, speedJL, traitJL, stateJL;
    String type;	//풍선의 타입
    int life;	//유닛의 생명
    int speed = 100;	//유닛의 속도
    int x = 70;
    int y = 0;
    int size;

    Graphics2D g2;
    int wayCount = 0;
    int wayPoint[][] = { { 65, 215 }, { 155, 215 }, { 155, 65 }, { 365, 65 }, { 365, 155 }, { 245, 155 }, { 245, 245 },
            { 435, 245 } };	//오른쪽 길 좌표
    int wayPoint2[][] = { { 60, 225 }, { 165, 225 }, { 165, 75 }, { 360, 75 }, { 360, 150 }, { 240, 150 }, { 240, 255 },
            { 435, 255 } };	//왼쪽 길 좌표
    int myWay[][];
    Land land[][];
    Land currentLand;
    GameManager gm;
    boolean damage;	//데미지를 줄 수 있는 유무
    JPanel inforPan;	//풍선 유닛에 대한 정보를 주는 패널
    double radian;
    Color damageColor;	//데미지 컬러
    boolean ice, poison;	//얼음이나 독을 맞은 상태 유무
    int poisionDamage;
    boolean immune;	//면역의 상태 여부

    Timer timer;

    Ballon(int way, Land lands[][], GameManager gm, String n, int l, int s, String t, int size) {
        life = l;
        this.speed = s;
        name = n;
        this.gm = gm;
        type = t;
        this.size = size;
        lifeJL = new JLabel("life : " + String.valueOf(life));
        speedJL = new JLabel("speed : " + String.valueOf(speed));
        traitJL = new JLabel("trait : 없음");
        stateJL = new JLabel("state : 양호");
        inforPan = new JPanel();
        setBounds(70, 0, size, size);
        if (way == 0)
            myWay = wayPoint;
        else
            myWay = wayPoint2;
        land = lands;
        timer = new Timer(speed, this);
    }

    class IceThread extends Thread {
        public void run() {
            ice = true;
            if (poison)
                stateJL.setText("state : 얼음 + 독");
            else
                stateJL.setText("state : 얼음");

            timer.setDelay(speed + speed / 3);
            speedJL.setText("speed : " + String.valueOf(speed + speed / 3));
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                System.out.println(e);
            }
            timer.setDelay(speed);
            ice = false;
            speedJL.setText("speed : " + String.valueOf(speed));
            if (poison)
                stateJL.setText("state : 독");
            else
                stateJL.setText("state : 양호");
        }
    };

    class PoisionThread extends Thread {
        public void run() {
            poison = true;
            if (ice)
                stateJL.setText("state : 얼음 + 독");
            else
                stateJL.setText("state : 독");

            for (int i = 0; i < 10; i++) {
                damageUnit(poisionDamage, Color.green, false);
                try {
                    Thread.sleep(700);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            timer.setDelay(speed);
            poison = false;
            if (ice)
                stateJL.setText("state : 얼음");
            else
                stateJL.setText("state : 양호");
        }
    };

    public void moveStart() {
        timer.start();
    }

    public void moveStop() {
        timer.stop();
    }

    public void showInformation() {
    }

    public void paint(Graphics g) {
    }

    public void move() {
    }

    public void damageUnit(int d, Color c, boolean range) {

        if (range) {
            Vector<Ballon> units = currentLand.getUnitVec();
            int i = currentLand.getY() / 30;
            int j = currentLand.getX() / 30;
            if (i - 1 > -1)
                land[i - 1][j].rangeAttack(d, c, false);
            if (j - 1 > -1)
                land[i][j - 1].rangeAttack(d, c, false);
            land[i][j].rangeAttack(d, c, false);
            if (j + 1 < 15)
                land[i][j + 1].rangeAttack(d, c, false);
            if (i + 1 < 15)
                land[i + 1][j].rangeAttack(d, c, false);
        }

        damageColor = c;
        life = life - d;
        lifeJL.setText("life : " + String.valueOf(life));
        damage = true;

        if (life < 1) {
            currentLand.remove(this);
            dead();
        } else {
            if (immune) {
            } else {
                if (c == Color.white && ice == false)
                    new IceThread().start();
                if (c == Color.green && poison == false) {
                    poisionDamage = d / 5;
                    new PoisionThread().start();
                }
            }
        }
        repaint();
    }

    public void actionPerformed(ActionEvent ae) {
        boolean xx = false;
        boolean yy = false;

        if (myWay[wayCount][0] > x)
            x += 5;
        else if (myWay[wayCount][0] < x)
            x -= 5;
        else
            xx = true;

        if (myWay[wayCount][1] > y)
            y += 5;
        else if (myWay[wayCount][1] < y)
            y -= 5;
        else
            yy = true;

        setBounds(x, y, size, size);

        int imsiX = x / 30;
        int imsiY = y / 30;
        if (currentLand != land[imsiY][imsiX]) {
            if (currentLand != null)
                currentLand.removeUnitVec(this);	//유닛을 지우고 타워의 타겟유닛을 null
            currentLand = land[imsiY][imsiX];
            currentLand.addUnitVec(this);
        }
        aimTower(currentLand.getTower());

        if (xx && yy) {
            wayCount++;

            if (wayCount == 8) {
                //gm.exitUnit();
                timer.stop();
                currentLand.remove(this);
                dead();
            }
        }
    }

    public void turn() {
    }

    public String getType() {
        return type;
    }

    public void aimTower(Vector<AnimalTower> towerVec) {
        for (int i = 0; i < towerVec.size(); i++) {
            AnimalTower t = towerVec.get(i);
            if (t.getAttactType().equals("A+B"))
                t.rangeCal(this);
            else if (type.equals(t.getAttactType()))
                t.rangeCal(this);
        }
    }

    public void dead() {
        timer.stop();
        //gm.deadUnit(this);
        Vector<AnimalTower> tv = currentLand.getTower();
        for (int i = 0; i < tv.size(); i++) {
            AnimalTower t = tv.get(i);
            t.deadTarget(this);
        }
        lifeJL.setText("life : " + String.valueOf(0));	//죽으면 풍선의 목숨은 0으로
    }

    public void launchInforPan() {
        inforPan.removeAll();
        inforPan.setBorder(new TitledBorder(""));
        inforPan.setLayout(new BoxLayout(inforPan, BoxLayout.Y_AXIS));
        inforPan.setAlignmentX(inforPan.LEFT_ALIGNMENT);
        inforPan.add(new JLabel("name : " + name));
        inforPan.add(new JLabel("type : " + type));
        inforPan.add(lifeJL);
        inforPan.add(speedJL);
        inforPan.add(stateJL);
        inforPan.add(traitJL);
        inforPan.repaint();
    }

    public void mousePressed(MouseEvent e) {
        launchInforPan();
//        gm.showInfor(inforPan);
//        GameManager.select = e.getComponent();
    }
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}

}
