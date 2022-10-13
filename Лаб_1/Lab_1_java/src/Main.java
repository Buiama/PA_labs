import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Random;

import static java.lang.Integer.MAX_VALUE;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");
//        int n = 4;
//        int[] a = new int[2*n];
//        for(int i=0;i<n;i++) {
//            a[i] = n-i;
//        }
//        System.out.println(Arrays.toString(a));
////        straightMerge(n,a);
//        System.out.println(Arrays.toString(a));
        polyPhaseSort();
    }
    public static void select() {
//        int i, z;
        if(d[j]<d[j+1]) j=j+1;
        else {
            if(d[j]==0) {
                level = level+1;
                z=a[i];
                for (i=1;i<=n;i++) {//!!!
                    d[i]=z+a[i+1]-a[i];
                    a[i]=z+a[i+1];
                }
            }
            j=0;//!!!
        }
        d[j] = d[j]-1;
    }
    public static void copyrun(File f0, File file) {
        do {
            copy();
        } while (true);//!?
    }
    public static void openRandomSeq(String system, long size) throws IOException {
        File file = new File("name.txt");
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
    }
    public static void startWrite() {

    }
    public static void copy() {

    }
    private static void openSeq(File file, String name) {

    }
    public static final int n=6;
    public static int i,j,z,level;
    public static int[] a = new int[n];
    public static int[] d = new int[n];
    public static void polyPhaseSort() throws IOException {
//        final int n =6;
        int mx,tn;
        int k,dn;
        int x,min;
        int[] t = new int[n];
        int[] ta = new int[n];
        File f0;
        File[] f = new File[n];
        String[] fn = new String[n];
        int eof = 5;
        int c=0;

        FileWriter[] in = new FileWriter[n];

//        openRandomSeq("gb",3);
        f0 = new File("name.txt");
        //listSeq();
//            openSeq(f[i],fn[i]);
        for (i=0;i<n;i++) {
            fn[i]="T"+(i+1)+".txt";
            f[i] = new File(fn[i]);
            f[i].createNewFile();
        }
        for (i=0;i<n-1;i++) {
            a[i]=1;d[i]=1;
            //startWrite(f[i]);//!?
            in[i]= new FileWriter(f[i]);
        }
        level=1;j=0;//!!!
        a[n-1]=0;d[n-1]=0;//!!!
//        startRead(f0);
        BufferedReader reader = Files.newBufferedReader(f0.toPath(), StandardCharsets.UTF_8);
        boolean eor;
        String firstX, firstZ;
        String[] firstY=new String[n];
        firstX = reader.readLine();
        do {
            select();
//            copyrun(f0,f[j]);
            do {
//                copy();
                firstY[j]=firstX;
                in[j].write(firstX+'\n');
                firstX = reader.readLine();
                eor=false;
                if(Integer.parseInt(firstX)<Integer.parseInt(firstY[j])) eor=true;
                c++;
            } while (!eor);//!?
        } while (c!=eof && (j!=n-2));//!?
        j=0;
        while (c!=eof) {//!?
            select();
            if(Integer.parseInt(firstY[j]) <= Integer.parseInt(firstX)) {
//            copyrun(f0,f[j]);
                do {
//                copy();
                    firstY[j]=firstX;
                    in[j].write(firstX+'\n');
                    firstX = reader.readLine();
                    eor=false;
                    if(Integer.parseInt(firstX)<Integer.parseInt(firstY[j])) eor=true;
                    c++;
                } while (!eor);//!?
                if(c!=eof) {//!?
                    d[j]=d[j]+1;
                }
//            copyrun(f0,f[j]);
                do {
//                copy();
                    firstY[j]=firstX;
                    in[j].write(firstX+'\n');
                    firstX = reader.readLine();
                    eor=false;
                    if(Integer.parseInt(firstX)<Integer.parseInt(firstY[j])) eor=true;
                } while (!eor);//!?
            }
//            copyrun(f0,f[j]);
            do {
//                copy();
                firstY[j]=firstX;
                in[j].write(firstX+'\n');
                firstX = reader.readLine();
                eor=false;
                if(Integer.parseInt(firstX)<Integer.parseInt(firstY[j])) eor=true;
            } while (!eor);//!?
        }
        BufferedReader[] readers = new BufferedReader[n];
        for(i=0;i<n-1;i++) {//!!!
            t[i] = i;
//            startRead();
            readers[i] = Files.newBufferedReader(f[i].toPath(), StandardCharsets.UTF_8);
        }
        t[n-1] = n-1;
        do {
            z=a[n-2]; d[n-1]=0;
//            startWrite(f[t[n-1]],t[n-1]);!!!!!!!!!!!!
            do {
                k = 0;
                for (i = 0; i < n - 1; i++) {//!!!
                    if (d[i] > 0) {
                        d[i] = d[i] - 1;
                    } else {
                        ta[k] = t[i]; k = k + 1;
                    }
                }
                if (k == 0) {
                    d[n-1] = d[n-1] + 1;
                }
                else {
                    do {
                        i = 0; mx = 0; min = Integer.parseInt(readers[ta[0]].readLine()); //f[ta[0]].first;
                        while (i < k) {
                            i = i + 1; x = Integer.parseInt(readers[ta[i]].readLine());//????
                            if (x < min) {
                                min = x; mx = i;
                            }
                        }
//                        copy(f[ta[mx]],f[t[n-1]]);
                        firstX=readers[ta[mx]].readLine();
                        firstZ=firstX;
                        in[t[n-1]].write(firstX);
                        eor=false;
                        if(Integer.parseInt(firstX)<Integer.parseInt(firstZ)) eor=true;

                        if (eor) {
                            for (int tx = mx; tx < k; tx++) {
                                ta[tx] = ta[tx + 1];
                            }
                            k = k - 1;
                        }
                    } while (k != 0);
                }
                z = z - 1;
            }while (z!=0);
//            startRead(f[t[n-1]],t[n-1]);
            tn=t[n-1];dn=d[n-1];z=a[n-2];
            for (i=n-1;i>0;i--) {
                t[i]=t[i-1];d[i]=d[i-1];a[i]=a[i-1]-z;
            }
            t[0]=tn;d[0]=dn;a[0]=z;
//            startWrite(f[t[1]], t[n]);
            in[t[1]].write(t[n-1]);
            level=level-1;
        } while (level!=0);
        for (i=0;i<n;i++) {
//            closeSeq(f[i]);
            in[i].close();
            readers[i].close();
        }
//        closeSeq(f0);
        reader.close();
        }



    public static void straightMerge(int n, int[] a) {//как в учебнике!
        int i,j, k, l,t;
        int h,m, p ,q,r;
        boolean up;

        up = true;
        p=1;
        do{
            h=1; m=n;
            if(up) {i=0; j=n-1; k=n; l=2*n-1;}
            else   {k=0; l=n-1; i=n; j=2*n-1;}
            do{
                if(m>=p) {q=p;}
                else {q=m;}
                m=m-q;
                if(m>=p) {r=p;}
                else {r=m;}
                m=m-r;
                while ((q!=0) && (r!=0)) {
                    if(a[i]<a[j]){a[k]=a[i];k=k+h;i=i+1;q=q-1;}
                    else         {a[k]=a[j];k=k+h;j=j-1;r=r-1;}
                }
                while (r>0)       {a[k]=a[j];k=k+h;j=j-1;r=r-1;}
                while (q>0)       {a[k]=a[i];k=k+h;i=i+1;q=q-1;}
                h=-h; t=k; k=l; l=t;
            } while (m!=0);
            up=!up;p=2*p;
        } while (p<=n);
        if(!up) {
            for(i=0;i<n;i++) {
                a[i]=a[i+n];
            }
        }
    }
}









    /*public static void select() {

    }
    public static void copyrun() {

    }
    public static void polyphaseSort() {
        final int n = 6;
        int i,j, mx, tn;
        int k, dn, z, level;
        int x, min;
        int[] a = new int[n];
        int[] d = new int[n];
        int[] t = new int[n];
        int[] ta = new int[n];
        Sequence f0;
        Sequence[] f = new Sequence[n];
        String[] fn = new String[n];

    }*/