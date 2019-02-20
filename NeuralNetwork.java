import java.util.concurrent.ThreadLocalRandom;

public class NeuralNetwork {
	private double[] weights;

	int inputNodes, hiddenNodes, outputNodes;

	private static final double[] startValues = { -1.0, 1.0 };

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

	public NeuralNetwork getChild(NeuralNetwork parent) {
		NeuralNetwork child = new NeuralNetwork(inputNodes, hiddenNodes, outputNodes, false);

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
