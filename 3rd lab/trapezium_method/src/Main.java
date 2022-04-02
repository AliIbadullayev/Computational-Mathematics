import java.io.IOException;
import java.util.function.DoubleFunction;

public class Main {
    public static final double DX = 0.0001;
    // For calculate error with method of Runge, you need this coefficient (Method Simpsons - 4, Trapezium and rectangle method - 2)
    public static final double K = 2;
    public static DoubleFunction<Double> func = null;

    private static DoubleFunction<Double> derive(DoubleFunction<Double> f) {
        return (x) -> (f.apply(x + DX) - f.apply(x)) / DX;
    }

    public static void main(String[] args) throws IOException {
        IO io = new IO();

        double[] inputData = io.readData(args.length==0 ? "" : args[0]);
        int num = (int) inputData[0];
        double a = inputData[1];
        double b = inputData[2];
        double epsilon = inputData[3];
        int n = 4;
        int iterations = 0;
        double h = (b-a) / n;

        int afterComma = digitsAfterPoint(epsilon);
        func = selectFunc(num);
        double result = 0;
        double resultPrev = Double.MAX_VALUE;
        double resultCurr = Double.MIN_VALUE;

        trapeziumMethod(a,
                b,
                epsilon,
                n,
                iterations,
                h,
                afterComma,
                result,
                resultPrev,
                resultCurr);
    }

    private static void trapeziumMethod(double a,
                                        double b,
                                        double epsilon,
                                        int n, int iterations,
                                        double h,
                                        int afterComma,
                                        double result,
                                        double resultPrev,
                                        double resultCurr){
        while (Math.abs(resultCurr - resultPrev)/(Math.pow(2,K) - 1) > epsilon){
            result = h/2*(func.apply(a)+func.apply(b));

            for(int i = 1; i<n; i++){
                result = result + h *func.apply(a+i*h);
            }
            resultPrev = resultCurr;
            resultCurr = result;
            n *=2;
            h = (b-a)/n;

            if (iterations>=1) printStep(iterations, resultCurr, resultPrev, afterComma, epsilon, n);
            iterations++;
        }

        System.out.println("<<RESULT>>");
        printStep(iterations-1, resultCurr, resultPrev, afterComma, epsilon,n );
    }

    private static DoubleFunction<Double> selectFunc(int num) {
        switch (num) {
            case 1:
                return (x) -> 3 * Math.pow(x, 3) + 5 * Math.pow(x, 2) + 3 * x - 6;
            case 2:
                return (x) -> 4 * Math.pow(x, 3) - 5 * Math.pow(x, 2) + 6 * x - 7;
            default:
                return (x) -> 3 * Math.pow(x, 3) - 2 * Math.pow(x, 2) - 7 * x - 8;
        }
    }

    private static void printStep(int iter, double currResult, double prevResult, int afterComma, double epsilon, int n) {
        System.out.println("Iteration " + iter);
        System.out.format("I = %."+afterComma+"f\n" , currResult );
        System.out.format("|I"+iter+"-I"+(iter-1)+"| = %."+afterComma+"f" , Math.abs(currResult - prevResult));
        System.out.format(" <= %."+afterComma+"f" , epsilon );
        System.out.println(" " +(Math.abs(currResult - prevResult)/(Math.pow(2,K) - 1)<=epsilon ? "(true)": "(false)"));
        System.out.println("n = " +n/4);
        System.out.println();
    }

    static int digitsAfterPoint(double epsilon){
        int res = 0;
        while(epsilon*10!=10){
            epsilon *=10;
            res++;
        }
        return res;
    }
}