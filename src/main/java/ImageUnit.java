import java.awt.*;

public class ImageUnit extends Ballon {
    Image img, img1, img2, img3, image;
    int moveCount;
    Toolkit toolkit;

    ImageUnit(int way, Land land[][], GameManager gm, String name, int level, String type) {
        super(way, land, gm, name, 20 * level, 100, type, 20);
        toolkit = Toolkit.getDefaultToolkit();
        img = toolkit.getImage("./images/cloudroad.gif");
    }

    public void paint(Graphics g) {
        if (g2 == null) {
            g2 = (Graphics2D)getGraphics();
            g2.translate(10, 10);
        }

        g.drawImage(img, 0, 0, 20, 20, this);
//        if (GameManager.select == this) {
//            g.setColor(Color.blue);
//            g.fillRect(1, 1, 18, 18);
//        }

        if (moveCount % 3 == 0)
            image = img1;
        else if (moveCount % 3 == 1)
            image = img2;
        else if (moveCount % 3 == 2)
            image = img3;
        else
            image = img2;
        moveCount++;

        g2.drawImage(image, -10, -10, 20, 20, this);

        if (damage) {
            g.setColor(damageColor);
            g.fillOval(2, 2, 15, 15);
            damage = false;
        }
        if (ice) {
            g.setColor(Color.white);
            g.drawOval(0, 0, 19, 19);
        }
        if (poison) {
            g.setColor(Color.green);
            g.drawOval(1, 1, 17, 17);
        }
    }

    public void turn() {}
}

class RedUnit extends ImageUnit {
    RedUnit(int way, Land land[][], GameManager gm, int level) {
        super(way, land, gm, "빨간 풍선", level, "A");
        img1 = toolkit.getImage("./images/red.gif");
        img2 = toolkit.getImage("./images/red.gif");
        img3 = toolkit.getImage("./images/red.gif");
        image = img2;
        traitJL.setText("trait : 없음");
    }
}

class YellowUnit extends ImageUnit {
    YellowUnit(int way, Land land[][], GameManager gm, int level) {
        super(way, land, gm, "노란 풍선", level, "A");
        img1 = toolkit.getImage("./images/yellow.gif");
        img2 = toolkit.getImage("./images/yellow.gif");
        img3 = toolkit.getImage("./images/yellow.gif");
        image = img2;
        speed = 70;
        timer.setDelay(speed);
        speedJL.setText("speed : " + String.valueOf(speed));
        traitJL.setText("trait : 빨리달리기");
    }
}

class GreenUnit extends ImageUnit {
    GreenUnit(int way, Land land[][], GameManager gm, int level) {
        super(way, land, gm, "초록 풍선", level, "A");
        img1 = toolkit.getImage("./images/green.gif");
        img2 = toolkit.getImage("./images/green.gif");
        img3 = toolkit.getImage("./images/green.gif");
        image = img2;
        immune = true;	//면역이 있는
        traitJL.setText("trait : 얼음과 독에 안걸림");
    }
}

class BlueUnit extends ImageUnit {
    BlueUnit(int way, Land land[][], GameManager gm, int level) {
        super(way, land, gm, "파랑 풍선", level, "B");
        img1 = toolkit.getImage("./images/blue.gif");
        img2 = toolkit.getImage("./images/blue.gif");
        img3 = toolkit.getImage("./images/blue.gif");
        image = img2;
        traitJL.setText("trait : 독보적");
    }
}

class PurPleUnit extends ImageUnit {
    PurPleUnit(int way, Land land[][], GameManager gm, int level) {
        super(way, land, gm, "보라 풍선", level, "A");
        img1 = toolkit.getImage("./images/purple.gif");
        img2 = toolkit.getImage("./images/purple.gif");
        img3 = toolkit.getImage("./images/purple.gif");
        image = img2;
        life = level * 20 * 5;
        lifeJL.setText("life : " + String.valueOf(life));
        traitJL.setText("trait : 체력 5배");
    }
}