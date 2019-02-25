import java.util.ArrayList;
import java.util.List;

public class Evaluation {

	ArrayList<double[]> dust = new ArrayList<double[]>();
	double Alpha = 10;
	double Beta = 1;
	double deltaTime = 0.125;

	public double SimulateRun(NeuralNetwork nn, int field, int runTime, double Xstart, double Ystart, double angleStart,
			double radius) {
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

		for (int z = 0; z < runTime; z++) {
			for (int q = 0; q < 12; q++) {
				s.updatePosition(Xpos, Ypos, Math.toRadians(angle));
				double distance = 9999;
				for (int i = 0; i < walls.length; i++) {
					double temp = s.getDistanceTo(walls[i][0], walls[i][1], walls[i][2], walls[i][3]);

					if (distance > temp) {
						distance = temp;
					}
				}
				angle = angle + 30;
				input[q] = distance;

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
			double DustRemovedThisStep = RemoveDust(Xpos, Ypos, radius);
			DustRemoved = DustRemoved + DustRemovedThisStep;
			DustRemovedLastStep =DustRemovedThisStep;
		}

		double fitness = EvaluationFunction(TotalCollisions, DustRemoved, TotalDust, Alpha, Beta);
		return fitness;

	}

	private double EvaluationFunction(double totalCollisions, double dustRemoved, double totalDust, double alpha,
			double beta) {
		if (totalCollisions > 0) { // can't feed 0 into a log function
			return alpha * (dustRemoved / totalDust) - beta * Math.log(totalCollisions);
		} else {
			return alpha * (dustRemoved / totalDust);
		}
	}

	private double RemoveDust(double xpos, double ypos, double radius) {
		double dustRemoved = 0;
		ArrayList<Integer> remove = new ArrayList<Integer>();
		for (int i = 0; i < dust.size(); i++) {
			double dist = Math.sqrt((xpos - dust.get(i)[0]) * (xpos - dust.get(i)[0])
					+ (ypos - dust.get(i)[1]) * (ypos - dust.get(i)[1]));

			if (dist <= radius) {
				remove.add(i);
				dustRemoved++;
			}
		}
		for (int i = 0; i < remove.size(); i++) {
			dust.remove(remove.get(i) - i);
		}
		return dustRemoved;

	}
}
