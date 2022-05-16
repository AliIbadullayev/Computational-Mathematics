## COMPUTATIONAL IMPLEMENTATION OF THE PROBLEM
```java
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleFunction;

public class Main {
    // x∈ [0, 2]   h=0,2
    public static DoubleFunction<Double> func_test = (x) -> 2*x/(Math.pow(x, 4) + 1);
    public static IO io = new IO();
    public static void main(String[] args) throws IOException {
        Map<Double, Double> points = new HashMap<>();
        BigDecimal tempi = BigDecimal.valueOf(0);
        for (int i = 0; i < 11; i++){
            points.put(tempi.doubleValue(), Double.valueOf(new DecimalFormat("0.000").format(func_test.apply(tempi.doubleValue()))));
            System.out.print("x"+ i +" = "+tempi.doubleValue()+"\ty"+ i );
            System.out.format(" = %.2f\n",func_test.apply(tempi.doubleValue()));
            tempi = tempi.add(BigDecimal.valueOf(0.2));
        }
        int n1 = 2;
        int n2 = 3;
        double SX = 0;
        double SXX = 0;
        double SXY = 0;
        double SY = 0;
        double SXXX = 0;
        double SXXXX = 0;
        double SXXY = 0;
        for (Double key: points.keySet()){
            SX += key;
            SXX += Math.pow(key,2);
            SXXX += Math.pow(key,3);
            SXXXX += Math.pow(key,4);
            SXY += key* points.get(key);
            SY += points.get(key);
            SXXY +=  Math.pow(key,2)* points.get(key);
        }
        System.out.println("SX = " +SX);
        System.out.println("SXX = " +SXX);
        System.out.println("SXXX = " +SXXX);
        System.out.println("SXXXX = " +SXXXX);
        System.out.println("SY = " +SY);
        System.out.println("SXY = " +SXY);
        System.out.println("SXXY = " +SXXY);

        double[] arr1 = {1, SX, SX,SXX,SY, SXY};
        double[] arr2 = {1, SX, SXX,SX, SXX, SXXX, SXX, SXXX, SXXXX, SY, SXY, SXXY};
        double[] b1 = {SY, SXY};
        double[] b2 = {SY, SXY, SXXY};
        double S1 = 0;
        double S2 = 0;

        System.out.println("FIRST APPROXIMATION");
        double[] x1 = gaussianMethod(arr1, 2);
        DoubleFunction<Double> f_X1 = (x) -> x1[0] + x1[1]*x;
        for (Double key: points.keySet()){
            S1 += Math.pow(f_X1.apply(key) - points.get(key),2);
        }
        System.out.println("S = " + S1);
        System.out.println("Theta1 = " + (Math.sqrt(S1/11)));
        System.out.println("SECOND APPROXIMATION");
        double[] x2 = gaussianMethod(arr2, n2);
        DoubleFunction<Double> f_X2 = (x) -> x2[0] + x2[1]*x + x2[2]*x*x;

        for (Double key: points.keySet()){
            S2 += Math.pow(f_X2.apply(key) - points.get(key),2);
        }
        System.out.println("S = " + S2);
        System.out.println("Theta2 = " + (Math.sqrt(S2/11)));
    }

    public static double[] gaussianMethod(double [] arr_res, int n){
        double[] x = new double[n];
        double [][] arr = new double[n][n];
        double[] b = new double[n];
        int stop = 0;
        double c;
        double s;
        int swaps = 0;

        // Fulling matrix by result array which has got from data input
        for (int i = 0; i < n; i++){
            for (int k = 0; k < n; k++){
                arr[i][k] = arr_res[k+(i*n)];
            }
            b[i] = arr_res[(n*n)+i];
        }
        double [][] arr_copy = new double[n][n];
        double [] b_copy = new double[n];

        for (int i = 0; i < n; i++) {
            System.arraycopy(arr[i], 0, arr_copy[i], 0, arr[i].length);
        }

        System.arraycopy(b, 0, b_copy, 0, arr.length);

        System.out.println("\t<<INPUT MATRIX>>");
        io.showExtendedMatrix(arr, b);

        // straight stroke (process of exception an elements of matrix to get triangle-matrix)
        for(int i = 0; i < n - 1 ; i++){
            while (arr[i][i] == 0  ){
                if (stop == n - i) {
                    System.err.println("The error in swapping lines!");
                    System.exit(0);
                }
                swapLines(arr, b,  i);
                stop++;
                swaps++;
            }

            for (int k = i+1; k < n; k++){
                c = arr[k][i] / arr[i][i];
                arr[k][i] = 0;
                for (int j = i+1; j < n; j++){
                    arr[k][j] = arr[k][j] - c * arr[i][j];
                }
                b[k] = b[k] - c * b[i];
            }
            stop = 0;
            System.out.println();
            System.out.println("\t<<Iteration №" + (i+1)+">>");
            io.showExtendedMatrix(arr, b);
        }

        // reverse stroke (process of getting an unknown variables)
        for (int i = n - 1 ; i >= 0 ; i--){
            s = 0;
            for (int j = i+1; j<n; j++){
                if (Double.isNaN(x[j])) s=s+0;
                else s = s + arr[i][j]*x[j];
            }
            if (Double.isInfinite(s)) {
                System.err.println("This system has no solution!\nBecause: "+"0!="+b[i+1]);
                System.exit(-1);
            }
            x[i] = (b[i] - s)/arr[i][i] == -0 ? 0:(b[i] - s)/arr[i][i] ;
        }
        System.out.println();
        System.out.println("\t<<RESULTS>>");
        System.out.println("<<Triangle Matrix>>");
        io.showExtendedMatrix(arr,b);
        System.out.println();
        System.out.println("Det A =\t" + findDeterminant(arr, swaps));
        System.out.print("X =\t");
        System.out.println(Arrays.toString(x));
        System.out.println("r =\t" + Arrays.toString(findDiscrepancy(arr_copy, x, b_copy )));
        return x;
    }

    private static double[] findDiscrepancy(double[][] arr, double[] x, double[] b) {
        double[] result = new double[b.length];
        double s = 0;
        for (int i = 0; i<arr.length; i++){
            for (int k = 0; k<arr.length; k++){
                s = s + arr[i][k]*x[k];
            }
            result[i] = s - b[i];
            s = 0;
        }
        return result;
    }

    private static double findDeterminant(double[][] arr, int swaps) {
        double det = arr[0][0];
        for (int i = 1; i<arr.length; i++){
            det *= arr[i][i] ;
        }
        return det==-0?0: (double) (det * Math.pow(-1, swaps));
    }

    private static void swapLines(double[][] ints, double[] b, int pos) {
        double[] temp = ints[pos];
        double tempB = b[pos];
        if (ints.length - 1 - pos >= 0) {
            System.arraycopy(ints, pos + 1, ints, pos, ints.length - 1 - pos);
        }
        if (b.length - 1 - pos >= 0) System.arraycopy(b, pos + 1, b, pos, b.length - 1 - pos);
        b[b.length-1] = tempB;
        ints[ints.length-1] = temp;
    }
}
```
``
10
0.2	0.40
0.4	0.78
0.6	1.06
0.8	1.14
1.0	1.00
1.2	0.78
1.4	0.58
1.6	0.42
1.8	0.31
2.0	0.24
``

``
7
1.1 2.73
2.3 5.12
3.7 7.74
4.5 8.91
5.4 10.59
6.8 12.75
7.5 13.43
``