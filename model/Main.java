package model;

/**
 * Created by klego on 14.03.2017.
 */

//1 -
public class Main {

    public static void main(String[] args) {
        DiskDirectory temp =new DiskDirectory(args[1], true,args[2]);
        temp.print(0);
    }
}