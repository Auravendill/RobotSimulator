
public class Sensor {
	private double offsetX0;// for angle=0
	private double offsetY0;
	private double offsetX;// current value
	private double offsetY;

	private double robotPositionX;
	private double robotPositionY;

	public Sensor(double offsetX, double offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetX0 = offsetX;
		this.offsetY0 = offsetY;
	}

	public Sensor(double angle, double distance, boolean dummy) {
		this.offsetX = Math.cos(angle) * distance;
		this.offsetY = Math.sin(angle) * distance;
		this.offsetX0 = offsetX;
		this.offsetY0 = offsetY;
	}

	public String toString() {
		return "Starting Offset: x=" + offsetX0 + ", y=" + offsetY0 + "\nCurrent Offset x=" + offsetX + ", y="
				+ offsetY;
	}

	public void updatePosition(double robotPositionX, double robotPositionY, double robotAngle) {
		this.robotPositionX = robotPositionX;
		this.robotPositionY = robotPositionY;

		double distance = Math.sqrt(offsetX0 * offsetX0 + offsetY0 * offsetY0);
		double oldAngle = Math.asin(offsetY0 / distance);
		double newAngle = oldAngle + robotAngle;

		this.offsetX = Math.cos(newAngle) * distance;
		this.offsetY = Math.sin(newAngle) * distance;
	}

	public double getDistanceTo(double startX, double startY, double endX, double endY) {
		double vx = endX - startX;
		double vy = endY - startY;

		// double delta = (robotPositionY * offsetX + startX + robotPositionX) /
		// (offsetX * vy - vx);
		double delta = (robotPositionY - startY + (startX * offsetY + robotPositionX * offsetY) / offsetX)
				/ (vy - (vx * offsetY) / (offsetX));
		double lambda = (startX + delta * vx + robotPositionX) / offsetX;

		if (delta > 1.0 || delta < 0.0) {
			// sensor cannot see this wall
			return Double.MAX_VALUE;
		}

		lambda -= 1.0; // sensor is 1 lambda in the direction of the offset

		if (lambda < 0.0) {
			if (lambda >= -2.0) {
				// wall is inside robot
				return -1.0;
			}
			// wall is on the wrong side
			return Double.MAX_VALUE;
		}

		double eOff = Math.sqrt(this.offsetX * this.offsetX + this.offsetY * this.offsetY); // euclidean length of the
																							// offset
		double distance = eOff * lambda;

		return distance;
	}
}
