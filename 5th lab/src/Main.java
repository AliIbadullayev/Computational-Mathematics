import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import java.util.function.DoubleFunction;

public class Main {

    public static IO io = new IO();
    public static void main(String[] args) throws IOException {
        SortedMap<Double, Double> points;
        Map<Integer, Double> thetas = new HashMap<>();
        points = io.readData();

        double x = points.get(Double.MAX_VALUE);
        points.remove(Double.MAX_VALUE);
        Double[] xs = points.keySet().toArray(new Double[0]);
        Double[] ys = points.values().toArray(new Double[0]);
        int n = points.size();
        double result;
        StringBuilder formula1 = new StringBuilder();
        StringBuilder formula2 = new StringBuilder();
        result = methodLaGranje(n,x,xs,ys,formula1);
        System.out.format("Результат интерполирования для метода Лагранжа: %.5f\n", result);
        result = methodNewton(n, x, xs, ys, formula2);
        System.out.format("Результат интерполирования для метода Ньютона: %.5f\n", result);
        formula1.deleteCharAt(formula1.length()-1);
        formula2.deleteCharAt(formula2.length()-1);


        String x_text = String.valueOf(points.keySet());
        String y_text = String.valueOf(points.values());

        String text = "calculator.setExpression({ id: 'f', latex: 'f(x)="+formula1+"' });\n" +
                "calculator.setExpression({ id: 'g', latex: 'g(x)="+formula2+"' });\n" +
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

    private static double methodNewton(int n, double x, Double[] xs, Double[] ys, StringBuilder str) {
        double result = 0;
        double sum = 1;
        double[][] matrix = new double[n][n];
        double t;
        // fulling the matrix with coefficients
        for (int i = 0 ; i < n ; i++) {
            for (int j = 0 ; j < n - i; j++){
                if (i == 0) matrix[i][j] = ys[j];
                else {
                    matrix[i][j] = matrix[i-1][j+1] - matrix[i-1][j];
                }
            }
        }
        int pos = 0;
        //find position in array
        for (int i = 0; i < n; i++ ){
            if (xs[i]<x) pos=i;
            else break;
        }
        //brain of method
        if (pos < xs.length/2) {
            t = (x - xs[pos])/(xs[1]-xs[0]);
            for (int j = 0 ; j < n - pos; j++){
                if (j == 0) {
                    result = matrix[0][pos];
                    continue;
                }
                sum *= matrix[j][pos];
                for (int k = 0; k < j; k++){
                    sum *= t - k;
                }
                sum /= findFactorial(j);
                result += sum;
                sum = 1;
            }
        } else {
            t = (x - xs[n-1])/(xs[1]-xs[0]);
            for (int j = 0 ; j < n; j++){
                if (j == 0) {
                    result = matrix[0][n-1];
                    continue;
                }
                sum *= matrix[j][n-j-1];
                for (int k = 0; k < j; k++){
                    sum *= t + k;
                }
                sum /= findFactorial(j);
                result += sum;
                sum = 1;
            }
        }
        // writing the formula
        double h = xs[1] - xs[0];
        for (int j = 0 ; j < n ; j++){
            if (j == 0) {
                str.append(matrix[0][0]).append(" + ");
                continue;
            }
            for (int k = 0; k < j; k++){
                str.append("(x-").append(xs[k]).append(")");
            }
            str.append("(").append(matrix[j][0]/findFactorial(j)/Math.pow(h,j)).append(")+");
        }
        return result;
    }

    private static double methodLaGranje(int n, double x, Double[] xs, Double[] ys, StringBuilder str) {
        double sum = 1;
        double result = 0;
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                if (j == i ) continue;
                sum *= (x - xs[j])/(xs[i] - xs[j]);
                str.append("(x-(").append(xs[j]).append("))/(").append(xs[i] - xs[j]).append(")*");
            }

            str.append(ys[i]).append("+");
            sum = sum * ys[i];
            result += sum;
            sum=1;
        }
        return result;
    }

    private static double findFactorial(double x){
        double result = 1;
        for (int i = 1; i <= x; i++){
            result *= i;
        }
        return result;
    }
}

