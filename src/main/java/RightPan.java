import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class RightPan extends JPanel {

	Ground groundPan;
	JButton btns[] = { new JButton("play"), new JButton("stop"), new JButton("play again")};
	Clip clip;
	JPanel musicControl;

	RightPan(Ground gp) {
		groundPan = gp;
		musicControl = new JPanel();
		launchRightPan();
	}

	public void launchRightPan() {
		setBounds(230, 60, 500, 450);	//setBounds(x, y, w, h)
		setBackground(Color.WHITE);
		setLayout(null);
		setBorder(new TitledBorder(""));
		add(groundPan);
		musicControl.setBounds(10, 370, 480, 70);
		musicControl.setBackground(Color.WHITE);
		MyActionListener al = new MyActionListener();

		for(int i=0; i<btns.length; i++) {
			btns[i].setSize(50, 50);
			btns[i].setLocation(200 + 15 * i, 300);
			musicControl.add(btns[i]);
			btns[i].addActionListener(al);
		}
		setVisible(true);
		add(musicControl);
		loadAudio("audio/37.wav");
	}
	void loadAudio(String pathName) {
		try {
			clip = AudioSystem.getClip(); // 비어있는 오디오 클립 만들기
			File audioFile = new File(pathName); // 오디오 파일의 경로명
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile); // 오디오 파일로부터
			clip.open(audioStream); // 재생할 오디오 스트림 열기
		}
		catch (LineUnavailableException e) { e.printStackTrace(); }
		catch (UnsupportedAudioFileException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); }

	}

	class MyActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			switch(e.getActionCommand()) {
				case "play": clip.start(); // 오디오 재생 시작
					break;
				case "stop": clip.stop(); // 오디오 재생 중단
					break;
				case "play again":
					clip.setFramePosition(0); // 재생 위치를 첫 프레임으로 변경
					clip.start(); // 오디오 재생 시작
					break;
			}
		}
	}
}
