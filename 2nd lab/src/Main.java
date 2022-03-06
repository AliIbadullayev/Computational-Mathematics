import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        IO io = new IO();

        double[] inputData = io.readData(args[0]);
        int function = (int) inputData[0];
        if (function > 3 || function < 1) System.err.println("In program is no such function;");

    }

    static double getFunc(int num, double x){
        if (num == 1) {
            return -2.7 * Math.pow(x, 3) - 1.48 * Math.pow(x, 2) + 19.23 * x + 6.35;
        } else if (num == 2) {
            return Math.pow(x, 3) - 1.89 * Math.pow(x, 2) - 2 * x + 1.76;
        } else if (num == 3) {
            return Math.sin(x) - 2 * Math.pow(x, 3) + Math.pow(x, 2);
        }
        return Double.MAX_VALUE;
    }
}
