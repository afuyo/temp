package com.nereus;

import org.apache.commons.collections4.MultiMap;
import org.javatuples.Sextet;
import org.javatuples.Triplet;

import java.util.*;
import java.util.stream.Collectors;

import static com.nereus.HLLDB.*;
import static com.nereus.MetadataAccessor.*;

public class MetadataService {

    /**
     * determine whether there is superset proper subset realtion
     *
     * @param leftName String
     * @param rightName String
     */
    public static Boolean isInProperSubsetSuperset (String leftName, String rightName)
    {

        // ADDRESS was wrongly classified as superset of POLICY condition was true but could then not find maxCardTuple
        // returning null pointer exception      01/05/2018
        return getProperSubset(leftName).stream().flatMap(x-> getSuperset(rightName).stream().filter(s->x.getValue3().equals(s.getValue0()))).findAny().isPresent();
        // rightSuperset contain policy-> address tuple but it was to subset. so need to qurey propersubset if it contains reference to superset
        // return rightSuperSet.stream().flatMap(x->leftProperSubset.stream().filter(s->x.getValue3().equals(s.getValue0()))).findAny().isPresent();

        // return isInProperSubsetSuperset.get(1);
    }

    public static Sextet getMatchMaxCardinalityTuple2 (String leftName, String rightName){

        Sextet properSub= null;


        boolean isInProperSubsetSuperset= false;
        final Comparator<Sextet<String, Set<String>, Long, String, Set<String>, Long>> comp = (s1, s2) -> Long.compare(s1.getValue2(), s2.getValue2());

        if(getSuperset(rightName)!=null && getProperSubset(leftName)!=null) {
            isInProperSubsetSuperset = isInProperSubsetSuperset(leftName,rightName);

            if(isInProperSubsetSuperset){
                properSub = getProperSubset(leftName).stream().filter(s-> s.getValue3().equals(getSuperset(rightName).iterator().next().getValue0())).max(comp).get();
            }
        }

        else  if(getProperSubset(leftName)!=null && getProperSubset(rightName)!=null  ) {

                 properSub = getProperSubset(leftName).stream().filter(s-> s.getValue3().equals(getProperSubset(rightName).iterator().next().getValue0())).max(comp).orElse(null);

            }

        return properSub;
    }

    /**
     * Takes collection of business key - foreign key relations tuples and
     * returns the matching one.
     * @param leftCollection
     * @param rightCollection
     * @return
     */

    private static Sextet getBkFkTuple(Collection leftCollection, Collection rightCollection)
    {

        Sextet bkFkTuple = null;
        Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>> left = leftCollection;
        Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>> right = rightCollection;

         if(left!=null && right !=null)
         {
             bkFkTuple = left.stream().filter(l -> right.stream()
                     .anyMatch(r -> l.getValue0().equals(r.getValue3())
                             && l.getValue1().equals(r.getValue4())
                             && l.getValue3().equals(r.getValue0())
                             && l.getValue4().equals(r.getValue1())))
                     .findFirst()
                     .orElse(null);
         }


        return bkFkTuple;
    }



    /**
     * Return matching business key to foreign key relation tuple
     * @param leftName
     * @param rightName
     * @return
     */

    public static Sextet getMatchBkFkTuple (String leftName, String rightName){

        Sextet properSub= null;

        if(getSuperset(rightName)!=null && getProperSubset(leftName)!=null) {

            Collection   properSubCol = getProperSubset(leftName).stream().filter(e->{return isBkFkRelationTuple(e);}).collect(Collectors.toList());
            Collection rightSuperSetCol= getSuperset(rightName).stream().filter(e->{return isBkFkRelationTuple(e);}).collect(Collectors.toList());

            properSub= getBkFkTuple(properSubCol,rightSuperSetCol);

            }

            else  if(getProperSubset(leftName)!=null && getProperSubset(rightName)!=null  ) {

                Collection properSubCol = getProperSubset(leftName).stream().filter(e->{return isBkFkRelationTuple(e);}).collect(Collectors.toList());
                Collection rightProperSubCol = getProperSubset(rightName).stream().filter(e->{return isBkFkRelationTuple(e);}).collect(Collectors.toList());

                properSub=getBkFkTuple(properSubCol,rightProperSubCol);

        }
        return properSub;
    }

