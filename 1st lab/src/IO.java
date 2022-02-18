import java.io.*;
import java.util.Arrays;

public class IO {
    public int[] readData(String args) throws IOException {
        BufferedReader br;
        String result;
        int[] arr_res;
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
        arr_res = Arrays.stream(result.split(" ")).mapToInt(Integer::parseInt).toArray();
        n = arr_res[0];

        if (arr_res.length!=n*n+n+1){
            System.err.println("Not correct input data in file!\nCheck amount of elements!\n<n - count of lines of system, matrix elements, extended matrix elements>");
            System.exit(-1);
        }
        return arr_res;
    }
}
