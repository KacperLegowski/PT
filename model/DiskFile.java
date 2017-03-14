package model;
import java.io.File;
import java.lang.System;
import java.text.SimpleDateFormat;
/**
 * Created by klego on 14.03.2017.
 */
public class DiskFile extends DiskElement {
    public DiskFile(File file){
        super(file);
    }
    protected void print(int depth) {
        SimpleDateFormat template=new SimpleDateFormat("yyyy-MM-dd");
        String formDate=template.format(lastModifiedMS);
        String toPrint="";
        for(int i=0;depth>i;i++)
        {
            toPrint=toPrint+"-";
        }
        toPrint=toPrint+file.getName();
        System.out.printf("%-80s %-80s %-80s %d \n",toPrint,"P",formDate,file.length());
    }
 /*...pozostałe pola i metody...*/
}
