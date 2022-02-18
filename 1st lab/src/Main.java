import java.io.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        // count of lines in system
        int n = 0;
        // matrix
        int[][] arr;
        // extension of matrix ( b array )
        int[] b = new int[n];
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

        String result;
        int[] arr_res = new int[0];

        if (args.length == 1 ) {
            System.out.println("You has selected file: " + args[0] );
            File input = new File(args[0]);
            if (!input.canRead()) System.err.println("Cannot open file!\nPlease check correctness of input file!");

            BufferedReader br = new BufferedReader(new FileReader(input));
            result = br.readLine();
            arr_res = Arrays.stream(result.split(" ")).mapToInt(Integer::parseInt).toArray();
            n = arr_res[0];

        }
        if (args.length == 0 ) {
            System.out.println("You hasn't select input file.\nPlease, enter input data (n - count of lines of system, matrix elements, extended matrix elements)");

        }

        arr = new int[n][n];
        for (int i = 0; i < n; i++){
            for (int k = 0; k < n; k++){
                System.out.println(arr_res[k+(i*n)+1]);
                arr[i][k] = arr_res[k+(i*n)+1];
            }
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
