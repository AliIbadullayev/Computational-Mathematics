import java.io.*;

public class IO {
    public double[] readData(String args) throws IOException {
        BufferedReader br;
        String result;
        String[] arr_res;
        double[] output = new double[4];

        br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("<<SIMPSONS METHOD>>");
        System.out.println("Please select the function:");
        System.out.println("1) 3x^3 + 5x^2 + 3x - 6");
        System.out.println("2) 4x^3 - 5x^2 + 6x - 7");
        System.out.println("3) 3x^3 - 2x^2 - 7x - 8");
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

