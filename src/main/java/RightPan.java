import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

class RightPan extends JPanel {
	Ground groundPan;
	
	RightPan(Ground gp) {
		groundPan = gp;
		launchLeftPan();
	}

	public void launchLeftPan() {
		setBounds(230,60,500,450);
		setBackground(Color.WHITE);
		setLayout(null);
		setBorder(new TitledBorder(""));
		add(groundPan);
	}	
}
