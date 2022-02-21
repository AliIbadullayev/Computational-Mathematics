import java.io.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        IO io = new IO();

        // count of lines in system
        int n;
        // matrix
        float[][] arr;
        float[][] arr_copy;
        // extension of matrix ( b array )
        float[] b;
        float[] b_copy;
        // result array of unknown variables
        float[] x;
        // counter of swap lines (if the count of swaps is equal to possible count of swaps then stop while cycle)
        int stop = 0;
        // coefficient of exception
        float c;
        // variable need to find the unknown variables (x-es)
        float s;
        // count of swaps
        int swaps = 0;

        float[] arr_res = io.readData( args.length==0 ? "" : args[0]);
        n = (int) arr_res[0];
        if (n>20) System.exit(-1);
        arr = new float[n][n];
        b = new float[n];
        x = new float[n];

        // Fulling matrix by result array which has got from data input
        for (int i = 0; i < n; i++){
            for (int k = 0; k < n; k++){
                arr[i][k] = arr_res[k+(i*n)+1];
            }
            b[i] = arr_res[(n*n)+i+1];
        }
        arr_copy = new float[n][n];
        b_copy = new float[n];

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
                if (Float.isNaN(x[j])) s=s+0;
                else s = s + arr[i][j]*x[j];
            }
            if (Float.isInfinite(s)) {
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

    }

    private static float[] findDiscrepancy(float[][] arr, float[] x, float[] b) {
        float[] result = new float[b.length];
        float s = 0;
        for (int i = 0; i<arr.length; i++){
            for (int k = 0; k<arr.length; k++){
                s = s + arr[i][k]*x[k];
            }
            result[i] = s - b[i];
            s = 0;
        }
        return result;
    }

    private static float findDeterminant(float[][] arr, int swaps) {
        float det = arr[0][0];
        for (int i = 1; i<arr.length; i++){
            det *= arr[i][i] ;
        }
        return det==-0?0: (float) (det * Math.pow(-1, swaps));
    }

    private static void swapLines(float[][] ints, float[] b, int pos) {
        float[] temp = ints[pos];
        float tempB = b[pos];
        if (ints.length - 1 - pos >= 0) {
            System.arraycopy(ints, pos + 1, ints, pos, ints.length - 1 - pos);
        }
        if (b.length - 1 - pos >= 0) System.arraycopy(b, pos + 1, b, pos, b.length - 1 - pos);
        b[b.length-1] = tempB;
        ints[ints.length-1] = temp;
    }
}
