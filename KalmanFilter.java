public class KalmanFilter {
    int n = 3;
    double[] mu = new double[n];
    double[][] A = new double[n][n];
    double[][] B = new double[n][n];
    double[] u = new double[n-1];
    double[][] Sigma = new double[n][n];
    double[][] R = new double[n][n];
    double[][] K = new double[n][n];
    double[][] C = new double[n][n];
    double[] z = new double[n];
    double[][] Q = new double[n][n];




    double[][] matrixMultiply(double[][] a, double[][] b, int n){
        double[][] c = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {


            }

        }
        return c;
    }


}
