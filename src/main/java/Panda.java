import java.awt.*;

public class Panda extends AnimalTower {
    Image img, image;
    Toolkit toolit;

    public Panda(GameManager gm) {
        super("팬더", 25, 1, 1000, "A", 100, "Not Blue Attack", gm);
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

        image = toolit.getImage("./images/panda.png");
        g.drawImage(image, 1, 1, 28, 28, this);

		/*g.setColor(Color.orange);
		for (int i = 0; i < upgrade; i++)
			g.fillRect(2, 26 - i * 5, 10, 4);*/

        g2.setColor(Color.black);
        g2.drawRect(0, -2, 14, 4);
    }

    public void Hit() {
        targetUnit.damageUnit(attact, Color.RED, false);
    }
}