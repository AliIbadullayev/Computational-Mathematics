import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;


public class Main {
    final static int ТОЧНОСТЬ_ЭЙЛЕР = 1;
    final static int ТОЧНОСТЬ_МИЛНА = 4;
    public static void main(String[] args) throws IOException {
        RandomAccessFile accessFile = new RandomAccessFile(new File("index.html"), "rw");
        IO io = new IO();
        //<a> <b> <h> <y0> <epsilon>
        double[] input_data = io.readData();
        if (input_data == null) {
            System.err.println("Input Error!");
            System.exit(-1);
        }

        double a = input_data[0];
        double b = input_data[1];
        double h = input_data[2];
        double y0 = input_data[3];
        double epsilon = input_data[4];
        int after_point = digitsAfterPoint(epsilon);
        eilerMethod(a,b,y0,h,after_point,epsilon,accessFile);
        milnMethod(a,b,y0,h,after_point,epsilon,accessFile);
        String text =
                "  </script>\n" +
                "</body>\n" +
                "</html>";
        accessFile.seek(accessFile.length());
        accessFile.write(text.getBytes());

        File htmlFile = new File("index.html");
        Desktop.getDesktop().browse(htmlFile.toURI());

    }

    private static void milnMethod(double a, double b, double y0, double h, int after_point, double epsilon, RandomAccessFile accessFile) throws IOException {
        System.out.println("Miln Method");
        ArrayList<ArrayList<Double>> pointsMiln = new ArrayList<ArrayList<Double>>(2);

        int n = (int) ((b - a) / h);
        System.out.println("h = " + h);
        System.out.format("%."+after_point+"f >= %."+after_point+"f\n", Math.abs(forRunge(a, y0, h, 1) - forRunge(a,y0,2*h,1))/(Math.pow(2,ТОЧНОСТЬ_МИЛНА)-1), epsilon);
        System.out.println("x \t\t y \t\t f(x,y)");
        pointsMiln = getKoshiSolution(a,y0,h,3, after_point);
        ArrayList<Double> y = pointsMiln.get(0);
        ArrayList<Double> y1 = pointsMiln.get(1);
        ArrayList<Double> x = pointsMiln.get(2);
        double temp_x = a + 3*h;
        double A = 0;
        double B = 0;
        for (int i = 3; i < n; i++){
            // predict
            y.add(i+1, y.get(i-3) + 4d/3d * h * (2 * y1.get(i) - y1.get(i-1) + 2 * y1.get(i-2)));
            temp_x += h;
            x.add(i+1, temp_x);
            B = y.get(i+1);
            // correct
            while (Math.abs(A-B) >= epsilon){
                A = B;
                y1.add(i+1, func(x.get(i+1), A));
                B = y.get(i-1)+h*(y1.get(i+1)+4*y1.get(i)+y1.get(i-1))/3;
            }
            y.add(i+1, B);
            System.out.format(new StringBuilder().append("%.").append(after_point).append("f \t %.").append(after_point).append("f\t %.").append(after_point).append("f\n").toString(), temp_x, y.get(i), func(temp_x, y.get(i)));
        }
//        h /= 2;
        System.out.println("--------------------------");

        String x_text_1 = Arrays.toString(pointsMiln.get(2).toArray());
        String y_text_1 = Arrays.toString(pointsMiln.get(1).toArray());

        String text = "calculator.setExpression({ id: 'f', latex: 'cos(x)' });\n" +
                "\tcalculator.setExpression({\n" +
                "  type: 'table',\n" +
                "  columns: [\n" +
                "    {\n" +
                "      latex: 'x',\n" +
                "      values: "+x_text_1+",\n" +
                "    },\n" +
                "    {\n" +
                "      latex: 'y',\n" +
                "      values: "+y_text_1+",\n" +
                "      color: Desmos.Colors.BLUE,\n" +
                "      columnMode: Desmos.ColumnModes.POINTS\n" +
                "    }\n" +
                "  ]\n" +
                "});\n";
        accessFile.seek(accessFile.length());
        accessFile.write(text.getBytes());
    }

