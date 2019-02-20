
public class UseSensors {
	Sensor s;
	 boolean first = true;

	public UseSensors(double radius) {
		Sensor s = new Sensor(0, radius, true);
	}

	public double[] GetDistances(double x, double y, double angle, int field,int radius) {
		 if(first) {
		 s = new Sensor(Math.toRadians(angle), radius,true);
		 first = false;
		 }
		// System.out.println(s.toString());
		Walls w = new Walls();
		int[][] walls = w.getWalls(field);
		
		double[] distances = new double[12];
		for (int z = 0; z < 12; z++) {
			s.updatePosition(x, y, Math.toRadians(angle));
			double distance = 9999;
			for (int i = 0; i < walls.length; i++) {
				double temp = s.getDistanceTo(walls[i][0], walls[i][1], walls[i][2], walls[i][3]);
				// System.out.println(distances[i]);
				if (distance > temp) {
					distance = temp;
				}
			}
			angle = angle + 30;
			distances[z] = distance;
			// System.out.println(distance);
		}
		return distances;

	}
}
