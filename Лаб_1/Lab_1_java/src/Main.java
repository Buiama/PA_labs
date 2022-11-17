import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Character.toUpperCase;
import static java.lang.Integer.MAX_VALUE;

public class Main {
    public static int n, j, l;

    public static void main(String[] args) {
        System.out.println("Hello world!");
        Scanner scan = new Scanner(System.in);

        System.out.print("Enter the name of the initial file: ");
        String name = scan.nextLine() + ".txt";

        String system = inputSystem();

        long size = inputSize(system);

        double time = System.currentTimeMillis();
        double resTime;
        try {
            long count = generateFile(name, system, size);
            resTime = System.currentTimeMillis() - time;

            System.out.println("The file named \"" + name + "\" with a size of " + size + system +
                    " was successfully generated within " + resTime + "ms!");

            System.out.println();
            n = inputN();

            System.out.print("Do you want to use modified version of the algorithm? (Y/N): ");
            boolean yn = checkYN();

            int res;
            time = System.currentTimeMillis();
            if(yn) {
                res = polyPhaseSort_M(count, name, system, size);
                resTime = System.currentTimeMillis() - time;
                System.out.println("The result of the modified algorithm is in the file T"+res+
                        ".txt and completed within "+resTime+"ms!");
            }
            else {
                res = polyPhaseSort(count, name);
                resTime = System.currentTimeMillis() - time;
                System.out.println("The result of the basic algorithm is in the file T"+res+
                        ".txt and completed within "+resTime+"ms!");
            }
            test(name, "T"+res+".txt");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private static String inputSystem() {
        Scanner sc = new Scanner(System.in);
        String system;
        while (true) {
            try {
                System.out.print("In which unit of information the file must be created? (Kb/Mb/Gb): ");
                system = sc.nextLine().toUpperCase();
                if(system.equals("GB") || system.equals("MB")||system.equals("KB") || system.equals("B")) break;
                else System.out.println("Try again!");
            }
            catch (Exception e) {
                System.out.println("Try again!");
                sc.next();
            }
        }
        return system;
    }

    private static long inputSize(String system) {
        Scanner sc = new Scanner(System.in);
        long size;
        while (true) {
            try {
                System.out.print("Enter the file size in " + system + ": ");
                size = sc.nextLong();
                if(size>0) break;
                else System.out.println("Size must be positive. Try again!");
            }
            catch (Exception e) {
                System.out.println("Size must be integer. Try again!");
                sc.next();
            }
        }
        return size;
    }

    private static int inputN() {
        Scanner sc = new Scanner(System.in);
        int n;
        System.out.print("How many files should be used for sorting? ");
        while (true) {
            try {
                System.out.print("N = ");
                n = sc.nextInt();
                if(n>2) break;
                else System.out.println("N must be greater than 2. Try again!");
            }
            catch (Exception e) {
                System.out.println("N must be integer. Try again!");
                sc.next();
            }
        }
        return n;
    }

    private static boolean checkYN() {
        Scanner scan = new Scanner(System.in);
        char yn = toUpperCase(scan.nextLine().charAt(0));
        return (yn=='Y' || yn=='1');
    }

    private static long generateFile(String name, String system, long size) throws IOException {
        File file = new File(name);
        file.createNewFile();
        FileWriter in = new FileWriter(file);
        Random rand = new Random();
        StringBuilder s = new StringBuilder();
        long count = 0;
        switch (system) {
            case "GB":
                size *= 1024;
            case "MB":
                size *= 1024;
            case "KB":
                size *= 1024;
        }
        while (file.length() < size) {
            for (int i = 0; i < 10240; ++i) {
                s.append(rand.nextInt(MAX_VALUE));
                s.append('\n');
            }
            in.write(s.toString());
            s.setLength(0);
            ++count;
        }
        in.close();
        return count * 10240;
    }

    public static void select(int[] a, int[] d) { // алгоритм распределения серий
        int i,a0;
        if(d[j]<d[j+1]) ++j;
        else {
            if(d[j]==0) {
                ++l;
                a0=a[0];
                for (i=0;i<n-1;++i) {
                    d[i]=a0+a[i+1]-a[i];
                    a[i] = a0 + a[i + 1];
                }
            }
            j=0;
        }
        --d[j];
    }

    public static int polyPhaseSort(long eof, String name) throws IOException {
        int i, z, k, x, mx, min, tmpT, tmpD;
        boolean eor = false;  // конец ли серии (end of run)
        int[] a = new int[n]; // идеальное число серий
        int[] d = new int[n]; // число пустых серий
        int[] t = new int[n]; // индексация файлов
        int[] ta = new int[n-1]; // индексы файлов, участвующих в слиянии
        File[] f = new File[n];
        File f0 = new File(name);

        FileWriter[] in = new FileWriter[n];

        for(i=0;i<n-1;++i) { // создаем и открываем файлы для записи серий
            f[i] = new File("T"+(i+1)+".txt");
            f[i].createNewFile();
            in[i]= new FileWriter(f[i]);
        }
        f[n-1] = new File("T"+n+".txt"); // создаем файл, куда сделаем первое слияние
        f[n-1].createNewFile();

        for(i=0;i<n-1;++i) {
            a[i] = 1; d[i] = 1;
        }
        l = 1; j = 0; a[n-1] = 0; d[n-1] = 0;

        BufferedReader reader = Files.newBufferedReader(f0.toPath(), StandardCharsets.UTF_8);
        String firstX; // текущее читаемое значение из начального файла
        String[] firstY=new String[n]; // текущее читаемое значение из файла распределения

        firstX = reader.readLine();
        do {
            select(a,d); // распределяем числа Фибоначчи по уровням
            do { // запись серии в файл с индексом j; можно было бы выделить в функцию writeRuns()
                firstY[j]=firstX;
                in[j].write(firstX+'\n');
                firstX = reader.readLine();
                if(firstX!=null) {
                    eor = Integer.parseInt(firstX) < Integer.parseInt(firstY[j]);
                }
                --eof;
            } while (!eor && eof!=0);
        } while (eof!=0 && j!=n-2); // если серий больше чем файлов
        j=0;
        while (eof!=0) { // записываем в файлы оставшиеся серии изначального файла
            select(a,d);
            // проверяем, получится ли при дозаписи цельная серия
            if(Integer.parseInt(firstY[j]) <= Integer.parseInt(firstX)) {
                eor=false;
                do { // writeRuns()
                    firstY[j]=firstX;
                    in[j].write(firstX+'\n');
                    firstX = reader.readLine();
                    if(firstX!=null) {
                        if (Integer.parseInt(firstX) < Integer.parseInt(firstY[j])) eor = true;
                    }
                    --eof;
                } while (!eor && eof!=0);
                if(eof==0) { // если конец файла, то добавляем пустую взамен той, которая слилась
                    ++d[j];
                }
                else { // если нет, то просто пишем дальше
                    eor = false;
                    do { // writeRuns()
                        firstY[j]=firstX;
                        in[j].write(firstX+'\n');
                        firstX = reader.readLine();
                        if(firstX!=null) {
                            if (Integer.parseInt(firstX) < Integer.parseInt(firstY[j])) eor = true;
                        }
                        --eof;
                    } while (!eor && eof!=0);
                }
            }
            else { // записываем новую серию
                eor = false;
                do { // writeRuns()
                    firstY[j]=firstX;
                    in[j].write(firstX+'\n');
                    firstX = reader.readLine();
                    if(firstX!=null) {
                        if (Integer.parseInt(firstX) < Integer.parseInt(firstY[j])) eor = true;
                    }
                    --eof;
                } while (!eor && eof!=0);
            }
        }
        reader.close();

        BufferedReader[] readers = new BufferedReader[n]; // открываем файлы на чтение
        for(i=0;i<n-1;++i) {
            in[i].close();
            t[i] = i;
            readers[i] = Files.newBufferedReader(f[i].toPath(), StandardCharsets.UTF_8);
        }
        t[n-1] = n-1; // выходной файл после прохода уровня

        for(i=0;i<n-1;++i) {
            firstY[i]=readers[i].readLine();
        }

        do { // сортируем слиянием
            in[t[n-1]]= new FileWriter(f[t[n-1]]);
            z=a[n-2]; d[n-1]=0;
            do { // z - число сливаемых из всех файлов серий
                k = 0;
                for (i = 0; i < n - 1; ++i) {
                    if (d[i] > 0) {
                        --d[i]; // слияние пустой серии
                    } else {
                        ta[k] = t[i]; ++k; // файлы, где еще есть серии
                    }
                }
                if (k == 0) {
                    ++d[n-1]; // запись пустой серии в выходной файл
                }
                else { // слияние реальной серии
                    do {
                        i = 0; mx = 0; min = Integer.parseInt(firstY[ta[0]]);
                        while (i < k-1) { // ищем минимальный элемент
                            ++i; x = Integer.parseInt(firstY[ta[i]]);
                            if (x < min) {
                                min = x; mx = i;
                            }
                        }
                        in[t[n-1]].write(min+"\n");
                        firstY[ta[mx]]= readers[ta[mx]].readLine();
                        if(firstY[ta[mx]]!=null) {
                            if (min > Integer.parseInt(firstY[ta[mx]])) {
                                eor = true;
                            }
                        }
                        else {
                            firstY[ta[mx]]= "2147483647";
                            eor=true;
                        }
                        if (eor) {
                            ta[mx]=ta[k-1];
                            --k;
                            eor=false;
                        }
                    } while (k > 0);
                }
                --z;
            }while (z>0);
            in[t[n-1]].close();
            // новый входной файл
            readers[t[n-1]] = Files.newBufferedReader(f[t[n-1]].toPath(), StandardCharsets.UTF_8);
            firstY[t[n-1]] = readers[t[n-1]].readLine();
            tmpT = t[n-1]; tmpD = d[n-1]; z = a[n-2];
            for (i=n-1;i>0;--i) { // обновление чисел Фибоначчи
                t[i]=t[i-1];
                d[i]=d[i-1];
                a[i]=a[i-1]-z;
            }
            // новый выходной файл
            t[0] = tmpT;
            d[0] = tmpD; a[0] = z;
            --l;
        } while (l!=0);
        in[t[0]].close();
        return (t[0]+1);
    }

    public static int polyPhaseSort_M(long eof, String name, String system, long size) throws IOException {
        int i, z, k, x, mx, min, tmpT, tmpD, lastInRun;
        long runLen; // количество чисел в серии
        boolean eor = false;  // конец ли серии (end of run)
        int[] a = new int[n]; // идеальное число серий
        int[] d = new int[n]; // число пустых серий
        int[] t = new int[n]; // индексация файлов
        int[] ta = new int[n-1]; // индексы файлов, участвующих в слиянии
        File[] f = new File[n];
        File f0 = new File(name);

        FileWriter[] in = new FileWriter[n];

        for(i=0; i<n-1; ++i) { // создаем и открываем файлы для записи серий
            f[i] = new File("T"+(i+1)+".txt");
            f[i].createNewFile();
            in[i] = new FileWriter(f[i]);
        }
        f[n-1] = new File("T"+n+".txt"); // создаем файл, куда сделаем первое слияние
        f[n-1].createNewFile();

        for(i=0; i<n-1; ++i) {
            a[i] = 1; d[i] = 1;
        }
        l = 1; j = 0;
        a[n-1] = 0; d[n-1] = 0;

        runLen = (long) (2.25*eof/size);
        switch (system) {
            case "B":
                runLen *= 1024;
            case "KB":
                runLen *= 1024;
            case "MB":
                runLen *= 1024;
        }

        int runsCount = (int) (eof/ runLen);  // количество серий длиной 2,25 GB
        int lastRunLen = (int) (eof% runLen); // остаток

        if(runsCount == 0) {
            runLen = lastRunLen;
            lastRunLen = 0;
            runsCount = 1;
        }

        int[] runs = new int[(int) runLen]; // для записи серии
        int[] lastRun = new int[lastRunLen]; // для записи последней серии
        StringBuilder s = new StringBuilder();
        BufferedReader reader = Files.newBufferedReader(f0.toPath(), StandardCharsets.UTF_8);
        do {
            select(a,d); // распределяем числа Фибоначчи по уровням
            for(i=0; i<runLen; ++i) {
                runs[i] = Integer.parseInt(reader.readLine());
            }
            Arrays.parallelSort(runs);
            for(i=0; i<runLen; ++i) {
                s.append(runs[i]).append('\n');
                if(i%1024==0) {
                    in[j].write(s.toString());
                    s.setLength(0);
                }
            }
            in[j].write(s.toString());
            s.setLength(0);
            --runsCount;
        } while (runsCount!=0 && j!=n-2); // если серий больше чем файлов
        if(runsCount==0 && lastRunLen!=0) {
            select(a,d); // распределяем числа Фибоначчи по уровням
            for(i=0; i<lastRunLen; ++i) {
                lastRun[i] = Integer.parseInt(reader.readLine());
            }
            Arrays.parallelSort(lastRun);
            for(i=0; i<lastRunLen; ++i) {
                s.append(lastRun[i]).append('\n');
                if(i%1024==0) {
                    in[j].write(s.toString());
                    s.setLength(0);
                }
            }
            in[j].write(s.toString());
            s.setLength(0);
            lastRunLen = 0;
        }

        j=0;
        while (runsCount!=0) { // записываем в файлы оставшиеся серии изначального файла
            select(a,d);
            for(i=0; i<runLen; ++i) {
                runs[i] = Integer.parseInt(reader.readLine());
            }
            Arrays.parallelSort(runs);
            for(i=0; i<runLen; ++i) {
                s.append(runs[i]).append('\n');
                if(i%1024==0) {
                    in[j].write(s.toString());
                    s.setLength(0);
                }
            }
            in[j].write(s.toString());
            s.setLength(0);
            --runsCount;
        }
        lastInRun = runs[(int) (runLen-1)];
        if(lastRunLen!=0) {
            select(a, d); // распределяем числа Фибоначчи по уровням
            for (i = 0; i < lastRunLen; ++i) {
                lastRun[i] = Integer.parseInt(reader.readLine());
            }
            Arrays.parallelSort(lastRun);
            for(i=0; i<lastRunLen; ++i) {
                s.append(lastRun[i]).append('\n');
                if(i%1024==0) {
                    in[j].write(s.toString());
                    s.setLength(0);
                }
            }
            in[j].write(s.toString());
            s.setLength(0);
            // если конец файла, то добавляем пустую взамен той, которая слилась
            if (lastInRun <= lastRun[0]) ++d[j];
        }
        reader.close();

        BufferedReader[] readers = new BufferedReader[n]; // открываем файлы на чтение
        for(i=0;i<n-1;++i) {
            in[i].close();
            t[i] = i;
            readers[i] = Files.newBufferedReader(f[i].toPath(), StandardCharsets.UTF_8);
        }
        t[n-1] = n-1; // выходной файл после прохода уровня

        String[] firstY=new String[n]; // текущее читаемое значение из файла распределения
        for(i=0;i<n-1;++i) {
            firstY[i]=readers[i].readLine();
        }

        do { // сортируем слиянием
            in[t[n-1]]= new FileWriter(f[t[n-1]]);
            z=a[n-2]; d[n-1]=0;
            do { // z - число сливаемых из всех файлов серий
                k = 0;
                for (i = 0; i < n - 1; ++i) {
                    if (d[i] > 0) {
                        --d[i]; // слияние пустой серии
                    } else {
                        ta[k] = t[i]; // файлы, где еще есть серии
                        ++k;
                    }
                }
                if (k == 0) {
                    ++d[n-1]; // запись пустой серии в выходной файл
                }
                else { // слияние реальной серии
                    do {
                        i = 0; mx = 0; min = Integer.parseInt(firstY[ta[0]]);
                        while (i < k-1) { // ищем минимальный элемент
                            ++i; x = Integer.parseInt(firstY[ta[i]]);
                            if (x < min) {
                                min = x; mx = i;
                            }
                        }
                        in[t[n-1]].write(min+"\n");
                        firstY[ta[mx]] = readers[ta[mx]].readLine();
                        if(firstY[ta[mx]]!=null) {
                            if (min > Integer.parseInt(firstY[ta[mx]])) {
                                eor = true;
                            }
                        }
                        else {
                            firstY[ta[mx]] = "2147483647";
                            eor = true;
                        }
                        if (eor) {
                            --k;
                            ta[mx] = ta[k];
                            eor = false;
                        }
                    } while (k > 0);
                }
                --z;
            }while (z > 0);
            in[t[n-1]].close();
            // новый входной файл
            readers[t[n-1]] = Files.newBufferedReader(f[t[n-1]].toPath(), StandardCharsets.UTF_8);
            firstY[t[n-1]] = readers[t[n-1]].readLine();
            tmpT =t[n-1];tmpD =d[n-1];
            z=a[n-2];
            for (i=n-1;i>0;--i) { // обновление чисел Фибоначчи
                t[i]=t[i-1];
                d[i]=d[i-1];
                a[i]=a[i-1]-z;
            }
            // новый выходной файл
            t[0]= tmpT;
            d[0]= tmpD; a[0]=z;
            --l;
        } while (l!=0);
        in[t[0]].close();
        return (t[0]+1);
    }

    public static void test(String initial, String sorted) throws IOException {
        BufferedReader reader = Files.newBufferedReader(Path.of(sorted), StandardCharsets.UTF_8);
        String first, second;
        boolean eor = true;
        long c = 1;
        first = reader.readLine();
        while ((second = reader.readLine())!=null) {
            if(Integer.parseInt(first)>Integer.parseInt(second)) {
                eor=false;
            }
            first=second;
            ++c;
        }
        reader.close();

        long cInit = 0;
        reader = Files.newBufferedReader(Path.of(initial), StandardCharsets.UTF_8);
        while ((first = reader.readLine())!=null) {
            ++cInit;
        }

        if(c==cInit && eor) System.out.println("File sorted correctly!");
    }
}