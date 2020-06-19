import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

class LeftPan extends JPanel {
	JButton startBtn;
	CardLayout cardLayout = new CardLayout();
	JPanel towerPan;
	GameManager gm;

	LeftPan(GameManager gm) {
		this.gm = gm;
		startBtn = new JButton("Game Start");
		launchRightPan();
	}

	public void launchRightPan() {
		JPanel btnPan = new JPanel();
		btnPan.setBorder(new TitledBorder(""));
		btnPan.add(startBtn);
		
		towerPan = new JPanel();
		towerPan.setBorder(new TitledBorder(""));
		//AnimalTower normalTw = new Bear(gm);
		//AnimalTower fireTw = new Panda(gm);
		//AnimalTower iceTw = new Frog(gm);
		//AnimalTower airTw = new Chicken(gm);
		//AnimalTower poisonTw = new Pig(gm);
		
		//towerPan.add(normalTw);
		//towerPan.add(fireTw);
		//towerPan.add(iceTw);
		//towerPan.add(airTw);
		//towerPan.add(poisonTw);

		add(btnPan);
		add(towerPan);
		setBounds(10,60,210,450);
		setBackground(Color.WHITE);
		setBorder(new TitledBorder(""));
	}
}
