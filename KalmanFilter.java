import java.util.ArrayList;

public class KalmanFilter {
    double xInit = Main.Xpos;
    double yInit = Main.Ypos;
    double thetaInit = Main.Angle;
    //double vInit = (Main.Vl+Main.Vr)/2;
    //double wInit = (Main.Vr-Main.Vl)/Main.radius*2;
    FeatureDetection f = new FeatureDetection();
    int n = 3;
    private double[] muPre,z = new double[n];
    private double[] mu = {xInit, yInit, thetaInit};
    private double[] u = new double[n-1];
    private double[][] I = {{1,0,0},{0,1,0},{0,0,1}};
    private double[][] K,Q,SigmaPre = new double[n][n];
    private double[][] A = I;
    private double[][] C = I;
    private double[][] B = {{Main.deltaTime*Math.cos(thetaInit),0},{Main.deltaTime*Math.sin(thetaInit),0},{0,Main.deltaTime}};
    private double[][] Sigma = {{10,0,0},{0,10,0},{0,0,10}}; // Initial with small values
    private double[][] R =  {{10,0,0},{0,10,0},{0,0,10}}; // Initial with small values

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
    private double[] matrVecMult(double[][] a, double[] b){
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
    private double[][] substraMatrix(double[][] a, double[][] b){
        double[][] c = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                c[i][j] = a[i][j] - b[i][j];
            }

        }
        return c;
    }
    private double[] substraVector(double[] a, double[] b){
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
    	//ArrayList <double[]> features = f.getFeaturesClose(Main.featureRange);
        // Prediction
        B[0][0] = Main.deltaTime*Math.cos(mu[2]);
        B[1][0] = Main.deltaTime*Math.sin(mu[2]);

        u = new double[]{(Main.Vl+Main.Vr)/2,(Main.Vr-Main.Vl)/Main.radius*2};
        z = new double[]{0,0,0}; // z = {x,y,theta} from sense
        
        //z= features.get(0);

        muPre = addVector(matrVecMult(A,mu), matrVecMult(B,u));
        SigmaPre =addMatrix(matrix3Multiply(A,Sigma,transpose(A)),R);

        // Correction
        double[][] error = addMatrix(Q,matrix3Multiply(C,SigmaPre,transpose(C)));
        K = matrix3Multiply(SigmaPre,transpose(C),inv(error));
        mu = addVector(muPre, matrVecMult(K, substraVector(z, matrVecMult(C,muPre))));
        Sigma = matrix2Multiply(substraMatrix(I,matrix2Multiply(K,C)),SigmaPre);

    }

    public double[] getMu(){
    	return mu;
    }




}
