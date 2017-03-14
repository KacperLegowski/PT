package model;
import java.util.Set;
import java.io.File;
import java.lang.System;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Date;
import java.text.SimpleDateFormat;
/**
 * Created by klego on 14.03.2017.
 */
public class DiskDirectory extends DiskElement{
    private Set<DiskElement> children;
    public DiskDirectory(File file,String interf)
    {
        super(file);
        System.out.print(file.getName());
        if(interf.compareTo("Comparator")==0 )
        {
            children=new TreeSet<DiskElement>(new CompFiles());
        }
        else if(interf.compareTo("Comparable")==0)
        {
            children=new TreeSet<DiskElement>();
        }
        File[] files=file.listFiles();
        for(int i=0;files.length>i;i++)
        {
            if(files[i].isDirectory())
            {
                DiskDirectory newDir=new DiskDirectory(files[i],interf);
                children.add(newDir);
            }
            else if(files[i].isFile())
            {
                DiskFile newFile=new DiskFile(files[i]);
                children.add(newFile);
            }
        }
    }
    public Set<DiskElement> getChildren()
    {
        return children;
    }
    public boolean equals(DiskDirectory o2)
    {
        if(super.equals(o2))
        {
            Set<DiskElement> seto2=o2.getChildren();
            if(seto2.size()==children.size())
            {
                DiskElement[] tab;
                DiskElement[] tab2;
                tab=seto2.toArray(new DiskElement[seto2.size()]);
                tab2=children.toArray(new DiskElement[seto2.size()]);
                for(int i=0;seto2.size()>i;i++)
                {
                    if(tab[i]!=tab2[i])
                    {
                        return false;
                    }
                }
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
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
        System.out.printf("%-80s %-80s %-80s %d \n",toPrint,"K",formDate,file.length());
        for(DiskElement element:children)
        {
            element.print(depth+1);
        }

    }
}
