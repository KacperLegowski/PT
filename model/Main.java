
package model;
import java.io.File;
import java.lang.System;
import java.util.Comparator;
import java.util.TreeSet;
public class Main {

    public static void main(String[] args) {
        String path="C:\\Users\\klego\\Desktop\\Others\\do-paczki";
        File file=new File(path);
        DiskDirectory newDir=new DiskDirectory(file,"Comparable");
        newDir.print(0);
    }
}
