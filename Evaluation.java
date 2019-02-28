import java.util.ArrayList;
import java.util.List;

public class Evaluation {

	static ArrayList<double[]> dust = new ArrayList<double[]>();
	static double Alpha = 10;
	static double Beta = 1;
	static double deltaTime = 0.125;

	public static double SimulateRun(NeuralNetwork nn, int field, int runTime, double Xstart, double Ystart, double angleStart,
			int radius) {
		deltaTime = Main.deltaTime;
		double Vl = 0;
		double Vr = 0;

		double Xpos = Xstart;
		double Ypos = Ystart;
		double Angle = angleStart;

		double[] NNoutput;
		double dustDensity = Main.dustDensity;
		double TotalCollisions = 0;
		double DustRemoved = 0;
		double DustRemovedLastStep = 0;
		double[] input = new double[15];

		Motion motion = new Motion(Xpos, Ypos, Math.toRadians(Angle), Vl, Vr, deltaTime);
		Walls w = new Walls();
		int[][] walls = w.getWalls(field);
		CircleIntersections intersect = new CircleIntersections();
		dust = w.getDust(field, dustDensity);

		double TotalDust = dust.size();

		Sensor s = new Sensor(Math.toRadians(angleStart), radius, true);
		double angle = Angle;
		UseSensors sensor = new UseSensors(radius);
		for (int z = 0; z < runTime; z++) {
			double [] distances = sensor.GetDistances(Xpos, Ypos, Angle, field, radius);
			for (int q = 0; q < 12; q++) {
				
				input[q] = distances[q];

			}
			
			input[12] = Vl;
			input[13] = Vr;
			input[14] = DustRemovedLastStep;
			
			NNoutput = nn.getOutput(input);
			Vl = NNoutput[0];
			Vr = NNoutput[1];
			
		
			motion = new Motion(Xpos, Ypos, Math.toRadians(Angle), Vl, Vr, deltaTime);

			double[] NewPos = motion.motion();

			boolean collision = false;
			int colidedWall = -1;
			int colisionCounter = 0;

			for (int i = 0; i < walls.length; i++) {
				List<Point> p = CircleIntersections.getCircleLineIntersectionPoint(new Point(walls[i][0], walls[i][1]),
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
			double DustRemovedThisStep = RemoveDust(Xpos, Ypos, radius);
			DustRemoved = DustRemoved + DustRemovedThisStep;
			DustRemovedLastStep =DustRemovedThisStep;
		}
		
		double fitness = EvaluationFunction(TotalCollisions, DustRemoved, TotalDust, Alpha, Beta);
		return fitness;

	}

	private static double EvaluationFunction(double totalCollisions, double dustRemoved, double totalDust, double alpha,
			double beta) {
		if (totalCollisions > 0) { // can't feed 0 into a log function
			return alpha * (dustRemoved / totalDust) - beta * Math.log(totalCollisions);
		} else {
			return alpha * (dustRemoved / totalDust);
		}
		
	}

	private static double RemoveDust(double xpos, double ypos, double radius) {
		
		ArrayList<Integer> remove = new ArrayList<Integer>();
		for (int i = 0; i < dust.size(); i++) {
			double dist = Math.sqrt((xpos - dust.get(i)[0]) * (xpos - dust.get(i)[0])
					+ (ypos - dust.get(i)[1]) * (ypos - dust.get(i)[1]));

			if (dist <= radius) {
				remove.add(i);
				
			}
		}
		for (int i = 0; i < remove.size(); i++) {
			dust.remove(remove.get(i) - i);
		}
		return remove.size();

	}
}
