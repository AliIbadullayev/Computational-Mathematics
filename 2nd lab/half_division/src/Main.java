import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main extends JComponent {
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
        // here reading input data into massive of doubles;
        double[] inputData = io.readData(args.length==0 ? "" : args[0]);
        num = (int) inputData[0];
        a = inputData[1];
        b = inputData[2];
        a_temp = a;
        b_temp = b;
        double epsilon = inputData[3];
        int afterComma = digitsAfterPoint(epsilon);


        double x = (a+b)/2;
        // count of iterations
        int n = 0;
        if (num > 3 || num < 1) {
            System.err.println("In program is no such function;");
            System.exit(-1);
        }
        if (!verify(num, a, b )) {
            System.err.println("This function has no root or has more than one root at this interval!");
            System.exit(-1);
        }
        if (!correctInput(epsilon)){
            System.err.println("Check the correctness of epsilon!");
            System.exit(-1);
        }
        while (Math.abs(a-b)>epsilon && Math.abs(getFunc(num, x))>=epsilon){
            if (getFunc(num, a)*getFunc(num, x)>0 ) a = x;
            else b = x;
            n++;
            printStep(afterComma, n, x);
            x = (a+b)/2;
        }
        System.out.println("<<RESULT>>");
        printStep(afterComma, n, x);
        drawFunction();
    }

    private static void printStep(int afterComma, int n, double x) {
        System.out.println("Iteration " + n);
        System.out.format("x -> %."+afterComma+"f\n" , x);
        System.out.format("f(x) -> %."+afterComma+"f\n", getFunc(num, x));
        System.out.format("|a-b| -> %."+afterComma+"f\n" , Math.abs(a-b));
        System.out.println();
    }

    private static void drawFunction() {
        JFrame frame = new JFrame();
        frame.setSize(800, 600);
        frame.setTitle("Half Division Method");
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
        } else if (num == 3) {
            return Math.sin(x) - 2 * Math.pow(x, 3) + Math.pow(x, 2);
        }
        return Double.MAX_VALUE;
    }

    //true - if all is good verified, else - false
    static boolean verify(int num, double a, double b){
        return getFunc(num, a) * getFunc(num, b) < 0;
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
