import java.util.ArrayList;

public class Walls {
	int[][] walls0 = { { 50, 50, 50, 250 }, { 50, 50, 350, 50 }, { 300, 50, 300, 150 }, { 350, 50, 350, 250 },
			{ 50, 250, 350, 250 }, { 100, 100, 250, 100 }, { 100, 100, 100, 150 }, { 250, 100, 250, 150 },
			{ 100, 150, 250, 150 } }; // {x,y,x2,y2}
	int[][] walls1 = { { 50, 50, 50, 250 }, { 50, 50, 350, 50 }, { 350, 50, 350, 250 }, { 50, 250, 350, 250 } }; // {x,y,x2,y2}
	int[][] walls2 = { { 50, 50, 250, 50 }, { 50, 50, 50, 350 }, {  50,350, 250, 350 }, { 250,50, 250 , 350} };
	public int[][] getWalls(int i) {

		
		if (i == 0) {
			return walls0;
		} else if (i == 1) {
			return walls1;
		} else if(i == 2) {
			return walls2;
			
		}
		else {
			return null;
		}

	}

	public ArrayList<double[]> getDust(int i, double density) {

		int[][] walls = getWalls(i);
		ArrayList<double[]> dust = new ArrayList<double[]>();
		double minX = 9999;
		double maxX = 0;
		double minY = 9999;
		double maxY = 0;

		for (int z = 0; z < walls.length; z++) {

			if (walls[z][0] < minX || walls[z][2] < minX) {
				minX = Math.min(walls[z][0], walls[z][2]);
			}
			if (walls[z][0] > maxX || walls[z][2] > maxX) {
				maxX = Math.max(walls[z][0], walls[z][2]);
			}

			if (walls[z][1] < minY || walls[z][3] < minY) {
				minY = Math.min(walls[z][1], walls[z][3]);
			}
			if (walls[z][1] > maxY || walls[z][3] > maxY) {
				maxY = Math.max(walls[z][1], walls[z][3]);
			}
		}
		for (double x = (minX + density / 2); x < maxX; x = x + density) {
			for (double y = (minY + density / 2); y < maxY; y = y + density) {
				if (i == 0) {
					if (x >= 100 && x <= 250 && y >= 100 && y <= 150) {

					} else {
						double[] dustParticle = { x, y };
						dust.add(dustParticle);
					}
				} else {
					double[] dustParticle = { x, y };
					dust.add(dustParticle);
				}
			}
		}
		return dust;
	}
}
