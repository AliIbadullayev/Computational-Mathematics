import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import java.util.function.DoubleFunction;

public class Main {

    public static IO io = new IO();
    public static void main(String[] args) throws IOException {
        Map<Double, Double> points;
        Map<Integer, Double> thetas = new HashMap<>();
        points = io.readData( args.length==0 ? "" : args[0]);


        double theta = Double.MAX_VALUE;
        int min = Integer.MAX_VALUE;
        int n = points.size();
        double AVG_X;
        double AVG_Y;
        double R;
        double SX = 0;
        double SXX = 0;
        double SXY = 0;
        double SY = 0;
        double SXXX = 0;
        double SXXXX = 0;
        double SXXXXX = 0;
        double SXXXXXX = 0;
        double SXXY = 0;
        double SXXXY = 0;
        double SLN_X = 0;
        double SLN_Y = 0;
        double SLN_XX = 0;
        double SLN_XY = 0;
        double SXLN_Y = 0;
        double SYLN_X = 0;
        for (Double key: points.keySet()){
            if (points.get(key) == 0) continue;
            SX += key;
            SXX += Math.pow(key,2);
            SXXX += Math.pow(key,3);
            SXXXX += Math.pow(key,4);
            SXXXXX += Math.pow(key,5);
            SXXXXXX += Math.pow(key,6);
            SY += points.get(key);
            SXY += key* points.get(key);
            SXXY +=  Math.pow(key,2)* points.get(key);
            SXXXY +=  Math.pow(key,3)* points.get(key);
            SLN_X += Math.log(key);
            SLN_Y += Math.log(points.get(key));
            SLN_XX += Math.pow(Math.log((key)),2);
            SLN_XY += Math.log(key)*Math.log(points.get(key));
            SXLN_Y += key*Math.log(points.get(key));
            SYLN_X += Math.log(key)*points.get(key);
        }
        AVG_X = SX/n;
        AVG_Y = SY/n;

        System.out.format("SX = %.3f\n",SX);
        System.out.format("SXX = %.3f\n",SXX);
        System.out.format("SXXX = %.3f\n",SXXX);
        System.out.format("SXXXX = %.3f\n",SXXXX);
        System.out.format("SXXXXX = %.3f\n",SXXXXX);
        System.out.format("SXXXXXX = %.3f\n",SXXXXXX);
        System.out.format("SY = %.3f\n",SY);
        System.out.format("SXY = %.3f\n",SXY);
        System.out.format("SXXY = %.3f\n",SXXY);
        System.out.format("SXXXY = %.3f\n",SXXXY);
        System.out.format("Sln(X) = %.3f\n",SLN_X);
        System.out.format("Sln(XX) = %.3f\n",SLN_Y);
        System.out.format("Sln(Y) = %.3f\n",SLN_Y);
        System.out.format("Sln(XY) = %.3f\n",SLN_X);
        System.out.println();

        double[] arr1 = {n, SX, SX,SXX,SY, SXY};
        double[] arr2 = {n, SX, SXX,SX, SXX, SXXX, SXX, SXXX, SXXXX, SY, SXY, SXXY};
        double[] arr3 = {n, SX, SXX, SXXX, SX, SXX, SXXX, SXXXX, SXX, SXXX, SXXXX, SXXXXX, SXXX, SXXXX, SXXXXX, SXXXXXX,  SY, SXY, SXXY, SXXXY};
        double[] arr4 = {n, SLN_X, SLN_X,SLN_XX, SLN_Y, SLN_XY};
        double[] arr5 = {n, SX, SX,SXX, SLN_Y, SXLN_Y};
        double[] arr6 = {n, SLN_X, SLN_X,SLN_XX, SY, SYLN_X};

        System.out.println("FIRST APPROXIMATION(linear)");
        double[] x1 = gaussianMethod(arr1, 2);
        DoubleFunction<Double> f_X1 = (x) -> x1[0] + x1[1]*x;
        thetas.put(1,io.showResults(points, f_X1));

        double SX_X_AVG_Y_Y_AVG = 0;
        double SX_X_AVG2 = 0;
        double SY_Y_AVG2 = 0;
        for (double key: points.keySet()){
            SX_X_AVG_Y_Y_AVG += ((key-AVG_X)*(points.get(key)-AVG_Y));
            SX_X_AVG2 += Math.pow(key-AVG_X,2);
            SY_Y_AVG2 += Math.pow(points.get(key)-AVG_Y,2);
        }
        R = SX_X_AVG_Y_Y_AVG/Math.sqrt(SX_X_AVG2*SY_Y_AVG2);
        System.out.format("r = %.3f\n", R); // Pirson coefficient
        io.showResultsLinear(points, f_X1);


        System.out.println("SECOND APPROXIMATION(2nd polynomial)");
        double[] x2 = gaussianMethod(arr2, 3);
        DoubleFunction<Double> f_X2 = (x) -> x2[0] + x2[1]*x + x2[2]*x*x;
        thetas.put(2,io.showResults(points, f_X2));

        System.out.println("THIRD APPROXIMATION(3rd polynomial)");
        double[] x3 = gaussianMethod(arr3, 4);
        DoubleFunction<Double> f_X3 = (x) -> x3[0] + x3[1]*x + x3[2]*x*x + x3[3]*x*x*x;
        thetas.put(3,io.showResults(points, f_X3));

        System.out.println("FOURTH APPROXIMATION(power function)");
        double[] x4 = gaussianMethod(arr4, 2);
        DoubleFunction<Double> f_X4 = (x) -> Math.exp(x4[0])*Math.pow(x,x4[1]);
        thetas.put(4,io.showResults(points, f_X4));

        System.out.println("FIFTH APPROXIMATION(exponential function)");
        double[] x5 = gaussianMethod(arr5, 2);
        DoubleFunction<Double> f_X5 = (x) -> Math.exp(x5[0])*Math.exp(x*x5[1]);
        thetas.put(5,io.showResults(points, f_X5));

        System.out.println("SIXTH APPROXIMATION(logarithm function)");
        double[] x6 = gaussianMethod(arr6, 2);
        DoubleFunction<Double> f_X6 = (x) -> x6[0] + x6[1]*Math.log(x);
        thetas.put(6,io.showResults(points, f_X6));

        for(int tht : thetas.keySet()){
            if (thetas.get(tht)<theta) {
                theta = thetas.get(tht);
                min = tht;
            }
        }
        System.out.print("THE BEST APPROXIMATION FUNCTION IS: ");
        switch (min){
            case 1:
                System.out.println("Linear");
                break;
            case 2:
                System.out.println("Polynom 2nd");
                break;
            case 3:
                System.out.println("Polynom 3rd");
                break;
            case 4:
                System.out.println("Power function");
                break;
            case 5:
                System.out.println("Exponential function");
                break;
            case 6:
                System.out.println("Logarithm function");
                break;
        }
        String x_text = String.valueOf(points.keySet());
        String y_text = String.valueOf(points.values());

        String text = "calculator.setExpression({ id: 'linear', latex: 'f(x)="+x1[1]+"x+"+x1[0]+"' });\n" +
                "calculator.setExpression({ id: 'polynom 2nd', latex: 'g(x)="+x2[2] +"x^2+"+x2[1]+"x+"+x2[0]+"' });\n" +
                "calculator.setExpression({ id: 'polynom 3rd', latex: 'h(x)="+x3[3]+ "x^3 +" + x3[2] +"x^2+"+x3[1]+"x+"+x3[0]+"' });\n" +
                "calculator.setExpression({ id: 'power', latex: 't(x)=e^{"+x4[0]+"}*x^{"+x4[1]+"}' });\n" +
                "calculator.setExpression({ id: 'exp', latex: 'k(x)=e^{"+x5[0]+"}*e^{x*"+x5[1]+"}' });\n" +
                "calculator.setExpression({ id: 'log', latex: 'j(x)=\\ ln(x)*"+x6[1]+ "+"+x6[0]+"' });\n" +
                "\tcalculator.setExpression({\n" +
                "  type: 'table',\n" +
                "  columns: [\n" +
                "    {\n" +
                "      latex: 'x',\n" +
                "      values: "+x_text+",\n" +
                "    },\n" +
                "    {\n" +
                "      latex: 'y',\n" +
                "      values: "+y_text+",\n" +
                "      color: Desmos.Colors.BLUE,\n" +
                "      columnMode: Desmos.ColumnModes.POINTS\n" +
                "    }\n" +
                "  ]\n" +
                "});\n" +
                "  </script>\n" +
                "</body>\n" +
                "</html>";

        RandomAccessFile accessFile = new RandomAccessFile(new File("index.html"), "rw");
        accessFile.seek(366);
        accessFile.write(text.getBytes());

        File htmlFile = new File("index.html");
        Desktop.getDesktop().browse(htmlFile.toURI());
    }

