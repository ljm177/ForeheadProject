import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class Ground extends JPanel {	
    Land land[][] = new Land[10][15];
	JLayeredPane landPan;
	GameManager gm;

    Ground(GameManager gameM) {
		gm = gameM;
		super.setBounds(5,10,490,350);
		super.setLayout(null);
		addLand();
    }

	public void addLand() {
		landPan = new JLayeredPane();
		landPan.setBounds(5,0,200,500);
		landPan.setLayout(null);
		landPan.setBorder(new TitledBorder(""));
		int landType[][] = {
			{2,2,1,2,2,2,2,2,2,2,2,2,2,2,2},
			{2,0,1,0,0,0,0,0,0,0,0,0,0,0,2},
			{2,0,1,0,0,1,1,1,1,1,1,1,1,0,2},
			{2,0,1,0,0,1,0,0,0,0,0,0,1,0,2},
			{2,0,1,0,0,1,0,0,0,0,0,0,1,0,2},
			{2,0,1,0,0,1,0,0,1,1,1,1,1,0,2},
			{2,0,1,0,0,1,0,0,1,0,0,0,0,0,2},
			{2,0,1,1,1,1,0,0,1,0,0,0,0,0,2},
			{2,0,0,0,0,0,0,0,1,1,1,1,1,1,1},
			{2,2,2,2,2,2,2,2,2,2,2,2,2,2,2}
			};
		
		for (int i = 0 ; i < 10 ; i++ ) {
			for (int j = 0; j < 15; j++) {
				if (landType[i][j] == 0) {
					land[i][j] = new Land(0);		
				}
				else if (landType[i][j] == 1){
					land[i][j] = new Land(1);		
				}
				else 
					land[i][j] = new Land(2);
								
				land[i][j].setBounds(j*30, i*30, 30, 30);
				landPan.add(land[i][j], new Integer(10));
			}
		}		
		landPan.setBounds(10,40,450,300);
		//landPan.setBounds(40,40,450,300);
		add(landPan);	
	}
	public Land[][] getLand() {
		return land;
	}
}
