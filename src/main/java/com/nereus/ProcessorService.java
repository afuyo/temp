package com.nereus;

import org.javatuples.Sextet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import static com.nereus.HLLDB.*;
import static com.nereus.MetadataAccessor.*;
import static com.nereus.MetadataService.*;

public class ProcessorService {

    public static boolean supersetContainsTable(String leftName,String rightName) {
       // Boolean contains = false;
        Boolean contains = false;
      /**  Collection<Sextet> superSets = (Collection) SUPERSET.get(leftName);
       if (superSets != null) {
       Iterator<Sextet> iter = superSets.iterator();

       while (iter.hasNext()) {
       if (iter.next().getValue3().equals(rightName)) {
       contains = true;
       // break; //TODO Customer having policy&&adress in superset will get false with argurement leftname=Customer rightName=Policy. false on second itereation althoug true
       //TODO this entire superset - superse/subset/propersubset(first condition in join)  needs rethinking and is not used at the moment
       } else {
       contains = false;
       }
       }

       }*/
        if(getSuperset(leftName)!=null)
        {
            contains=getSuperset(leftName).stream().anyMatch(e->e.getValue3().equals(rightName));
        }

        return contains;
    }

    public static void removeRelations(String leftName, String rightName) {
        //superset on the right
        /** Superset - subset relationship has been identified and materialized in a join
         * We need to keep truck of it and move these relations to a pool of used relationships.
         */
       // Collection<Sextet<String, Set<String>, Long, String, Set<String>, Long>> superX = (Collection) SUPERSET.get(rightName);
       // Collection<Sextet<String, Set<String>, Long, String, Set<String>, Long>> subX = (Collection) SUBSET.get(leftName);




        //Use set to capture only one relationship.


        if (getSuperset(rightName) != null) {
           //  getSuperset(rightName).stream().filter(e->e.getValue0().equals(rightName) & e.getValue3().equals(leftName)).findFirst().orElse(null);

            getSuperset(rightName).forEach((e) -> {
                if (e.getValue0().equals(rightName) & e.getValue3().equals(leftName)) {

                    if (!getConsumedTuples().contains(e)) {
                        putConsumedTuple(rightName,e);
                    }
                   /** if (!SEPTET.containsValue(e)) {
                        SEPTET.put(rightName, e);
                    }**/
                }


            });

            if (getSubset(leftName) != null) {
                getSubset(leftName).forEach((e) -> {
                    if (e.getValue0().equals(leftName) & e.getValue3().equals(rightName)) {

                        if (!isConsumedTuple(e)) {
                           // SEPTET.put(leftName, e);
                            putConsumedTuple(leftName,e);
                        }
                    }


                });
            }
        }
    }

    public static void transferRelations(String subName, String superName, String newName) {
        //copy supersets, propersubset and overlaps from the right talb
        //supersets only if not contains current left and right

      //  Collection<Sextet<String, Set<String>, Long, String, Set<String>, Long>> superX = (Collection) SUPERSET.get(superName);
         if(getSuperset(superName)!=null) {

             /**putSupersetTuple(newName,getSuperset(superName)
                     .stream()
                     .filter(e->!e.getValue3().equals(subName)&&!isConsumedTuple(e))
                     .findFirst().orElse(null));**/

             getSuperset(superName).forEach((e) -> {

                 if (!e.getValue3().equals(subName) && !isConsumedTuple(e)) {
                     //System.err.println("subName"+subName+" superName "+superName+" newName "+newName);
                     //System.err.println(e);
                    // SUPERSET.put(newName, e);
                     putSupersetTuple(newName,e);

                 }
             });

         }

       // Collection<Sextet<String, Set<String>, Long, String, Set<String>, Long>> properSubX = (Collection) PROPERSUBSET.get(superName);

        if(getProperSubset(superName)!=null) {
        /**   putProperSubsetTuple(newName,getProperSubset(superName)
                   .stream()
                   .filter(e->!e.getValue3().equals(subName)&&!isConsumedTuple(e))
                   .findFirst().orElse(null));**/
            getProperSubset(superName).forEach((e) -> {

                if (!e.getValue3().equals(subName) && !SEPTET.containsValue(e)) {
                    //System.err.println("subName" + subName + " superName " + superName + " newName " + newName);
                    //System.err.println(e);
                    //PROPERSUBSET.put(newName, e);
                    putProperSubsetTuple(newName,e);

                }
            });
        }

        Collection<Sextet<String, Set<String>, Long, String, Set<String>, Long>> overlapX = (Collection) OVERLAP.get(superName);

       if(getOverlap(superName)!=null) {
      /**      putOverLapTuple(superName,getOverlap(superName)
                    .stream()
                    .filter(e->!e.getValue3().equals(subName))
                    .findFirst().orElse(null));**/
           overlapX.forEach((e) -> {

                if (!e.getValue3().equals(subName)) {

                   // OVERLAP.put(newName, e);
                    putOverLapTuple(newName,e);

                }
            });
        }
    }

    public static Collection<Sextet> getJoiningTuple2(String leftName, String rightName)
    {
        Boolean contains = false;
        Collection<Sextet> subset = (Collection) SUBSET.get(leftName);
        Collection<Sextet> superset = (Collection) SUPERSET.get(rightName);
        Collection<Sextet> properCol =  new ArrayList<>();

        //there is subset for that node
        if (subset != null) {

            /** Iterator<Sextet> iter = subset.iterator();
             while (iter.hasNext()) {

             Sextet sextet = iter.next();
             /**   System.out.println("sextet"+sextet);
             System.out.println("lname "+leftName);
             System.out.println("rname "+ rightName);**/

            /**  if (sextet.getValue3().equals(rightName))
             {
             contains= true;
             }**/



            //}
            //check if right matches left
            if(superset != null && superset.stream().anyMatch(e->e.getValue3().equals(subset.iterator().next().getValue0()))) {
                // superset.stream().anyMatch(e->e.getValue3().equals(subset.iterator().next().getValue0()));
                System.out.println("getJoininTuple2 left and right"+leftName+" "+rightName);
                //check if left matches right
                if(subset.stream().anyMatch(s -> s.getValue3().equals(superset.iterator().next().getValue0()))) {
                    properCol.add(subset.stream().filter(s -> s.getValue3().equals(superset.iterator().next().getValue0())).findFirst().get());
                }
                //contains = subset.stream().anyMatch(s -> s.getValue3().equals(superset.iterator().next().getValue0()));

            }


        }
        return properCol;
    }

}
