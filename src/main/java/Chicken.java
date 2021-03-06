import java.awt.*;

public class Chicken extends AnimalTower {
    Image img, image;
    Toolkit toolit;

    public Chicken(GameManager gm) {
        super("��", 50, 2, 800, "B", 250, "Only Blue Attack", gm);
        toolit = Toolkit.getDefaultToolkit();
        img = toolit.getImage("./images/sky.gif");
    }

    public void paint(Graphics g) {
        if (g2 == null) {
            g2 = (Graphics2D) getGraphics();
            g2.translate(15, 15);
        }
        g.drawImage(img, 0, 0, 30, 30, this);
        if (GameManager.select == this) {
            g.setColor(Color.yellow);
            g.fillRect(1, 1, 28, 28);
        }

        image = toolit.getImage("./images/chicken.png");
        g.drawImage(image, 1, 1, 28, 28, this);

		/*g.setColor(Color.orange);
		for (int i = 0; i < upgrade; i++)
			g.fillRect(2, 26 - i * 5, 10, 4);*/

        g2.setColor(Color.black);
        g2.drawRect(0, -2, 14, 4);
    }

    public void Hit() {
        targetUnit.damageUnit(attact, Color.BLUE, false);
    }
}