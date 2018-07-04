package com.nereus.Utils;

import com.nereus.HLLDB;
import org.javatuples.Pair;
import org.javatuples.Septet;
import org.javatuples.Sextet;
import org.javatuples.Triplet;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import static com.nereus.HLLDB.*;

public class Output {

    public static void printMetaData(String nodeName)
    {
        Collection<String> col0 = (Collection<String>) IDENTIFIEDBY.get(nodeName);
        if(col0!=null) {
            Iterator iter0 = col0.iterator();

            System.out.println("Identified by ");
            while (iter0.hasNext()) {
                System.out.println(iter0.next());
            }
        }
        Collection<Sextet> col = (Collection) SUPERSET.get(nodeName);

        if (col!=null) {
            Iterator iter = col.iterator();

            System.out.println("Superset for " + nodeName);
            while (iter.hasNext()) {


                System.out.println(iter.next().toString());

            }
        }

        Collection<Sextet> colsubsets = (Collection) SUBSET.get(nodeName);

        if (colsubsets!=null){
            Iterator iter2 = colsubsets.iterator();

            System.out.println("Subset for "+nodeName);
            while (iter2.hasNext())
            {


                System.out.println(iter2.next().toString());

            }
        }

        Collection<Sextet> col3 = (Collection) PROPERSUBSET.get(nodeName);

        if( col3!=null) {
            Iterator iter3 = col3.iterator();

            System.out.println("ProperSubset for " + nodeName);
            while (iter3.hasNext()) {


                System.out.println(iter3.next().toString());

            }
        }

        /**  Collection<Sextet> col4 = (Collection) OVERLAP.get(nodeName);

         if (col4!=null) {
         Iterator iter4 = col4.iterator();

         System.out.println("Overlap for " + nodeName);
         while (iter4.hasNext()) {


         System.out.println(iter4.next().toString());

         }
         }**/

        Collection<Septet> col5 = (Collection) SEPTET.get(nodeName);

        if (col5!=null) {
            Iterator iter5 = col5.iterator();

            System.out.println("Septet for " + nodeName);
            while (iter5.hasNext()) {


                System.out.println(iter5.next().toString());

            }
        }

        Triplet col6 = JOINTRIPLET.get(nodeName);

        System.out.println("Triplent for  "+nodeName);

        if (col6!=null) {
            System.out.println(col6.toString());
        }

        System.out.println("Joins for "+ nodeName);
        Collection col7 = (Collection)JOINS.get(nodeName);

        if(col7!=null)
        { System.out.println(col7.stream().findAny().get().toString());}

        /**CARDINALITIES****************/
        HashMap col8 =  SETCARDINALITY.get(nodeName);

        if (col8!=null) {


            System.out.println("Cardinalities for  " + nodeName);
            System.out.println(col8.toString());

        }

    }




}