    /**
     * Find relations between proper subsetes based on max cardinality.
     * @param lname
     * @param rname
     * @return
     */
    public static boolean checkMatchProperSubset (String lname , String rname )
    {
        Boolean checked = false;

        if (isMatchProperSubset2(lname,rname)){


            if(!hasBeenJoined2(lname,rname)
                    &&!hasAncestors(lname)

                    ) {

                Sextet<String, Set<String>, Long, String, Set<String>, Long> tuple = MetadataService.getMatchMaxCardinalityTuple2(lname, rname);
               /** boolean debug1 = isConsumedTuple(tuple);
                boolean debug2 = hasDescendants(rname);
                boolean debug3 = isPartOfRegroup(lname);
                boolean debug4 = isPartOfRegroup2(lname);
            //    boolean debug5 = hasOtherBkFkRelations(lname, rname);
              //  boolean debug6 = isBkFkRelation(lname, rname);
               // boolean debug7 = isBkFkRelationTuple(tuple);
                System.out.print("%%%%%%%%@@@@@");
               // System.out.println(" Is BKFK "+debug7);
                System.out.println(tuple);
               // System.out.println(tupleBkFk);
                System.out.println("lname"+lname);
                System.out.println("rname"+rname);**/

                if (!isConsumedTuple(tuple)
                        && !hasDescendants(rname)
                        && !isPartOfRegroup(rname)
                        && !isPartOfRegroup2(lname)
                        /**
                         * ProperSubset for Address
                         * [Address, [customer], 9971, Customer, [customer], 9971]
                         * [Address, [customer], 9971, Policy, [customer], 9971]
                         * Looks like Address has descendants but the second tuple is just relation between two foreign keys
                         * so the condition below would check if it is bk fk relation but in order to keep clean lets use with dictionary option.
                         */
                      //&& !hasOtherBkFkRelations(lname, rname)

                        ) {

                    putOrderedJoinTuple(lname,tuple);
                    putConsumedTuple(lname, tuple);
                    Triplet<String, String, Boolean> transformed = Triplet.with(lname, rname, true);
                    putTransformedDataset(lname,transformed);
                    String newName = lname + "1";
                    putTransformedDataset(newName,transformed);

                    /*************METADATA STUFF***************/
                     putNeoMetadata(lname,newName,transformed);
                    /*******************************************/

                    putTempRegroupedDataset(newName);
                    putDatasetKey(newName, tuple.getValue1().toString());

                    checked = true;

                    putSubsetTuple(newName,tuple);

                    if (isSupersetEmpty(rname)) {
                        Sextet<String, Set<String>, Long, String, Set<String>, Long> tuple2 = MetadataService.getMatchMaxCardinalityTuple2(rname, lname);

                        putConsumedTuple(rname,tuple2);
                        Triplet<String, String, Boolean> transformedR = Triplet.with(rname, lname, true);
                        putTransformedDataset(rname,transformedR);
                        String newNameR = rname + "1";
                        putDatasetKey(newNameR,tuple2.getValue1().toString());
                        putTempRegroupedDataset(newNameR);

                        /*************METADATA STUFF***************/
                        putNeoMetadata(rname,newNameR,transformedR);

                        putSupersetTuple(newNameR,tuple2);
                        ProcessorService.transferRelations(lname, rname, newNameR);
                    }

                }

            }

        }
        return checked;
    }

