package com.nereus;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.javatuples.Octet;
import org.javatuples.Pair;
import org.javatuples.Sextet;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import static com.nereus.HLLDB.*;

public class MetadataAccessor {


    /**
     * Returns conatiner of all datasets
     * @return
     */
    public static ArrayList<String> getNodes(){
        return NODES;
    }

    /**
     * Returns temporary container of joined datasets
     * @return
     */
    public static ArrayList<String> getTempJoinedNodes(){
        return TEMPJOINS;
    }

    /**
     *
     * @return
     */
    public static ArrayList<String> getTempRegroupedNodes(){
        return TEMPREGROUPED;
    }
    /**
     * Return collection of proper subsets for a dataset.
     * @param dataSetName
     * @return
     */
    public static Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>> getProperSubset(String dataSetName)
    {
        Collection  properSubset = (Collection) PROPERSUBSET.get(dataSetName);
        return properSubset;

    }

    /**
     * Return collection containing superset tuples
     * @param dataSetName
     * @return
     */
    public static Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>>  getSuperset( String dataSetName){
        Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>> superSet= (Collection) SUPERSET.get(dataSetName);
        return superSet;
    }

    /**
     * Return collection containing subset tuples
     * @param dataSetName
     * @return
     */
    public static Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>>  getSubset( String dataSetName){
        Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>> superSet= (Collection) SUBSET.get(dataSetName);
        return superSet;
    }

    public static Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>> getOverlap(String dataSetName)
    {
        Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>> overLap = (Collection) OVERLAP.get(dataSetName);
        return overLap;
    }

    /**
     * Return joined tuple.
     * @param dataSetName
     * @return
     */

    public static Triplet<String,String,Boolean> getJoinedTuple(String dataSetName)
    {
        Triplet<String,String,Boolean> triplet = JOINTRIPLET.get(dataSetName);
        return triplet;
    }

    /**
     * Return all tuples part of join or regroup.
     * @return
     */
    public  static Collection<Triplet<String,String,Boolean>> getJoinedTuples()
    {
        return  (Collection) JOINTRIPLET.values();

    }

    /**
     * Return all datasets names that are are part of join or regroup.
     * @return
     */

    public static Set<String> getDataSetsWithDescendants()
    {
        return JOINTRIPLET.keySet();
    }

    /**
     *
     * @return
     */
    public static Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>>  getConsumedTuples()
    {
        return (Collection) SEPTET.values();
    }

    /**
     *
     * @param dataSetName
     * @return
     */
    public static HashMap<Set<String>,Integer> getSetCardinality(String dataSetName){
        return SETCARDINALITY.get(dataSetName);
    }

    /**
     *
     * @param dataSetName
     */
    public static void putTempJoinedNode(String dataSetName){
        TEMPJOINS.add(dataSetName);
    }

    /**
     * Label operation as used in a join.
     * @param dataSetName
     * @param consumedTuple
     */
    public static void putConsumedTuple(String dataSetName,Sextet<String, Set<String>, Long, String, Set<String>, Long> consumedTuple){

        if(!SEPTET.containsValue(consumedTuple))
        {
            SEPTET.put(dataSetName,consumedTuple);
        }


    }

    /**
     * Insert superset tuple
     * @param dataSetName
     * @param superSetTuple
     */

    public static void putSupersetTuple(String dataSetName,Sextet<String, Set<String>, Long, String, Set<String>, Long> superSetTuple )
    {
        if(superSetTuple!=null) {
            SUPERSET.put(dataSetName, superSetTuple);
        }
    }

    /**
     * Insert subset tuple
     * @param dataSetName
     * @param subSetTuple
     */

    public static void putSubsetTuple(String dataSetName,Sextet<String, Set<String>, Long, String, Set<String>, Long> subSetTuple )
    {
        if(subSetTuple!=null) {
            SUBSET.put(dataSetName, subSetTuple);
        }
    }

    /**
     * Insert propersubset tuple
     * @param dataSetName
     * @param properSubsetTuple
     */
    public static void putProperSubsetTuple (String dataSetName,Sextet<String, Set<String>, Long, String, Set<String>, Long> properSubsetTuple) {
        if (properSubsetTuple != null) {
            PROPERSUBSET.put(dataSetName,properSubsetTuple);
        }
    }

    /**
     * Insert overlap tuple
     * @param dataSetName
     * @param overLapTuple
     */
    public static void putOverLapTuple(String dataSetName,Sextet<String, Set<String>, Long, String, Set<String>, Long> overLapTuple )
    {
        if(overLapTuple!=null)
        {
            OVERLAP.put(dataSetName,overLapTuple);
        }
    }

    /**
     * Insert ordered joins.
     * @param dataSetName
     * @param tuple
     */
    public static void putOrderedJoinTuple(String dataSetName,Sextet<String, Set<String>, Long, String, Set<String>, Long> tuple ){
        if(tuple!=null) {
            TMPJOINTUPLES.put(dataSetName, tuple);
        }
    }

    /**
     * Insert triple of format Triplet.with(lname, rname, true); Last value true means transformed before join.
     * @param dataSetName
     * @param transformed
     */
    public static void putTransformedDataset(String dataSetName, Triplet<String, String, Boolean> transformed ){
        JOINTRIPLET.put(dataSetName, transformed);
    }

    /**
     * Insert triple of format Triplet.with(lname, rname, false); Last value  means joined. Cannot be joined again.
     * @param dataSetName
     * @param joined
     */

    public static void putJoinedDataset(String dataSetName, Triplet<String, String, Boolean> joined ){
        JOINTRIPLET.put(dataSetName, joined);
    }
    /**
     *
     * @param dataSetName
     */
    public static void putTempRegroupedDataset(String dataSetName)
    {
        TEMPREGROUPED.add(dataSetName);
    }

    /**
     *
     * @param dataSetName
     * @param key
     */
    public static void  putDatasetKey (String dataSetName , String key)
    {
        IDENTIFIEDBY.put(dataSetName, key);
    }

    /**
     *
     * @param dataSetName
     * @param columnCardinality
     */
    public static void putSetCardinality(String dataSetName, HashMap<Set<String>,Integer> columnCardinality){
        SETCARDINALITY.put(dataSetName,columnCardinality);
    }

    public static void putOrderedUnionIntersection(String dataSetName, Octet<String,Set<String>,Long,String,Set<String>,Long,Long,Long> unionInter){
        UNIONINTER.put(dataSetName,unionInter);

    }

    /**Metadata necessary for NEO4J genereation**(
     *
     */
    /**
     * Properties necessary fore genereating graph in Neo4j
     * @param leftName
     * @param newName
     * @param transformed
     */
    public static void putNeoMetadata(String leftName, String newName,Triplet<String, String, Boolean> transformed ){
        TRANSFORMSTARTEND.put(leftName, newName);
        TRANSFORMSTARTEND2.put(newName, leftName);
        JOINORREGROUPTRIPLET.put(newName, transformed);
    }

    public static void putBuilderNeoMetadata(String leftName,String newTableName,String rightName, Sextet<String,Set<String>,Long,String,
            Set<String>,Long> tuple,Triplet<String, String, Boolean> joinedL )
    {
        JOINS.put(newTableName,tuple);
        JOINSSTARTEND.put(leftName,newTableName);
        JOINSSTARTEND.put(rightName,newTableName);
        Pair<String,String> startEnd = Pair.with(leftName,rightName);
        JOINSSTARTEND2.put(newTableName,startEnd);
        JOINORREGROUPTRIPLET.put(newTableName,joinedL);
    }
}
