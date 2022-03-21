import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main extends JComponent{
    public static double a;
    public static double b;
    public static double a_temp;
    public static double b_temp;
    public static int num;

    public void paintComponent(Graphics g)
    {
        //w is x, and h is y (as in x/y values in a graph)
        int w = this.getWidth()/2;
        int h = this.getHeight()/2;
        final double MAX_X = Math.max(Math.abs(b_temp), Math.abs(a_temp));
        final double SCALE = w / MAX_X / 2;

        Graphics2D g1 = (Graphics2D) g;
        g1.setStroke(new BasicStroke(2));
        g1.setColor(Color.black);
        g1.drawLine(0,h,w*2,h);
        g1.drawLine(w,0,w,h*2);
        g1.drawString("0", w - 14, h + 20);
        g1.drawString(""+a_temp, w+(int)(SCALE*a_temp), h+20);
        g1.drawString(""+b_temp, w+(int)(SCALE*b_temp), h+20);
        g1.drawOval(w+(int)(SCALE*a_temp), h,3,3);
        g1.drawOval(w+(int)(SCALE*b_temp), h,3,3);


        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.red);
        //line1
        Polygon p = new Polygon();
        for (double x = a_temp; x <= b_temp; x=x+0.01) {
            double xScaled = x * SCALE;
            p.addPoint((int) (w + xScaled),h - ((int)(SCALE/2*getFunc(num, x))));
        }
        g2.drawPolyline(p.xpoints, p.ypoints, p.npoints);
    }

    public static void main(String[] args) throws IOException {
        IO io = new IO();

        double[] inputData = io.readData(args.length==0 ? "" : args[0]);
        num = (int) inputData[0];
        a = inputData[1];
        b = inputData[2];
        a_temp = a;
        b_temp = b;
        double epsilon = inputData[3];
        int afterComma = digitsAfterPoint(epsilon);
        double lambda = getLambda(num, a, b);

        // count of iterations
        int n = 0;
        double x0 = getX0(num, lambda, a, b);
        double x1 = getFiFunc(num, lambda, x0);
        if (num > 3 || num < 1) {
            System.err.println("In program is no such function;");
            System.exit(-1);
        }
        if (!verify(num,lambda, x0)) {
            System.err.println("The function diverges on a given interval!");
            System.exit(-1);
        }
        if (!correctInput(epsilon)){
            System.err.println("Check the correctness of epsilon!");
            System.exit(-1);
        }

        System.out.format("x"+n+" -> %.3f\n" , x0);
        while (Math.abs(x0-x1)>=epsilon){
            n++;
            printStep(afterComma, n, x0, x1);
            x0 = x1;
            x1 = getFiFunc(num, lambda, x0);
        }
        n++;

        System.out.println("<<RESULT>>");
        printStep(afterComma, n, x0, x1);

        drawFunction();
    }

    private static void printStep(int afterComma, int n, double x0, double x1) {
        System.out.println("Iteration " + n);
        System.out.format("x"+n+" -> %."+afterComma+"f\n" , x1);
        System.out.format("f(x"+n+") -> %."+afterComma+"f\n", getFunc(num, x1));
        System.out.format("|x"+n+"-x"+(n-1)+"| -> %."+afterComma+"f\n" , Math.abs(x0-x1));
        System.out.println();
    }

    private static void drawFunction() {
        JFrame frame = new JFrame();
        frame.setSize(800, 600);
        frame.setTitle("Simple Iterations Method");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        Main draw = new Main();
        frame.add(draw);
        frame.setVisible(true);
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
            return (-8.1*x*x)-(2.96*x)+19.23;
        } else if (num == 2) {
            return (3*x*x)-(3.78*x)-2;
        } else
            return (-6*x*x)+(2*x)+Math.cos(x);
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
    static boolean verify(int num, double lambda, double x){
        return Math.abs(getDeriveFiFunc(num, lambda, x)) < 1;
    }

    static boolean correctInput(double epsilon){
        return epsilon < 1&& epsilon>0;
    }

    static int digitsAfterPoint(double epsilon){
        String text = Double.toString(epsilon);
        int integerPlaces = text.indexOf('.');
        return text.length() - integerPlaces - 1;
    }
}