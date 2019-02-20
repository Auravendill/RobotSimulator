import java.util.ArrayList;

public class Tournament {
	public NeuralNetwork[] TournamentSelection(int tournamentSize, int runTime, int radius, NeuralNetwork[] nn) {
		NeuralNetwork[] tournamentWinners = new NeuralNetwork[nn.length];
		int field = 0;
		double[] fitness = new double[nn.length];
		Evaluation e = new Evaluation();
		Walls w = new Walls();
		ArrayList<double[]> dust = w.getDust(field, Main.dustDensity);
		int random = (int) (Math.random()*dust.size());
		double Xstart = dust.get(random)[0];
		double Ystart = dust.get(random)[1];
		double angleStart = Math.random()*359;
		for(int i=0; i< nn.length;i++) {
			fitness[i] = e.SimulateRun(nn[i], field, runTime, Xstart, Ystart, angleStart, radius);
		}
		
		for(int i=0; i< nn.length;i++) {
			double bestFitness = -1;
			int bestEntrant = 0;
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
}
