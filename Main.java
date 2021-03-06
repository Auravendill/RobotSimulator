
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
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
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
	static int featureRange = 75;

	static boolean itterate = true;

	static boolean visualize = true;
	static boolean useNN = false;
	static boolean trainNN = false;
	static boolean localization = true;

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
	static double[][] FeatureDistances; // {distance to feature, angle to feature}
	
	static int DustRemovedLastStep = 0;

	static ArrayList<double[]> dust = new ArrayList<double[]>();
	static ArrayList<double[]> path = new ArrayList<double[]>();
	static ArrayList<double[]> localizationPath = new ArrayList<double[]>();
	static ArrayList<double[]> Zpath = new ArrayList<double[]>();
	static ArrayList<double[][]> SigmaPath = new ArrayList<double[][]>();
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
		if(localization == true) {
			
			
			
			
			double[] start = {Xpos, Ypos};
			path.add(start);
			
		}
		if (visualize == true) {
			if(trainNN == true) {
				System.out.println("MaxFitness was: "+ MaxFitness);			
				System.out.println("average fitness was: "+ AVfitness);
				System.out.println("standard error was: "+ SError);
				System.out.println("deviation was: "+ Deviations);
				System.out.println("the weights of the best NN are: "+ NeuralNetwork);
			}
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
			if(localization != true) {
			dust = w.getDust(field, dustDensity);
			}
			CircleIntersections intersect = new CircleIntersections();
			// containing three elements: position x,y and angle

			// Xpos = 150;
			// Angle=0;
			// frame.repaint();
			NeuralNetwork trainedNN = new NeuralNetwork(inputNodes, hiddenNodes, outputNodes, true);
			//trainedNN.setWeights(preTrained);
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
					double[] pos = {Xpos,Ypos};
					path.add(pos);
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
		FeatureDetection f = new FeatureDetection();
		
		int[][] walls = w.getWalls(field);
		int[][] features = f.getFeatures();
		KalmanFilter filter = new KalmanFilter();
		
		filter.kalmanFilter();
		double[] mu = filter.getMu();
		double[][] sigma = filter.getSigma();
		double[] filterZ = filter.getZ();
		
		for (int z = 0; z < walls.length; z++) {
			g.drawLine(walls[z][0], walls[z][1], walls[z][2], walls[z][3]);
		}

		g.drawOval((int) Xpos - radius, (int) Ypos - radius, radius * 2, radius * 2);

		g.setColor(Color.RED);

		g.drawLine((int) Xpos, (int) Ypos, (int) (Xpos + radius * Math.sin(Math.toRadians(Angle + 90))),
				(int) (Ypos + radius * Math.cos(Math.toRadians(Angle + 90))));
		g.setColor(Color.BLACK);
		if (trainNN == false && useNN == false && localization == false) {
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
		if(localization == false) {
			RemoveDust();
			
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(2));
			for (int i = 0; i < dust.size(); i++) {
				g2.drawLine((int) dust.get(i)[0], (int) dust.get(i)[1], (int) dust.get(i)[0], (int) dust.get(i)[1]);
			}
		}
		else {
			
			localizationPath.add(mu);
			Zpath.add(filterZ);
			SigmaPath.add(sigma);
			FeatureDistances = f.getFeatureDistances();
			
			
			g.setColor(Color.BLUE);
			for (int i = 1; i < path.size(); i++) {
				g.drawLine((int) path.get(i-1)[0], (int) path.get(i-1)[1], (int) path.get(i)[0], (int) path.get(i)[1]);
			}
			
			
			
			
			Graphics2D g2 = (Graphics2D) g;
			
			int counter =0;
			for (int i = 0; i < localizationPath.size(); i++) {
				counter++;
				g2.setColor(Color.RED);
				g2.drawLine((int) localizationPath.get(i)[0], (int) localizationPath.get(i)[1], (int) localizationPath.get(i)[0], (int) localizationPath.get(i)[1]);
				if(counter ==100) {
					g2.setColor(Color.MAGENTA);
					counter =0;
					double covarianceX = Math.max(Zpath.get(i)[0], localizationPath.get(i)[0])-Math.min(Zpath.get(i)[0], localizationPath.get(i)[0]);
					double covarianceY = Math.max(Zpath.get(i)[1], localizationPath.get(i)[1])-Math.min(Zpath.get(i)[1], localizationPath.get(i)[1]);
					AffineTransform old = g2.getTransform();
					
					g2.rotate(Math.toRadians(Zpath.get(i)[2]), localizationPath.get(i)[0]-covarianceX*2 + covarianceX*4/2, localizationPath.get(i)[1]-covarianceY*2 + covarianceY*4/2);		
					g2.drawOval((int) (localizationPath.get(i)[0]-covarianceX*2),(int) (localizationPath.get(i)[1]-covarianceY*2),(int) covarianceX*4,(int) covarianceY*4);
					g2.setTransform(old);
				}
				
			}
			
			g2.setStroke(new BasicStroke(5));
			g2.setColor(Color.GREEN);
			for (int i = 0; i < features.length; i++) {
				g2.drawLine(features[i][0], features[i][1], features[i][0], features[i][1]);
			}
			g2.setStroke(new BasicStroke(1));
			for (int i = 0; i < FeatureDistances.length; i++) {
				
				if(FeatureDistances[i][0]<=(featureRange)) {
					g2.drawLine(features[i][0], features[i][1],(int) Xpos,(int) Ypos);
				}
			}
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
		if (useNN == false&& localization == false) {
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
		if(localization == true) {
			int id = e.getID();
			String keyString;
			if (id == KeyEvent.KEY_PRESSED) {
				char c = e.getKeyChar();
				if ("w".contentEquals(Character.toString(c))) {
					Vl = Vl + 1;
					Vr = Vr + 1;
				} else if ("s".contentEquals(Character.toString(c))) {
					Vl = Vl - 1;
					Vr = Vr - 1;
				 
				} else if ("a".contentEquals(Character.toString(c))) {
					Vr = Vr + 1;
					Vl = Vl - 1;
				} else if ("d".contentEquals(Character.toString(c))) {
					Vr = Vr - 1;
					Vl = Vl + 1;
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
			}
		
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
