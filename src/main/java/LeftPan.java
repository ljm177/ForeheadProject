import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class LeftPan extends JPanel {
	JButton startBtn;
	CardLayout cardLayout = new CardLayout();
	JPanel inforPan, towerPan, informationPan;
	GameManager gm;

	public LeftPan(GameManager gm) {
		this.gm = gm;
		startBtn = new JButton("Game Start");
		startBtn.addActionListener(gm);
		launchLeftPan();
	}

	public void launchLeftPan() {
		JPanel btnPan = new JPanel();	//start 버튼이 있는 Panel
		btnPan.setBorder(new TitledBorder(""));
		btnPan.add(startBtn);

		towerPan = new JPanel();
		towerPan.setBorder(new TitledBorder(""));
		AnimalTower baerTw = new Bear(gm);
		baerTw.addMouseListener(baerTw);
		AnimalTower pandaTw = new Panda(gm);
		pandaTw.addMouseListener(pandaTw);
		AnimalTower frogTw = new Frog(gm);
		frogTw.addMouseListener(frogTw);
		AnimalTower chickenTw = new Chicken(gm);
		chickenTw.addMouseListener(chickenTw);
		AnimalTower pigTw = new Pig(gm);
		pigTw.addMouseListener(pigTw);
		gm.addAllTower(baerTw);
		gm.addAllTower(pandaTw);
		gm.addAllTower(frogTw);
		gm.addAllTower(chickenTw);
		gm.addAllTower(pigTw);

		towerPan.add(baerTw);
		towerPan.add(pandaTw);
		towerPan.add(frogTw);
		towerPan.add(chickenTw);
		towerPan.add(pigTw);

		inforPan = new JPanel();
		inforPan.setBorder(new TitledBorder(""));
		inforPan.setLayout(cardLayout);
		informationPan = new JPanel();
		inforPan.add("defaultJP", new JPanel());
		inforPan.add("inforJP", informationPan);
		cardLayout.show(inforPan, "defaultJP");

		add(btnPan);
		add(towerPan);
		add(inforPan);
		setBounds(10, 60, 210, 450);
		setBackground(Color.WHITE);
		setBorder(new TitledBorder(""));
	}

	public void showInforLP(JPanel in) {
		informationPan.removeAll();
		informationPan.add(in);
		cardLayout.show(inforPan, "defaultJP");
		cardLayout.show(inforPan, "inforJP");
	}

	public void setEnabledStartBtn(boolean b) {
		startBtn.setEnabled(b);
	}
}
