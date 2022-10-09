import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.lang.Integer.MAX_VALUE;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the name of file: ");
        String name = scan.nextLine() + ".txt";
        System.out.print("In which number system to create a file? (Kb/Mb/Gb): ");
        String system = scan.nextLine().toUpperCase();
        System.out.print("Enter file size in " + system + ": ");
        long size = scan.nextLong();
        long time = System.currentTimeMillis();
        try {
            long count = generateFile(name, system, size);
            System.out.print("A file with the name \"" + name + "\" and size of " + size + system +
                    " was successfully generated in ");
            System.out.println((double) (System.currentTimeMillis() - time) + "ms!");
            System.out.print("\nHow many files to use for sorting? N = ");
            int n = scan.nextInt();
//            time = System.currentTimeMillis();
//            ArrayList<ArrayList<Integer>> arr = fibonacci(6, n-1);
//            System.out.println(arr);
//            System.out.println((double) (System.currentTimeMillis() - time) + "ms!");
            int i = polyPhaseSort(name, n, count, system); // отсортированный файл
        } catch (Exception e) {
            System.err.println(e);
        }
//        File file = new File(name);
//        Scanner reader = new Scanner(file);
    }
    public static ArrayList<ArrayList<Integer>> fibonacci(int rows, int cols) {
//        int rows = 6;
        int sum;
        int res = 2;
        ArrayList<ArrayList<Integer>> arr = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            res--;
            if(res==0) {
                res=cols+1;
            }
            sum = 0;
            arr.add(new ArrayList<>());
            for (int j = 0; j < cols; j++) {
                if(i == 0 && j == 0) {
                    arr.get(i).add(1);
                }
                else if(i==0) {
                    arr.get(i).add(0);
                }
                else if (j == cols - 1) {
                    arr.get(i).add(j, arr.get(i - 1).get(0));
                }
                else {
                    arr.get(i).add(j, arr.get(i - 1).get(0) + arr.get(i - 1).get(j + 1));
                }
                sum +=arr.get(i).get(j);
            }
            arr.get(i).add(sum);
            arr.get(i).add(res);
        }
        return arr;
    }

    public static int polyPhaseSort(String name, int n, long count, String system) throws IOException {
        for(int i=1;i<n;i++) {
            File file = new File("T" + i + ".txt");
            file.createNewFile();
        }
        ArrayList<ArrayList<Integer>> fibList = fibonacci(20, n-1);
        BufferedReader reader = Files.newBufferedReader(Path.of(name), StandardCharsets.UTF_8);
        File file = new File(name);
        long time = System.currentTimeMillis();
        int gig = 500000000;//java -XX:+PrintFlagsFinal -version | findstr /i "HeapSize PermSize ThreadStackSize"
        int k=0;
//        switch (system) {
//            case "GB":
//                size *= 1024 * 1024 * 1024;
//                break;
//            case "MB":
//                size *= 1024 * 1024;
//                break;
//            case "KB":
//                size *= 1024;
//                break;
//        }
        while (count/fibList.get(k).get(n-1)>gig) {
            k++;
        }
        System.out.println(k);
        int[] arr = new int[500000000];
        for(int i=0;i<50000000;i++) {
            arr[i] = Integer.parseInt((reader.readLine()));
        }
        System.out.println("\nArray read = "+ (double) (System.currentTimeMillis() - time) + "ms!");
        time = System.currentTimeMillis();
        Arrays.parallelSort(arr);
        System.out.println("Array.parallelSort = "+ (double) (System.currentTimeMillis() - time) + "ms!");
//        for(int i=0;i<10;i++) {
//            System.out.println(arrAm[i]);
//        }
//        System.out.println();
//        for(int i=990;i<1000;i++) {
//            System.out.println(arr[i]);
//        }
        return 1;
    }

    public static long generateFile(String name, String system, long size) throws IOException {
        File file = new File(name);
        file.createNewFile();
        FileWriter in = new FileWriter(file);
        Random rand = new Random();
        StringBuilder s = new StringBuilder();
        long count =0;
        switch (system) {
            case "GB":
                size *= 1024 * 1024 * 1024;
                break;
            case "MB":
                size *= 1024 * 1024;
                break;
            case "KB":
                size *= 1024;
                break;
        }
        while (file.length() < size) {
            for (int i = 0; i < 10240; i++) {
                s.append(rand.nextInt(MAX_VALUE));
                s.append('\n');
            }
            in.write(s.toString());
            s.setLength(0);
            count++;
        }
        in.close();
        return count*10240;
    }
}
//for (int i=0;i<10;i++) {
//for (int j=0;j<n+1;j++) {
//System.out.print(fibList.get(i).get(j)+" ");
//}
//System.out.println();
//}