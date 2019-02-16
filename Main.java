import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel implements KeyListener {
	static double Vl =0;
	static double Vr =0;
	static double Angle =0;
	static double Xpos = 100;
	static double Ypos =200;
	static double deltaTime = 1;
	int radius = 15;//15 pixels
	static boolean itterate = true;

	public static void main(String[] args) {

		JFrame frame = new JFrame("Robot Controller");
		frame.getContentPane().add(new Main());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(new Main());

		frame.setSize(600, 400);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);

		frame.setVisible(true);

		// need deltaTime........
		Motion motion = new Motion(Xpos, Ypos, Math.toRadians(Angle), Vl, Vr, deltaTime);

		// containing three elements: position x,y and angle




		//Xpos = 150;
		//Angle=0;
		//frame.repaint();

		while(itterate) {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(Xpos);
			motion = new Motion(Xpos, Ypos, Math.toRadians(Angle), Vl, Vr, deltaTime);

			double[] NewPos = motion.motion();
			//if(collision(NewPos[0], NewPos[1]) == false) {

			Xpos = NewPos[0];
			Ypos = NewPos[1];
			Angle = Math.toDegrees(NewPos[2]);
			if(Angle>=360) {
				Angle =Angle - 360;
			}
			if(Angle<=-360) {
				Angle =Angle + 360;
			}
			System.out.println("(" + Xpos + ", " + Ypos + ")");
			frame.repaint();
			//}
			//else {
			//do collision things
			//draw(Xpos, Ypos, Angle);
			//}
		}

	}
	public void paint(Graphics g){

		Walls w = new Walls();
		int[][] walls = w.getWalls();
		for(int z=0;z< walls.length;z++) {
			g.drawLine(walls[z][0], walls[z][1], walls[z][2], walls[z][3]);
		}

		g.drawOval((int)Xpos-radius,(int)Ypos-radius,radius*2,radius*2);

		g.setColor(Color.RED);

		g.drawLine((int)Xpos, (int)Ypos,(int)( Xpos+radius* Math.sin(Math.toRadians(Angle+90))),(int)( Ypos+radius* Math.cos(Math.toRadians(Angle+90))));
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
			if("w".contentEquals(Character.toString(c))) {
				Vl = Vl + 1;
			}
			else if("s".contentEquals(Character.toString(c))) {
				Vl = Vl - 1;
			}
			else if("o".contentEquals(Character.toString(c))) {
				Vr = Vr + 1;
			}
			else if("l".contentEquals(Character.toString(c))) {
				Vr = Vr - 1;
			}
			else if("x".contentEquals(Character.toString(c))) {
				Vr = 0;
				Vl = 0;
			}
			else if("t".contentEquals(Character.toString(c))) {
				Vr = Vr + 1;
				Vl = Vl + 1;
			}
			else if("g".contentEquals(Character.toString(c))) {
				Vr = Vr - 1;
				Vl = Vl - 1;
			}
			if(Vr >5) {
				Vr = 5;
			}
			if(Vl >5) {
				Vl = 5;
			}
			if(Vr <-5) {
				Vr = -5;
			}
			if(Vl <-5) {
				Vl = -5;
			}
			System.out.println("Vl = "+Vl);
			System.out.println("Vr = "+Vr);



		}

	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
