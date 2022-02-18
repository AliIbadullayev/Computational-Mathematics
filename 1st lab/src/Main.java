import java.io.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        IO io = new IO();

        // count of lines in system
        int n = 0;
        // matrix
        int[][] arr;
        // extension of matrix ( b array )
        int[] b;
        // temporary massive which is used to copy elements in lines if arr[i][i] = 0;
        int[] temp = new int[n];
        // result array of unknown variables
        int[] x = new int[n];
        // counter of swap lines (if the count of swaps is equal to possible count of swaps then stop while cycle)
        int stop = 0;
        // coefficient of exception
        int c;
        // variable need to find the unknown variables (x-es)
        int s;

        int[] arr_res = io.readData( args.length==0 ? "" : args[0]);
        n = arr_res[0];

        arr = new int[n][n];
        b = new int[n];

        // Fulling matrix by result array which has got from data input
        for (int i = 0; i < n; i++){
            for (int k = 0; k < n; k++){
                System.out.println(arr_res[k+(i*n)+1]);
                arr[i][k] = arr_res[k+(i*n)+1];
            }
            b[i] = arr_res[(n*n)+i+1];
        }



        // straight stroke (process of exception an elements of matrix to get triangle-matrix)
        for(int i = 1; i < n ; i++){
            while (arr[i][i] != 0 && stop != n - i ){
                swapLines(arr[i]);
            }
            for (int k = i+1; k < n; k++){
                c = arr[k][i] / arr[i][i];
                arr[k][i] = 0;
                for (int j = i+1; j < n; j++){
                    arr[k][j] = arr[k][j] - c * arr[i][j];
                }
                b[k] = b[k] - c * b[i];
            }
        }
        // reverse stroke (process of getting an unknown variables)
        for (int i = n ; i > 0 ; --i){
            s = 0;
            for (int j = i+1; j<n; j++){
                s = s + arr[i][j]*x[j];
            }
            x[i] = (b[i] - s)/arr[i][i];
        }
    }

    private static void swapLines(int[] ints) {

    }
}
