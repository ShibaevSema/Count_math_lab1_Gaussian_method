import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.StringTokenizer;

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

        System.out.println("Выполняется метод Гаусса:");
        int i = 0;
        int swapK = 0;//число перестановок строк
        while (i <= n) { // ОСНОВНОЙ ЦИКЛ
            double L = matA[i][i];
            double LB = matB[i];
            boolean zeroFlag = false;
            for (int j = i; j <= n; j++) { // 1-ЦИКЛ - ищем макс. элемент в i-ом столбце
                double M = matA[j][i];
                if (M != 0 || L != 0) {
                    if (M > L) {
                        for (int k = 0; k <= n; k++) { // меняем строку макс. элемента с i-ой строкой
                            double tmpL = matA[i][k];
                            matA[i][k] = matA[j][k];
                            matA[j][k] = tmpL;
                        }
                        L = matA[i][i]; // обновили L
                        matB[i] = matB[j]; //тоже самое сделали с правой частью
                        matB[j] = LB;
                        LB = matB[i];
                        swapK++;
                    }
                } else {
                    zeroFlag = true;
                    matA[j][i] = 0;
                }// если один ноль - то зануляем остальные элементы столбца (ниже i-1)
            }
            if (zeroFlag) {//если занулили - то к остальным не переходим
                i++;
                continue;
            }
            //2-ЦИКЛ - приводим к каноническому виду i-ую строку
            matB[i] = matB[i] / matA[i][i];
            double Ci = matA[i][i];
            for (int k = i; k <= n; k++) {
                matA[i][k] = matA[i][k] / Ci;
            }
            //
            //3-ЦИКЛ теперь надо занулить все что ниже L - по заверщении основного цикла - привести к треугольному ввиду
            for (int j = i + 1; j <= n; j++) {
                double C = matA[j][i] / matA[i][i]; // коэффициент, что будем использовать для вычитания строк
                //цикл для приведения строк в двумерном массиве А :
                for (int k = i; k < n; k++) {
                    matA[j][k] = matA[j][k] - matA[i][k] * C;
                }
                //осталось вычесть в массиве B ( в правой части ):
                matB[j] = matB[j] - matB[i] * C;
            }
            i++;
        } // завершаем основной цикл

        double compositionI = 1;// произведение диагональных элементов
        for (i = 0; i <= n; i++) {
            compositionI = compositionI * matA[i][i];
        }
        double detA = pow((-1), swapK) * compositionI;
        System.out.println("***1.Определитель матрицы:***");
        System.out.println(detA);

        System.out.println("***2.Матрица была привидена к треугольному виду:***");
        for (int j = 0; j <= size; j++) {
            for (int k = 0; k <= size; k++) System.out.print(matA[j][k] + " ");
            System.out.println("(" + matB[j] + ")");
            System.out.println();
        }
        // теперь найдем решения - обратный ход
        System.out.println("***3.Векторы неизвестных:***");
        for (int k = n; k >= 0; k--) {
            double rightSide = matB[k];
            for (int p = n; p > k; p--) { // меньше K - чтобы строка с одним элементам не заходила !
                rightSide -= matA[k][p] * roots[p];
            }
            roots[k] = rightSide / matA[k][k];
        }
        for (int k = 0; k <= n; k++) {
            int numberVector = k + 1;
            System.out.println("Вектор неизвестных X" + "[ " + numberVector + " ]" + "=" + roots[k]);
        }

        System.out.println();
        System.out.println("***4.Векторы невязок:***");
        double sumA = 0;
        for (i = 0; i <= size; i++) {
            double vectorNev = matSavedB[i];
            for (int k = 0; k <= size; k++) {
                vectorNev -= matSavedA[i][k] * roots[k];
            }
            vectorNev = -vectorNev;
            int numberVector = i + 1;
            System.out.println("Вектор невязки r" + numberVector + "=" + vectorNev);

        }
    }


    public Matrix inputMatrix() throws Exception {
        System.out.println("Выберете ввод матрицы - КЛАВИАТУРА/ФАЙЛ");
        boolean trueAnswer = true;
        while (trueAnswer) {
            try {
                Scanner in = new Scanner(System.in);
                String answer = in.nextLine();
                if (answer.equals("КЛАВИАТУРА")) {
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
                            for (int i = 0; i <= size; i++) System.out.print(matA[j][i] + " ");
                            System.out.println("(" + matB[j] + ")");
                            System.out.println();
                        }
                        trueAnswer = false;
                    } else break;
                } else if (answer.equals("ФАЙЛ")) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("file.txt")));
                    String inp;
                    inp = reader.readLine();
                    size = Integer.parseInt(inp) - 1;
                    System.out.println("n=" + inp);
                    boolean success = false;
                    String[] numbers;
                    for (int i = 0; i <= size; i++) {
                        while (!success) {
                            try {
                                inp = reader.readLine();
                                System.out.println(inp);
                                numbers = inp.split(" ");
                                for (int k = 0; k <= size; k++) {
                                    matA[i][k] = Double.parseDouble(numbers[k]);
                                }
                                matB[i] = Double.parseDouble(numbers[size + 1]);
                                success = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("Ошибка в файле. Проверте - для отделения десятичной части используйте ','.");
                            }

                        }
                    }
                    trueAnswer = false;
                } else {
                    System.out.println("Введите корректный ответ");
                }
            } catch (Exception e) {
                System.out.println("Некорректный ввод данных - повторите попытку");
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

