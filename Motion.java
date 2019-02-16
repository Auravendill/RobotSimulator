public class Motion {

    private double x, y, theta;
    private double vLeft, vRight;
    private double deltaTime ;

    private final double l = 3;
    private final double vMax = 3;


    public Motion(double x, double y, double theta, double vLeft, double vRight, double deltaTime){
        this.x = x;
        this.y = y;
        this.theta = theta;
        this.vLeft = vLeft;
        this.vRight = vRight;
        this.deltaTime = deltaTime;

    }

    public double[] motion() {


        double[] arr0 = new double[3];
        if (vLeft > vMax) vLeft = vMax;
        if (vRight > vMax) vRight = vMax;

        if (vRight == vLeft) {
            arr0[0] = (x + vLeft)*deltaTime*Math.cos(theta);
            arr0[1] = (y + vRight)*deltaTime*Math.sin(theta);
            arr0[2] = theta;


        } else {

            // R & w
            double R = l / 2. * (vLeft + vRight) / (vRight - vLeft);
            double w = (vRight - vLeft) / l;

            // ICC
            double ICCx = x - R * Math.sin(theta);
            double ICCy = y + R * Math.cos(theta);

            // new position and angle
            double angle = w * deltaTime;
            double[][] rotationMatrix = {{Math.cos(angle), -Math.sin(angle), 0},
                    {Math.sin(angle), Math.cos(angle), 0}, {0, 0, 1}};

            double[] arr1 = {x - ICCx, y - ICCy, theta};
            double[] arr2 = {ICCx, ICCy, angle};
            double[] arr3 = {0., 0., 0.};
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    arr3[i] += arr1[j] * rotationMatrix[i][j];
                }
                arr0[i] = arr3[i] + arr2[i];
            }

        }
        x = arr0[0];
        y = arr0[1];
        theta = arr0[2];


        return arr0;
    }
}
