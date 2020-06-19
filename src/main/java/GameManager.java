import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

class GameManager extends JPanel {
	int money = 300;
	int level = 0;
	int nextTime = 30;
	int score = 0;
	int life = 20;
	BallonPangPang td;
	Ground ground;
	TopPan topPan;
	LeftPan leftPan;
	RightPan rightPan;
	Vector <AnimalTower>allTower = new Vector<AnimalTower>();

	GameManager(BallonPangPang td) {
		this.td = td;
		ground = new Ground(this);
		topPan = new TopPan(money);
		leftPan = new LeftPan(this);
		rightPan = new RightPan(ground);
		launchManager();
	}

	public void launchManager() {
		setLayout(null);
		setBounds(10,5,750,550);
		setBorder(new TitledBorder("BallonPangPang"));
		add(topPan);
		add(leftPan);
		add(rightPan);
	}
}
