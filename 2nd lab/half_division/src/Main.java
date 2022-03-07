import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        IO io = new IO();

        double[] inputData = io.readData(args.length==0 ? "" : args[0]);
        int num = (int) inputData[0];
        double a = inputData[1];
        double b = inputData[2];
        double epsilon = inputData[3];
        int afterComma = 0;
        double t = 1;
        while (t != epsilon){
            t= t/10;
            afterComma++;
        }

        double x = (a+b)/2;
        // count of iterations
        int n = 0;
        if (num > 3 || num < 1) {
            System.err.println("In program is no such function;");
            System.exit(-1);
        }
        if (!verify(num, a, b )) {
            System.err.println("This function has no root");
            System.exit(-1);
        }
        while (Math.abs(a-b)>epsilon && Math.abs(getFunc(num, x))>=epsilon){
            if (getFunc(num, a)*getFunc(num, x)>0 ) a = x;
            else b = x;
            n++;
            System.out.println("Iteration " + n);
            System.out.format("x -> %."+afterComma+"f\n" , x);
            System.out.format("f(x) -> %."+afterComma+"f\n", getFunc(num, x));
            System.out.format("|a-b| -> %."+afterComma+"f\n" , Math.abs(a-b));
            System.out.println();
            x = (a+b)/2;
        }
        System.out.println("<<RESULT>>");
        System.out.println("Ends at " + n + " iteration");
        System.out.format("x -> %."+afterComma+"f\n" , x);
        System.out.format("f(x) -> %."+afterComma+"f\n", getFunc(num, x));
        System.out.format("|a-b| -> %."+afterComma+"f\n" , Math.abs(a-b));
        System.out.println();

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

    //true - if all is good verified, else - false
    static boolean verify(int num, double a, double b){
        return getFunc(num, a) * getFunc(num, b) < 0;
    }
}
