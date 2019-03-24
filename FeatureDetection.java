import java.util.ArrayList;

public class FeatureDetection {

	int[][] features = { { 80, 80 }, { 230, 220 }, { 280, 100 }, { 150, 150 }, { 170, 70 }, { 70, 200 }, { 330, 180 },
			{ 250, 140 }};

	double[][] featureVector = new double[features.length][2];
	ArrayList<double[]> Vector = new ArrayList<double[]>();
	double angle = 0;

	public int[][] getFeatures() {

		return features;

	}

	public double[][] getFeatureDistances() {

		for (int x = 0; x < features.length; x++) {
			featureVector[x][0] = Math
					.sqrt(Math.pow(Main.Xpos - features[x][0], 2) + Math.pow(Main.Ypos - features[x][1], 2));

			angle = Math.toDegrees(-1 * Math.atan2(features[x][1] - Main.Ypos, features[x][0] - Main.Xpos));

			if (angle < 0) {
				angle += 360;
			}

			featureVector[x][1] = angle;
		}
		return featureVector;
	}
	/*
	 * public ArrayList<double[]> getFeaturesClose(int distance) { double[][]
	 * featureVectors = getFeatureDistances(); for(int
	 * i=0;i<featureVectors.length;i++) { if(featureVectors[i][0]<distance) {
	 * double[] vector = {features[i][0], features[i][1], featureVectors[i][1]};
	 * Vector.add(vector); } } return Vector;
	 * 
	 * }
	 */

	private double atan2(double x, double y) {
		if (x == 0) {
			if (y == 0) {
				return 0;
			} else {
				return Math.signum(y) * Math.PI * 0.5;
			}
		} else {
			if (x > 0) {
				return Math.atan(y / x);
			} else {
				return Math.signum(y) * (Math.PI - Math.atan(Math.abs(y / x)));
			}
		}
	}

	private double noiseGenerator() {
		// dummy values
		double min = -3;
		double max = 3;

		return (Math.random() - min) * (max - min);
	}

	public double[] generateNoisyInput(int featureNumber, double positionX, double positionY) {
		double distance;
		double angle;
		double feature;

		double[][] distArray = getFeatureDistances();

		distance = distArray[featureNumber][0] + noiseGenerator();
		angle = atan2(features[featureNumber][0] - positionX, features[featureNumber][1] - positionY)
				- distArray[featureNumber][1] + noiseGenerator();
		feature = featureNumber;// currently no noise here;

		double[] result = { distance, -angle, feature };
		
		return result;
	}
}
