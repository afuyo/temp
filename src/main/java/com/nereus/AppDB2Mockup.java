package com.nereus;


import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.nereus.AvroUtils.PopulateSampledData;
import com.nereus.Utils.NeoUtils;
import com.nereus.Utils.Output;
import net.agkn.hll.HLL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static com.nereus.AvroUtils.Constants.KAFKA_GROUP_ID;
import static com.nereus.HLLDB.*;
//import java.lang.reflect.Array;

/**Adding second iteration **/

public class AppDB2Mockup {


    /**********************************************************************/

    /************************************************************************/



   public static void generateGraph()
   {

       int next= NODES.size();
       int start= 0;
       int j= 1;

       while(start<next)
       {
           start=next;
           //joinNodes55();
           Builder.joinNodes();
           String s = "Metadata "+j+" for ";

           if(true) {
               NODES.forEach((e) -> {
                   System.out.println("'''''''''''***************** " + s + e);
                   Output.printMetaData(e);


               });
           }
           j++;
           next= NODES.size();
       }

   }

    public static void main(String[] arg) throws IOException, CloneNotSupportedException {
      //  System.out.println("Hello");
        //System.out.println(SetInfo.customerLines);

        HashFunction hashFunction = Hashing.murmur3_128();
        ArrayList<GraphNode> graph = new ArrayList<>();
        HashMap<String,String> setsTable = new HashMap();
        MetadataService.hardCodeBusinessKeysAvro();
        MetadataService.hardCodeForeignKeysAvro();
      //  HashMap<String,String> schemas = DAO.getSchemas();

        MockUp.clearSets();
        MockUp.printSets();
        MockUp.populateSetsAvro();
        MockUp.printSets();
        generateGraph();


        /*System.out.println("JoinTrippelt");
        JOINTRIPLET.forEach((k,v)->{
            System.out.println(k.toString());
            System.out.println(v.toString());
            //System.out.println(k.contains("Payment1"));
            //System.out.println(v.contains("Payment1"));
            // System.out.println(joinTriplet.keySet().contains("Payment1"));
        });



        System.out.println("Septet");



        SEPTET.forEach((k,v)->{
            System.out.println(k.toString()+" "+v.toString());

        });*/

        //NeoUtils.writeToCSV();
        //NeoUtils.writeToNeo(schemas);
        //NeoUtils.writeToNeo2();
       // NeoUtils.writeToNeo3(schemas);
       // NeoUtils.writeToNeo();

       /** System.out.println("TempRegrouped "+(tempRegrouped.hashCode()));
        System.out.println("TempRegrouped "+(tempRegrouped.toString()));
        System.out.println("Idenfied by "+(IDENTIFIEDBY.hashCode()));
        System.out.println("Idenfied by "+(IDENTIFIEDBY.toString()));
        System.out.println("Septetst are equal "+(septet.hashCode()==TestClass.septet));
        System.out.println("SUPERSETs are equal "+(SUPERSET.hashCode()==TestClass.superset));
        System.out.println("Subsets are equal "+(subset.hashCode()==TestClass.subset));
        System.out.println("Overlap is equal "+(overlap.hashCode()==TestClass.overlap));
        System.out.println("JointTriplet is equal "+(joinTriplet.hashCode()==TestClass.jointriplet));
        System.out.println("Identified by "+(IDENTIFIEDBY.hashCode()==TestClass.identifiedBy));**/


        //  System.out.println("Get joining tuple for Customer & Claim"+getJoiningTuple("Customer","Claim").toString());
        //System.out.println("Is subset Customer Claim "+isSubset2("Customer","Claim"));
        //  System.out.println("getJoininTupe"+getJoiningTuple("Policy","Customer").toString());
        // System.out.println("getJoiningTuple2"+getJoiningTuple2("Policy","Customer").toString());
       // System.out.println("Septet "+septet.toString());

       /* SEPTET.forEach((k,v)->{
            System.out.println("Septet%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%55");
            System.out.println("Key "+k);
            System.out.println("Value "+v);
        });
*/
       // TestCases.getMaxCardinalityTuple2Test();

        // TestCases.isMatcgProperSubsetTest();
        //Payment1&Claim1
        //Customer&Policy1
        //isSubset("Payment1&Claim1","Customer&Policy1");

        //System.out.println("Right Key "+getRightKey("Customer&Policy"));
        //System.out.println("Left key "+getLeftKey("Customer&Policy"));

        //		String sourceTopicList = "STATPEJ.POC_CLAIM,STATPEJ.POC_CLAIMPAYMENT,STATPEJ.POC_CUSTOMER,STATPEJ.POC_POLICY";
//		int consumerGroupId = 127;
//		long targetTotalCount = 50000;
         //HashMap<String,Object> hashMap=PopulateSampledData.populateHLLObjects("STATPEJ.POC_CLAIM,STATPEJ.POC_CLAIMPAYMENT,STATPEJ.POC_CUSTOMER,STATPEJ.POC_POLICY",135,50000);
         //System.out.println(schemas.get("STATPEJ.POC_CUSTOMER"));
        // System.out.println(schemas.get("STATPEJ.POC_POLICY"));
        //Schema schema= SchemaGeneratorUtils.generateJoinedSchema(schemas.get("STATPEJ.POC_CUSTOMER"),"STATPEJ.POC_CUSTOMER",schemas.get("STATPEJ.POC_POLICY"),"STATPEJ.POC_POLICY");
        //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@22222");
        //System.out.println("schema"+schema.toString());
        // System.out.println("Schema "+schema.toString());
        /**@SuppressWarnings("unchecked")
        HashMap<String,String> schemas = (HashMap<String,String>)hashMap.get("schemaCollection");
        @SuppressWarnings("unchecked")
        ArrayList<HashMap<String, HashMap<Set<String>, HLL>>> hllCollection2 = (ArrayList<HashMap<String, HashMap<Set<String>, HLL>>> ) hashMap.get("hllCollection");

        hllCollection2.forEach((a) -> {

                    Set<String> setAName = a.keySet();
                    System.out.println("KeySet2 " + a.values().toString());
                });**/

        //System.out.println(schemas.get("STATPEJ.POC_CLAIM"));
/*
        System.out.println("################&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        //System.out.println(schemas.toString());
        System.out.println("getKey "+NeoUtils.getKey("STATPEJ.POC_CLAIMPAYMENT1&STATPEJ.POC_CLAIM1"));
        System.out.println("@@@@@@SCHEMAAAAAAAAASSSSSSSSSS");
        schemas.forEach((k,v)->{

            System.out.println("Key "+k);
            System.out.println("Value "+v);
        });
        System.out.println("@@@@@@SCHEMAAAAAAAAASSSSSSSSSS");*/

       /** NODES.forEach((e)-> {
            //System.out.println("SCHEMA "+e+"---"+schemas.get(e));

            if(JOINORREGROUPTRIPLET.get(e)!=null) {
                if (!(boolean) JOINORREGROUPTRIPLET.get(e).getValue2()) {
                    //false is a join
                    String left = JOINORREGROUPTRIPLET.get(e).getValue0().toString();
                    String right = JOINORREGROUPTRIPLET.get(e).getValue1().toString();
                    String leftSchema = schemas.get(left);
                    String rightSchema = schemas.get(right);
                    System.out.println("left "+left);
                    System.out.println("right "+right);
                    System.out.println("rightSchema "+rightSchema);
                    System.out.println("leftSchema "+leftSchema);

                   System.out.println("Schema " + SchemaGeneratorUtils.generateJoinedSchema(leftSchema, left, rightSchema, right));

                }
            }

        });**/

      /**  SEPTET.forEach((k,v)->{
            System.out.println("Key "+k);
            System.out.println("Value "+v);

        });**/

//     System.out.println("GETObjectName "+NeoUtils.getObjectName("STATPEJ.POC_CLAIMPAYMENT21AndSTATPEJ.POC_CLAIM21"));

/*     System.out.println("JOINREGROUPED");
     JOINORREGROUPTRIPLET.forEach((k,v)->{
         System.out.println("Key "+k);
         System.out.println("Value "+v);
     });
    // NeoUtils.createAvroSchemas(schemas);*/
   /**  System.out.println("AVRO_SCEMAS");
     AVRO_SCHEMAS.forEach((k,v)->{
         System.out.println("Key "+k);
         System.out.println("Value "+v);
     });**/

    /** System.out.println("Nodes");
     NODES.forEach((e)->
     {
         System.out.println(e);
     });
    System.out.println("UNIONINTER");
     UNIONINTER.forEach((k,v)->{
         long i= (long)v.getValue7();
         if(i>0L) {
             System.out.println("Key " + k);
             System.out.println("Value " + v);
         }
     });**/

    /**    System.out.println("SETCARDINALITY");
        SETCARDINALITY.forEach((k,v)->{
            System.out.println("Key " + k);
            System.out.println("Value " + v);
        });**/
    }//main
}//class