import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        double[] arr = new double[]{
                20, 26, 32, 34, 26, 28, 22, 30, 17, 24,
                30, 28, 18, 22, 24, 26, 34, 28, 22, 20,
                34, 24, 28, 20, 32, 17, 22, 24, 26, 30,
                30, 22, 26, 35, 28, 24, 30, 32, 28, 18,
                20, 30, 17, 24, 32, 28, 22, 26, 24, 30,
                34, 26, 24, 28, 22, 30, 35, 32, 20, 17,
                28, 22, 36, 30, 20, 26, 28, 23, 24, 32,
                20, 26, 30, 24, 32, 17, 22, 28, 35, 26,
                28, 35, 32, 22, 26, 24, 26, 24, 30, 24,
                18, 24, 26, 28, 35, 30, 26, 22, 26, 28
        };
        int n = arr.length;
        double min;
        double max;
        double range;
        double x_median = 0;
        double dispersion = 0;

        Arrays.sort(arr);
        System.out.println("Вариационный ряд\n" +Arrays.toString(arr) );

        min = arr[0];
        max = arr[n-1];
        range = max - min;

        SortedMap<Double, Integer> map = new TreeMap<>();
        for (double v : arr) {
            if (map.containsKey(v)) map.put(v, map.get(v) + 1);
            else map.put(v, 1);
        }

        for (double v : arr) {
            x_median += v;
        }
        x_median = x_median/n;
        Map<Double, Double> fx = new TreeMap<>();
        fx.put(0d,  map.firstKey());
        int count = 1;
        int summ_n = 0;

        for (Map.Entry<Double, Integer> v : map.entrySet()) {
            dispersion = (v.getKey() - x_median)*(v.getKey() - x_median)* v.getValue();
            summ_n += v.getValue();
            fx.put((double) summ_n/n, v.getKey());
            count ++;
        }
        dispersion = dispersion / n;
        double h = range/9;

        System.out.println("Xmin = " + min );
        System.out.println("Xmax = " + max);
        System.out.println("Range = " + range + " Размах");
        System.out.println("h = " + h);
        System.out.println("MX = " + x_median);
        System.out.println("sigma = " + Math.sqrt(dispersion) + " Среднеквадратичное отклонение");

        StringBuilder stringBuilder = new StringBuilder();
        count = 0;
        double previous = map.firstKey();
        for (Map.Entry<Double, Double> v : fx.entrySet()){
            if (count == 0) {
                stringBuilder.append("calculator.setExpression({ id: 'func_").append(count).append("', latex: '").append(v.getKey()).append("\\").append("\\{x<").append(previous).append("\\").append("\\").append("}', color: Desmos.Colors.BLACK});\n");
                count++;
                continue;
            }
            stringBuilder.append("calculator.setExpression({ id: 'func_").append(count).append("', latex: '").append(v.getKey()).append("\\").append("\\{").append(previous).append("<x<=").append(v.getValue()).append("\\").append("\\").append("}', color: Desmos.Colors.BLACK });\n");
            previous = v.getValue();
            count++;
        }
        // unique values
        Double[] values = map.keySet().toArray(new Double[0]);
        // interval values
        Double[] values_h = new Double[(int) ((int)(arr[n-1] - arr[0]+h)/h)+1];
        // amount of values in interval
        int[] polygon_summs = new int[values_h.length-1];


        count = 0;
        for (double i = arr[0]; i < arr[n-1] +h/2 ; i = i + h) {
            values_h[count] = i;
            count++;
        }

        count = 0;


        int summ = 0;
        for (int i = 0; i < values_h.length-1; i++){
            for (double j = values[count]; j <=values_h[i+1]; j=values[count]){
                summ += map.get(j);
                count++;
                if (count == values.length ) break;
            }
            polygon_summs[i] = summ;
            summ = 0;
            if (polygon_summs.length -1 == i) break;
        }
        double[] polygon_values = new double[values_h.length-1];
        for (int i = 0; i < polygon_values.length; i++) {
            polygon_values[i] = (values_h[i] + h/2) ;
        }

        double[] polygon_frequency = new double[polygon_values.length];
        double[] polygon_density = new double[polygon_values.length];

        for (int i = 0; i < values_h.length-1; i++) {
            polygon_frequency[i] = (double) polygon_summs[i]/n;
            polygon_density[i] = polygon_frequency[i]/h;
        }

        System.out.println("\n\tИнтервальный статистический ряд");
        System.out.println("\tИнтервал\tЧастота\t   Частость\tПлотность частости");
        for (int i = 0; i < values_h.length - 1; i++) {
            System.out.format("(%.2f;%.2f]\t\t%s\t\t%.2f\t\t%.2f\n", values_h[i], values_h[i+1],polygon_summs[i], (double)polygon_summs[i]/n, (double)polygon_summs[i]/n/h);
        }

        summ_n = 0;


        // This part is unique for this task
        fx.clear();
        fx.put(0d, values_h[0]);
        x_median = 0;
        for (int i = 1; i < 9; i++) {
            summ_n += polygon_summs[i];
            fx.put((double) summ_n / n, values_h[i]);
        }
        dispersion=0;
        for (int i = 0; i < 9; i++) {
            x_median +=values_h[i]*polygon_summs[i];
            dispersion +=values_h[i]*values_h[i]*polygon_summs[i];
//            System.out.println(values_h[i]*values_h[i]);
            System.out.println(polygon_values[i]);
        }
//        System.out.println(x_median);
        x_median = x_median/n;
//        System.out.println(x_median);
        dispersion = dispersion/n - x_median*x_median;
        System.out.println(dispersion);
        System.out.println(Math.sqrt(dispersion));
        //

        stringBuilder.append("\tcalculator.setExpression({\n" + "  type: 'table',\n" + "  columns: [\n" + "    {\n" + "      latex: 'x',\n" + "      values: ").append(Arrays.toString(values_h)).append(",\n").append("    },\n").append("    {\n").append("      latex: 'y',\n").append("      values: ").append(Arrays.toString(fx.keySet().toArray(new Double[0]))).append(",\n").append("      color: Desmos.Colors.BLACK,\n").append("      columnMode: Desmos.ColumnModes.LINES\n").append("    }\n").append("  ]\n").append("});\n");
        stringBuilder.append("calculator.setExpression({ id: 'a', latex: 'a=").append(Arrays.toString(polygon_density)).append("', color: Desmos.Colors.BLUE});\n");
        stringBuilder.append("calculator.setExpression({ id: 'b', latex: 'b=").append(Arrays.toString(values_h)).append("', color: Desmos.Colors.BLUE});\n");
        stringBuilder.append("calculator.setExpression({ id: 'area_y").append(count).append("', latex: '0<= y<= a\\\\{b<=x<=b+").append(h).append("\\\\}', color: Desmos.Colors.BLUE});\n");
        stringBuilder.append("calculator.setExpression({ id: 'area_x").append(count).append("', latex: 'b<=x<=b+").append(h).append("\\\\{0<=y<=a\\\\}', color: Desmos.Colors.BLUE});\n");
        stringBuilder.append("  </script>\n" +
                "</body>\n" +
                "</html>");

        RandomAccessFile accessFile = new RandomAccessFile(new File("index.html"), "rw");
        accessFile.seek(366);
        accessFile.write(stringBuilder.toString().getBytes());

        File htmlFile = new File("index.html");
//        Desktop.getDesktop().browse(htmlFile.toURI());
    }
}
