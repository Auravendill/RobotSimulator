import java.util.ArrayList;
import java.util.List;

public class Tournament {
	double[] fitnessThisGeneration;
	public NeuralNetwork[] TournamentSelection(int tournamentSize,int amountOfWinners, int runTime, int radius, NeuralNetwork[] nn) {
		NeuralNetwork[] tournamentWinners = new NeuralNetwork[amountOfWinners];
		int field = 0;
		double[] fitness = new double[nn.length];
		Evaluation e = new Evaluation();
		Walls w = new Walls();
		int[][] walls = w.getWalls(field);
		CircleIntersections intersect = new CircleIntersections();
		
		
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
		double Xstart = 200;
		double Ystart = 200;
		double angleStart = 0;
		boolean collision = true;
		while(collision) {
			//Xstart = Math.random()*(maxX-minX - radius-1)+ minX+radius+1;
			//Ystart = Math.random()*(maxY-minY - radius-1)+ minY+radius+1;
			//angleStart = Math.random()*359;
			int collisions =0;
			for (int i = 0; i < walls.length; i++) {
				List<Point> p = intersect.getCircleLineIntersectionPoint(new Point(walls[i][0], walls[i][1]),
						new Point(walls[i][2], walls[i][3]), new Point(Xstart, Ystart), radius);
				if(p.size() > 0) {
					collisions++;
				}
			}
			if(collisions ==0) {
				collision = false;
			}
			if (field == 0) {
				if (Xstart >= 100 && Xstart <= 250 && Ystart >= 100 && Ystart <= 150) {
					collision = true;
				}
			}
		}
		for(int i=0; i< nn.length;i++) {
			fitness[i] = e.SimulateRun(nn[i], field, runTime, Xstart, Ystart, angleStart, radius);
		}
		fitnessThisGeneration = fitness;
		double bestFitness = -99;
		int bestEntrant = 0;
		for(int i=0; i< nn.length;i++) {
			
			if(bestFitness< fitness[i]) {
				bestFitness= fitness[i];
				bestEntrant = i;
			}
		}
		tournamentWinners[0]= nn[bestEntrant];
		
		for(int i=1; i< amountOfWinners;i++) {
			bestFitness = -99;
			bestEntrant = 0;
			for(int z = 0;z<tournamentSize;z++) {
				int tournamentEntrant = (int) (Math.random()*nn.length);
				if(bestFitness< fitness[tournamentEntrant]) {
					bestFitness= fitness[tournamentEntrant];
					bestEntrant = tournamentEntrant;
				}
			}
			 tournamentWinners[i]= nn[bestEntrant];
		}
		return tournamentWinners;
		
	}
	public double[] GetFitness() {
		return fitnessThisGeneration;
	}
	public double GetBestFitness() {
		double bestFitness = -99;
		for(int i=0; i<fitnessThisGeneration.length;i++) {
			if(fitnessThisGeneration[i]>bestFitness) {
				bestFitness = fitnessThisGeneration[i];
			}
		}
		return bestFitness;
	}
	public int GetBestNN() {
		double bestFitness = -99;
		int bestEntrant = 0;
		for(int i=0; i<fitnessThisGeneration.length;i++) {
			if(fitnessThisGeneration[i]>bestFitness) {
				bestFitness = fitnessThisGeneration[i];
				bestEntrant = i;
			}
		}
		return bestEntrant;
	}
}
