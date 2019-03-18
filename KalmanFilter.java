public class KalmanFilter {
    int n = 3;
    public double[] mu = new double[n];
    double[][] A = new double[n][n];
    double[][] B = new double[n][n];
    double[] u = new double[n-1];
    double[][] Sigma = new double[n][n];
    double[][] R = new double[n][n];
    double[][] K = new double[n][n];
    double[][] C = new double[n][n];
    double[] z = new double[n];
    double[][] Q = new double[n][n];
    double[][] I = {{1,0,0},{0,1,0},{0,0,1}};

    private double[][] matrix2Multiply(double[][] a, double[][] b){
        double[][] c = new double[n][n];
        double sum;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sum = 0.;
                for (int k = 0; k < n; k++) {
                    sum += a[i][k]*b[k][j];
                }
                c[i][j] = sum;
            }

        }
        return c;
    }
    private double[][] matrix3Multiply(double[][]a, double[][] b, double[][] c){
        return matrix2Multiply(matrix2Multiply(a,b),c);
    }
    private double[] matrixVectorMultiply(double[][] a, double[] b){
        double[] c = new double[n];
        double sum;
        for (int i = 0; i < n; i++) {
            sum = 0.;
            for (int k = 0; k < n; k++) {
                sum += a[i][k]*b[k];
            }
            c[i] = sum;
        }
        return c;
    }
    private double[][] addMatrix(double[][] a, double[][] b){
        double[][] c = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                c[i][j] = a[i][j] + b[i][j];
            }

        }
        return c;
    }
    private double[] addVector(double[] a, double[] b){
        double[] c = new double[n];
        for (int i = 0; i < n; i++) {
            c[i] = a[i] + b[i];
        }
        return c;
    }
    private double[][] substractionMatrix(double[][] a, double[][] b){
        double[][] c = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                c[i][j] = a[i][j] - b[i][j];
            }

        }
        return c;
    }
    private double[] substractionVector(double[] a, double[] b){
        double[] c = new double[n];
        for (int i = 0; i < n; i++) {
            c[i] = a[i] - b[i];
        }
        return c;
    }
    private double[][] transpose(double[][] a){
        double[][] c = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                c[i][j] = a[j][i];
            }

        }
        return c;
    }
    private double det(double[][] a){
        double c = a[0][2]*a[1][0]*a[2][1] + a[0][1]*a[1][2]*a[2][0]+a[0][0]*a[1][1]*a[2][2]
                -a[0][0]*a[2][1]*a[1][2]-a[1][0]*a[0][1]*a[2][2] - a[0][2]*a[1][1]*a[2][0];
        return c;
    }
    private double[][] inv(double[][] a){

        double[][] inverse = new double[n][n];
        double determinant = det(a);

        inverse[0][0] = (a[1][1]*a[2][2]-a[1][2]*a[2][1])/determinant;
        inverse[0][1] = -(a[1][0]*a[2][2]-a[1][2]*a[2][0])/determinant;
        inverse[0][2] = (a[1][0]*a[2][1]-a[1][1]*a[2][0])/determinant;
        inverse[1][0] = -(a[0][1]*a[2][2]-a[0][2]*a[2][1])/determinant;
        inverse[1][1] = (a[0][0]*a[2][2]-a[0][2]*a[2][0])/determinant;
        inverse[1][2] =-( a[0][0]*a[2][1]-a[0][1]*a[2][0])/determinant;
        inverse[2][0] = (a[0][1]*a[1][2]-a[0][2]*a[1][1])/determinant;
        inverse[2][1] = -(a[0][0]*a[1][2]-a[0][2]*a[1][0])/determinant;
        inverse[2][2] = (a[0][0]*a[1][1]-a[0][1]*a[1][0])/determinant;
        inverse = transpose(inverse);

        return inverse;
    }

    public void kalmanFilter(){

        mu = addVector(matrixVectorMultiply(A,mu),matrixVectorMultiply(B,u));
        Sigma =addMatrix(matrix3Multiply(A,Sigma,transpose(A)),R);

        double[][] error = addMatrix(Q,matrix3Multiply(C,Sigma,transpose(C)));
        K = matrix3Multiply(Sigma,transpose(C),inv(error));
        mu = addVector(mu,matrixVectorMultiply(K,substractionVector(z,matrixVectorMultiply(C,mu))));
        Sigma = matrix2Multiply(substractionMatrix(I,matrix2Multiply(K,C)),Sigma);

    }






}
