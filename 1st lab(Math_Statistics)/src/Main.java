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
                0.9, 0.24, 0.55, -1.45, 0.17, -0.56, 1.45, 0.86, -0.22, -0.91,
                -1, 0.62, -1.45, -0.52, -1.31, 1.45, 0.54, -1.73, -0.64, 1.45
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
            if (v == arr[0]){
                map.put(v, 1);
                continue;
            }
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
        double h = range/(1+3.332*Math.log10(n));

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

        Double[] values = map.keySet().toArray(new Double[0]);
        Double[] values_h = new Double[(int) ((int)(arr[n-1] - arr[0]+h)/h)+2];
        int[] polygon_summs = new int[values_h.length-1];
        values_h[0] = map.firstKey()-h/2;

        count = 0;
        for (double i = arr[0]-h/2; i < arr[n-1] +h/2 ; i = i + h) {
            values_h[count] = i;
            count++;
        }

        count = 0;


        int summ = 0;
        for (int i = 0; i < values_h.length-1; i++){
            for (double j = values[count]; j <values_h[i+1]; j=values[count]){
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

        double[] polygon_density = new double[polygon_values.length];

        for (int i = 0; i < values_h.length-1; i++) {
            polygon_density[i] = (double) polygon_summs[i]/n;
        }

        System.out.println("\n\tИнтервальный статистический ряд");
        System.out.println("\tИнтервал\tЧастота\t   Частость");
        for (int i = 0; i < values_h.length - 1; i++) {
            System.out.format("(%.2f;%.2f]\t\t%s\t\t%.2f\n", values_h[i], values_h[i+1],polygon_summs[i], (double)polygon_summs[i]/n);
        }

        stringBuilder.append("\tcalculator.setExpression({\n" + "  type: 'table',\n" + "  columns: [\n" + "    {\n" + "      latex: 'x',\n" + "      values: ").append(Arrays.toString(polygon_values)).append(",\n").append("    },\n").append("    {\n").append("      latex: 'y',\n").append("      values: ").append(Arrays.toString(polygon_density)).append(",\n").append("      color: Desmos.Colors.BLACK,\n").append("      columnMode: Desmos.ColumnModes.LINES\n").append("    }\n").append("  ]\n").append("});\n");
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
        Desktop.getDesktop().browse(htmlFile.toURI());
    }
}
