import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;

public class MatrixTest extends TestCase{

    public double inf_norm ( double[] a, int n){

        int i,j;
        double s,norm = 0.;

        for( i = 0; i < n; i++ )
        {
            s = Math.abs( a[i] );
            if( s > norm ) norm = s;
        }

        return norm;
    }

    @Test
    void getReflectionMatrix() {
        int n = 100;
        double alpha = 1;
        double beta = 1e1;
        double[][] matrix = new double[n][n];
        double[][] another = new double[n][n];
        double[] xAcc = new double[n];
        int count = 1;
        double ksi = 0;


        while (Double.compare(ksi, 1e-1) < 0){
            System.out.println(count +" "+ alpha);
            Gen gen = new Gen();
            gen.mygen(matrix, another, n, alpha, beta, 1, 2, 1, 1);
            double[][] copy = new double[n][n];
            for (int k = 0; k < n; k++){
                for (int l = 0; l < n; l++){
                    copy[k][l] = matrix[k][l];
                }
            }
            double[] copyxAcc = new double[n];
            for (int i = 0; i < n; i++){
                xAcc[i] = (Math.random()*10)+1;
                copyxAcc[i] = xAcc[i];
            }


            double[] fvec = Reflection.matrMultVec(xAcc, matrix);// f = A * x(точное)
            double[][] result = Reflection.methodReflection(matrix, fvec);
            double[] xOur = Reflection.reverse(result);//х приближенное

            double[] z = new double[xOur.length];
            for (int i = 0; i < xOur.length; i++){
                z[i] = xOur[i] - xAcc[i]; //ошибка
            }
            DecimalFormat Dformat = new DecimalFormat("0.0000E00");
            System.out.println(Dformat.format(inf_norm(z,z.length)) + " - ошибка\n");


            ksi = inf_norm(z,z.length)/inf_norm(xAcc, xAcc.length);
            System.out.println(Dformat.format(ksi) + " - относительная ошибка\n");


            double[] r = new double[n];
            double[] Ax = Reflection.matrMultVec(xOur, copy);
            for (int i = 0; i < n; i++){
                r[i]= Ax[i] - fvec[i];
            }

            System.out.println(Dformat.format(inf_norm(r, n)) + " - невязка\n");

            double ro = inf_norm(r, n)/inf_norm(fvec, n);
            System.out.println(Dformat.format(ro) + " - относительная невязка");

            beta *=1e1;
            count++;
        }

    }
}
