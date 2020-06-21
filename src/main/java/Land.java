import java.awt.*;
import java.util.Vector;

import javax.swing.JPanel;

class Land extends JPanel {
    int x;
    int y;
    int type = 0;
    Vector<Ballon> unitVec;
    Vector<AnimalTower> towerVec;
    Image img;

    Land(int x, int y, int t) {
        this.x = x;
        this.y = y;
        type = t;
        unitVec = new Vector<Ballon>();
        towerVec = new Vector<AnimalTower>();
        Toolkit toolit = Toolkit.getDefaultToolkit();
        try {
            if (t == 1)    //맵을 그려주는
                img = toolit.getImage("./images/cloudroad.gif");
            else if (t == 0)
                img = toolit.getImage("./images/sky.gif");
            else
                img = toolit.getImage("./images/cloud.gif");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, 30, 30, this);
    }

    public void rangeAttack(int d, Color c, boolean range) {    //d는 데미지 양, c는 색, range는 범위 안에 있는가
        if (type == 1) {    //길 위에 있을 때
            for (int i = 0; i < unitVec.size(); i++) {
                Ballon unit = unitVec.get(i);
                unit.damageUnit(d, c, range);
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getType() {
        return type;
    }

    public Vector<Ballon> getUnitVec() {
        return unitVec;
    }

    public void addUnitVec(Ballon b) {
        if (unitVec.contains(b))    //unitVec에 b가 있으면
            System.out.println("+b");
        else
            unitVec.add(b);   //새로 추가
    }

    public void removeUnitVec(Ballon b) {
        if (unitVec.contains(b)) {
            unitVec.remove(b);
            for (int i = 0; i < towerVec.size(); i++) {
                AnimalTower t = towerVec.get(i);
                t.lostUnit(b);    //타켓유닛 null
            }
        } else
            System.out.println("-b");
    }

    public Vector<AnimalTower> getTower() {
        return towerVec;
    }

    public void addTowerVec(AnimalTower t) {
        if (towerVec.contains(t))
            System.out.println("+t");
        else
            towerVec.add(t);
    }
}
