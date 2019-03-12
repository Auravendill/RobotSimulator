
//import java.awt.BorderLayout;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
//import java.awt.Graphics2D;
import java.awt.Graphics2D;
import java.awt.Toolkit;
//import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
//import java.awt.geom.Line2D;
//import java.util.Arrays;
//import java.util.Collections;
import java.util.List;
//import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel implements KeyListener {
	static double Vl = 0;
	static double Vr = 0;
	static double MaxV = 15;
	static double Angle = 0;
	static double Xpos = 200;
	static double Ypos = 200;
	static double deltaTime = 0.125;
	static double dustDensity = 10;
	static int radius = 15;// 15 pixels
	static int field = 1;
	static int DustRemoved=0;

	static boolean itterate = true;

	static boolean visualize = true;
	static boolean useNN = true;
	static boolean trainNN = false;

	static int generations = 200;
	static int population = 40;
	static int inputNodes = 15;
	static int hiddenNodes = 12;
	static int outputNodes = 2;

	static int tournamentSize = 5;
	static int amountOfWinners = 15;
	static int runTime = 2000;
	static String AVfitness="";
	static String SError="";
	static String MaxFitness="";
	static String NeuralNetwork="";
	
	static String Deviations="";
	
	static double[] standardDeviation = new double[generations];
	static double[] standardError = new double[generations];
	static double[] deviation = new double[generations];
	static double[] preTrained= {0.07944484161183851, -0.9627965835402303, 5.255500765641127, -3.8144818186034533, -4.723091291589656, -2.0539564844764104, 4.4315488913773535, 4.4253050685368125, 4.1615082299445625, -9.302894940245471, 4.339275624037297, -1.8888634364286396, -6.160830386232105, -0.08889371553836733, -2.57007604955583, 5.595133459549, -2.466464734181728, -2.969224286959471, 3.225763190342281, 4.842815374799072, 4.076753094630737, -5.983378858211268, 2.8284952695418752, -0.9012881457673156, 6.622787566122668, 4.705039339125234, 4.164541647850445, 1.1606645959512534, -1.5127655148574704, -6.509044149765453, -1.9419274357801743, 2.088624780598222, 1.050026053337831, -3.301204209448713, 2.051019894946437, -0.5058879954549411, -3.0827884949590034, 3.7363166233771494, -1.7249665261734473, -2.864538607439436, 9.12306518049363, -5.32612880099576, 4.480016398957975, 0.02254263846320015, -9.559955290126444, 0.2409162467766377, 4.964181427958925, -5.267379336517239, -4.792696842275933, 0.586955596823258, 0.7471771364216222, -2.204384145842644, -6.728859425945277, 5.7631771471003805, 3.3152856618047224, -6.495699730051614, -4.738579383047324, -3.0554874655583575, -2.6545223936357862, -1.406653532327975, -3.3395742385753064, -1.6531799238802636, 5.509328842626598, -0.2571003520707942, -5.152770567888991, -3.34323165035954, -0.08510909030901548, 2.394088577999188, 7.186218326159187, 11.715646371154246, 0.8182919362507475, -4.5568872300912115, 8.538147877738844, 6.7754240434662885, 1.7491627638801743, -4.065131612722252, -9.47025246629277, 4.453453871811936, -6.672848256535196, 1.543564180628632, -3.294189149359794, 0.16314038799225994, -3.308319668548482, -6.100615121644033, 1.8412994218797785, -2.2515478977975967, 0.9595041875233532, -0.6851161010640858, 4.09075648300227, -5.296313861811409, -4.696344034025875, -3.6377179938656115, -2.816260272572449, 6.768999642838433, 0.35544351938981844, -5.374103704002116, 3.1580880669329066, -5.913559713399684, 1.667126907689565, -0.8485033480312358, -3.1970296043552726, -0.23217917707612568, -8.836880601531508, 0.7855115143202451, 4.537646190308841, -4.44697181017365, 3.470431322075129, -4.719361949468724, 5.797840305797466, 1.9014730792766041, -4.87011252584288, -6.805014469568806, -0.10310103200627108, -6.314922403102953, -1.4763430347859572, 1.379005630644099, -3.2111283871254006, 6.92738207360225, -1.3821150695010012, -3.9151384188235845, 5.60801745644417, 6.199954245648298, 6.066376523797276, 5.27734301832332, 1.2242617083101397, -5.234137996550036, 3.550562794646604, -1.072331591693585, 4.122455214502362, 6.287286050937425, 5.978769497129314, -8.329555300918678, -2.9832141057955424, -1.3937657878481433, -1.0261169788048576, -1.6384594413696045, 7.505105897413624, 6.537303819283331, -3.9484435959749664, 9.403715347063983, -12.099330498562932, 11.076143345348994, -2.7942363917996875, -6.991604506402717, 3.4560155012687073, 3.2683178769471155, -4.537996384289817, 3.3676973133414636, -3.9653626299354343, 2.930126007258192, 3.7679231068256005, 5.694960109894298, 0.11629282713200517, 0.21500937658741082, -8.774728717175506, 4.036899157443571, -0.780634716871492, 2.4989683073094557, -0.6874677170218035, 5.391452701133723, -6.531545226899821, -0.6335754486365213, -1.849452227762984, -6.22873031624224, 5.164699869333782, 9.207738541341712, 1.8040870549061658, 7.188804841910862, -3.385596744493146, -0.8129860647106781, 5.276595435104335, -0.3524171309073134, -0.46129336668091003, 0.5529853584072022, 8.212746495065186, -3.251599315729486, -1.0435392197174154, -6.545518488816366, 6.769371779951627, -3.1679910673629106, -2.9561166726056607, -3.7345542210594624, -3.8902197666650915, -5.686662471036116, -4.007420603306679, -3.2465856169011604, 8.232495368133764, 6.4515195478689, 5.81752291447704, -10.012060636904643, 4.519008971283542, -4.105289963351716, -7.8584521863452945, 3.9025688449826976, -2.285792722833948, 3.080656266491035, -3.1689948371848393, 6.572500325374278, -3.8435504571505024, -2.693186028714778, -5.5925953241209285, 0.04105095555824079, 0.12766601140836809, 9.21396803095213
};
	static int DustRemovedLastStep = 0;

	static ArrayList<double[]> dust = new ArrayList<double[]>();
	static UseSensors sensor = new UseSensors(radius);

	public static void main(String[] args) {
		int bestNN = 0;
		Tournament selection = new Tournament();
		NeuralNetwork[] nn = new NeuralNetwork[population];
		NeuralNetwork BestNN = new NeuralNetwork(inputNodes, hiddenNodes, outputNodes, true);
		if (trainNN == true) {
			for (int i = 0; i < population; i++) {
				nn[i] = new NeuralNetwork(inputNodes, hiddenNodes, outputNodes, true);
			}
			for (int x = 0; x < generations; x++) {
				deviation[x] = getDeviation(nn);
				NeuralNetwork[] winners = selection.TournamentSelection(tournamentSize, amountOfWinners, runTime,
						radius, nn);
				BestNN = winners[0];
				int z = 0;
				for (int i = 0; i < population; i++) {

					if (i < amountOfWinners) {
						nn[i] = winners[i];
					} else {
						boolean repeat = true;
						int rand = 0;
						while (repeat) {
							rand = (int) (Math.random() * amountOfWinners);
							if (rand != z) {
								repeat = false;
							}
						}

						nn[i] = nn[z].getChild(nn[rand]);

						z++;

						if (z == amountOfWinners) {
							z = 0;
						}
					}
				}
				// for (int i = 1; i < amountOfWinners; i++) {
				// nn[i].mutate(nn[i].mutationChance, nn[i].radiation);
				// }
				double[] fitness = selection.GetFitness();
				double average =0;
				for(int w=0; w<fitness.length;w++ ) {
					average = average+fitness[w];
				}
				average = average/ fitness.length;
				System.out.println("best fitness in generation: " + x + " is: " + selection.GetBestFitness());
				System.out.println("average fitness in generation: " + x + " is: " + average);
				AVfitness = AVfitness +", "+average;
				
				MaxFitness = MaxFitness +", "+selection.GetBestFitness();
				
				Deviations = Deviations +", "+deviation[x];
				double temp =0;
				for(int w=0; w<fitness.length;w++ ) {
					temp = temp+((fitness[w]-average)*(fitness[w]-average));
				}
				standardDeviation[x]= Math.sqrt(temp/(population-1));
				standardError[x] = standardDeviation[x]/Math.sqrt(population);
				SError = SError +", "+standardError[x];
			}
			bestNN = selection.GetBestNN();
			double[] temp = BestNN.getWeights();
			for(int x=0;x<temp.length;x++) {
				if(x==0) {
					NeuralNetwork = ""+temp[x];
				}
				else {
					NeuralNetwork = NeuralNetwork+", "+temp[x];
				}
			}

		}
		if (visualize == true) {
			System.out.println("MaxFitness was: "+ MaxFitness);			
			System.out.println("average fitness was: "+ AVfitness);
			System.out.println("standard error was: "+ SError);
			System.out.println("deviation was: "+ Deviations);
			System.out.println("the weights of the best NN are: "+ NeuralNetwork);
			
			JFrame frame = new JFrame("Robot Controller");
			frame.getContentPane().add(new Main());
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.addKeyListener(new Main());

			frame.setSize(600, 400);
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

			frame.setVisible(true);

			// need deltaTime........
			Motion motion = new Motion(Xpos, Ypos, Math.toRadians(Angle), Vl, Vr, deltaTime);
			Walls w = new Walls();
			int[][] walls = w.getWalls(field);
			dust = w.getDust(field, dustDensity);
			CircleIntersections intersect = new CircleIntersections();
			// containing three elements: position x,y and angle

			// Xpos = 150;
			// Angle=0;
			// frame.repaint();
			NeuralNetwork trainedNN = new NeuralNetwork(inputNodes, hiddenNodes, outputNodes, true);
			trainedNN.setWeights(preTrained);
			while (itterate) {
			//for(int n=0;n<2000;n++) {
				try {
					Thread.sleep((int) (20));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (trainNN == false && useNN == false) {
					// System.out.println(Xpos);
					motion = new Motion(Xpos, Ypos, Math.toRadians(Angle), Vl, Vr, deltaTime);
				} else {
					double[] input = new double[15];
					double[] NNoutput;
					double TotalDust = dust.size();

					double [] distances = sensor.GetDistances(Xpos, Ypos, Angle, field, radius);
					

					
						for (int q = 0; q < 12; q++) {
							
							input[q] = distances[q];

						}

						input[12] = Vl;
						input[13] = Vr;
						input[14] = DustRemovedLastStep;
						if(useNN == true) {
							
							NNoutput = trainedNN.getOutput(input);
						}
						else {
							NNoutput = BestNN.getOutput(input);
						}
						Vl = NNoutput[0];
						Vr = NNoutput[1];
						
						motion = new Motion(Xpos, Ypos, Math.toRadians(Angle), Vl, Vr, deltaTime);

						double[] NewPos = motion.motion();
						
						boolean collision = false;
						int colidedWall = -1;
						int colisionCounter = 0;

						for (int i = 0; i < walls.length; i++) {
							List<Point> p = CircleIntersections.getCircleLineIntersectionPoint(
									new Point(walls[i][0], walls[i][1]), new Point(walls[i][2], walls[i][3]),
									new Point(NewPos[0], NewPos[1]), radius);
							if (p.size() > 0) {

								if ((p.get(0).x >= Math.min(walls[i][0], walls[i][2])
										&& p.get(0).x <= Math.max(walls[i][0], walls[i][2]))
										&& (p.get(0).y >= Math.min(walls[i][1], walls[i][3])
												&& p.get(0).y <= Math.max(walls[i][1], walls[i][3]))) {
									collision = true;
									colisionCounter++;
									colidedWall = i;

									// there is a collision

								}
								if (p.size() > 1
										&& (p.get(1).x >= Math.min(walls[i][0], walls[i][2])
												&& p.get(1).x <= Math.max(walls[i][0], walls[i][2]))
										&& (p.get(1).y >= Math.min(walls[i][1], walls[i][3])
												&& p.get(1).y <= Math.max(walls[i][1], walls[i][3]))) {
									collision = true;
									colisionCounter++;
									colidedWall = i;

									// there is a collision

								}

							}
						}
						if (collision == false) {
							Xpos = NewPos[0];

							Ypos = NewPos[1];

							Angle = Math.toDegrees(NewPos[2]);

						} else {

							if (colisionCounter == 2) {
								if (walls[colidedWall][0] == walls[colidedWall][2] && colidedWall != -1) {// vertical
																											// wall

									Ypos = NewPos[1];

									Angle = Math.toDegrees(NewPos[2]);
								} else if (walls[colidedWall][1] == walls[colidedWall][3] && colidedWall != -1) {
									Xpos = NewPos[0];

									Angle = Math.toDegrees(NewPos[2]);
								}
							}
						}

					
				}
				if (trainNN == false) {
					double[] NewPos = motion.motion();

					boolean collision = false;
					int colidedWall = -1;
					int colisionCounter = 0;

					for (int i = 0; i < walls.length; i++) {
						List<Point> p = CircleIntersections.getCircleLineIntersectionPoint(
								new Point(walls[i][0], walls[i][1]), new Point(walls[i][2], walls[i][3]),
								new Point(NewPos[0], NewPos[1]), radius);
						if (p.size() > 0) {

							if ((p.get(0).x >= Math.min(walls[i][0], walls[i][2])
									&& p.get(0).x <= Math.max(walls[i][0], walls[i][2]))
									&& (p.get(0).y >= Math.min(walls[i][1], walls[i][3])
											&& p.get(0).y <= Math.max(walls[i][1], walls[i][3]))) {
								collision = true;
								colisionCounter++;
								colidedWall = i;

								// there is a collision

							}
							if (p.size() > 1
									&& (p.get(1).x >= Math.min(walls[i][0], walls[i][2])
											&& p.get(1).x <= Math.max(walls[i][0], walls[i][2]))
									&& (p.get(1).y >= Math.min(walls[i][1], walls[i][3])
											&& p.get(1).y <= Math.max(walls[i][1], walls[i][3]))) {
								collision = true;
								colisionCounter++;
								colidedWall = i;

								// there is a collision

							}

						}
					}
					if (collision == false) {
						Xpos = NewPos[0];

						Ypos = NewPos[1];

						Angle = Math.toDegrees(NewPos[2]);

					} else {

						if (colisionCounter == 2) {
							if (walls[colidedWall][0] == walls[colidedWall][2] && colidedWall != -1) {// vertical wall

								Ypos = NewPos[1];

								Angle = Math.toDegrees(NewPos[2]);
							} else if (walls[colidedWall][1] == walls[colidedWall][3] && colidedWall != -1) {
								Xpos = NewPos[0];

								Angle = Math.toDegrees(NewPos[2]);
							}
						}
					}
				}
				if (Angle >= 360) {
					Angle = Angle - 360;
				}
				if (Angle <= -360) {
					Angle = Angle + 360;
				}
				// System.out.println("(" + Xpos + ", " + Ypos + ")");
				frame.repaint();

			}
		}
	}
	private static double getDeviation(NeuralNetwork[] nn) {
		double deviation =0;
		double[] weights1;
		double[] weights2;
		double distance = 0;
		for(int x=0;x<nn.length;x++) {
			weights1 = nn[x].getWeights();
			for(int z=0;z<nn.length;z++) {
				if(z!=x) {
					weights2 = nn[z].getWeights();
					for(int w=0;w<weights1.length;w++) {
						if(weights1[w] >= 0 && weights2[w] >= 0) {
							distance = Math.max(weights1[w],weights2[w])-Math.min(weights1[w],weights2[w]);
						}
						else if (weights1[w] < 0 && weights2[w] < 0) {
							distance = -1*(weights1[w]+weights2[w]);
						}
						else {
							distance = Math.max(weights1[w],weights2[w])+(-1*Math.min(weights1[w],weights2[w]));
						}
						
						deviation = deviation + distance;
					}
				}
			}			
		}
		return deviation;
	}
	public void paint(Graphics g) {

		Walls w = new Walls();
		int[][] walls = w.getWalls(field);
		for (int z = 0; z < walls.length; z++) {
			g.drawLine(walls[z][0], walls[z][1], walls[z][2], walls[z][3]);
		}

		g.drawOval((int) Xpos - radius, (int) Ypos - radius, radius * 2, radius * 2);

		g.setColor(Color.RED);

		g.drawLine((int) Xpos, (int) Ypos, (int) (Xpos + radius * Math.sin(Math.toRadians(Angle + 90))),
				(int) (Ypos + radius * Math.cos(Math.toRadians(Angle + 90))));
		g.setColor(Color.BLACK);
		if (trainNN == false && useNN == false) {
			double[] distances = new double[12];

			distances = sensor.GetDistances(Xpos, Ypos, Angle, field, radius);
			for (int i = 0; i < 12; i++) {
				double tempAngle = Angle + 90 + i * 30;
				if (tempAngle > 360) {
					tempAngle = tempAngle - 360;
				}
				double xText = 0;
				if (tempAngle - 90 > 90 && tempAngle - 90 < 270) {
					xText = (Xpos + (radius + 20) * Math.sin(Math.toRadians(tempAngle)));
				} else {
					xText = (Xpos + (radius + 10) * Math.sin(Math.toRadians(tempAngle)));
				}
				double yText = (Ypos + (radius + 15) * Math.cos(Math.toRadians(tempAngle)));
				g.drawString(Integer.toString((int) distances[i]), (int) xText, (int) yText);
			}
		}
		RemoveDust();
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2));
		for (int i = 0; i < dust.size(); i++) {
			g2.drawLine((int) dust.get(i)[0], (int) dust.get(i)[1], (int) dust.get(i)[0], (int) dust.get(i)[1]);
		}
	}

	private void RemoveDust() {
		DustRemovedLastStep = 0;
		
		ArrayList<Integer> remove = new ArrayList<Integer>();
		for (int i = 0; i < dust.size(); i++) {
			double dist = Math.sqrt((Xpos - dust.get(i)[0]) * (Xpos - dust.get(i)[0])
					+ (Ypos - dust.get(i)[1]) * (Ypos - dust.get(i)[1]));

			if (dist <= radius) {
				remove.add(i);
				DustRemoved++;
			}
		}
		for (int i = 0; i < remove.size(); i++) {
			dust.remove(remove.get(i) - i);
			DustRemovedLastStep++;
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (useNN == false) {
			int id = e.getID();
			String keyString;
			if (id == KeyEvent.KEY_PRESSED) {
				char c = e.getKeyChar();
				if ("w".contentEquals(Character.toString(c))) {
					Vl = Vl + 1;
				} else if ("s".contentEquals(Character.toString(c))) {
					Vl = Vl - 1;
				} else if ("o".contentEquals(Character.toString(c))) {
					Vr = Vr + 1;
				} else if ("l".contentEquals(Character.toString(c))) {
					Vr = Vr - 1;
				} else if ("x".contentEquals(Character.toString(c))) {
					Vr = 0;
					Vl = 0;
				} else if ("t".contentEquals(Character.toString(c))) {
					Vr = Vr + 1;
					Vl = Vl + 1;
				} else if ("g".contentEquals(Character.toString(c))) {
					Vr = Vr - 1;
					Vl = Vl - 1;
				}
				if (Vr > MaxV) {
					Vr = MaxV;
				}
				if (Vl > MaxV) {
					Vl = MaxV;
				}
				if (Vr < -MaxV) {
					Vr = -MaxV;
				}
				if (Vl < -MaxV) {
					Vl = -MaxV;
				}
				System.out.println("Vl = " + Vl);
				System.out.println("Vr = " + Vr);

			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
