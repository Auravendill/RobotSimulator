
public class UseSensors {
	
	public double[] GetDistances(double x, double y, double angle, int radius){
		Sensor s = new Sensor(Math.toRadians(angle), radius,true);
		s.updatePosition(x, y, Math.toRadians(0));
		
		Walls w = new Walls();
		int[][] walls = w.getWalls();
		
		
		double[] distances = new double[walls.length];
		
		for(int i=0;i<walls.length;i++) {
			distances[i]= s.getDistanceTo(walls[i][0], walls[i][1], walls[i][2], walls[i][3]);
			System.out.println(distances[i]);
		}
		return null;
		
	}
}
