package com.nereus;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.nereus.AvroUtils.PopulateSampledData;
import net.agkn.hll.HLL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static com.nereus.AvroUtils.Constants.KAFKA_GROUP_ID;
import static com.nereus.HLLDB.CUSTOMER_TOPIC;
import static com.nereus.HLLDB.POLICY_TOPIC;

public class DAO {
    public static HashMap<String,String> getSchemas()
    {

        HashFunction hashFunction = Hashing.murmur3_128();
        ArrayList<GraphNode> graph = new ArrayList<>();
        HashMap<String,String> setsTable = new HashMap();

        HashMap<String,HashMap<Set<String>,HLL>> custCardinalities2= new HashMap<>();
        HashMap<String,HashMap<Set<String>,HLL>> polCardinalities2= new HashMap<>();
        HashMap<String,HashMap<Set<String>,HLL>> claimCardinalities2= new HashMap<>();
        HashMap<String,HashMap<Set<String>,HLL>> payCardinalities2= new HashMap<>();
        HashMap<String,HashMap<Set<String>,HLL>> addressCardinalities2= new HashMap<>();
        HashMap<String,HashMap<Set<String>,HLL>> claimHandlerCardinalities2= new HashMap<>();



        custCardinalities2.put(CUSTOMER_TOPIC,new HashMap<>());
        polCardinalities2.put(POLICY_TOPIC,new HashMap<>());
       // polCardinalities2.put(CLAIM_TOPIC,new HashMap<>());

        ArrayList <HashMap<String,HashMap<Set<String>,HLL>>> hllCollection = new ArrayList<>();
        hllCollection.add(custCardinalities2);
        hllCollection.add(polCardinalities2);
        hllCollection.add(claimCardinalities2);
        hllCollection.add(payCardinalities2);
        hllCollection.add(addressCardinalities2);

        HashMap<String,Object> hashMap= PopulateSampledData.populateHLLObjects(CUSTOMER_TOPIC+","+POLICY_TOPIC,KAFKA_GROUP_ID,5000);


        @SuppressWarnings("unchecked")
        HashMap<String,String> schemas = (HashMap<String,String>)hashMap.get("schemaCollection");
        @SuppressWarnings("unchecked")
        ArrayList<HashMap<String, HashMap<Set<String>, HLL>>> hllCollection2 = (ArrayList<HashMap<String, HashMap<Set<String>, HLL>>> ) hashMap.get("hllCollection");

        hllCollection2.forEach((a) -> {

            Set<String> setAName = a.keySet();
            System.out.println("KeySet2 " + a.values().toString());
            a.forEach((k,v)->{
                System.out.println("_______??????????????????????CLAIM_TOPIC");
                System.out.println(k);
                v.forEach((k1,v1)->{
                    System.out.println(k1);
                    System.out.println(v1.cardinality());
                });
            });
        });
        @SuppressWarnings("unchecked")
        HashMap<String, Long> dataCountMap =
                (HashMap<String, Long> )hashMap.get("dataCountCollection");
        //"STATPEJ.POC_CLAIM","STATPEJ.POC_CLAIMPAYMENT","STATPEJ.POC_CUSTOMER","STATPEJ.POC_POLICY"

        System.out.println("################HASHMAP");
        System.out.println(hashMap);

     //   SetInfo.claimLines=dataCountMap.get(CLAIM_TOPIC).intValue();
        SetInfo.customerLines=dataCountMap.get(CUSTOMER_TOPIC).intValue();
        SetInfo.policyLines=dataCountMap.get(POLICY_TOPIC).intValue();


        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println(SetInfo.customerLines+" Customer Lines");
        System.out.println(SetInfo.policyLines+" Policy Lines");
        System.out.println(SetInfo.claimLines+" Claim Lines");
        System.out.println(SetInfo.paymentLines+" Payment lines");
        System.out.println(SetInfo.addressLines+" Address lines");
        System.out.println(SetInfo.claimLines+" ClaimHandlerLines ");

        hllCollection2.forEach((a) -> {

            Set<String> setAName = a.keySet();
            System.out.println("KeySet "+a.values().toString());
            hllCollection2.forEach((b)->{
                if (b.equals(a)){

                }
                else {
                    Set<String> setBName = b.keySet();
                    try {
                        // findCommonKeys2(a, b,setsTable);
                        // findCommonKeys3(a, b,setAName,setBName);
                        Builder.fit(a, b,setAName,setBName);

                    } //catch( CloneNotSupportedException e)
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                }


            });
        });


        return schemas;
    }
}
