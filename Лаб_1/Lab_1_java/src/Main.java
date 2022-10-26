import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Integer.MAX_VALUE;

public class Main {
    public static int n, j, l;

    public static void main(String[] args) {
        System.out.println("Hello world!");
        Scanner scan = new Scanner(System.in);

        System.out.print("Enter the name of the initial file: ");
        String name = scan.nextLine() + ".txt";

        System.out.print("In which unit of information to create a file? (Kb/Mb/Gb): ");
        String system = scan.nextLine().toUpperCase();

        System.out.print("Enter file size in " + system + ": ");
        long size = scan.nextLong();

        double time = System.currentTimeMillis();
        try {
            long count = generateFile(name, system, size);
            System.out.println(count);
            System.out.print("A file named \"" + name + "\" with a size of " + size + system +
                    " was successfully generated in ");
            System.out.println(System.currentTimeMillis() - time + "ms!");

            System.out.print("\nHow many files to use for sorting? N = ");
            n = scan.nextInt();

            time = System.currentTimeMillis();
            polyPhaseSort(count,name);//102440960,"haha.txt"
            System.out.println(System.currentTimeMillis() - time + "ms!");
        } catch (Exception e) {
            System.err.println(e);
        }
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
            for (int i = 0; i < 10240; i++) {
                s.append(rand.nextInt(MAX_VALUE));
                s.append('\n');
            }
            in.write(s.toString());
            s.setLength(0);
            count++;
        }
        in.close();
        return count * 10240;
    }

    public static void select(int[] a, int[] d) {
        int i,a0;
        if((d[j])<(d[j+1])) j++;
        else {
            if(d[j]==0) {
                l++;
                a0=a[0];
                for (i=0;i<n-1;i++) {
                    d[i]=a0+a[i+1]-a[i];
                    a[i] = a0 + a[i + 1];
                }
            }
            j=0;
        }
        d[j] = d[j] - 1;
    }

    public static void polyPhaseSort(long eof, String name) throws IOException {
        int i, z, k, x, mx, min,tn,dn;
        int c=0;
        boolean eor;
        int[] a = new int[n];
        int[] d = new int[n];
        int[] t = new int[n];
        int[] ta = new int[n-1];
        File[] f = new File[n];
        File f0 = new File(name);

        FileWriter[] in = new FileWriter[n];

        for(i=0;i<n-1;i++) {
            f[i] = new File("T"+(i+1)+".txt");
            f[i].createNewFile();
            in[i]= new FileWriter(f[i]);
        }
        f[n-1] = new File("T"+n+".txt");
        f[n-1].createNewFile();

        for(i=0;i<n-1;i++) {
            a[i] = 1; d[i] = 1;
        }
        l = 1; j = 0; a[n-1] = 0; d[n-1] = 0;

        BufferedReader reader = Files.newBufferedReader(f0.toPath(), StandardCharsets.UTF_8);
        String firstX;
        String[] firstY=new String[n];
        firstX = reader.readLine();
        do {
            select(a,d);
            eor=false;
//            copyrun(f0,f[j]);
            do {
//                copy();
                firstY[j]=firstX;
                in[j].write(firstX+'\n');
                firstX = reader.readLine();
                if(firstX!=null) {
                    if (Integer.parseInt(firstX) < Integer.parseInt(firstY[j])) eor = true;
                }
                c++;
            } while (!eor && c!=eof);
        } while (c!=eof && (j!=n-2));
        j=0;
        while (c!=eof) {
            select(a,d);
            if(Integer.parseInt(firstY[j]) <= Integer.parseInt(firstX)) {
//            copyrun(f0,f[j]);
                eor=false;
                do {
//                copy();
                    firstY[j]=firstX;
                    in[j].write(firstX+'\n');
                    firstX = reader.readLine();
                    if(firstX!=null) {
                        if (Integer.parseInt(firstX) < Integer.parseInt(firstY[j])) eor = true;
                    }
                    c++;
                } while (!eor && c!=eof);
                if(c==eof) {
                    d[j]=d[j]+1;
                }
                else {
//            copyrun(f0,f[j]);
                    eor = false;
                    do {
//                copy();
                        firstY[j]=firstX;
                        in[j].write(firstX+'\n');
                        firstX = reader.readLine();
                        if(firstX!=null) {
                            if (Integer.parseInt(firstX) < Integer.parseInt(firstY[j])) eor = true;
                        }
                        c++;
                    } while (!eor && c!=eof);
                }
            }
            else {
//            copyrun(f0,f[j]);
                eor = false;
                do {
//                copy();
                    firstY[j]=firstX;
                    in[j].write(firstX+'\n');
                    firstX = reader.readLine();
                    if(firstX!=null) {
                        if (Integer.parseInt(firstX) < Integer.parseInt(firstY[j])) eor = true;
                    }
                    c++;
                } while (!eor && c!=eof);
            }
        }
        reader.close();
        BufferedReader[] readers = new BufferedReader[n];
        for(i=0;i<n-1;i++) {
            in[i].close();
            t[i] = i;
////            startRead();
            readers[i] = Files.newBufferedReader(f[i].toPath(), StandardCharsets.UTF_8);
        }
        t[n-1] = n-1;


        for(i=0;i<n-1;i++) {
            firstY[i]=readers[i].readLine();
        }
        do {
            in[t[n-1]]= new FileWriter(f[t[n-1]]);
            z=a[n-2]; d[n-1]=0;
            do {
                k = 0;
                for (i = 0; i < n - 1; i++) {
                    if (d[i] > 0) {
                        d[i] = d[i] - 1;
                    } else {
                        ta[k] = t[i];k = k + 1;
                    }
                }
                if (k == 0) {
                    d[n-1] = d[n-1] + 1;
                }
                else {
                    do {
                        eor=false;
                        i = 0; mx = 0; min = Integer.parseInt(firstY[ta[0]]);
                        while (i < k-1) {
                            i = i + 1; x = Integer.parseInt(firstY[ta[i]]);
                            if (x < min) {
                                min = x; mx = i;
                            }
                        }
//                        copy(f[ta[mx]],f[t[n-1]]);
                        in[t[n-1]].write(min+"\n");
                        firstY[ta[mx]]= readers[ta[mx]].readLine();
                        if(firstY[ta[mx]]!=null) {
                            if (min > Integer.parseInt(firstY[ta[mx]])) {
                                eor = true;
                            }
                        }
                        else {
                            firstY[ta[mx]]= String.valueOf(MAX_VALUE);
                            eor=true;
                        }
                        if (eor) {
                            ta[mx]=ta[k-1];
                            k = k - 1;
                        }
                    } while (k > 0);
                }
                z = z - 1;
            }while (z>0);
            in[t[n-1]].close();
            readers[t[n-1]] = Files.newBufferedReader(f[t[n-1]].toPath(), StandardCharsets.UTF_8);
            firstY[t[n-1]] = readers[t[n-1]].readLine();
            tn=t[n-1];dn=d[n-1];z=a[n-2];
            for (i=n-1;i>0;i--) {
                t[i]=t[i-1];d[i]=d[i-1];a[i]=a[i-1]-z;
            }
            t[0]=tn;d[0]=dn;a[0]=z;
            l=l-1;
        } while (l!=0);
        in[t[0]].close();
        System.out.println("Sorted in file T"+(t[0]+1));
    }
}