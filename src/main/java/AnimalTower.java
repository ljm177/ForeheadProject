import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

public class AnimalTower extends Canvas implements ActionListener, MouseListener {
    String name;
    Graphics2D g2;
    double radian;
    int x = 165;
    int y = 165;
    int ptX0;
    int ptY0;
    int positionI;
    int positionJ;
    int mouseX;
    int mouseY;
    int attact;
    int range;
    int speed;
    Ballon targetUnit;
    String attactType;
    int cost;
    Vector<Land> searchLands;
    JPanel inforPan;
    JButton buyBtn, cancelBtn;
    JButton upgradeBtn;
    JPanel informationPan;
    boolean bild; // 건설의 여부
    String information;
    GameManager gm;
    Timer attack;
    Potan potan;

    public AnimalTower(String n, int a, int r, int s, String atype, int c, String i, GameManager g) {
        name = n;
        attact = a;
        range = r;
        speed = s;
        attactType = atype;
        cost = c;
        information = i;
        gm = g;
        potan = new Potan();
        attack = new Timer(speed, this); //speed는 걸리는 밀리초, this는 호출될 이벤트
        searchLands = new Vector<Land>();

        g2 = (Graphics2D) super.getGraphics();
        inforPan = new JPanel();
        informationPan = new JPanel();
        launchInforPan();
        setBounds(150, 150, 30, 30);
    }

    public void launchInforPan() {
        inforPan.removeAll();
        inforPan.setBorder(new TitledBorder(""));
        inforPan.setLayout(new BoxLayout(inforPan, BoxLayout.Y_AXIS));
        inforPan.setAlignmentX(inforPan.LEFT_ALIGNMENT);
        inforPan.add(new JLabel("name : " + name));
        inforPan.add(new JLabel("power : " + String.valueOf(attact)));
        inforPan.add(new JLabel("range : " + String.valueOf(range)));
        inforPan.add(new JLabel("speed : " + String.valueOf(speed)));
        inforPan.add(new JLabel("cost  : " + String.valueOf(cost)));
        inforPan.add(new JLabel("information : " + information));
        if (bild == false) {
            buyBtn = new JButton("Buy");
            buyBtn.addActionListener(this);
            inforPan.add(buyBtn);
        }
        inforPan.repaint();
    }

    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == buyBtn) {
            gm.buyTower(name, cost);
        }
        if (targetUnit != null) {
            potan.shootting();
        }
    }

    public String getAttactType() {
        return attactType;
    }

    public void paint(Graphics g) {
    }

    public void TowerZone(int h, int w, Land land[][]) { //정찰 영역 
        bild = true;

        int startI = h - range;
        if (startI < 0)
            startI = 0;
        int endI = h + range;
        if (endI > 9)
            endI = 9;
        int startJ = w - range;
        if (startJ < 0)
            startJ = 0;
        int endJ = w + range;
        if (endJ > 14)
            endJ = 14;

        for (int i = startI; i < endI + 1; i++) {
            for (int j = startJ; j < endJ + 1; j++) {
                if (land[i][j].getType() == 1) {
                    searchLands.add(land[i][j]);
                    land[i][j].addTowerVec(this); //타워벡터에 새로운 타워 추가 그것을 land에 추가,,
                }
            }
        }
    }

    public void setXY(int x, int y, int pI, int pJ) {
        this.x = x;
        this.y = y;
        this.ptX0 = x + 10;
        this.ptY0 = y + 10;
        positionI = pI;
        positionJ = pJ;
    }

    public int getPositionI() {
        return positionI;
    }

    public int getPositionJ() {
        return positionJ;
    }

    public void rangeCal(Ballon b) { //사정거리 계산 메소드
        if (targetUnit == null) {
            targetUnit = b; //타겟 유닛으로 설정 후
            attack.start(); //공격 스레드 시작
        } else if (targetUnit == b) {
            turn(b.getX(), b.getY());
        } else {
        }
    }

    public void lostUnit(Ballon b) {
        if (b == targetUnit) {
            targetUnit = null; //타겟 유닛 null
        }
    }

    public void deadTarget(Ballon b) {
        if (targetUnit == b) {
            targetUnit = null;
            attack.stop();
        }
    }

    public void turn(int mx, int my) {
        mouseX = mx;
        mouseY = my;
        try {
            g2.rotate(-1 * radian);
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        }
        repaint();
        radian = Math.atan2(mouseY - y, mouseX - x);
        g2.rotate(radian);
        repaint();
    }

    public void mousePressed(MouseEvent e) { //마우스가 눌려졌을 때 이벤트
        launchInforPan();
        gm.showInfor(inforPan);
        GameManager.select = e.getComponent();
        gm.repaintAllTower();
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public Potan getPotan() {
        return potan;
    }

    class Potan extends Canvas implements ActionListener {
        Image img;
        boolean boom;
        Timer timer;
        int ptX;
        int ptY;
        int targetX;
        int targetY;
        String upDownX;
        String upDownY;

        public Potan() {
            timer = new Timer(80, this);
            setBounds(ptX, ptY, 5, 5);
            setVisible(false);
        }

        public void setXY(int x, int y) {
            ptX = x;
            ptY = y;
        }

        public void paint(Graphics g) {
            if (name.equals("곰"))
                g.setColor(Color.black);
            else if (name.equals("팬더"))
                g.setColor(Color.red);
            else if (name.equals("개구리")) {
                g.setColor(Color.black);
                g.fillRect(0, 0, 5, 5);
                g.setColor(Color.white);
            } else if (name.equals("닭"))
                g.setColor(Color.blue);
            else if (name.equals("돼지")) {
                g.setColor(Color.black);
                g.fillRect(0, 0, 5, 5);
                g.setColor(Color.green);
            }
            g.fillOval(0, 0, 5, 5);
        }

        public void shootting() {
            if (targetUnit != null){
                setBounds(ptX0, ptY0, 5, 5);
                ptX = ptX0;
                ptY = ptY0;
                setVisible(true);
                targetX = targetUnit.getX();
                targetY = targetUnit.getY();

                if (targetX > ptX)
                    upDownX = "up";
                else
                    upDownX = "down";
                if (targetY > ptY)
                    upDownY = "up";
                else
                    upDownY = "down";

                timer.start();
            }
        }

        public void actionPerformed(ActionEvent ev) {
            boolean ptXStop = false;
            boolean ptYStop = false;
            if (targetUnit == null){
                timer.stop();
                setVisible(false);
            } else {
                if ("up".equals(upDownX)) {
                    ptX += 20;
                    if (targetX < ptX){
                        ptX = targetUnit.getX();
                        ptXStop = true;
                    }
                } else if ("down".equals(upDownX)) {
                    ptX -= 20;
                    if (targetX > ptX) {
                        ptX = targetUnit.getX();
                        ptXStop = true;
                    }
                }

                if ("up".equals(upDownY)) {
                    ptY += 20;
                    if (targetY < ptY) {
                        ptY = targetUnit.getY();
                        ptYStop = true;
                    }
                } else if ("down".equals(upDownY)) {
                    ptY -= 20;
                    if (targetY > ptY) {
                        ptY = targetUnit.getY();
                        ptYStop = true;
                    }
                }

                setBounds(ptX, ptY, 5, 5);

                if (ptXStop && ptYStop){
                    Hit();
                    setVisible(false);
                    timer.stop();
                    ptX = ptX0;
                    ptY = ptY0;
                }
            }
        }
    }

    public void Hit() {
        targetUnit.damageUnit(attact, Color.black, false);
    }
}

