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

    public double[][] matrix2Multiply(double[][] a, double[][] b){
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
    public double[][] matrix3Multiply(double[][]a, double[][] b, double[][] c){
        return matrix2Multiply(matrix2Multiply(a,b),c);
    }
    public double[] matrixVectorMultiply(double[][] a, double[] b){
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
    public double[][] addMatrix(double[][] a, double[][] b){
        double[][] c = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                c[i][j] = a[i][j] + b[i][j];
            }

        }
        return c;
    }
    public double[] addVector(double[] a, double[] b){
        double[] c = new double[n];
        for (int i = 0; i < n; i++) {
            c[i] = a[i] + b[i];
        }
        return c;
    }
    public double[][] substractionMatrix(double[][] a, double[][] b){
        double[][] c = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                c[i][j] = a[i][j] - b[i][j];
            }

        }
        return c;
    }
    public double[] substractionVector(double[] a, double[] b){
        double[] c = new double[n];
        for (int i = 0; i < n; i++) {
            c[i] = a[i] - b[i];
        }
        return c;
    }
    public double[][] transpose(double[][] a){
        double[][] c = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                c[i][j] = a[j][i];
            }

        }
        return c;
    }
    public double[][] inverse(double[][] a){
        double[][] c = new double[n][n];

        return c;
    }

    public void kalmanFilter(){

        mu = addVector(matrixVectorMultiply(A,mu),matrixVectorMultiply(B,u));
        Sigma =addMatrix(matrix3Multiply(A,Sigma,transpose(A)),R);

        double[][] error = addMatrix(Q,matrix3Multiply(C,Sigma,transpose(C)));
        K = matrix3Multiply(Sigma,transpose(C),inverse(error));
        mu = addVector(mu,matrixVectorMultiply(K,substractionVector(z,matrixVectorMultiply(C,mu))));
        Sigma = matrix2Multiply(substractionMatrix(I,matrix2Multiply(K,C)),Sigma);

    }






}
