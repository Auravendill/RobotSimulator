
//import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
//import java.awt.Graphics2D;

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
	static double Xpos = 100;
	static double Ypos = 200;
	static double deltaTime = 0.125;
	static double dustDensity = 10;
	static int radius = 15;// 15 pixels
	static int field = 0;
	static boolean itterate = true;
	static ArrayList<double[]> dust = new ArrayList<double[]>();
	UseSensors sensor = new UseSensors(radius);
	

	public static void main(String[] args) {

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

		while (itterate) {
			try {
				Thread.sleep((int) (1000 * deltaTime));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println(Xpos);
			motion = new Motion(Xpos, Ypos, Math.toRadians(Angle), Vl, Vr, deltaTime);

			double[] NewPos = motion.motion();
			// if(collision(NewPos[0], NewPos[1]) == false) {
			boolean collision = false;
			int colidedWall = -1;
			int colisionCounter = 0;

			for (int i = 0; i < walls.length; i++) {
				List<Point> p = intersect.getCircleLineIntersectionPoint(new Point(walls[i][0], walls[i][1]),
						new Point(walls[i][2], walls[i][3]), new Point(NewPos[0], NewPos[1]), radius);
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

			if (Angle >= 360) {
				Angle = Angle - 360;
			}
			if (Angle <= -360) {
				Angle = Angle + 360;
			}
			// System.out.println("(" + Xpos + ", " + Ypos + ")");
			frame.repaint();
			// }
			// else {
			// do collision things
			// draw(Xpos, Ypos, Angle);
			// }
		}

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
		RemoveDust();
		for (int i = 0; i < dust.size(); i++) {
			g.drawLine((int) dust.get(i)[0], (int) dust.get(i)[1], (int) dust.get(i)[0], (int) dust.get(i)[1]);
		}
	}
	
	private void RemoveDust() {
		
		ArrayList <Integer> remove= new ArrayList <Integer>();
		for(int i=0; i<dust.size();i++) {
			double dist = Math.sqrt((Xpos - dust.get(i)[0])*(Xpos - dust.get(i)[0]) + (Ypos - dust.get(i)[1])*(Ypos - dust.get(i)[1]));
				    
			if( dist <= radius) {
				remove.add(i);				
			}
		}
		for(int i=0; i<remove.size();i++) {
			dust.remove(remove.get(i)-i);
		}
		

	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
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

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
