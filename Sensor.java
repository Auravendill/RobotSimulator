
public class Sensor {
	private double offsetX;
	private double offsetY;

	private double robotPositionX;
	private double robotPositionY;

	public Sensor(double offsetX, double offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public void updatePosition(double robotPositionX, double robotPositionY) {
		this.robotPositionX = robotPositionX;
		this.robotPositionY = robotPositionY;
	}

	public double getDistanceTo(double startX, double startY, double endX, double endY) {
		double vx = endX - startX;
		double vy = endY - startY;

		double delta = (this.robotPositionY * this.offsetX + startX + this.robotPositionX) / (this.offsetX * vy - vx);
		double lambda = (startX + delta * vx + this.robotPositionX) / this.offsetX;

		if (delta > 1.0 || delta < 0.0) {
			// sensor cannot see this wall
			return Double.MAX_VALUE;
		}

		lambda -= 1.0; // sensor is 1 lambda in the direction of the offset

		if (lambda > 0.0) {
			// wall is on the wrong side
			return Double.MAX_VALUE;
		}

		double eOff = Math.sqrt(this.offsetX * this.offsetX + this.offsetY * this.offsetY); // euclidean length of the
																							// offset
		double distance = eOff * lambda;

		return distance;
	}
}
