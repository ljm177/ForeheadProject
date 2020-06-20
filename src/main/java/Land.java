import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

class Land extends JPanel {
	int type = 0;
	Image img;
	Land(int t) {
		type = t;
		Toolkit toolit = Toolkit.getDefaultToolkit();
		try{
			if ( t == 1)
				img = toolit.getImage("./images/cloudroad.gif");
		else if (t == 0)
				img = toolit.getImage("./images/sky.gif");
		else
				img = toolit.getImage("./images/cloud.gif");		
		}catch(Exception e){
			System.out.println(e);
		}		
	}
	public void paintComponent(Graphics g) {
		g.drawImage(img,0,0,30,30,this);		
	}
}