    /**
     * Find relations between proper subsetes based on BK-FK
     * @param lname
     * @param rname
     * @return
     */
    public static boolean checkMatchProperSubsetWithDictionary (String lname , String rname )
    {
        Boolean checked = false;

        if (isMatchProperSubsetWithDict(lname,rname)){


            if(!hasBeenJoined2(lname,rname)
                    &&!hasAncestors(lname)

                    ) {

                Sextet<String, Set<String>, Long, String, Set<String>, Long> tuple = MetadataService.getMatchMaxCardinalityTuple2(lname, rname);
                Sextet<String, Set<String>, Long, String, Set<String>, Long> tupleBkFk = MetadataService.getMatchBkFkTuple(lname,rname);
             /**   boolean debug1 = getConsumedTuples().contains(tuple);
                boolean debug2 = hasDescendants(rname);
                boolean debug3 = isPartOfRegroup(lname);
                boolean debug4 = isPartOfRegroup2(lname);
                boolean debug5 = hasOtherBkFkRelationsWithDictionary(lname, rname);
                boolean debug6 = isBkFkRelation(lname, rname);
                boolean debug7 = isBkFkRelationTuple(tuple);
                System.out.print("%%%%%%%%@@@@@");
                System.out.println(" Is BKFK "+debug7);
                System.out.println(tuple);
                System.out.println(tupleBkFk);
                System.out.println("lname"+lname);
                System.out.println("rname"+rname);
                System.out.println("tuple equeals tupleBk"+tuple.equals(tupleBkFk));**/

                 if(tupleBkFk!=null) {
                 tuple = tupleBkFk;
                 }
                //mark tuple as used
                //07/05
                // if(!SEPTET.containsValue(tuple)&&!hasDescendants(rname)&&!isPartOfRegroup(rname)&&!isPartOfRegroup2(lname)) {
                //TODO: Add logic that verfies if isBKFkRelations and test on GHI.
                //TODO: this does not work in current state it works on GHI but not on lname.equals("STATPEJ.POC_CLAIMPAYMENT51AndSTATPEJ.POC_CLAIM51")  and STATPEJ.POC_POLICY
                if(
                        //isBkFkRelation(lname,rname)
                          //      || isBkFkRelationTuple(tuple)
                        tupleBkFk!=null
                        ){

                    if (!isConsumedTuple(tuple)
                            && !hasDescendants(rname)
                            && !isPartOfRegroup(rname)
                            && !isPartOfRegroup2(lname)
                            && !hasOtherBkFkRelationsWithDictionary(lname, rname)
                        //  && isBkFkRelation(lname,rname)
                        // &&isBkFkRelationTuple(tuple)
                            ) {
                        putOrderedJoinTuple(lname,tuple);
                       // boolean debug8 = isBkFkRelationTuple(tuple);
                        putConsumedTuple(lname,tuple);
                        Triplet<String, String, Boolean> transformed = Triplet.with(lname, rname, true);
                        putTransformedDataset(lname,transformed);

                        String newName = lname + "1";

                        putTransformedDataset(newName,transformed);

                        /*************METADATA STUFF***************/
                        putNeoMetadata(lname,newName,transformed);

                        /*******************************************/


                        putTempRegroupedDataset(newName);
                        putDatasetKey(newName,tuple.getValue1().toString());
                        checked = true;


                        putSubsetTuple(newName,tuple);

                        if (isSupersetEmpty(rname)) {
                            Sextet<String, Set<String>, Long, String, Set<String>, Long> tuple2 = MetadataService.getMatchBkFkTuple(rname, lname);

                            putConsumedTuple(rname,tuple2);
                            Triplet<String, String, Boolean> transformedR = Triplet.with(rname, lname, true);

                            putTransformedDataset(rname,transformedR);
                            String newNameR = rname + "1";

                            putDatasetKey(newNameR,tuple2.getValue1().toString());

                            putTempRegroupedDataset(newNameR);

                            /*************METADATA STUFF***************/
                            putNeoMetadata(rname,newNameR,transformedR);
                             /***********************************************/

                            putSupersetTuple(newNameR,tuple2);


                            ProcessorService.transferRelations(lname, rname, newNameR);
                        }
                    }
                }
            }
        }
        return checked;
    }

