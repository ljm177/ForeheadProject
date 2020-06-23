import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class GameManager extends JPanel implements ActionListener {
	int money = 300;
	int level = 0;
	int time = 25;
	int score = 0;
	int life = 10;
	boolean playing = false;	//게임을 하는 중인가? -> 유닛을 만들면서 true로 바뀌고 Tread game이 시작됨
	Thread game = new Thread() {
		public void run() {
			for (int i = 0; i < 40; i++) { // level 40까지
				if (playing) {
					level++; // 레벨 1씩 올라감
					topPan.setLevelJL(level); // topPan의 레벨 증가시켜줌
					for (int time = 25; time >= 0; time--) {
						if (playing) {
							topPan.setTimeJL(time);
							if (i % 5 == 4) { // 5레벨마다 보스몹 -> 한마리만 등장시키려고 했는데 구현 못함..
								try {
									if (time > 15) {
										makeUnit();
										Thread.sleep(1000);
									}
								} catch (Exception e) {
									System.out.println(e);
								}
							} else {
								try {

									if (time > 15) {
										makeUnit();
										Thread.sleep(1000); // 1초에 한마리씩
									} else {
										break;
									}
								} catch (Exception e) {
									System.out.println(e);
								}
							}
						}
					}
				}
				// time의 범위를 나눠서 25초~15초 사이에는 1초에 한번씩 몬스터 생성 , 15초 밑부터는 시간만 줄어들도록 함

				for (int time = 15; time >= 0; time--) {
					try {
						topPan.setTimeJL(time);
						Thread.sleep(1000);

					} catch (Exception e) {
						System.out.println(e);
					}
				}
				money = money + 10 + i * 10;
			}
			time = 25;
			topPan.setTimeJL(time);
		}
	};
	BallonPangPang bp;
	Ground ground;
	TopPan topPan;
	LeftPan leftPan;
	RightPan rightPan;
	static Component select;
	Vector<AnimalTower> allTower = new Vector<AnimalTower>();
	String n;

	GameManager(BallonPangPang bp) {
		this.bp = bp;
		ground = new Ground(this);
		topPan = new TopPan(money);
		leftPan = new LeftPan(this);
		rightPan = new RightPan(ground);
		launchManager();	//게임 상태판, 동물타워 버튼 판, 게임 판 부착
	}

	public void launchManager() {
		setLayout(null);
		setBounds(10, 5, 750, 550);
		setBorder(new TitledBorder("BallonPangPang"));
		add(topPan);
		add(leftPan);
		add(rightPan);
	}

	public void buyTower(String name, int cost) {	//name은 구매할 동물 타워의 이름, cost은 구매할 타워의 가격
		AnimalTower tower = null;
		System.out.println(name);
		if (money >= cost) {
			if (name.equals("곰"))
				tower = new Bear(this);
			else if (name.equals("팬더"))
				tower = new Panda(this);
			else if (name.equals("개구리"))
				tower = new Frog(this);
			else if (name.equals("닭"))
				tower = new Chicken(this);
			else if (name.equals("돼지"))
				tower = new Pig(this);
			else
				System.out.println("만족하는 동물 타워가 없습니다.");
			tower.addMouseListener(tower);	//새로 만들어 지는 동물 타워에 마우스 이벤트 달기
			ground.bildTower(tower, cost);	//살 타워와 비용을 받음
		} else
			System.out.println("돈이부족해요");
	}

	public void buy(int cost) {
		money = money - cost;	//cost가 살 타워의 가격
		topPan.setMoneyJL(money);
	}

	public void upgrade(AnimalTower t, int cost) {
		if (money < cost) {
			System.out.println("업그레이드 비용이 부족합니다.");
		} else {
			money = money - cost;
			topPan.setMoneyJL(money);
			t.upgrade();
		}
	}

	public void addAllTower(AnimalTower t) {
		allTower.add(t);
	}

	public void repaintAllTower() {
		for (int i = 0; i < allTower.size(); i++) {
			AnimalTower t = allTower.get(i);
			t.repaint();
		}
	}

	public Ballon makeUnit() {
		Ballon unit = null;
		if (level % 5 == 1)
			unit = new RedUnit(0, ground.getLand(), this, level);
		else if (level % 5 == 2)
			unit = new YellowUnit(0, ground.getLand(), this, level);
		else if (level % 5 == 3)
			unit = new GreenUnit(0, ground.getLand(), this, level);
		else if (level % 5 == 4)
			unit = new BlueUnit(0, ground.getLand(), this, level);
		else if (level % 5 == 0)
			unit = new PurPleUnit(0, ground.getLand(), this, level);
		else
			System.out.println("맞는타입의 유닛이 없습니다.");

		unit.addMouseListener(unit);	//유닛에 마우스 이벤트 달기(마우스 누르면 유닛의 정보가 뜸)
		ground.addUnit(unit);
		ground.repaint();
		unit.moveStart();
		return unit;
	}

	public void actionPerformed(ActionEvent ae) {	//LeftPan의 Game Start 버튼의 이벤트 구현 메소드
		if (playing == false) {	//초기 playing은 false인 상태
			level = 0;
			playing = true;
			game.start();	//game 스레드 시작해서 유닛 나오는 시간 조절과 동시에 게임 상태 표시
			leftPan.setEnabledStartBtn(false);	//한번 누른 후 다시 눌리지 않게
		}
	}

	public void showInfor(JPanel jp) {
		leftPan.showInforLP(jp);
	}

	public void deadUnit(Ballon b) {
		ground.removeUnit(b);
		b = null;
		if (playing) {
			money = money + 1 + level / 2;
			topPan.setMoneyJL(money);
			score = score + 10;
			topPan.setScoreJL(score);
		}
	}

	public void exitUnit() { // 풍선이 길을 지나가버릴때 (라이프 -1)
		life -= 1;
		topPan.setLifeJL(life);
		if (life < 1) {
			if (playing) {
				playing = false;
				JOptionPane.showMessageDialog(new JFrame(), "Game Over", "알림", JOptionPane.PLAIN_MESSAGE);
				System.out.println("Game Over");
				bp.initGame();
				try {
					FileWriter fw = new FileWriter("ranking.txt", true);
					BufferedWriter bf = new BufferedWriter(fw);
					bf.write(score + "\n");
					bf.close();
					new Rank(); //랭킹 sorting
					bp.frame.dispose();
					new RankingPan(); //랭킹 출력
				} catch (Exception aa) {

					System.out.println(aa);
				}
			}
			leftPan.setEnabledStartBtn(true);
		}
	}
}
