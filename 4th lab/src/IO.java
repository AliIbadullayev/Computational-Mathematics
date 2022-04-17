import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleFunction;

public class IO {
    public Map<Double, Double> readData(String args) throws IOException {
        BufferedReader br;
        // temporary array to split string with points
        String[] temp_arr;
        // result array
        Map<Double, Double> points = new HashMap<>();
        int n;

        if (!args.equals("") ) {
            System.out.println("You has selected file: " + args );
            File input = new File(args);
            if (!input.canRead()) System.err.println("Cannot open file!\nPlease check correctness of input file!");
            br = new BufferedReader(new FileReader(input));
        }
        else {
            System.out.println("You hasn't select input file.\nPlease, enter input data \nn - count of lines of system, \ncoordinates of points \n 1 2\n2.5 3\n...");
            br = new BufferedReader(new InputStreamReader(System.in));
        }
        n = Integer.parseInt((br.readLine()));

        try {
            for (int i = 0; i < n; i++) {
                temp_arr = br.readLine().split("\\s+");
                points.put(Double.parseDouble(temp_arr[0]), Double.parseDouble(temp_arr[1]));
            }
        }catch (IOException e) {
            System.err.println("Not correct input data in file!");
        }

        return points;
    }
    public void showExtendedMatrix(double[][] arr, double[] b){
        System.out.print("A =");
        for (double [] anInt : arr) {
            System.out.println("\t"+ Arrays.toString(anInt));
        }
        System.out.println();
        System.out.print ("B =\t");
        System.out.println(Arrays.toString(b));
    }

    public void showResults(Map<Double, Double> points, DoubleFunction<Double> func) {
        double S = 0;
        for (Double key: points.keySet()){
            S += Math.pow(func.apply(key) - points.get(key),2);
        }
        System.out.format("S = %.3f\n", S);
        System.out.format("Theta = %.3f\n", (Math.sqrt(S/points.size())));
        System.out.println();
    }
}
