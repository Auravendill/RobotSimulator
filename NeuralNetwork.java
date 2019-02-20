import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class NeuralNetwork {
	private double[] weights;

	int inputNodes, hiddenNodes, outputNodes;

	private static final double[] startValues = { -1.0, 1.0 };

	double mutationChance = 0.02, radiation = 0.5;

	public NeuralNetwork(int inputNodes, int hiddenNodes, int outputNodes, boolean start) {
		weights = new double[inputNodes + hiddenNodes + outputNodes];
		this.inputNodes = inputNodes;
		this.hiddenNodes = hiddenNodes;
		this.outputNodes = outputNodes;

		if (start) {
			for (int i = 0; i < weights.length; ++i) {
				double value = ThreadLocalRandom.current().nextDouble(startValues[0], startValues[1]);
				weights[i] = value;
			}
		}
	}

	private double getNode(int layer, int number) {
		switch (layer) {
		case 0:// inputLayer
			if (number < inputNodes) {
				return weights[number];
			} else {
				throw new ArrayIndexOutOfBoundsException();
			}
		case 1:// hiddenLayer
			if (number < inputNodes + hiddenNodes) {
				return weights[inputNodes + number];
			} else {
				throw new ArrayIndexOutOfBoundsException();
			}
		case 2:// outputLayer
			return weights[inputNodes + hiddenNodes + number];
		default:
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	public double[] getOutput(double[] inputs) {
		return null;// placeholder
	}

	public double[] getWeights() {
		return weights;
	}

	public void setWeights(double[] weights) {
		this.weights = weights;
	}

	public double getMutationChance() {
		return mutationChance;
	}

	public void setMutationChance(double mutationChance) {
		this.mutationChance = mutationChance;
	}

	public double getRadiation() {
		return radiation;
	}

	public void setRadiation(double radiation) {
		this.radiation = radiation;
	}

	public NeuralNetwork getChild(NeuralNetwork parent) {
		NeuralNetwork child = new NeuralNetwork(inputNodes, hiddenNodes, outputNodes, false);
		double[] parentWeights = parent.getWeights();
		child.setWeights(kPointCrossover(weights, parentWeights, 2));
		// child.setWeights(arithmeticCrossover(weights, parentWeights));//alternative
		child.mutate(mutationChance, radiation);
		return child;
	}

	private double[] kPointCrossover(double[] father, double[] mother, int k) {
		double[] child = new double[father.length];
		ArrayList<Integer> crossoverPoints = new ArrayList<Integer>();
		for (int i = 0; i < k; ++i) {
			int crossoverPoint;
			do {
				crossoverPoint = (int) ThreadLocalRandom.current().nextDouble(0, father.length);
			} while (!crossoverPoints.contains(crossoverPoint));
			crossoverPoints.add(crossoverPoint);
		}
		crossoverPoints.sort(null);
		for (int i = 0; i <= k; ++i) {
			if ((i & 1) == 0) {
				int b = (i == 0) ? 0 : crossoverPoints.get(k - 1);
				int e = (i == k) ? father.length : crossoverPoints.get(k);
				for (int j = b; j < e; ++j) {
					child[j] = father[j];
				}
			} else {
				int e = (i == k) ? father.length : crossoverPoints.get(k);
				for (int j = crossoverPoints.get(k - 1); j < e; ++j) {
					child[j] = mother[j];
				}
			}
		}
		return child;
	}

	private double[] arithmeticCrossover(double[] father, double[] mother) {
		double[] child = new double[father.length];
		for (int i = 0; i < child.length; ++i) {
			child[i] = (father[i] + mother[i]) / 2.0;
		}
		return child;
	}

	private void mutate(double mutationChance, double radiation) {
		for (int i = 0; i < weights.length; ++i) {
			double rnd = Math.random();
			if (rnd < mutationChance) {
				double geneChange = ThreadLocalRandom.current().nextDouble(-1 * radiation, radiation);
				weights[i] += geneChange;
			}
		}

	}
}
