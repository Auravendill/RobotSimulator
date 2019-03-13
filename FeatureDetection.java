
public class FeatureDetection {

	int[][] features = { { 80, 80 }, { 230, 220 }, {280,100}, {150,150}, {170,70}, {70,200}, {330,180}, {250,140}};
	
	double[][] featureVector = new double[features.length][2];
	double angle =0;
	
	public int[][] getFeatures() {

		return features;

	}

	public double[][] getFeatureDistances() {
		
		for (int x = 0; x < features.length; x++) {
			featureVector[x][0] = Math.sqrt(Math.pow(Main.Xpos - features[x][0], 2) + Math.pow(Main.Ypos - features[x][1], 2));
			
			angle = Math.toDegrees(-1*Math.atan2(features[x][1] - Main.Ypos, features[x][0] - Main.Xpos));

		    if(angle < 0){
		        angle += 360;
		    }
		    
			featureVector[x][1] = angle;
		}
		return featureVector;
	}
}
