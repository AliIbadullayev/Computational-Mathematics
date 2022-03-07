import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        IO io = new IO();

        double[] inputData = io.readData(args.length==0 ? "" : args[0]);
        int num = (int) inputData[0];
        double a = inputData[1];
        double b = inputData[2];
        double epsilon = inputData[3];
        double lambda = getLambda(num, a, b);

        // count of iterations
        int n = 0;
        if (num > 3 || num < 1) {
            System.err.println("In program is no such function;");
            System.exit(-1);
        }
        if (!verify(num,lambda, a, b )) {
            System.err.println("This function has no root or has more than one root at this interval!");
            System.exit(-1);
        }
        double x0 = getX0(num, lambda, a, b);
        double x1 = getFiFunc(num, lambda, x0);
        System.out.format("x"+n+" -> %.3f\n" , x0);
        while (Math.abs(x0-x1)>=epsilon){
            n++;

            System.out.println("Iteration " + n);
            System.out.format("x"+n+" -> %.3f\n" , x1);
            System.out.format("f(x"+n+") -> %.3f\n", getFunc(num, x1));
            System.out.format("|x"+n+"-x"+(n-1)+"| -> %.3f\n" , Math.abs(x0-x1));
            System.out.println();
            x0 = x1;
            x1 = getFiFunc(num, lambda, x0);
        }
        n++;

        System.out.println("<<RESULT>>");
        System.out.println("Ends at "+ n +" iterations");
        System.out.format("x"+n+" -> %.3f\n" , x1);
        System.out.format("f(x"+n+") -> %.3f\n", getFunc(num, x1));
        System.out.format("|x"+n+"-x"+(n-1)+"| -> %.3f\n" , Math.abs(x0-x1));
        System.out.println();
    }

    static double getFunc(int num, double x){
        if (num == 1) {
            return -2.7 * Math.pow(x, 3) - 1.48 * Math.pow(x, 2) + 19.23 * x + 6.35;
        } else if (num == 2) {
            return Math.pow(x, 3) - 1.89 * Math.pow(x, 2) - 2 * x + 1.76;
        } else
            return Math.sin(x) - 2 * Math.pow(x, 3) + Math.pow(x, 2);
    }

    static double getDeriveFunc(int num, double x){
        if (num == 1) {
            return -8.1*x*x-2.96*x+19.23;
        } else if (num == 2) {
            return 3*x*x-3.78*x-2;
        } else
            return -6*x*x+2*x+Math.cos(x);
    }

    static double getLambda(int num, double a, double b){
        if (getDeriveFunc(num, a) > getDeriveFunc(num, b)) return -(1/getDeriveFunc(num,a));
        else return -(1/getDeriveFunc(num,b));
    }

    static double getFiFunc(int num, double lambda, double x){
        if (num == 1) {
            return (-2.7 * Math.pow(x, 3) - 1.48 * Math.pow(x, 2) + 19.23 * x + 6.35)*lambda + x;
        } else if (num == 2) {
            return (Math.pow(x, 3) - 1.89 * Math.pow(x, 2) - 2 * x + 1.76)*lambda + x;
        } else
            return (Math.sin(x) - 2 * Math.pow(x, 3) + Math.pow(x, 2))*lambda + x;
    }

    static double getDeriveFiFunc(int num, double lambda, double x){
        if (num == 1) {
            return getDeriveFunc(num, x)*lambda + 1;
        } else if (num == 2) {
            return getDeriveFunc(num, x)*lambda + 1;
        } else
            return getDeriveFunc(2, x)*lambda + 1;
    }
    static double getX0(int num, double lambda, double a, double b){
        if (getDeriveFiFunc(num, lambda, a) > getDeriveFiFunc(num, lambda, a)) return a;
        else return b;
    }

    //true - if all is good verified, else - false
    static boolean verify(int num, double lambda, double a, double b){
        return Math.abs(getDeriveFiFunc(num, lambda, a)) < 1 || Math.abs(getDeriveFiFunc(num, lambda, b)) < 1 && getFunc(num, a) * getFunc(num, b) < 0;
    }
}
