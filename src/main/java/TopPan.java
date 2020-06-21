import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class TopPan extends JPanel {
	JLabel levelJL, timeJL, moneyJL, scoreJL, lifeJL;

	public TopPan(int m) {
		levelJL = new JLabel("Level : 1");
		timeJL = new JLabel("Time : 20");
		moneyJL = new JLabel("Money : " + String.valueOf(m));
		scoreJL = new JLabel("Score : 0");
		lifeJL = new JLabel("Life : 10");
		launchTopPan();
	}

	public void launchTopPan() {
		setBounds(10, 20, 720, 30);
		setBackground(Color.WHITE);
		setLayout(new FlowLayout(FlowLayout.CENTER, 50, 0));
		setBorder(new TitledBorder(""));
		add(lifeJL);
		add(levelJL);
		add(timeJL);
		add(moneyJL);
		add(scoreJL);
	}

	public void setLevelJL(int l) {
		levelJL.setText("Level : " + String.valueOf(l));
	}

	public void setTimeJL(int t) {
		timeJL.setText("Time : " + String.valueOf(t));
	}

	public void setMoneyJL(int m) {
		moneyJL.setText("Money : " + String.valueOf(m));
	}

	public void setScoreJL(int s) {
		scoreJL.setText("Score : " + String.valueOf(s));
	}

	public void setLifeJL(int l) {
		lifeJL.setText("Life : " + String.valueOf(l));
	}
}
