import java.io.*;
import java.util.*;
import java.util.function.DoubleFunction;

public class IO {
    public SortedMap<Double, Double> readData() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));;
        BufferedReader fr;
        // temporary array to split string with points
        String[] result;
        // result array
        SortedMap<Double, Double> points = new TreeMap<>();
        System.out.println("Программа для подсчета интерполяции 2-мя методами: Лагранжа, Ньютона");
        System.out.println("Исходные данные задаются в виде: 1) набора данных (таблицы x,y), 2) на основе выбранной функции (например, x^2.");
        int вид = Integer.parseInt(br.readLine());
            System.out.println("Учтите, что интервалы между x-ами должны быть одинаковыми! (Для метода Ньютона обязательно!)");
            if (вид == 2 ) {
                System.out.println("Правило ввода данных: \nn(amount of points)\nx(to find)\nx0\nx1\n...\nxn");
            } else {
                System.out.println("Правило ввода данных: \nn(amount of points)\nx(to find)\nx0 y0\nx1 y1\n...\nxn yn");
            }

        System.out.println("Введите способ ввода данных: 1)файл, 2) командная строка");
        int ввод = Integer.parseInt((br.readLine()));
        if (ввод==1) {
            System.out.print("Введите название файла: " );
            String args = br.readLine();
            System.out.println();
            File file = new File(args);
            if (!file.canRead()) System.err.println("Cannot open file!\nPlease check correctness of input file!");
            fr = new BufferedReader(new FileReader(file));
            double n = Integer.parseInt(fr.readLine());
            double x = Double.parseDouble(fr.readLine());
            points.put(Double.MAX_VALUE, x);
            if (вид == 2 ) {
                for (int i = 0; i < n; i++){
                    x = Double.parseDouble(fr.readLine());
                    points.put(x, getX2Value(x));
                }
            }
            else {
                for (int i = 0; i < n ; i++){
                    result = fr.readLine().split(" ");
                    points.put(Double.parseDouble(result[0]), Double.parseDouble(result[1]));
                }
            }
        } else {
            System.out.println("You hasn't select input file.\nPlease, enter input data \nn - count of lines of system, \ncoordinates of points \n 1 2\n2.5 3\n...");
            double n = Integer.parseInt(br.readLine());
            double x = Double.parseDouble(br.readLine());
            points.put(Double.MAX_VALUE, x);
            if (вид == 2 ) {
                for (int i = 0; i < n; i++){
                    x = Double.parseDouble(br.readLine());
                    points.put(x, getX2Value(x));
                }
            }
            else {
                for (int i = 0; i < n ; i++){
                    result = br.readLine().split(" ");
                    points.put(Double.parseDouble(result[0]), Double.parseDouble(result[1]));
                }
            }
        }
        return points;
    }

    double getX2Value(double x){
        return x*x;
    }
}
