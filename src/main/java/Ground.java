import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.border.*;
import java.util.*;

public class Ground extends JPanel implements MouseListener, MouseMotionListener {
	int mouseX;
	int mouseY;
	Land land[][] = new Land[10][15];
	Boolean position[][] = new Boolean[20][30];
	AnimalTower tower;
	JLayeredPane landPan;	//깊이를 정해서 붙일 수 있는 팬
	JPanel mousePan;	//mousePan은 타워의 설치 유무를 따지기 위한 panel
	int cost;
	GameManager gm;

	Ground(GameManager gameM) {
		gm = gameM;
		super.setBounds(5, 10, 490, 350);
		super.setLayout(null);
		super.setBackground(Color.WHITE);
		for (int i = 0; i < 20; i++)	//초기화
			for (int j = 0; j < 30; j++)
				position[i][j] = new Boolean(false);
		addLand();
	}

	public void addLand() {
		landPan = new JLayeredPane();
		landPan.setBounds(5, 0, 200, 500);
		landPan.setLayout(null);
		mousePan = new JPanel();
		mousePan.setBounds(0, 0, 30, 30);
		mousePan.setBackground(Color.white);
		landPan.add(mousePan, new Integer(70));	//0이랑 가까울 수록 깊은거
		mousePan.setVisible(false);

		landPan.setBorder(new TitledBorder(""));
		int landType[][] = { { 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 },
				{ 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2 }, { 2, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 2 },
				{ 2, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 2 }, { 2, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 2 },
				{ 2, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 2 }, { 2, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 2 },
				{ 2, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 2 }, { 2, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1 },
				{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 } };

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				if (landType[i][j] == 0) {
					land[i][j] = new Land(j * 30, i * 30, 0);
					position[i * 2][j * 2] = true;
					position[i * 2][j * 2 + 1] = true;
					position[i * 2 + 1][j * 2] = true;
					position[i * 2 + 1][j * 2 + 1] = true;

				} else if (landType[i][j] == 1) {
					land[i][j] = new Land(j * 30, i * 30, 1);
				} else
					land[i][j] = new Land(j * 30, i * 30, 2);

				land[i][j].setBounds(j * 30, i * 30, 30, 30);
				landPan.add(land[i][j], new Integer(10));
			}
		}

		landPan.setBounds(10, 40, 450, 300);
		landPan.addMouseListener(this);
		landPan.addMouseMotionListener(this);
		add(landPan);
	}

	public void bildTower(AnimalTower at, int buyCost) {
		tower = at;
		cost = buyCost;
	}

	public void mousePressed(MouseEvent e) { //마우스를 눌렀을 때
		int pI = e.getY() / 15;
		int pJ = e.getX() / 15;

		if (tower != null && position[pI][pJ] && position[pI][pJ + 1] && position[pI + 1][pJ]
				&& position[pI + 1][pJ + 1]) {
			int x = e.getX() - e.getX() % 15;
			int y = e.getY() - e.getY() % 15;
			tower.setXY(x, y, pI, pJ);
			tower.setBounds(x, y, 30, 30);
			landPan.add(tower, new Integer(50));
			AnimalTower.Potan pt = tower.getPotan();
			pt.setXY(x + 10, y + 10);
			pt.setBounds(x + 10, y + 10, 10, 10);
			landPan.add(pt, new Integer(60));
			tower.TowerZone(e.getY() / 30, e.getX() / 30, land);
			//gm.addAllTower(tower);
			tower = null;
			mousePan.setVisible(false);

			position[pI][pJ] = false;
			position[pI][pJ + 1] = false;
			position[pI + 1][pJ] = false;
			position[pI + 1][pJ + 1] = false;
			//gm.buy(cost);
		} else {
			tower = null;
			mousePan.setVisible(false);
		}
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {	//마우스가 컴포넌트 위에 올라올 때
		if (tower != null)	//타워가 있을 때
			mousePan.setVisible(true);	//보이게 (타워가 지을 수 있는 곳과 없는 곳에 색을 나타나게 하기 위한 이벤트)
	}

	public void mouseExited(MouseEvent e) {		//컴포넌트 위에 올라온 마우스가 컴포넌트를 벗어날 때
		if (tower != null)
			mousePan.setVisible(false);	//안보이게
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {	//마우스가 컴포넌트 위에서 움직일 때
		if (tower != null) {
			int x = e.getX() - e.getX() % 15;
			int y = e.getY() - e.getY() % 15;
			mousePan.setBounds(x, y, 30, 30);

			int pI = e.getY() / 15;
			int pJ = e.getX() / 15;
			if (position[pI][pJ] && position[pI][pJ + 1] && position[pI + 1][pJ] && position[pI + 1][pJ + 1])	//타워를 설치할 수 있는
				mousePan.setBackground(Color.white);
			else	//타워를 설치하지 못하는
				mousePan.setBackground(Color.red);
		}
	}

	public void mouseDragged(MouseEvent e) {
	}

	public Land[][] getLand() {
		return land;
	}

	public void addUnit(Ballon unit) {
		landPan.add(unit, new Integer(100));
	}

	public void removeUnit(Ballon b) {
		landPan.remove(b);	//판에서 지우고
		b.removeMouseListener(b);	//달아줬던 이벤트 지우고
		b = null;	//없애고
		landPan.repaint();	//다시 그림
	}
}