    public static boolean isSupersetEmpty (String rname)
    {
      /**  Boolean yes = false;

        //if(SUPERSET.get(rname)==null)
        if(getSupersetMap(rname)==null)
        {
            yes=true;
        }**/
        return getSuperset(rname)==null;
    }
    public static boolean isMatchProperSubset2(String leftName, String rightName)
    {
        Boolean contains = false;
        Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>> rightList= (Collection) SUPERSET.get(rightName);
        Sextet<String,Set<String>,Long,String,Set<String>,Long> maxCardTuple = MetadataService.getMatchMaxCardinalityTuple2(leftName,rightName);
        // Collection<Sextet> properSubSets = (Collection) properSubset.get(leftName);
        Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>> propSub= (Collection) PROPERSUBSET.get(leftName);


        if(maxCardTuple!= null)
        {
            /**if less than the other than properSubset **/
            if(maxCardTuple.getValue3().equals(rightName)&&(maxCardTuple.getValue2().intValue() <= maxCardTuple.getValue5().intValue())&& !maxCardTuple.getValue1().isEmpty()) {

                //   System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%GROUP BY ON THIS COLUMN");
                //   System.out.println(maxCardTuple.toString());


                contains = true;
            }
            else if (maxCardTuple.getValue2().intValue() <= (maxCardTuple.getValue5().intValue())&& !maxCardTuple.getValue1().isEmpty())
            {
                contains=true;
            }
            /***this condition will always be true as since maxCardTuple/getMatchMaxCardinality returns value from propersubset**/
            else if(getProperSubset(leftName)!=null && getProperSubset(leftName).contains(maxCardTuple)) {
                //now we check if right reverse is present in the SUPERSET

                if(getSuperset(rightName)!=null)
                {
                    contains= getSuperset(rightName).stream().anyMatch(s -> s.getValue0().equals(maxCardTuple.getValue3()) && s.getValue1().equals(maxCardTuple.getValue4()));
                }

            }


        }
        return contains;
    }

    public static boolean isMatchProperSubsetWithDict(String leftName, String rightName)
    {
        Boolean contains = false;
        Sextet<String,Set<String>,Long,String,Set<String>,Long> bkFkTuple = getMatchBkFkTuple(leftName,rightName);

        if(bkFkTuple!= null)
        {
            if(bkFkTuple.getValue3().equals(rightName)&&(bkFkTuple.getValue2().intValue() <= bkFkTuple.getValue5().intValue())&& !bkFkTuple.getValue1().isEmpty()) {
                contains = true;
            }
            else if (bkFkTuple.getValue2().intValue() <= (bkFkTuple.getValue5().intValue())&& !bkFkTuple.getValue1().isEmpty())
            {
                contains=true;
            }
            else if(getProperSubset(leftName)!=null && getProperSubset(leftName).contains(bkFkTuple)) {

                if(getSuperset(rightName)!=null)
                {
                    contains= getSuperset(rightName).stream().anyMatch(s -> s.getValue0().equals(bkFkTuple.getValue3()) && s.getValue1().equals(bkFkTuple.getValue4()));
                }
                }
            }
        return contains;
    }

    /**
     * Returns true if dataset has already been joined once.
     * @param leftName
     * @param rightName
     * @return
     */
    public static boolean hasBeenJoined2 (String leftName, String rightName)
    {
        Boolean hasBeenJoined = false;

        Triplet<String,String,Boolean> triplet = getJoinedTuple(leftName);
        if (triplet!=null) {
            if ((triplet.getValue0() == leftName || triplet.getValue1() == leftName) && triplet.getValue2().equals(false)) {
                hasBeenJoined = true;
            }
        }
        Triplet<String,String,Boolean> rightTrip = getJoinedTuple(rightName);
        if(rightTrip!= null)
        {
            if((rightTrip.getValue0() == rightName || rightTrip.getValue1()==rightName) && rightTrip.getValue2().equals(false)  )
            {
                hasBeenJoined=true;
            }
        }

        return hasBeenJoined;
    }

