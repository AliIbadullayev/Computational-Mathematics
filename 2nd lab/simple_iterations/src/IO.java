import java.io.*;
import java.util.Arrays;

public class IO {
    public double[] readData(String args) throws IOException {
        BufferedReader br;
        String result;
        String[] arr_res;
        double[] output = new double[4];

        br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("<<SIMPLE ITERATION METHOD>>");
        System.out.println("Please select the function:");
        System.out.println("1) -2,7x^3 - 1,48x^2 + 19,23x + 6,35");
        System.out.println("2) x^3 - 1,89x^2 - 2x + 1,76");
        System.out.println("3) sin(x) - 2x^3 + x^2");
        result = br.readLine();

        if (!args.equals("") ) {
            System.out.println("You has selected file: " + args );
            File input = new File(args);
            if (!input.canRead()) System.err.println("Cannot open file!\nPlease check correctness of input file!");
            br = new BufferedReader(new FileReader(input));
        }
        else {
            System.out.println("You hasn't select input file.\nPlease, enter input data (interval of your function <a> <b>, epsilon (0.01)) \nExample: 3 4 0.01");
            br = new BufferedReader(new InputStreamReader(System.in));
        }
        result = result + " " + br.readLine();
        arr_res = result.split("\\s+");

        for (int i = 0; i<4; i++){
            output [i] = Double.parseDouble(arr_res[i]);
        }

        if (arr_res.length!=4){
            System.err.println("Not correct input data in file!\nPlease, enter input data (interval of your function <a> <b>, epsilon (0.01)) \\nExample: 3 4 0.01\"");
            System.exit(-1);
        }
        return output;
    }
}

