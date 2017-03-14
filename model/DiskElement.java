package model;
import java.io.File;
import java.util.Comparator;
import java.lang.System;
import java.util.Date;

/**
 * Created by klego on 14.03.2017.
 */
public abstract class DiskElement implements Comparable<DiskElement>{
    protected File file; //java.io.File;
    protected Date lastModifiedMS;
    public DiskElement(File file1){
        this.file=file1;
        this.lastModifiedMS=new Date(file.lastModified());
    }
    public File getFile()
    {
        return this.file;
    }
    public Date getlastModifiedMS()
    {
        return this.lastModifiedMS;
    }
    public boolean equals(DiskElement o2)
    {
        if(file==o2.getFile() && lastModifiedMS==o2.getlastModifiedMS() && this.getClass()==o2.getClass())
        {
            return true;
        }
        else return false;
    }
    public int compareTo(DiskElement other)
    {
        if(file.getName().compareTo((other.getFile()).getName())>0)
        {
            return 1;
        }
        else if(file.getName().compareTo((other.getFile()).getName())==0)
        {
            return 0;
        }
        else{
            return -1;
        }
    }
    protected abstract void print(int depth);
}