    /**
     * Returns true if there are parent child relations.
     * @param leftName
     * @return
     */
    public static boolean hasAncestors(String leftName){
        Boolean ancestors = false;
        //Collection<Sextet> col = (Collection) SUPERSET.get(leftName);
        Collection col = getSuperset(leftName);
        if (col!=null){
            ancestors=true;
        }
        return ancestors;
    }

    /**
     * Check if the datasets have business key, foreign key relation in propersubset
     * @param leftName
     * @param rightName
     * @return boolean
     */
    public static boolean isBkFkRelation(String leftName, String rightName)
    {
       // Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>> col =  (Collection) PROPERSUBSET.get(leftName);

         boolean debug3 = getProperSubset(leftName).stream().anyMatch((e)->isBusinessKey2(e.getValue0(),e.getValue1()) && isBusinessKey2(e.getValue0(),e.getValue4()) && e.getValue3().equals(rightName));
        boolean debug4 = getProperSubset(leftName).stream().anyMatch((e)->isBusinessKey2(e.getValue3(),e.getValue4()) && isBusinessKey2(e.getValue3(),e.getValue1()) && e.getValue3().equals(rightName));

        return debug3 || debug4;
    }

    /**
     * Check if suggested tuple is business key / foreign key relation
     * @param joinTuple
     * @return
     */

    public static boolean isBkFkRelationTuple (Sextet<String,Set<String>,Long,String,Set<String>,Long> joinTuple)
    {
       /* boolean debug = isBusinessKey2(joinTuple.getValue0(),joinTuple.getValue1())&&isBusinessKey2(joinTuple.getValue0(),joinTuple.getValue4());
        boolean debug2 = isBusinessKey2(joinTuple.getValue3(),joinTuple.getValue4())&&isBusinessKey2(joinTuple.getValue3(),joinTuple.getValue1());*/
        boolean debug = isBusinessKey2(joinTuple.getValue0(),joinTuple.getValue1())&&isForeignKey(joinTuple.getValue0(),joinTuple.getValue4());
        boolean debug2 = isBusinessKey2(joinTuple.getValue3(),joinTuple.getValue4())&&isForeignKey(joinTuple.getValue3(),joinTuple.getValue1());

        return  debug || debug2;
    }

    public static boolean hasOtherBkFkRelations(String leftName, String rightName )
    {
        //Check if there is BK -FK relationshp to others than current data set
        // if yes wait
        boolean hasBkFkRelations = false;
       // Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>> col =  (Collection) PROPERSUBSET.get(leftName);
        boolean debug = isBkFkRelation(leftName,rightName);
        //Make sure DatasetNema(value0) and columnName(value1) is BK and is not related to other data sets(value3)
        // if true it has other relationships and we should wait

        /** GHI file In this case we look into Policy Customer
         * col looks like this A Policy,[customer],9971,Customer,[customer],9971]
         * B Policy,[policy],10060,Claim,[policy],5055]
         * C Policy,[customer],9971,Address,[customer],9971
         * Since we are considering Policy Customer FK-BK relation we would have to transform
         * but we also have Policy BK Claim FK relationship so in we case do nothing and move on
         * beacuse Claims does note equal Customer
         * Policy and Address relationship is ignored none of them is BK
         */
        //TODO: DEPTH might become a problem. rightName will probably not be there for newly created datasets
        //String rname = getProperSubset(leftName).stream().filter(e->isBkFkRelationTuple(e)).findFirst().get().getValue3();
        hasBkFkRelations= getProperSubset(leftName).stream().anyMatch((e)->isBusinessKey2(e.getValue0(),e.getValue1()) && !e.getValue3().equals(rightName) );


        return hasBkFkRelations;
    }

