import java.util.ArrayList;
import java.util.List;

public class Evaluation {

	ArrayList <double[]> dust = new  ArrayList <double[]>();
	 
	public double SimulateRun(NeuralNetwork nn,int field, int runTime, double Xstart, double Ystart, double angleStart,
			double radius) {
		double Vl = 0;
		double Vr = 0;
		double Alpha = 1;
		double Beta = 1;
		double Xpos = Xstart;
		double Ypos = Ystart;
		double Angle = angleStart;
		double deltaTime = 1;
		double[] NNoutput;
		double dustDensity =10;
		double TotalCollisions = 0;
		double DustRemoved = 0;
		

		Motion motion = new Motion(Xpos, Ypos, Math.toRadians(Angle), Vl, Vr, deltaTime);
		Walls w = new Walls();
		int[][] walls = w.getWalls(field);
		CircleIntersections intersect = new CircleIntersections();
		dust = w.getDust(field, dustDensity);

		double TotalDust = dust.size();
		
		for (int z = 0; z < runTime; z++) {

			// NNoutput = nn.getOutput(input);
			// Vl = NNoutput[];
			// Vr = NNoutput[];

			motion = new Motion(Xpos, Ypos, Math.toRadians(Angle), Vl, Vr, deltaTime);

			double[] NewPos = motion.motion();

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
				TotalCollisions++;
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

			DustRemoved = DustRemoved + RemoveDust(Xpos, Ypos, radius);
		}

		double fitness = EvaluationFunction(TotalCollisions, DustRemoved,TotalDust, Alpha, Beta);
		return fitness;

	}

	private double EvaluationFunction(double totalCollisions, double dustRemoved,double totalDust, double alpha, double beta) {
		if(totalCollisions >0) { // can't feed 0 into a log function
			return alpha*(dustRemoved/totalDust)-beta*Math.log(totalCollisions);
		}
		else {
			return alpha*(dustRemoved/totalDust);
		}
	}

	private double RemoveDust(double xpos, double ypos, double radius) {
		double dustRemoved = 0;
		ArrayList <Integer> remove= new ArrayList <Integer>();
		for(int i=0; i<dust.size();i++) {
			double dist = Math.sqrt((xpos - dust.get(i)[0])*(xpos - dust.get(i)[0]) + (ypos - dust.get(i)[1])*(ypos - dust.get(i)[1]));
				    
			if( dist <= radius) {
				remove.add(i);	
				dustRemoved++;
			}
		}
		for(int i=0; i<remove.size();i++) {
			dust.remove(remove.get(i)-i);
		}
		return dustRemoved;

	}
}
