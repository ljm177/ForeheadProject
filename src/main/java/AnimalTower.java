import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

class AnimalTower extends Canvas {
	String name;
    Graphics2D g2;
	Ballon targetUnit;
	Vector <Land>searchLands;
	GameManager gm;

	AnimalTower(String n, GameManager g) {
		name = n;
		gm = g;
		searchLands = new Vector<Land>();
        g2 = (Graphics2D)super.getGraphics();
        setBounds(150,150,30,30);
    }

	public void paint(Graphics g) { }
}
