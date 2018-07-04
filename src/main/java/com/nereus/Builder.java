package com.nereus;

import net.agkn.hll.HLL;
import org.javatuples.Octet;
import org.javatuples.Pair;
import org.javatuples.Sextet;
import org.javatuples.Triplet;
import static com.nereus.HLLDB.*;
import static com.nereus.MetadataAccessor.*;
import static com.nereus.MetadataService.*;
import static com.nereus.ProcessorService.*;

import java.util.*;

public class Builder {

    public static void joinNodes()
    {

        Iterator<String> left = getNodes().iterator();
        ArrayList<String> tempList = new ArrayList<>();
        ArrayList<String> regrouped = new ArrayList<>();


        if(!left.hasNext())
        {
            System.out.println("No more elements");
            // joinNodes5(tempGraph);
            //return tempGraph;
        }

        while (left.hasNext()){
            Iterator<String> right = getNodes().iterator();
            String lname = left.next();


            String superL = new String();
            /** if (leftNode.identifiers.iterator().hasNext())
             {
             superL = leftNode.identifiers.iterator().next();
             System.out.println("SuperL "+superL);
             }**/


            //System.out.println("***************LNAME"+lname);
            while (right.hasNext())
            {
                String rname = right.next();
                Boolean hasJoined = MetadataService.hasBeenJoined2(lname,rname);
                // System.out.println("Lname"+lname+"RName "+rname);

                if(lname!=rname) {
                    /**This part should contain 3 conditions **/


                    /** JOIN on supersets **/
                    Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>> supersets = getSuperset(lname);
                    if(supersets!= null)
                    {
                        if(ProcessorService.supersetContainsTable(lname,rname))

                        {
                            if(!hasJoined){
                                if(isSubset2(lname,rname)) {
                                    //TODO Perhaps make it posible to join on supersets
                                    //TODO In current POC it was never really an issue
                                }
                            }
                        }
                    }
                    /**JOIN on subsets **/

                    Collection<Sextet> col = getJoiningTuple2(lname,rname);
                    if (col != null ) {
                        Iterator<Sextet> iter = col.iterator();
                        while (iter.hasNext()) {

                            Sextet tuple = iter.next();

                            /**Subset join tuple **/
                            //   if(tuple.getValue0().equals(rname))
                            if(MetadataService.isSubset2(lname,rname))
                            {
                                //Triplet<String,String,Boolean> merged= joinTriplet.getOrDefault(rname,defaultTriplet);


                                // if(!merged.getValue2()) {
                                if(!hasJoined){
                                    //TODO has no ancestors here in the condition join only if there ar no other dependencies
                                    //if(merged.getValue0()!=lname& merged.getValue1()!=rname) {


                                    //subset.remove(leftNode.name);
                                    // subset.removeMapping(leftNodeName,tuple);
                                    //TODO join procedure
                                    //SEPTET.put(lname, tuple);
                                    putConsumedTuple(lname,tuple);

                                    removeRelations(tuple.getValue0().toString(),tuple.getValue3().toString());

                                    System.out.println("*************JOIN on superset -subset  relation ");
                                    System.out.println(tuple.toString());
                                    System.out.println("LeftNode " + lname);
                                    System.out.println("SEX " + tuple.getValue0());
                                    System.out.println("Sextet get values(3)"+tuple.getValue3());
                                    System.out.println("Right " + rname);
                                    System.out.println("JoinTriplet "+JOINTRIPLET.toString());
                                    Triplet<String, String, Boolean> joinedL = Triplet.with(lname, rname, false);
                                    Triplet<String, String, Boolean> joinedR = Triplet.with(rname, lname, false);
                                    putJoinedDataset(lname,joinedL);
                                    putJoinedDataset(rname,joinedR);
                                    //JOINTRIPLET.put(lname,joinedL);
                                    //JOINTRIPLET.put(rname,joinedR);
                                    String newTableName = lname+"And"+rname;
                                    /**added this to get left and right key **/
                                    /**METADATA*********************************/
                                    //septet.put(newTableName,tuple);
                                    putBuilderNeoMetadata(lname,newTableName,rname,tuple,joinedL);
                                  //  JOINS.put(newTableName,tuple);
                                   // JOINSSTARTEND.put(lname,newTableName);
                                   // JOINSSTARTEND.put(rname,newTableName);
                                   // Pair<String,String> startEnd = Pair.with(lname,rname);
                                    //JOINSSTARTEND2.put(newTableName,startEnd);
                                    //JOINORREGROUPTRIPLET.put(lname,joined);
                                    //JOINORREGROUPTRIPLET.put(rname,joined2);
                                    //JOINORREGROUPTRIPLET.put(newTableName,joinedL);

                                    /******************************************/
                                    /**  if(!tempList.contains(newTableName)){
                                     tempList.add(newTableName);
                                     }**/

                                    if(!getTempJoinedNodes().contains(newTableName)){
                                        putTempJoinedNode(newTableName);
                                    }
                                    //TODO copy properties to the new table
                                    transferRelations(lname,rname,newTableName);
                                    //right table is the superset get identfier
                                   // HLLDB.IDENTIFIEDBY.put(newTableName,tuple.getValue4().toString());
                                    putDatasetKey(newTableName,tuple.getValue4().toString());
                                    //TODO: new stuff
                                   // Set<String> tmpSet = new HashSet<>();
                                   // tmpSet.add(tuple.getValue4().toString());
                                   // IDENTIFIEDBY2.put(newTableName,tmpSet);

                                }
                            }
                        }
                    }


                 //  if (MetadataService.checkMatchProperSubset(lname,rname))
                    //testing with Dictionary based on BK-FK realtionship. Before it was based on MaxCardinality
                   if (MetadataService.checkMatchProperSubsetWithDictionary(lname,rname))
                    {
                        System.out.println("checkingMatchProperSubset "+ lname +" rname "+rname);
                    }

                }
            }




        }
        System.out.println("Adding nodes ");

        if(!getNodes().containsAll(getTempJoinedNodes())){
            System.out.println("Adding newly joined nodes "+HLLDB.TEMPJOINS.toString());
            getNodes().addAll(getTempJoinedNodes());
           getTempJoinedNodes().clear();
        }

      /*  if(!nodes.containsAll(regrouped)){
            System.out.println("Adding regrouped nodes "+regrouped.toString());
            nodes.addAll(regrouped);
        }*/

        if(!getNodes().containsAll(getTempRegroupedNodes())){

            System.out.println("Adding regrouped nodes "+HLLDB.TEMPREGROUPED.toString());
            getNodes().addAll(getTempRegroupedNodes());
            getTempRegroupedNodes().clear();
        }


    }

/*
    public static void fit ( HashMap<String,HashMap<Set<String>,HLL>> left, HashMap<String,HashMap<Set<String>,HLL>> right,Set<String> setAName,Set<String> setBName ) throws CloneNotSupportedException
    {


        String leftNameDepracated = setAName.toString().replace("[","").replace("]","").toString();
        String rightNameDepracated = setBName.toString().replace("[","").replace("]","").toString();

        Set<String> policy_pk;
        String identifier = new String();
        Integer [] setACardinality = new Integer[1];
        Integer [] setBCardinality = new Integer[1];
        HLL policyHLL = new HLL(14, 5);
        HLL hllA1 = new HLL(14,5);


        Set<Set<String>> leftTagSets = left.get(leftNameDepracated).keySet();
        Set<Set<String>> rightTagSets = right.get(rightNameDepracated).keySet();



        // System.out.println("--------------------");
        // System.out.println("Set A" +polTagSets.toString());
        //  System.out.println("Set B"+customerTagSets.toString());
        //int leftRows = SetInfo.getCardinality(leftNameDepracated);
        //int rightRows = SetInfo.getCardinality(rightNameDepracated);
        int leftRows = SetInfo.getCardinalityA(leftNameDepracated);
        int rightRows = SetInfo.getCardinalityA(rightNameDepracated);
        // System.out.println("Number of leftRows for SetA "+leftRows);


        //Set<String> subsets = new HashSet<>();
        //Set<String> identifiers = new HashSet<>();
        //Set<String> subidentifiers = new HashSet<>();

        // System.out.println("--------------------");

        //Set <String> temp = new HashSet<>();
        for (Set<String> set : leftTagSets) {
            policy_pk = set;
            //  System.out.println("*****************************************");
            //System.out.println("PolKeys"+set.toString() + polCardinalities.get(policy_pk).cardinality());
            // System.out.println("SetX "+set);
            // System.out.println("SetX2 "+set.toString().equals("[customertime, policy]"));
            // if(left.get(leftNameDepracated).get(policy_pk)!=null) {
            policyHLL = left.get(leftNameDepracated).get(policy_pk);
            long temp1 = policyHLL.cardinality();
            //  System.out.println("Cardinality policyHLL "+policyHLL.cardinality());
            //  System.out.println("###########################################");
            if(set.size()==1 && !set.isEmpty())
            {
                //Used for BK discovery for now only on single columns
               // System.out.println("********************************************");

                int cardinality = (int) policyHLL.cardinality();
                String column = set.toString().replace("[","").replace("]","").toString();
                 HashMap<Set<String>,Integer> columnCardinality = getSetCardinality(leftNameDepracated);

                 if (columnCardinality!=null) {
                     columnCardinality.put(set,cardinality);
                     //SETCARDINALITY.put(leftNameDepracated,columnCardinality);
                     putSetCardinality(leftNameDepracated,columnCardinality);
                 }
               // System.out.println(leftNameDepracated);
               // System.out.println(cardinality+"  "+column);
               //   System.out.println("Is business key "+MetadataService.isBusinessKey2(leftNameDepracated,set) );
               // System.out.println("********************************************");
            }

            for (Set<String> t : rightTagSets) {

                if (set.size()==t.size()) {
                    if(set.size()==1) {
                        System.out.println("SET############### " + set);
                    }
                    hllA1 = policyHLL.clone();
                    // System.out.println("HLLA1Cardinality "+hllA1.cardinality());
                    // System.out.println("CustKeys"+t.toString());

                    *//**Get the second dataset to comapare with A*//*
                    HLL hllB = right.get(rightNameDepracated).get(t);
                    long leftCaridinality = hllA1.cardinality();
                    long rightCardinality = hllB.cardinality();
                    // hllA1.clear();
                    hllA1.union(hllB);
                    //hllA1.cardinality()
                    //Metadata used for BK deduction


                   *//**  System.out.println("Set A " + set);
                     System.out.println("Set A size "+set.size());
                     System.out.println("Set A cardinality " + leftCaridinality);
                     System.out.println("Set B " + t);
                     System.out.println("Set B size " + t.size());
                     System.out.println("Set B caridinality " + rightCardinality);
                     System.out.println("Union" + hllA1.cardinality());**//*

                    long aU = hllA1.cardinality();
                    long inter = (leftCaridinality + rightCardinality) - aU;
                    //TODO approximately greater procedure
                    //TODO for now let's use 50-100 1-2% error rate
                    Octet<String,Set<String>,Long,String,Set<String>,Long,Long,Long> unionInter = Octet.with(leftNameDepracated,set,leftCaridinality,rightNameDepracated,t,rightCardinality,aU,inter);
                  //  UNIONINTER.put(leftNameDepracated,unionInter);
                    putOrderedUnionIntersection(leftNameDepracated,unionInter);
                 //  if (inter > 0) { //ABC AVRO3
                   // if (inter > 36) { //AVRO4
                   // if (inter >0) { //AVRO3
                  // if(inter>520) { //DEF
                  // if(inter>21) { //AVRO 5
                    if(inter>7000) { //DBTEST 2
                        // if (inter>2500){

                        //System.out.println("*****************************************");

                        //System.out.println("Cardinality policyHLL "+policyHLL.cardinality());
                        *//** System.out.println("-------------------");
                         System.out.println("NodeName A " + leftNameDepracated);
                         System.out.println("NodeNamed B " + rightNameDepracated);
                         System.out.println("For key in  A");
                         //System.out.println("PolKeys"+set.toString() + polCardinalities.get(policy_pk).cardinality());
                         System.out.println(set.toString());


                         // System.out.println("HLLA1Cardinality "+hllA1.cardinality());
                         System.out.println("We find key in  B");
                         System.out.println(t.toString());

                         System.out.println("Set A " + set.toString() + " cardinality " + leftCaridinality);
                         System.out.println("Set B " + t.toString() + " caridinality " + rightCardinality);
                         System.out.println("Union" + hllA1.cardinality());
                         System.out.println("Intersection " + inter);**//*

                        if (leftCaridinality == inter || rightCardinality == inter) {

                            //subsets.add(set.toString());
                            //subsets.add(t.toString());
                            //setsTable.put(set.toString(),t.toString()); //new

                            //if cardinality not equals left rows but we know there is intersection needs group by put it into proper subset.
                            // if (leftCaridinality != leftRows) {
                            if(!MetadataService.approximatelyEqual(leftCaridinality,leftRows,2)){
                                Sextet<String, Set<String>, Long, String, Set<String>, Long> tempSextet =
                                        Sextet.with(leftNameDepracated, set, leftCaridinality, rightNameDepracated, t, rightCardinality);
                                Sextet<String, Set<String>, Long, String, Set<String>, Long> rightSextet =
                                        Sextet.with(rightNameDepracated, t, rightCardinality, leftNameDepracated, set, leftCaridinality);
                                if(!leftNameDepracated.isEmpty()) {
                                    //Get rid of empty sets [] 4/26/2018 getValue5.isEmpty not possible
                                    if(!set.isEmpty() && !t.isEmpty() ){


                                   // PROPERSUBSET.put(leftNameDepracated, tempSextet);
                                        putProperSubsetTuple(leftNameDepracated,tempSextet);
                                    }
                                }
                                *//**add values on the other side of the relationship**//*
                                *//**if(!properSubset.containsValue(rightSextet)){
                                 properSubset.put(rightName,rightSextet);}**//*
                                System.out.println("-------------------");
                                System.out.println("NodeName A " + leftNameDepracated);
                                System.out.println("NodeNamed B " + rightNameDepracated);
                                System.out.println(set.toString() + "isProper Subset " + t.toString());
                                *//*************************************************************************//*

                                //System.out.println("NodeName A " + leftNameDepracated);
                                //System.out.println("NodeNamed B " + rightNameDepracated);
                                *//** System.out.println("For key in  A");
                                 //System.out.println("PolKeys"+set.toString() + polCardinalities.get(policy_pk).cardinality());
                                 System.out.println(set.toString());


                                 // System.out.println("HLLA1Cardinality "+hllA1.cardinality());
                                 System.out.println("We find key in  B");
                                 System.out.println(t.toString());

                                 System.out.println("Set A " + set.toString() + " cardinality " + leftCaridinality);
                                 System.out.println("Set B " + t.toString() + " caridinality " + rightCardinality);
                                 System.out.println("Union" + hllA1.cardinality());
                                 System.out.println("Intersection " + inter);**//*
                                *//************************************************************//*

                            }
                            System.out.println(set.toString() + "Proper Subset " + t.toString());

                            //  if (inter == rightRows && leftCaridinality==leftRows ) {
                            if(MetadataService.approximatelyEqual(inter,rightRows,2)&&MetadataService.approximatelyEqual(leftCaridinality,leftRows,2)){
                                // if (inter == rightRows  ) {
                                System.out.println("Nodename " + rightNameDepracated + " is subset of " + leftNameDepracated + " which is superset");
                                Sextet<String, Set<String>, Long, String, Set<String>, Long> tempSextet =
                                        Sextet.with(leftNameDepracated, set, leftCaridinality, rightNameDepracated, t, rightCardinality);
                                //superset.put(leftName,tempSextet);
                                Sextet<String, Set<String>, Long, String, Set<String>, Long> tempSextet2 =
                                        Sextet.with(rightNameDepracated, t, rightCardinality, leftNameDepracated, set, leftCaridinality);
                                if(!leftNameDepracated.isEmpty()) {
                                    if(!set.isEmpty() && !t.isEmpty()){
                                    //SUBSET.put(rightNameDepracated, tempSextet2);
                                        putSubsetTuple(rightNameDepracated,tempSextet2);
                                    }
                                }// change from tempSextet2

                            }

                            // if (leftCaridinality == leftRows) {
                            if(MetadataService.approximatelyEqual(leftCaridinality,leftRows,2)){
                                // System.out.println("Number of leftRows " + leftRows);
                                //System.out.println("This is primary key");

                                if(!leftNameDepracated.isEmpty()) {
                                   // IDENTIFIEDBY.put(leftNameDepracated, set);
                                    putDatasetKey(leftNameDepracated,set.toString());
                                }
                                //TODO new stuff

                              *//**  if(!leftNameDepracated.isEmpty()) {
                                    if(IDENTIFIEDBY2.get(leftNameDepracated)!=null){
                                        // Set<String> set1= new HashSet<>((ArrayList<String>)HLLDB.identifiedBy.get(leftNameDepracated));
                                        Set<String> set1= (Set<String>) IDENTIFIEDBY2.get(leftNameDepracated);
                                        // ArrayList<Set<String>> listOfSets = (ArrayList<Set<String>>)HLLDB.identifiedBy2.get(leftNameDepracated);
                                        // int set1Size = listOfSets.get(0).size();

                                        //MetadataService.isGreaterThan(set1,set);
                                        if(set1.size()>set.size()){
                                            IDENTIFIEDBY2.put(leftNameDepracated, set);
                                        }


                                    } else
                                    { IDENTIFIEDBY2.put(leftNameDepracated, set);}
                                }**//*
                                if ( leftCaridinality >= rightCardinality ) {
                                    // if(true) {
                                    Sextet<String, Set<String>, Long, String, Set<String>, Long> tempSextet =
                                            Sextet.with(leftNameDepracated, set, leftCaridinality, rightNameDepracated, t, rightCardinality);
                                    if(!leftNameDepracated.isEmpty()) {
                                       // SUPERSET.put(leftNameDepracated, tempSextet);
                                        putSupersetTuple(leftNameDepracated,tempSextet);
                                    }
                                    //identifiers.add(set.toString());
                                    identifier = set.toString();
                                    // temp.add(t.toString());

                                    // System.out.println(set.toString());

                                }

                               // else if (leftCaridinality < rightCardinality && rightCardinality<rightRows ) {
                               else if (leftCaridinality < rightCardinality && !MetadataService.approximatelyEqual(rightCardinality,rightRows,2) ) {
                                    //setsTable.put(set.toString(),t.toString());
                                    //TODO add identifiers for the subsets???

                                    //Superset to properSubset relation
                                    //rightCardianlity is less then rightRows so it is a foreign key
                                    Sextet<String, Set<String>, Long, String, Set<String>, Long> tempSextet =
                                            Sextet.with(leftNameDepracated, set, leftCaridinality, rightNameDepracated, t, rightCardinality);
                                    if(!leftNameDepracated.isEmpty()) {
                                        //SUPERSET.put(leftNameDepracated, tempSextet);
                                        putSupersetTuple(leftNameDepracated,tempSextet);
                                    }
                                    //subidentifiers.add(set.toString());
                                    //subidentifiers.add(t.toString());
                                    //setsTable.put(set.toString(),t.toString());  //new
                                }
                            }
                        } else {

                            *//*** inter is greater than zero so there is some relationship, basically add everything that is somehow related**//*
                            Sextet<String, Set<String>, Long, String, Set<String>, Long> tempSextet3 =
                                    Sextet.with(leftNameDepracated, set, leftCaridinality, rightNameDepracated, t, rightCardinality);
                            if(!leftNameDepracated.isEmpty()) {
                                OVERLAP.put(leftNameDepracated, tempSextet3);
                            }
                            //  System.out.println("Overlaps ******");

                            // System.out.println("A " + leftNameDepracated + " " + set.toString());
                            //  System.out.println("B " + rightNameDepracated + " " + t.toString());
                            //subsets.add(set.toString());
                            //subsets.add(t.toString());
                            //setsTable.put(set.toString(),t.toString()); //new

                        }


                    } //

                }

            } // inner loop
            //  } //if policy_pk not null



            //   }
            //System.out.println("*******************************************");

        } //outer loop

        //System.out.println("#########################################");

        // GraphNode graphNode = new GraphNode(leftName,identifiers,subsets,subidentifiers);
        // graphNode.cardinality=rows;


        //   graph.add(graphNode);
        //graphNode.print();



    }*/