    /**
     * Return true if there are other BK-FK relations, different from current tuple.
     * @param leftName
     * @param rightName
     * @return
     */

    public static boolean hasOtherBkFkRelationsWithDictionary(String leftName, String rightName )
    {

        boolean hasBkFkRelations = false;


      /**  col=col.stream().filter(e->!SEPTET.containsValue(e.getValue0()) &&
                !SEPTET.containsValue(e.getValue1())
                && ! SEPTET.containsValue(e.getValue3())
                && ! SEPTET.containsValue(e.getValue4())
        ).collect(Collectors.toList());**/
        boolean debug = isBkFkRelation(leftName,rightName);

        //Make sure DatasetNema(value0) and columnName(value1) is BK and is not related to other data sets(value3)
        // if true it has other relationships and we should wait

        /** GHI file In this case we look into Policy Customer
         * col looks like this A Policy,[customer],9971,Customer,[customer],9971]
         * B Policy,[policy],10060,Claim,[policy],5055]
         * C Policy,[customer],9971,Address,[customer],9971
         * Since we are considering Policy Customer FK-BK relation we would have to transform
         * but we also have Policy BK Claim FK relationship so in we case do nothing and move on
         * beacuse Claims does note equal Customer
         * Policy and Address relationship is ignored none of them is BK
         */
        /** String rname= col.stream().filter(e->isBkFkRelationTuple(e))
                  .findFirst()
                  .map(e->e.getValue3());**/
       String rname = getProperSubset(leftName).stream().filter(e->isBkFkRelationTuple(e)).findFirst().get().getValue3();



        hasBkFkRelations = getProperSubset(leftName).stream().anyMatch((e)-> isBkFkRelationTuple(e) && ! e.getValue3().equals(rname));


        return hasBkFkRelations;
    }


    /**
     * Return true if dataset  is involved in regroup.
     * @param dataSetName
     * @return
     */

    public static boolean isPartOfRegroup (String dataSetName)
    {

       Boolean regroup=getJoinedTuples().stream().anyMatch(e->e.getValue1().equals(dataSetName) && e.getValue2().equals(true));

        return regroup;
    }

    /**
     * Return true if dataSet is involved in regroup.
     * @param dataSetName
     * @return
     */

    public static boolean isPartOfRegroup2 (String dataSetName)
    {
     //   Boolean regroup = false;
       // ArrayList<Boolean> isRegroup = new ArrayList<>();
        Boolean regroup=getJoinedTuples().stream().anyMatch(e->e.getValue0().equals(dataSetName) && e.getValue2().equals(true));

        return regroup;
    }
    public static boolean hasDescendants (String dataSetName)
    {

        return getDataSetsWithDescendants().contains(dataSetName);
    }

    /**
     * Return true if the set is subset.
     * @param leftName
     * @param rightName
     * @return
     */

    public static boolean isSubset2 (String leftName, String rightName) {
        Boolean contains = false;
        if (getSubset(leftName) != null && getSuperset(rightName)!=null) {

                contains = getSubset(leftName).stream().anyMatch(s -> s.getValue3().equals(getSuperset(rightName).iterator().next().getValue0()));
        }
        return contains;
    }

    /**
     * determine whether two numbers are "approximately equal" by seeing if they
     * are within a certain "tolerance percentage," with `tolerancePercentage` given
     * as a percentage (such as 10.0 meaning "10%").
     *
     * @param tolerancePercentage 1 = 1%, 2.5 = 2.5%, etc.
     */
    public static boolean approximatelyEqual(float desiredValue, float actualValue, float tolerancePercentage)
    {
        float diff = Math.abs(desiredValue-actualValue);
        float tolerance = tolerancePercentage/100*desiredValue;


        return diff<tolerance;
    }

