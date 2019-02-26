
//import sun.applet.Main;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.StrictMath.exp;

public class NeuralNetwork {
	private double[] weights;

	int inputNodes, hiddenNodes, outputNodes;

	double speedMax = 15;
	// int population = 20;
	int weightsSize = inputNodes * hiddenNodes + outputNodes * hiddenNodes;
	// public double[][] weightsPopulation = new double[population][weightsSize];

	private static final double[] startValues = { -7.0, 7.0 };

	double mutationChance = 0.1, radiation = 5;

	public NeuralNetwork(int inputNodes, int hiddenNodes, int outputNodes, boolean start) {
		weights = new double[inputNodes * hiddenNodes + outputNodes * hiddenNodes];
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

	public double sigmoid(double x) {
		return 1. / (1. + exp(-x));
	}

	public double[] getOutput(double[] inputs) {

		double[][] weightsLayer2 = new double[hiddenNodes][inputNodes];
		double[][] weightsLayer3 = new double[outputNodes][hiddenNodes];

		for (int i = 0; i < hiddenNodes; i++) {
			for (int j = 0; j < inputNodes; j++) {
				weightsLayer2[i][j] = weights[i * j + j];
			}
		}

		for (int i = 0; i < outputNodes; i++) {
			for (int j = 0; j < hiddenNodes; j++) {
				weightsLayer3[i][j] = weights[inputNodes * outputNodes + i * j + j];
			}
		}

		double[] activationLayer2 = new double[hiddenNodes];
		double[] activationLayer3 = new double[outputNodes];
		double sum;

		for (int i = 0; i < hiddenNodes; i++) {
			sum = 0.;
			for (int j = 0; j < inputNodes; j++) {
				sum += inputs[j] * weightsLayer2[i][j];
			}
			activationLayer2[i] = sigmoid(sum);
		}

		for (int i = 0; i < outputNodes; i++) {
			sum = 0.;
			for (int j = 0; j < hiddenNodes; j++) {
				sum += activationLayer2[j] * weightsLayer3[i][j];
			}
			activationLayer3[i] = sigmoid(sum);
		}

		double[] speed = new double[2];
		for (int i = 0; i < outputNodes; i++) {
			speed[i] = activationLayer3[i] * 2 * speedMax - speedMax;
		}

		return speed;// placeholder
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
		child.setWeights(twoPointCrossover(weights, parentWeights));
		// child.setWeights(kPointCrossover(weights, parentWeights, 2));
		// child.setWeights(arithmeticCrossover(weights, parentWeights));// alternative
		child.mutate(mutationChance, radiation);
		return child;
	}

	// public void populationGenerator() {
	// // int weightsSize = inputNodes * hiddenNodes + outputNodes * hiddenNodes;
	// // double[][] weightsPopulation = new double[population][weightsSize];
	// for (int i = 0; i < population; i++) {
	// for (int j = 0; j < weightsSize; j++) {
	// weightsPopulation[i][j] = Math.random();
	// }
	// }
	//
	// }
	//
	// public void uniformCrossover() {
	// int weightsSize = inputNodes * hiddenNodes + outputNodes * hiddenNodes;
	// int choice1, choice2;
	// double trans;
	//
	// for (int i = 0; i < population; i++) {
	// choice1 = (int) (Math.random() * population);
	// choice2 = (int) (Math.random() * population);
	// for (int j = 0; j < weightsSize;) {
	// trans = weightsPopulation[choice1][j];
	// weightsPopulation[choice1][j] = weightsPopulation[choice2][j];
	// weightsPopulation[choice2][j] = trans;
	// j = j + 2;
	// }
	// }
	// }

	private double[] twoPointCrossover(double[] father, double[] mother) {
		double[] child = new double[father.length];
		int crossoverPoint = (int) ThreadLocalRandom.current().nextDouble(0, father.length);
		for (int i = 0; i < crossoverPoint; ++i) {
			child[i] = father[i];
		}
		for (int j = crossoverPoint; j < father.length; ++j) {
			child[j] = mother[j];
		}
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

	public void mutate(double mutationChance, double radiation) {
		for (int i = 0; i < weights.length; ++i) {

			double rnd = Math.random();
			if (rnd < mutationChance) {
				double geneChange = ThreadLocalRandom.current().nextDouble(-1 * radiation, radiation);
				weights[i] += geneChange;
			}
		}

	}
}
