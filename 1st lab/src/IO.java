import java.io.*;
import java.util.Arrays;

public class IO {
    public float[] readData(String args) throws IOException {
        BufferedReader br;
        String result;
        String[] arr_res;
        float[] matrix;
        int n;

        if (!args.equals("") ) {
            System.out.println("You has selected file: " + args );
            File input = new File(args);
            if (!input.canRead()) System.err.println("Cannot open file!\nPlease check correctness of input file!");
            br = new BufferedReader(new FileReader(input));
        }
        else {
            System.out.println("You hasn't select input file.\nPlease, enter input data (n - count of lines of system, matrix elements, extended matrix elements)");
            br = new BufferedReader(new InputStreamReader(System.in));
        }
        result = br.readLine();
        arr_res = result.split("\\s+");
        n = Integer.parseInt(arr_res[0]);
        matrix = new float[n*n + 1 +n];
        for (int i = 0; i<n*n + 1 +n; i++){
            matrix [i] = Float.parseFloat(arr_res[i]);
        }

        if (arr_res.length!=n*n+n+1){
            System.err.println("Not correct input data in file!\nCheck amount of elements!\n<n - count of lines of system, matrix elements, extended matrix elements>");
            System.exit(-1);
        }
        return matrix;
    }
    public void showExtendedMatrix(float[][] arr, float[] b){
        System.out.print("A =");
        for (float[] anInt : arr) {
            System.out.println("\t"+Arrays.toString(anInt));
        }
        System.out.println();
        System.out.print ("B =\t");
        System.out.println(Arrays.toString(b));

    }
}