    public static double[] gaussianMethod(double [] arr_res, int n){
        double[] x = new double[n];
        double [][] arr = new double[n][n];
        double[] b = new double[n];
        int stop = 0;
        double c;
        double s;


        // Fulling matrix by result array which has got from data input
        for (int i = 0; i < n; i++){
            for (int k = 0; k < n; k++){
                arr[i][k] = arr_res[k+(i*n)];
            }
            b[i] = arr_res[(n*n)+i];
        }
        double [][] arr_copy = new double[n][n];
        double [] b_copy = new double[n];

        for (int i = 0; i < n; i++) {
            System.arraycopy(arr[i], 0, arr_copy[i], 0, arr[i].length);
        }

        System.arraycopy(b, 0, b_copy, 0, arr.length);

        System.out.println("\t<<INPUT MATRIX>>");
        io.showExtendedMatrix(arr, b);

        // straight stroke (process of exception an elements of matrix to get triangle-matrix)
        for(int i = 0; i < n - 1 ; i++){
            while (arr[i][i] == 0  ){
                if (stop == n - i) {
                    System.err.println("The error in swapping lines!");
                    System.exit(0);
                }
                swapLines(arr, b,  i);
                stop++;
            }

            for (int k = i+1; k < n; k++){
                c = arr[k][i] / arr[i][i];
                arr[k][i] = 0;
                for (int j = i+1; j < n; j++){
                    arr[k][j] = arr[k][j] - c * arr[i][j];
                }
                b[k] = b[k] - c * b[i];
            }
            stop = 0;
            System.out.println();
            System.out.println("\t<<Iteration ???" + (i+1)+">>");
            io.showExtendedMatrix(arr, b);
        }


        // reverse stroke (process of getting an unknown variables)
        for (int i = n - 1 ; i >= 0 ; i--){
            s = 0;
            for (int j = i+1; j<n; j++){
                if (Double.isNaN(x[j])) s=s+0;
                else s = s + arr[i][j]*x[j];
            }
            if (Double.isInfinite(s)) {
                System.err.println("This system has no solution!\nBecause: "+"0!="+b[i+1]);
                System.exit(-1);
            }
            x[i] = (b[i] - s)/arr[i][i] == -0 ? 0:(b[i] - s)/arr[i][i] ;
        }
        System.out.println("_____________________________________________________");
        System.out.print("X =\t");
        System.out.println(Arrays.toString(x));

        return x;
    }

    private static void swapLines(double[][] ints, double[] b, int pos) {
        double[] temp = ints[pos];
        double tempB = b[pos];
        if (ints.length - 1 - pos >= 0) {
            System.arraycopy(ints, pos + 1, ints, pos, ints.length - 1 - pos);
        }
        if (b.length - 1 - pos >= 0) System.arraycopy(b, pos + 1, b, pos, b.length - 1 - pos);
        b[b.length-1] = tempB;
        ints[ints.length-1] = temp;
    }
}

