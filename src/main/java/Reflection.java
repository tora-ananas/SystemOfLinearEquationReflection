public class Reflection {

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

    public static double[][] matrMult(double[][] matrStart, double[][] matrFin) { //matrStart - исходная, matrFin - матрица хаусхолдера.
        int m = matrFin.length;
        int o = matrStart.length;
        double[][] matrixMult = new double[m][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < o; k++) {
                    matrixMult[i][j] += matrFin[i][k] * matrStart[k][j];
                }
            }
        }
        return matrixMult;
    }

    public static double[] matrMultVec(double[] vecStart, double[][] matrFin) { //vecStart - вектор, matrFin - матрица хаусхолдера.
        int m = matrFin.length;
        double[] matrixMultVec = new double[m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                matrixMultVec[i] += matrFin[i][j] * vecStart[j];
            }
        }
        return matrixMultVec;
    }

    //тут нужно указать какой по счету столбик мы выбираем
    public static double[] vecColFromMatrix(double[][] matrixInput, int a){
        double[] vecColFromMatr = new double[matrixInput.length-a];
        for (int i = 0; i < vecColFromMatr.length; i++) {
            vecColFromMatr[i] = matrixInput[i+a][a];
        }
        return vecColFromMatr;
    }

    public static double[][] matrPlusIdentity(double[][] matrix, int a){ // здесь указывается итерация
        int len = matrix.length;
        double[][] matrPlusIdentity = new double[len+a][len+a];
        for (int i = 0; i < a; i++) {
            matrPlusIdentity[i][i] = 1;
        }
        for (int i = a; i < len+a; i++) {
            for (int j = a; j < len+a; j++) {
                matrPlusIdentity[i][j] = matrix[i-a][j-a];
            }
        }
        return matrPlusIdentity;
    }

    //TODO еще добавить умножение правой части на матрицу отражений +
    public static double[][] methodReflection(double[][] matrix, double[] vecRight){
        int lenl = matrix.length;
        double[][] matr = new double[lenl][lenl+1];
        double[] a = vecColFromMatrix(matrix, 0);
        for (int i = 0; i < lenl-1; i++){
            double[] v = vecV(a);
            double[] vecW= vecW(v) ;
            double[][] matrRefl = matrixHouseholder(vecW);
            // на первой итерации ничего не надо увеличивать. на второй +1, на третьей +2 и т.д.
            matrRefl = matrPlusIdentity(matrRefl, i);
            matrix = matrMult(matrix, matrRefl); //домножили матрицу исходную на матрицу отражений
            vecRight = matrMultVec(vecRight, matrRefl); //домножили правую часть на матрицу отражений
            for (int j = i; j < lenl; j++) {
                for (int k = i; k < lenl; k++) {
                    matr[j][k] = matrix[j][k];
                }
                matr[j][lenl] = vecRight[j];
            }
            a = vecColFromMatrix(matr, i+1);
        }
        return matr;
    }


    public static double[] reverse(double[][] matr){
        int n = matr.length; // если матрица 3х4 то тут 3
        int m = matr[0].length; // тут 4
        double d, s;
        double[] x = new double[n];
        for (int k = n - 1; k >= 0; k--){
            d = 0;
            for (int j = k + 1; j < n; j++){
                s = matr[k][j] * x[j];
                d = d + s;
            }
            x[k] = (matr[k][m-1] - d) / matr[k][k];
        }
        return x;
    }


    public static void main(String[] args) {
        /*double a1[] = {2, 1.2, 1};
        System.out.println(norm(a1));
        double[] vecW = vecW(vecV(a1));
        double[][] matrFin = matrixHouseholder(vecW);*/
        double[][] matrStart = { {3, 2, -5}, {2, -1, 3}, {1, 2, -1}};
        double[] vec = {-1, 13, 9};
        double[][] res = methodReflection(matrStart, vec);
        double[] roots = reverse(res);
        /*double[][] matrMult = matrMult(matrStart, matrFin);
        //после этого надо взять второй столбец получившейся матрицы.
        double a2[] = vecColFromMatrix(matrMult, 2);*/

        /*double[] test = {1,2,3};
        double[][] test1 = {{1,2,3}, {4,5,6}, {7,8,9}};
        double[] multVec = new double[test1.length];
        multVec = matrMultVec(test,test1);*/


        /*double[][] matr = { {1,2,3,4},{2,5,6,3},{2,4,3,6}};
        double[] root =  reverse(matr);*/

        //невязка r = b - Ax
        double[] r = new double[vec.length];
        double[] Ax = matrMultVec(roots, matrStart);
        for (int i = 0; i < vec.length; i++){
            r[i]= vec[i] - Ax[i];
        }

    }

}