    /**
     * True if tuple has been used in a join
     * @param tuple
     * @return
     */
    public static boolean isConsumedTuple(Sextet<String,Set<String>,Long,String,Set<String>,Long> tuple)
    {
        return getConsumedTuples().contains(tuple);
    }

    /**
     * Initailzes SETCARDINALITY  map with source nodes
     * this map will hold information about carditanlity o
     * for each column in the data set.
     *
     */
    public static void initCardinalityMap()
    {
        SOURCE_NODES.forEach((e)->{
            HashMap<Set<String>,Integer> tmpMap = new HashMap<>();
            SETCARDINALITY.put(e,tmpMap);
        });
    }

  /**  public static void hardCodeBusinessKeys()
    {
       String claimnumber = "claimnumber";
        Set<String> claimBk = new HashSet<>();
        claimBk.add(claimnumber);
        TEMPBKEYS.put(CLAIM_TOPIC,claimBk);
        String customer = "customer";
        Set<String> customerBk = new HashSet<>();
        customerBk.add(customer);
        TEMPBKEYS.put(CUSTOMER_TOPIC,customerBk);
        String payment = "claimnumber";
        Set<String> paymentBk = new HashSet<>();
        paymentBk.add(payment);
        TEMPBKEYS.put(PAYMENT_TOPIC,paymentBk);
        String policy = "policy";
        Set<String> policyBk = new HashSet<>();
        policyBk.add(policy);
        TEMPBKEYS.put(POLICY_TOPIC,policyBk);
        String address = "address";
        Set<String> addressBk = new HashSet<>();
        addressBk.add(address);
        TEMPBKEYS.put(ADDRESS_TOPIC,addressBk);





    }*/
    public static void hardCodeForeignKeysAvro()
    {
     /*   String customer = "MFE_NR2";
        Set<String> customerFk = new HashSet<>();
        customerFk.add(customer);
        TEMPFKEYS.put(CUSTOMER_TOPIC,customerFk);*/
        String policy = "MFE_NR1";
        Set<String> policyFk = new HashSet<>();
        policyFk.add(policy);
        TEMPFKEYS.put(POLICY_TOPIC,policyFk);
    }
    public static void hardCodeBusinessKeysAvro()
    {
       //Avro 5
      /**  String claimnumber = "CLAIMID";
        Set<String> claimBk = new HashSet<>();
        claimBk.add(claimnumber);
        TEMPBKEYS.put(CLAIM_TOPIC,claimBk);**/
        String customer = "MFE_NR1, MFE_REL_TYPE, MFE_NR2";
        Set<String> customerBk = new HashSet<>();
        customerBk.add(customer);
        TEMPBKEYS.put(CUSTOMER_TOPIC,customerBk);
        String policy = "MFE_NR";
        Set<String> policyBk = new HashSet<>();
        policyBk.add(policy);
        TEMPBKEYS.put(POLICY_TOPIC,policyBk);
          /*String payment = "MFE_NR1";
        Set<String> paymentBk = new HashSet<>();
        paymentBk.add(payment);
        TEMPBKEYS.put(PAYMENT_TOPIC,paymentBk);*/
        /**String address = "ADDRESSID";
        Set<String> addressBk = new HashSet<>();
        addressBk.add(address);
        TEMPBKEYS.put(ADDRESS_TOPIC,addressBk);
        String claimhandler = "CLAIMHANDLERID";
        Set<String> claimHandlerBk = new HashSet<>();
        claimHandlerBk.add(claimhandler);
        TEMPBKEYS.put(CLAIMHANDLER_TOPIC,claimHandlerBk);*/



    }