    public static void fit ( HashMap<String,HashMap<Set<String>,HLL>> left, HashMap<String,HashMap<Set<String>,HLL>> right,Set<String> setAName,Set<String> setBName ) throws CloneNotSupportedException
    {
        String leftName = setAName.toString().replace("[","").replace("]","").toString();
        String rightName = setBName.toString().replace("[","").replace("]","").toString();

        Set<String> policy_pk;
        String identifier = new String();
        Integer [] setACardinality = new Integer[1];
        Integer [] setBCardinality = new Integer[1];
        HLL policyHLL = new HLL(14, 5);
        HLL hllA1 = new HLL(14,5);


        Set<Set<String>> leftTagSets = left.get(leftName).keySet();
        Set<Set<String>> rightTagSets = right.get(rightName).keySet();

        int leftRows = SetInfo.getCardinalityA(leftName);
        int rightRows = SetInfo.getCardinalityA(rightName);
        for (Set<String> set : leftTagSets) {
            policy_pk = set;
            policyHLL = left.get(leftName).get(policy_pk);
            long temp1 = policyHLL.cardinality();

            if(set.size()==1 && !set.isEmpty())
            {
                int cardinality = (int) policyHLL.cardinality();
                String column = set.toString().replace("[","").replace("]","").toString();
                HashMap<Set<String>,Integer> columnCardinality = getSetCardinality(leftName);

                if (columnCardinality!=null) {
                    columnCardinality.put(set,cardinality);
                    //SETCARDINALITY.put(leftName,columnCardinality);
                    putSetCardinality(leftName,columnCardinality);
                }
            }

            for (Set<String> t : rightTagSets) {

                if (set.size()==t.size()) {
                    hllA1 = policyHLL.clone();

                    /**Get the second dataset to comapare with A*/
                    HLL hllB = right.get(rightName).get(t);
                    long leftCaridinality = hllA1.cardinality();
                    long rightCardinality = hllB.cardinality();
                    hllA1.union(hllB);


                    long aU = hllA1.cardinality();
                    long inter = (leftCaridinality + rightCardinality) - aU;
                    Octet<String,Set<String>,Long,String,Set<String>,Long,Long,Long> unionInter = Octet.with(leftName,set,leftCaridinality,rightName,t,rightCardinality,aU,inter);

                    putOrderedUnionIntersection(leftName,unionInter);

                    Sextet<String, Set<String>, Long, String, Set<String>, Long> tupleL =
                            Sextet.with(leftName, set, leftCaridinality, rightName, t, rightCardinality);
                    Sextet<String, Set<String>, Long, String, Set<String>, Long> tupleR =
                            Sextet.with(rightName, t, rightCardinality, leftName, set, leftCaridinality);
                    //  if (inter > 0) { //ABC AVRO3
                    // if (inter > 36) { //AVRO4
                    // if (inter >0) { //AVRO3
                    // if(inter>520) { //DEF
                    if(inter>21) { //AVRO 5
                        if (leftCaridinality == inter || rightCardinality == inter) {

                            if(!MetadataService.approximatelyEqual(leftCaridinality,leftRows,2)){
                                if(!leftName.isEmpty()) {
                                    if(!set.isEmpty() && !t.isEmpty() ){
                                        putProperSubsetTuple(leftName,tupleL);
                                    }
                                }
                            }

                            if(approximatelyEqual(inter,rightRows,2)&&MetadataService.approximatelyEqual(leftCaridinality,leftRows,2)){
                                System.out.println("Nodename " + rightName + " is subset of " + leftName + " which is superset");
                                if(!leftName.isEmpty()) {
                                    if(!set.isEmpty() && !t.isEmpty()){
                                        putSubsetTuple(rightName,tupleR);
                                    }
                                }
                            }

                            if(approximatelyEqual(leftCaridinality,leftRows,2)){
                                if(!leftName.isEmpty()) {
                                    putDatasetKey(leftName,set.toString());
                                }
                                if ( leftCaridinality >= rightCardinality ) {
                                    if(!leftName.isEmpty()) {
                                        putSupersetTuple(leftName,tupleL);
                                    }
                                }
                                else if (leftCaridinality < rightCardinality && !MetadataService.approximatelyEqual(rightCardinality,rightRows,2) ) {
                                    if(!leftName.isEmpty()) {
                                        putSupersetTuple(leftName,tupleL);
                                    }
                                }
                            }
                        } else {
                            /*** inter is greater than zero so there is some relationship, basically add everything that is somehow related**/
                            if(!leftName.isEmpty()) {
                                putOverLapTuple(leftName,tupleL);
                            }
                        }
                    }
                }
            }
        }
    }

