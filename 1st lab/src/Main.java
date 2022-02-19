import java.io.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        IO io = new IO();

        // count of lines in system
        int n = 0;
        // matrix
        float[][] arr;
        // extension of matrix ( b array )
        float[] b;
        // temporary massive which is used to copy elements in lines if arr[i][i] = 0;
        float[] temp = new float[n];
        // result array of unknown variables
//        float[] x = new float[n];
        // counter of swap lines (if the count of swaps is equal to possible count of swaps then stop while cycle)
        int stop = 0;
        // coefficient of exception
        float c;
        // variable need to find the unknown variables (x-es)
        float s;

        int[] arr_res = io.readData( args.length==0 ? "" : args[0]);
        n = arr_res[0];

        arr = new float[n][n];
        b = new float[n];

        // Fulling matrix by result array which has got from data input
        for (int i = 0; i < n; i++){
            for (int k = 0; k < n; k++){
                System.out.println(arr_res[k+(i*n)+1]);
                arr[i][k] = arr_res[k+(i*n)+1];
            }
            b[i] = arr_res[(n*n)+i+1];
        }


        for (float[] anInt : arr) {
            System.out.println(Arrays.toString(anInt));
        }

        // straight stroke (process of exception an elements of matrix to get triangle-matrix)
        for(int i = 0; i < n - 1 ; i++){
            while (arr[i][i] == 0  ){
                if (stop == n) {
                    System.err.println("The error in swapping lines!");
                    System.exit(0);
                }
                swapLines(arr, i);
                stop++;
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
            for (float[] anInt : arr) {
                System.out.println(Arrays.toString(anInt));
            }
        }

        float[] x = new float[n];
        // reverse stroke (process of getting an unknown variables)
        for (int i = n - 1 ; i > 0 ; --i){
            s = 0;
            for (int j = i+1; j<n; j++){
                s = s + arr[i][j]*x[j];
            }
            x[i] = (b[i] - s)/arr[i][i];
        }
        System.out.println("RESULT VECTOR-COLUMN OF X-ES ");
        System.out.println(Arrays.toString(x));
    }

    private static void swapLines(float[][] ints, int pos) {
        float[] temp = ints[pos];
        if (ints.length - 1 - pos >= 0) {
            System.arraycopy(ints, pos + 1, ints, pos, ints.length - 1 - pos);
        }
        ints[ints.length-1] = temp;
    }
}
