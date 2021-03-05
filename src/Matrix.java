import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.StringTokenizer;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class Matrix {
    private int size = 20;
    private double[][] matA = new double[size][size];
    private double[] matB = new double[size];
    private double[] roots = new double[size];
    private double[][] matSavedA;
    private double[] matSavedB;

    public void methodGaussian(Matrix matrix) {

        int n = matrix.getSize();
        double matA[][] = matrix.getMatA();
        double matB[] = matrix.getMatB();
        System.out.println();
        System.out.println("Чтож, выполняем метод Гаусса:");
        System.out.println();
        int i = 0;
        int swapK = 0;//число перестановок строк

        System.out.println("Найдем максимальный элемент в i-ом столбце");
        for (int j = 0; j <= size; j++) {
            for (int k = 0; k <= size; k++) System.out.print(String.format("|%20.5f", matA[j][k]));
            System.out.println(String.format("|  {%20.5f}", matB[j]));
            System.out.println();
        }

        while (i < n) { // ОСНОВНОЙ ЦИКЛ
            double L = matA[i][i];
            double LB = matB[i];
            boolean zeroFlag = false;
            double max = L;
            int maxI = i;
            for (int j = i; j <= n; j++) { // 1-ЦИКЛ - ищем макс. элемент в i-ом столбце
                double M = matA[j][i];
                if (M > max) {
                    max = M;
                    maxI = j;
                }
            }
            for (int k = 0; k <= n; k++) { // меняем строку макс. элемента с i-ой строкой
                double tmpL = matA[i][k];
                matA[i][k] = matA[maxI][k];
                matA[maxI][k] = tmpL;
            }
            L = matA[i][i]; // обновили L
            matB[i] = matB[maxI]; //тоже самое сделали с правой частью
            matB[maxI] = LB;
            LB = matB[i];
            swapK++;

            System.out.println("Нашли максимальный элемент и переставили строки (или занулили столбец) : ");
            for (int j = 0; j <= size; j++) {
                for (int k = 0; k <= size; k++) System.out.print(String.format("|%20.5f", matA[j][k]));
                System.out.println(String.format("|  {%20.5f}", matB[j]));
                System.out.println();
            }

            if (max == 0) {//если макс = 0 , то зануляем его и возращаемся на начало основного цикла
                for (int j = i; j <= n; j++) {
                    matA[j][i] = 0;
                    i++;
                    continue;
                }
            }

            //3-ЦИКЛ теперь надо занулить все что ниже L - по заверщении основного цикла - привести к треугольному ввиду
            for (int j = i + 1; j <= n; j++) {
                double C = matA[j][i] / matA[i][i]; // коэффициент, что будем использовать для вычитания строк
                //цикл для приведения строк в двумерном массиве А :
                for (int k = i; k <= n; k++) {
                    matA[j][k] = matA[j][k] - matA[i][k] * C;
                }
                //осталось вычесть в массиве B ( в правой части ):
                matB[j] = matB[j] - matB[i] * C;
                System.out.println("Приводим матрицу к треугольному виду:");
                for (int o = 0; o <= size; o++) {
                    for (int k = 0; k <= size; k++) System.out.print(String.format("|%20.5f", matA[o][k]));
                    System.out.println(String.format("|  {%20.5f}", matB[o]));
                    System.out.println();
                }
            }
            System.out.println(" . . . ");
            System.out.println();
            i++;
        } // завершаем основной цикл

        double compositionI = 1;// произведение диагональных элементов
        for (i = 0; i <= n; i++) {
            compositionI = compositionI * matA[i][i];
        }
        double detA = pow((-1), swapK) * compositionI;
        System.out.println("***1.Определитель матрицы:***");
        System.out.println(String.format("%1.3f", detA));
        System.out.println();
        System.out.println("***2.Матрица была привидена к треугольному виду:***");
        for (int j = 0; j <= size; j++) {
            for (int k = 0; k <= size; k++) System.out.print(String.format("|%20.5f", matA[j][k]));
            System.out.println(String.format("|  {%20.5f}", matB[j]));
            System.out.println();
        }
        // теперь найдем решения - обратный ход
        System.out.println("***3.Векторы неизвестных:***");
        for (int k = n; k >= 0; k--) {
            double rightSide = matB[k];
            for (int p = n; p > k; p--) { // меньше K - чтобы строка с одним элементам не заходила !
                rightSide -= matA[k][p] * roots[p];
            }

            if (abs(rightSide) < 9.999999999E-10) {
                int number = k + 1;
                roots[k] = 0;
                continue;
            }
            if (matA[k][k] != 0 || abs(matA[k][k]) > 9.999999999E-10) roots[k] = rightSide / matA[k][k];
            else {
                int number = k + 1;
                roots[k] = 0;
            }
        }
        for (int k = 0; k <= n; k++) {
            int numberVector = k + 1;
            System.out.println("Вектор неизвестных X" + "[ " + numberVector + " ]" + "=" + String.format("%1.3f", roots[k]));
        }

        System.out.println();
        System.out.println("***4.Векторы невязок:***");
        double sumA = 0;
        for (i = 0; i <= n; i++) {
            double vectorNev = matSavedB[i];
            for (int k = 0; k <= n; k++) {
                vectorNev -= matSavedA[i][k] * roots[k];
            }
            int numberVector = i + 1;
            System.out.println("Вектор невязки r" + numberVector + "=" + vectorNev);
        }
        System.out.println("( ｡･_･｡)人(｡･_･｡ )");
    }


    public Matrix inputMatrix(String arg) throws Exception {
        System.out.println("Выберете ввод матрицы . Если клавиатура - введите 1, файл - 0.");
        boolean trueAnswer = true;
        while (trueAnswer) {
            try {
                Scanner in = new Scanner(System.in);
                String answer = in.nextLine();
                if (answer.equals("1")) {
                    System.out.println("Введите размерность матрицы n (n<=20)");
                    String n = in.nextLine();
                    size = Integer.parseInt(n) - 1;
                    if (size <= 20) {
                        for (int j = 0; j <= size; j++) {
                            int number = j + 1;
                            System.out.println("Введите " + number + "-ую строку матрицы A - числа через пробел");
                            n = in.nextLine();
                            StringTokenizer stringTokenizer = new StringTokenizer(n, " ");
                            int i = 0;
                            while (stringTokenizer.hasMoreTokens() && i <= size) {
                                matA[j][i] = Double.parseDouble(stringTokenizer.nextToken());
                                i++;
                            }
                            System.out.println("Введите b" + number);
                            matB[j] = Double.parseDouble(n = in.nextLine());
                        }
                        System.out.println("Вы ввели матрицу:");
                        for (int j = 0; j <= size; j++) {
                            for (int k = 0; k <= size; k++) System.out.print(String.format("|%10.3f", matA[j][k]));
                            System.out.println(String.format("|  {%10.3f}", matB[j]));
                            System.out.println();
                        }
                        System.out.println("ヘ( ^o^)ノ＼(^_^ )");
                        trueAnswer = false;
                    } else break;
                } else if (answer.equals("0")) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(arg)));
                    String inp;
                    inp = reader.readLine();
                    size = Integer.parseInt(inp) - 1;
                    System.out.println("Размерность вашей матрицы=" + inp);
                    String[] numbers;
                    for (int i = 0; i <= size; i++) {
                        try {
                            inp = reader.readLine();
                            numbers = inp.split(" ");
                            for (int k = 0; k <= size; k++) {
                                matA[i][k] = Double.parseDouble(numbers[k]);
                            }
                            matB[i] = Double.parseDouble(numbers[size + 1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Ошибка в файле. Проверте - для отделения десятичной части используйте '.'.");
                            System.exit(0);
                        }
                    }
                    System.out.println("Вы ввели матрицу :");
                    for (int j = 0; j <= size; j++) {
                        for (int k = 0; k <= size; k++) System.out.print(String.format("|%10.3f", matA[j][k]));
                        System.out.println(String.format("|  {%10.3f}", matB[j]));
                        System.out.println();
                    }
                    System.out.println("ヘ( ^o^)ノ＼(^_^ )");
                    trueAnswer = false;
                } else {
                    System.out.println("Введите корректный ответ - 1 для клавиатуры, 0 - для файла");
                }
            } catch (Exception e) {
                System.out.println("ヘ( ^o^)ノ＼(^_^ )");
                System.out.println("Некорректный ввод данных - повторите попытку с самого начала. 1 - для ввода с клавиатуры, 0 - для файла.");
            }
        }
        matSavedA = matA;
        matSavedB = matB;
        Matrix matrix = new Matrix();
        matrix.setMatA(matA);
        matrix.setMatB(matB);
        matrix.setSize(size);
        return matrix;
    }


    public double[][] getMatA() {
        return matA;
    }

    public double[] getMatB() {
        return matB;
    }

    public int getSize() {
        return size;
    }

    public void setMatA(double[][] matA) {
        this.matA = matA;
    }

    public void setMatB(double[] matB) {
        this.matB = matB;
    }

    public void setSize(int size) {
        this.size = size;
    }
}

