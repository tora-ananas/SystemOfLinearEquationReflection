
public class Matrix {

    //считаем длину вектора-столбца
    public static double norm(double[] matr){
        double sum =0;
        for (int i = 0; i < matr.length; i++) {
            sum += Math.abs(matr[i])*Math.abs(matr[i]);
        }
        return Math.sqrt(sum);
    }

    public static double[] vecV(double[] vecA){
        double[] vecV = vecA.clone();
        vecV[0] = norm(vecA)+vecA[0];
        return vecV;
    }

    public static double[] vecW(double[] vecV){
        double[] vecW = new double[vecV.length];
        double vecNorm = norm(vecV);
        for (int i = 0; i < vecV.length; i++) {
            vecW[i] = vecV[i]/vecNorm;
        }
        return vecW;
    }

    public static double[][] matrixHouseholder(double[] vecW){
        double[][] matrix = new double[vecW.length][vecW.length];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i][i] = 1;
            for (int j = 0; j < matrix.length; j++) {
                matrix[i][j] -= vecW[i]*vecW[j]*2;
            }
        }
        return matrix;
    }

    //получили матрицу слау новую.
    public static double[][] matrMult(double[][] matrStart, double[][] matrFin, int a){ //matrStart - исходная, matrFin - матрица хаусхолдера.
        int m = matrFin.length;
        int n = matrStart[0].length;
        int o = matrStart.length-a;
        double[][] matrixMult = new double[m][n - a];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < o; k++) {
                    matrixMult[i][j] += matrFin[i][k]*matrStart[k+a][j+a];
                }
            }
        }
        int j = matrixMult[0].length-1;
        for (int i = 0; i < m; i++) {
            for (int k = 0; k < o; k++) {
                matrixMult[i][j] += matrFin[i][k]*matrStart[k+a][j];
            }
        }
        return matrixMult;
    }

    //тут нужно указать какой по счету столбик мы выбираем
    public static double[] vecColFromMatrix(double[][] matrixInput, int a){
        double[] vecColFromMatr = new double[matrixInput.length-a+1];
        for (int i = 0; i < vecColFromMatr.length; i++) {
            vecColFromMatr[i] = matrixInput[i+a-1][a-1];
        }
        return vecColFromMatr;
    }

    //TODO: ПЕРЕДЕЛАТЬ В КВАДРАТНУЮ МАТРИЦУ С ПРАВОЙ ЧАСТЬЮ.
    public static double[][] methodReflection(double[][] matrix){
        int len = matrix[0].length;
        int lenl = matrix.length;
        double[][] matr = new double[lenl][len];
        for (int i = 0; i < len-2; i++){
            double[] a = vecColFromMatrix(matrix, i+1);
            double[] v = vecV(a);
            double[] vecW= vecW(v) ;
            double[][] matrRefl = matrixHouseholder(vecW);
            matrix = matrMult(matrix, matrRefl, i);
            for (int j = i; j < lenl; j++) {
                for (int k = i; k < len; k++) {
                    matr[j][k] = matrix[j-i][k-i];
                }
            }
        }
        return matr;
    }

    public static void main(String[] args) {
        /*double a1[] = {2, 1.2, 1};
        System.out.println(norm(a1));
        double[] vecW = vecW(vecV(a1));
        double[][] matrFin = matrixHouseholder(vecW);*/
        double[][] matrStart = { {2, -9, 5, -4}, {1.2, -5.9999, 6, 0.6001}, {1, -1, -7.5, -8.5} };
        /*double[][] matrMult = matrMult(matrStart, matrFin);
        //после этого надо взять второй столбец получившейся матрицы.
        double a2[] = vecColFromMatrix(matrMult, 2);*/
        methodReflection(matrStart);

    }


}