    private static void eilerMethod(double a, double b, double y0, double h, int after_point, double epsilon, RandomAccessFile accessFile) throws IOException {
        System.out.println("Eiler method");
        ArrayList<Double> y = new ArrayList<>();
        ArrayList<Double> y1 = new ArrayList<>();
        ArrayList<Double> x = new ArrayList<>();
        while (Math.abs(forRunge(a, y0, h, 1) - forRunge(a,y0,2*h,1))/(Math.pow(2,ТОЧНОСТЬ_ЭЙЛЕР)-1) >= epsilon) {
            int n = (int) ((b - a) / h);
            double temp_y = y0;
            double temp_x = a;
            System.out.println("h = " + h);

            y = new ArrayList<>();
            y1 = new ArrayList<>();
            x = new ArrayList<>();

            System.out.format("%."+after_point+"f >= %."+after_point+"f\n", Math.abs(forRunge(a, y0, h, 1) - forRunge(a,y0,2*h,1))/(Math.pow(2,ТОЧНОСТЬ_ЭЙЛЕР)-1), epsilon);
            System.out.println("x \t\t y \t\t f(x,y)");
            for (int i = 0; i <= n; i++) {
                x.add(temp_x);
                y.add(temp_y);
                y1.add(func(temp_x, temp_y));
                System.out.format(new StringBuilder().append("%.").append(after_point).append("f \t %.").append(after_point).append("f\t %.").append(after_point).append("f\n").toString(), temp_x, temp_y, func(temp_x, temp_y));
                temp_y = temp_y + h * func(temp_x, temp_y);
                temp_x = temp_x + h;
            }
            h=h/2;
            System.out.println("--------------------------");
        }
        String x_text_1 = Arrays.toString(x.toArray());
        String y_text_1 = Arrays.toString(y1.toArray());

        String text =
                "\tcalculator.setExpression({\n" +
                        "  type: 'table',\n" +
                        "  columns: [\n" +
                        "    {\n" +
                        "      latex: 'x',\n" +
                        "      values: "+x_text_1+",\n" +
                        "    },\n" +
                        "    {\n" +
                        "      latex: 'y',\n" +
                        "      values: "+y_text_1+",\n" +
                        "      color: Desmos.Colors.RED,\n" +
                        "      columnMode: Desmos.ColumnModes.LINES\n" +
                        "    }\n" +
                        "  ]\n" +
                        "});\n";
        accessFile.seek(accessFile.length());
        accessFile.write(text.getBytes());
    }

    private static double func(double x, double y){
        return Math.cos(x);
    }

    private static double forRunge(double x, double y, double h, int n) {
        double temp_y = y;
        double temp_x = x;
        for (int i = 0 ; i <= n ; i++){
            temp_y = temp_y + h*func(temp_x,temp_y);
            temp_x = temp_x + h;
        }
        return temp_y;
    }
    private static ArrayList<ArrayList<Double>> getKoshiSolution(double x, double y, double h, int n, int after_point) {
        ArrayList<ArrayList<Double>> points = new ArrayList<ArrayList<Double>>(2);
        double temp_y = y;
        double temp_x = x;
        ArrayList<Double> yList = new ArrayList<>();
        ArrayList<Double> fList = new ArrayList<>();
        ArrayList<Double> xList = new ArrayList<>();
        for (int i = 0 ; i <= n ; i++){
            yList.add(temp_y);
            fList.add(func(temp_x,temp_y));
            xList.add(temp_x);
            System.out.format(new StringBuilder().append("%.").append(after_point).append("f \t %.").append(after_point).append("f\t %.").append(after_point).append("f\n").toString(), temp_x, temp_y, func(temp_x, temp_y));
            temp_y = temp_y + h*func(temp_x,temp_y);
            temp_x = temp_x + h;
        }
        points.add(0, yList);
        points.add(1, fList);
        points.add(2, xList);

        return points;
    }

    private static int digitsAfterPoint(double epsilon){
        int res = 0;
        while(epsilon*10!=10){
            epsilon *=10;
            res++;
        }
        return res;
    }
}