 /**   public static void hardCodeBusinessKeysAvroA2()
    {
        //Avro 5
        String claimnumber = "CLAIMID";
        Set<String> claimBk = new HashSet<>();
        claimBk.add(claimnumber);
        TEMPBKEYS.put(CLAIM_TOPIC,claimBk);
        String customer = "CUSTOMERID";
        Set<String> customerBk = new HashSet<>();
        customerBk.add(customer);
        TEMPBKEYS.put(CUSTOMER_TOPIC,customerBk);
        String payment = "PAYMENTID";
        Set<String> paymentBk = new HashSet<>();
        paymentBk.add(payment);
        TEMPBKEYS.put(PAYMENT_TOPIC,paymentBk);
        String policy = "POLICYID";
        Set<String> policyBk = new HashSet<>();
        policyBk.add(policy);
        TEMPBKEYS.put(POLICY_TOPIC,policyBk);
        String address = "ADDRESSID";
        Set<String> addressBk = new HashSet<>();
        addressBk.add(address);
        TEMPBKEYS.put(ADDRESS_TOPIC,addressBk);
        String claimhandler = "CLAIMHANDLERID";
        Set<String> claimHandlerBk = new HashSet<>();
        claimHandlerBk.add(claimhandler);
        TEMPBKEYS.put(CLAIMHANDLER_TOPIC,claimHandlerBk);
        String address2 = "ADDRESSID";
        Set<String> address2Bk = new HashSet<>();
        addressBk.add(address2);
        String ADDRESS2_TOPIC = "STATPEJ.POC_ADDRESS52";
        TEMPBKEYS.put(ADDRESS2_TOPIC,addressBk);



}**/

    /**
     * Return business key for a dataset
     * @param dataSetName
     * @return
     */
     public static Set<String> getBusinessKey(String dataSetName)
     {
         return TEMPBKEYS.get(dataSetName);
     }

    /**
     * Return foreign key referencing dataset
     * @param dataSetName
     * @return
     */
     public static Set<String> getForeignKey(String dataSetName)
     {
         return TEMPFKEYS.get(dataSetName);
     }

     public static boolean isForeignKey(String dataSetName, Set<String> columnName)
     {
         Set<String> dataSetFk = getForeignKey(dataSetName);
         boolean debug = dataSetFk.toString().contains(columnName.toString());
         boolean debug1 = dataSetFk.contains(columnName);
         return debug;
     }
    /**
     * Determine if column is business key, based on hard coded values in dictionary.
     * @param dataSetName
     * @param columnName
     * @return
     */
    public  static boolean isBusinessKey2(String dataSetName, Set<String> columnName)
    {
        Set<String> dataSetBk = getBusinessKey(dataSetName);
        boolean debug1 = dataSetBk.equals(columnName);
        return getBusinessKey(dataSetName).equals(columnName);

    }

    /**
     *  * Determine if column is a candidate key / business key deducing base on cardinality
     *      * Since we deal with transactions log PK will be probably BK+timestamp
     *      * So we assume BK will have second highest cardinality
     * @param dataSetName
     * @param column
     * @return
     */

    public static boolean isBusinessKey(String dataSetName, Set<String> column )
    {
        //SetCArdinality for claim contains only one column?
       // HashMap<String,HashMap<Set<String>,Integer>> setCard = (HashMap<String,HashMap<Set<String>,Integer>>) SETCARDINALITY.clone();
        HashMap<Set<String>,Integer> columnMap = SETCARDINALITY.get(dataSetName);
        //Let's find primaryKey first
       Set<String> primaryKey= Collections.max(columnMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
       Integer primaryKeyCardinality =  Collections.max(columnMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getValue();
       //the second best should be BK so let's remove PK and get max again
       // columnMap.remove(primaryKey);
        Map<Set<String>,Integer> tempMap = columnMap.entrySet()
                .stream()
                .filter(map->!map.getKey().equals(primaryKey))
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
        Set<String> businessKey = Collections.max(tempMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
        System.out.println(dataSetName+" BK "+ businessKey);

        return businessKey.equals(column);
    }


}
