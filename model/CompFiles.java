/**
 * Created by klego on 14.03.2017.
 */
package model;
import java.util.Comparator;
import java.io.File;

class CompFiles implements Comparator<DiskElement>{
    public int compare(DiskElement o1, DiskElement o2) {
        if(o1.getFile().length()>o2.getFile().length())
        {
            return 1;
        }
        else if(o1.getFile().length()==o2.getFile().length())
        {
            return 0;
        }
        else
        {
            return -1;
        }
    }
};