    /**  public static void findCommonKeys3 ( HashMap<String,HashMap<Set<String>,HLL>> left, HashMap<String,HashMap<Set<String>,HLL>> right,Set<String> setAName,Set<String> setBName ) throws CloneNotSupportedException
     {


     String leftNameDepracated = setAName.toString().replace("[","").replace("]","").toString();
     String rightNameDepracated = setBName.toString().replace("[","").replace("]","").toString();

     Set<String> policy_pk;
     String identifier = new String();
     Integer [] setACardinality = new Integer[1];
     Integer [] setBCardinality = new Integer[1];
     HLL policyHLL = new HLL(14, 5);
     HLL hllA1 = new HLL(14,5);


     Set<Set<String>> leftTagSets = left.get(leftNameDepracated).keySet();
     Set<Set<String>> rightTagSets = right.get(rightNameDepracated).keySet();

     // System.out.println("--------------------");
     // System.out.println("Set A" +polTagSets.toString());
     //  System.out.println("Set B"+customerTagSets.toString());
     int leftRows = SetInfo.getCardinality(leftNameDepracated);
     int rightRows = SetInfo.getCardinality(rightNameDepracated);
     // System.out.println("Number of leftRows for SetA "+leftRows);


     //Set<String> subsets = new HashSet<>();
     //Set<String> identifiers = new HashSet<>();
     //Set<String> subidentifiers = new HashSet<>();

     // System.out.println("--------------------");

     //Set <String> temp = new HashSet<>();
     for (Set<String> set : leftTagSets) {
     policy_pk = set;
     //  System.out.println("*****************************************");
     //System.out.println("PolKeys"+set.toString() + polCardinalities.get(policy_pk).cardinality());
     System.out.println("SetX "+set);
     System.out.println("SetX2 "+set.toString().equals("[customertime, policy]"));
     // if(left.get(leftNameDepracated).get(policy_pk)!=null) {
     policyHLL = left.get(leftNameDepracated).get(policy_pk);
     //  System.out.println("Cardinality policyHLL "+policyHLL.cardinality());
     //  System.out.println("###########################################");

     for (Set<String> t : rightTagSets) {

     if (set.size()==t.size()) {
     hllA1 = policyHLL.clone();
     // System.out.println("HLLA1Cardinality "+hllA1.cardinality());
     // System.out.println("CustKeys"+t.toString());


     HLL hllB = right.get(rightNameDepracated).get(t);
     long leftCaridinality = hllA1.cardinality();
     long rightCardinality = hllB.cardinality();
     // hllA1.clear();
     hllA1.union(hllB);

     System.out.println("Set A " + set);
     System.out.println("Set A size "+set.size());
     System.out.println("Set A cardinality " + leftCaridinality);
     System.out.println("Set B " + t);
     System.out.println("Set B size " + t.size());
     System.out.println("Set B caridinality " + rightCardinality);
     System.out.println("Union" + hllA1.cardinality());

     long aU = hllA1.cardinality();
     long inter = (leftCaridinality + rightCardinality) - aU;
     //inter = inter - 10000;
     if (inter > 1) {

     //System.out.println("*****************************************");

     //System.out.println("Cardinality policyHLL "+policyHLL.cardinality());
     System.out.println("-------------------");
     System.out.println("NodeName A " + leftNameDepracated);
     System.out.println("NodeNamed B " + rightNameDepracated);
     System.out.println("For key in  A");
     //System.out.println("PolKeys"+set.toString() + polCardinalities.get(policy_pk).cardinality());
     System.out.println(set.toString());


     // System.out.println("HLLA1Cardinality "+hllA1.cardinality());
     System.out.println("We find key in  B");
     System.out.println(t.toString());

     System.out.println("Set A " + set.toString() + " cardinality " + leftCaridinality);
     System.out.println("Set B " + t.toString() + " caridinality " + rightCardinality);
     System.out.println("Union" + hllA1.cardinality());
     System.out.println("Intersection " + inter);

     if (leftCaridinality == inter || rightCardinality == inter) {

     //subsets.add(set.toString());
     //subsets.add(t.toString());
     //setsTable.put(set.toString(),t.toString()); //new

     //if cardinality not equals left rows but we know there is intersection needs group by put it into proper subset.
     // if (leftCaridinality != leftRows) {
     if(!MetadataService.approximatelyEqual(leftCaridinality,leftRows,1)){
     Sextet<String, Set<String>, Long, String, Set<String>, Long> tempSextet =
     Sextet.with(leftNameDepracated, set, leftCaridinality, rightNameDepracated, t, rightCardinality);
     Sextet<String, Set<String>, Long, String, Set<String>, Long> rightSextet =
     Sextet.with(rightNameDepracated, t, rightCardinality, leftNameDepracated, set, leftCaridinality);
     if(!leftNameDepracated.isEmpty()) {
     PROPERSUBSET.put(leftNameDepracated, tempSextet);
     }
     System.out.println(set.toString() + "isProper Subset " + t.toString());
     }
     System.out.println(set.toString() + "Proper Subset " + t.toString());

     //  if (inter == rightRows && leftCaridinality==leftRows ) {
     if(MetadataService.approximatelyEqual(inter,rightRows,1)&&MetadataService.approximatelyEqual(leftCaridinality,leftRows,1)){
     // if (inter == rightRows  ) {
     System.out.println("Nodename " + rightNameDepracated + " is subset of " + leftNameDepracated + " which is superset");
     Sextet<String, Set<String>, Long, String, Set<String>, Long> tempSextet =
     Sextet.with(leftNameDepracated, set, leftCaridinality, rightNameDepracated, t, rightCardinality);
     //superset.put(leftName,tempSextet);
     Sextet<String, Set<String>, Long, String, Set<String>, Long> tempSextet2 =
     Sextet.with(rightNameDepracated, t, rightCardinality, leftNameDepracated, set, leftCaridinality);
     if(!leftNameDepracated.isEmpty()) {
     SUBSET.put(rightNameDepracated, tempSextet2);
     }// change from tempSextet2

     }

     // if (leftCaridinality == leftRows) {
     if(MetadataService.approximatelyEqual(leftCaridinality,leftRows,1)){
     // System.out.println("Number of leftRows " + leftRows);
     //System.out.println("This is primary key");

     if(!leftNameDepracated.isEmpty()) {
     if(IDENTIFIEDBY.get(leftNameDepracated)!=null){
     Set<String> set1= (Set<String>) IDENTIFIEDBY.get(leftNameDepracated);
     if(set1.size()>set.size()){
     IDENTIFIEDBY.put(leftNameDepracated, set);
     }


     } else
     { IDENTIFIEDBY.put(leftNameDepracated, set);}
     }
     if ( leftCaridinality >= rightCardinality ) {
     // if(true) {
     Sextet<String, Set<String>, Long, String, Set<String>, Long> tempSextet =
     Sextet.with(leftNameDepracated, set, leftCaridinality, rightNameDepracated, t, rightCardinality);
     if(!leftNameDepracated.isEmpty()) {
     SUPERSET.put(leftNameDepracated, tempSextet);
     }
     //identifiers.add(set.toString());
     identifier = set.toString();
     // temp.add(t.toString());

     // System.out.println(set.toString());
     } else if (leftCaridinality < rightCardinality && rightCardinality<rightRows ) {
     //setsTable.put(set.toString(),t.toString());
     //TODO add identifiers for the subsets???

     //Superset to properSubset relation
     Sextet<String, Set<String>, Long, String, Set<String>, Long> tempSextet =
     Sextet.with(leftNameDepracated, set, leftCaridinality, rightNameDepracated, t, rightCardinality);
     if(!leftNameDepracated.isEmpty()) {
     SUPERSET.put(leftNameDepracated, tempSextet);
     }
     //subidentifiers.add(set.toString());
     //subidentifiers.add(t.toString());
     //setsTable.put(set.toString(),t.toString());  //new
     }
     }
     } else {


     Sextet<String, Set<String>, Long, String, Set<String>, Long> tempSextet3 =
     Sextet.with(leftNameDepracated, set, leftCaridinality, rightNameDepracated, t, rightCardinality);
     if(!leftNameDepracated.isEmpty()) {
     OVERLAP.put(leftNameDepracated, tempSextet3);
     }
     System.out.println("Overlaps ******");

     System.out.println("A " + leftNameDepracated + " " + set.toString());
     System.out.println("B " + rightNameDepracated + " " + t.toString());
     //subsets.add(set.toString());
     //subsets.add(t.toString());
     //setsTable.put(set.toString(),t.toString()); //new

     }


     } //

     }

     } // inner loop
     //  } //if policy_pk not null



     //   }
     //System.out.println("*******************************************");

     } //outer loop

     //System.out.println("#########################################");

     // GraphNode graphNode = new GraphNode(leftName,identifiers,subsets,subidentifiers);
     // graphNode.cardinality=rows;


     //   graph.add(graphNode);
     //graphNode.print();



     }**/



}
