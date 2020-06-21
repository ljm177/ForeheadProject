import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class RightPan extends JPanel {
	Ground groundPan;

	public RightPan(Ground gp) {
		groundPan = gp;
		launchRightPan();
	}

	public void launchRightPan() {
		setBounds(230, 60, 500, 450);	//setBounds(x, y, w, h)
		setBackground(Color.WHITE);
		setLayout(null);
		setBorder(new TitledBorder(""));
		add(groundPan);
	}
}
