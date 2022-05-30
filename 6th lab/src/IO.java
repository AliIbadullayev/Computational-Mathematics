import java.io.*;
import java.util.*;

public class IO {
    public double[] readData() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));;
        BufferedReader fr;
        // temporary array to split string with points
        String[] result;
        double[] data;
        System.out.println("Программа для подсчета интерполяции 2-мя методами: Эйлера, Милна");
        System.out.println("Правило ввода аргументов: <a> <b> <h> <y0> <epsilon>");
        System.out.println("Введите способ ввода данных: 1)файл, 2) командная строка");
        int ввод = Integer.parseInt((br.readLine()));
        if (ввод==1) {
            System.out.print("Введите название файла: " );
            String args = br.readLine();
            System.out.println();
            File file = new File(args);
            if (!file.canRead()) System.err.println("Cannot open file!\nPlease check correctness of input file!");
            fr = new BufferedReader(new FileReader(file));
            result  = fr.readLine().split(" ");
            data = Arrays.stream(result)
                    .mapToDouble(Double::parseDouble)
                    .toArray();

        } else {
            System.out.println("You hasn't select input file.\nPlease, enter input data \nn - count of lines of system, \ncoordinates of points \n 1 2\n2.5 3\n...");
            double n = Integer.parseInt(br.readLine());
            double x = Double.parseDouble(br.readLine());
            result  = br.readLine().split(" ");
            data = Arrays.stream(result)
                    .mapToDouble(Double::parseDouble)
                    .toArray();
        }
        if (!verify(data[0], data[1], data[2], data[4])){
            return null;
        }
        return data;
    }

    boolean verify (double a, double b, double h, double epsilon){
        if (b-a < h ) {
            System.err.println("`h` can't be bigger than interval");
            return false;
        }
        if (epsilon > 1 || epsilon < 0) {
            System.err.println("epsilon isn't correct");
            return false;
        }
        return true;
    }
}
