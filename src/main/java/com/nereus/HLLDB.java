package com.nereus;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.javatuples.Pair;
import org.javatuples.Sextet;
import org.javatuples.Triplet;
import org.javatuples.Octet;

import java.util.*;

public class HLLDB {

    /**AVRO **/



   /**ADRESS LOCALLY*/
  /**  public static final String CUSTOMER_TOPIC = "Customer";
    public static final String POLICY_TOPIC = "Policy";
    public static final String CLAIM_TOPIC = "Claim";
    public static final String PAYMENT_TOPIC = "Payment";
    public static final String ADDRESS_TOPIC = "Address";*/

  /**  public static ArrayList<String> SOURCE_NODES= new ArrayList<>(Arrays.asList("STATPEJ.POC_CLAIM2","STATPEJ.POC_CLAIMPAYMENT2","STATPEJ.POC_CUSTOMER2","STATPEJ.POC_POLICY2","STATPEJ.POC_ADDRESS2"));
    public static ArrayList<String> NODES= new ArrayList<>(Arrays.asList("STATPEJ.POC_CLAIM2","STATPEJ.POC_CLAIMPAYMENT2","STATPEJ.POC_CUSTOMER2","STATPEJ.POC_POLICY2","STATPEJ.POC_ADDRESS2"));
    public static ArrayList<String> STREAMNODES = new ArrayList<>(Arrays.asList("ClaimSt","CustomerSt","PaymentSt","PolicySt","AddressSt"));**/


 //local
   /**public static ArrayList<String> NODES = new ArrayList<>(Arrays.asList("Claim","Customer","Payment","Policy","Address"));
   public static ArrayList<String> STREAMNODES = new ArrayList<>(Arrays.asList("Claim","Customer","Payment","Policy","Address"));**/
   /***                  ****************************/

    public static ArrayList<String> TEMPREGROUPED = new ArrayList<>();
    public static ArrayList<String> TEMPJOINS = new ArrayList<>();
    public static MultiMap<String,String> IDENTIFIEDBY = new MultiValueMap<>();
   // public static HashMap<String,Set<String>> IDENTIFIEDBY2 = new HashMap<>();
    public static MultiMap<String,Sextet> SUPERSET = new MultiValueMap<>();
    public static MultiMap<String,Sextet> SUBSET = new MultiValueMap<>();
    public static MultiMap<String,Sextet<String,Set<String>,Long,String,Set<String>,Long>> PROPERSUBSET = new MultiValueMap<>();
    public static MultiMap<String,Sextet> OVERLAP = new MultiValueMap<>();
    public static MultiMap<String,Sextet> SEPTET = new MultiValueMap<>();

    //shows all joins and regroups if true regroug else join
    public static HashMap<String,Triplet> JOINTRIPLET = new HashMap<>();
    //capture carinality for each column in order to find BK

    public static HashMap<String,HashMap<Set<String>,Integer>> SETCARDINALITY = new HashMap<>();
    public static HashMap<String,Set<String>> TEMPBKEYS = new HashMap<>();
    public static HashMap<String,Set<String>> TEMPFKEYS = new HashMap<>();

    /**Graph Metadata ********************************************/
    //contains joins and regroups, true if regroup. <newTablename,<lname,rname,TRUE/FALSE>
    //public static LinkedHashMap<String,Triplet> JOINORREGROUPTRIPLET = new LinkedHashMap<>();
    public static Multimap<String,Triplet> JOINORREGROUPTRIPLET = LinkedListMultimap.create();
    public static MultiMap<String,Sextet> JOINS = new MultiValueMap<>();
    public static HashMap<String,String> JOINSSTARTEND = new HashMap<>();
    public static HashMap<String,Pair> JOINSSTARTEND2 = new HashMap<>();
    public static HashMap<String,String> TRANSFORMSTARTEND = new HashMap<>();
    public static HashMap<String,String> TRANSFORMSTARTEND2 = new HashMap<>();
    public static HashMap<String,String> AVRO_SCHEMAS = new HashMap<>();
    public static HashMap<String,String> streamTableMapping = new HashMap<>();
    public static Multimap<String,Sextet> TMPJOINTUPLES = LinkedListMultimap.create();

    /**LOGGING **/
    //Builder.fit
    public static Multimap<String,Octet> UNIONINTER = LinkedListMultimap.create();

    /**ADDRESS AVRO*/

    //avro

/**
    public static final String CUSTOMER_TOPIC ="STATPEJ.POC_CUSTOMER2";
    public static final String POLICY_TOPIC ="STATPEJ.POC_POLICY2";
    public static final String CLAIM_TOPIC  = "STATPEJ.POC_CLAIM2";
    public static final String PAYMENT_TOPIC = "STATPEJ.POC_CLAIMPAYMENT2";
    public static final String ADDRESS_TOPIC = "STATPEJ.POC_ADDRESS2";
    public static ArrayList<String> SOURCE_NODES= new ArrayList<>(Arrays.asList("STATPEJ.POC_CLAIM2","STATPEJ.POC_CUSTOMER2","STATPEJ.POC_CLAIMPAYMENT2","STATPEJ.POC_POLICY2","STATPEJ.POC_ADDRESS2"));
    public static ArrayList<String> NODES= new ArrayList<>(Arrays.asList("STATPEJ.POC_CLAIM2","STATPEJ.POC_CUSTOMER2","STATPEJ.POC_CLAIMPAYMENT2","STATPEJ.POC_POLICY2","STATPEJ.POC_ADDRESS2"));
    public static ArrayList<String> STREAMNODES = new ArrayList<>(Arrays.asList("ClaimSt","CustomerSt","PaymentSt","PolicySt","AddressSt"));
    static {


       // streamTableMapping.put("STATPEJ.POC_CLAIM","ClaimSt");
       // streamTableMapping.put("STATPEJ.POC_CUSTOMER","CustomerSt");
       // streamTableMapping.put("STATPEJ.POC_CLAIMPAYMENT","PaymentSt");
       // streamTableMapping.put("STATPEJ.POC_POLICY","PolicySt");


        streamTableMapping.put("STATPEJ.POC_CLAIM2","ClaimSt");
        streamTableMapping.put("STATPEJ.POC_CUSTOMER2","CustomerSt");
        streamTableMapping.put("STATPEJ.POC_CLAIMPAYMENT2","PaymentSt");
        streamTableMapping.put("STATPEJ.POC_POLICY2","PolicySt");
        streamTableMapping.put("STATPEJ.POC_ADDRESS2","AddressSt");
    }
    public static HashMap<String,String> streamTopicMapping = new HashMap();
    static {

        streamTableMapping.put("ClaimSt","STATPEJ.POC_CLAIM2");
        streamTableMapping.put("CustomerSt","STATPEJ.POC_CUSTOMER2");
        streamTableMapping.put("PaymentSt","STATPEJ.POC_CLAIMPAYMENT2");
        streamTableMapping.put("PolicySt","STATPEJ.POC_POLICY2");
        streamTableMapping.put("AddressSt","STATPEJ.POC_ADDRESS2");
    }
 **/

 /**ADDRESS AVRO3*/

 //avro
/***

 public static final String CUSTOMER_TOPIC ="STATPEJ.POC_CUSTOMER3";
 public static final String POLICY_TOPIC ="STATPEJ.POC_POLICY3";
 public static final String CLAIM_TOPIC  = "STATPEJ.POC_CLAIM3";
 public static final String PAYMENT_TOPIC = "STATPEJ.POC_CLAIMPAYMENT3";
 public static final String ADDRESS_TOPIC = "STATPEJ.POC_ADDRESS3";
 public static final String CLAIMHANDLER_TOPIC = "STATPEJ.POC_CLAIMHANDLER4";
 public static ArrayList<String> SOURCE_NODES= new ArrayList<>(Arrays.asList("STATPEJ.POC_CLAIM3","STATPEJ.POC_CUSTOMER3","STATPEJ.POC_CLAIMPAYMENT3","STATPEJ.POC_POLICY3","STATPEJ.POC_ADDRESS3"));
 public static ArrayList<String> NODES= new ArrayList<>(Arrays.asList("STATPEJ.POC_CLAIM3","STATPEJ.POC_CUSTOMER3","STATPEJ.POC_CLAIMPAYMENT3","STATPEJ.POC_POLICY3","STATPEJ.POC_ADDRESS3"));
 public static ArrayList<String> STREAMNODES = new ArrayList<>(Arrays.asList("ClaimSt","CustomerSt","PaymentSt","PolicySt","AddressSt"));
 static {


  // streamTableMapping.put("STATPEJ.POC_CLAIM","ClaimSt");
  // streamTableMapping.put("STATPEJ.POC_CUSTOMER","CustomerSt");
  // streamTableMapping.put("STATPEJ.POC_CLAIMPAYMENT","PaymentSt");
  // streamTableMapping.put("STATPEJ.POC_POLICY","PolicySt");


  streamTableMapping.put("STATPEJ.POC_CLAIM3","ClaimSt");
  streamTableMapping.put("STATPEJ.POC_CUSTOMER3","CustomerSt");
  streamTableMapping.put("STATPEJ.POC_CLAIMPAYMENT3","PaymentSt");
  streamTableMapping.put("STATPEJ.POC_POLICY3","PolicySt");
  streamTableMapping.put("STATPEJ.POC_ADDRESS3","AddressSt");
 }
 public static HashMap<String,String> streamTopicMapping = new HashMap();
 static {

  streamTableMapping.put("ClaimSt","STATPEJ.POC_CLAIM3");
  streamTableMapping.put("CustomerSt","STATPEJ.POC_CUSTOMER3");
  streamTableMapping.put("PaymentSt","STATPEJ.POC_CLAIMPAYMENT3");
  streamTableMapping.put("PolicySt","STATPEJ.POC_POLICY3");
  streamTableMapping.put("AddressSt","STATPEJ.POC_ADDRESS3");
 }
**/
 /**ADDRESS AVRO4*/

 //avro

/***
 public static final String CUSTOMER_TOPIC ="STATPEJ.POC_CUSTOMER4";
 public static final String POLICY_TOPIC ="STATPEJ.POC_POLICY4";
 public static final String CLAIM_TOPIC  = "STATPEJ.POC_CLAIM4";
 public static final String PAYMENT_TOPIC = "STATPEJ.POC_CLAIMPAYMENT4";
 public static final String ADDRESS_TOPIC = "STATPEJ.POC_ADDRESS4";
 public static final String CLAIMHANDLER_TOPIC = "STATPEJ.POC_CLAIMHANDLER4";
 public static ArrayList<String> SOURCE_NODES= new ArrayList<>(Arrays.asList("STATPEJ.POC_CLAIM4","STATPEJ.POC_CUSTOMER4","STATPEJ.POC_CLAIMPAYMENT4","STATPEJ.POC_POLICY4","STATPEJ.POC_ADDRESS4","STATPEJ.POC_CLAIMHANDLER4"));
 public static ArrayList<String> NODES= new ArrayList<>(Arrays.asList("STATPEJ.POC_CLAIM4","STATPEJ.POC_CUSTOMER4","STATPEJ.POC_CLAIMPAYMENT4","STATPEJ.POC_POLICY4","STATPEJ.POC_ADDRESS4","STATPEJ.POC_CLAIMHANDLER4"));
 public static ArrayList<String> STREAMNODES = new ArrayList<>(Arrays.asList("ClaimSt","CustomerSt","PaymentSt","PolicySt","AddressSt","ClaimHandlerSt"));
 static {


 // streamTableMapping.put("STATPEJ.POC_CLAIM","ClaimSt");
 // streamTableMapping.put("STATPEJ.POC_CUSTOMER","CustomerSt");
 // streamTableMapping.put("STATPEJ.POC_CLAIMPAYMENT","PaymentSt");
 // streamTableMapping.put("STATPEJ.POC_POLICY","PolicySt");


 streamTableMapping.put("STATPEJ.POC_CLAIM4","ClaimSt");
 streamTableMapping.put("STATPEJ.POC_CUSTOMER4","CustomerSt");
 streamTableMapping.put("STATPEJ.POC_CLAIMPAYMENT4","PaymentSt");
 streamTableMapping.put("STATPEJ.POC_POLICY4","PolicySt");
 streamTableMapping.put("STATPEJ.POC_ADDRESS4","AddressSt");
  streamTableMapping.put("STATPEJ.POC_CLAIMHANDLER4","ClaimHandlerSt");
 }
 public static HashMap<String,String> streamTopicMapping = new HashMap();
 static {

 streamTableMapping.put("ClaimSt","STATPEJ.POC_CLAIM4");
 streamTableMapping.put("CustomerSt","STATPEJ.POC_CUSTOMER4");
 streamTableMapping.put("PaymentSt","STATPEJ.POC_CLAIMPAYMENT4");
 streamTableMapping.put("PolicySt","STATPEJ.POC_POLICY4");
 streamTableMapping.put("AddressSt","STATPEJ.POC_ADDRESS4");
  streamTableMapping.put("ClaimHandlerSt","STATPEJ.POC_CLAIMHANDLER4");
 }

**/
/**AVRO 5**/
/**
 public static final String CUSTOMER_TOPIC ="STATPEJ.POC_CUSTOMER5";
 public static final String POLICY_TOPIC ="STATPEJ.POC_POLICY5";
 public static final String CLAIM_TOPIC  = "STATPEJ.POC_CLAIM5";
 public static final String PAYMENT_TOPIC = "STATPEJ.POC_CLAIMPAYMENT5";
 public static final String ADDRESS_TOPIC = "STATPEJ.POC_ADDRESS5";
 public static final String CLAIMHANDLER_TOPIC = "STATPEJ.POC_CLAIMHANDLER5";
 public static ArrayList<String> SOURCE_NODES= new ArrayList<>(Arrays.asList("STATPEJ.POC_CLAIM5","STATPEJ.POC_CUSTOMER5","STATPEJ.POC_CLAIMPAYMENT5","STATPEJ.POC_POLICY5","STATPEJ.POC_ADDRESS5","STATPEJ.POC_CLAIMHANDLER5"));
 public static ArrayList<String> NODES= new ArrayList<>(Arrays.asList("STATPEJ.POC_CLAIM5","STATPEJ.POC_CUSTOMER5","STATPEJ.POC_CLAIMPAYMENT5","STATPEJ.POC_POLICY5","STATPEJ.POC_ADDRESS5","STATPEJ.POC_CLAIMHANDLER5"));
 public static ArrayList<String> STREAMNODES = new ArrayList<>(Arrays.asList("ClaimSt","CustomerSt","PaymentSt","PolicySt","AddressSt","ClaimHandlerSt"));
 static {


 // streamTableMapping.put("STATPEJ.POC_CLAIM","ClaimSt");
 // streamTableMapping.put("STATPEJ.POC_CUSTOMER","CustomerSt");
 // streamTableMapping.put("STATPEJ.POC_CLAIMPAYMENT","PaymentSt");
 // streamTableMapping.put("STATPEJ.POC_POLICY","PolicySt");


 streamTableMapping.put("STATPEJ.POC_CLAIM5","ClaimSt");
 streamTableMapping.put("STATPEJ.POC_CUSTOMER5","CustomerSt");
 streamTableMapping.put("STATPEJ.POC_CLAIMPAYMENT5","PaymentSt");
 streamTableMapping.put("STATPEJ.POC_POLICY5","PolicySt");
 streamTableMapping.put("STATPEJ.POC_ADDRESS5","AddressSt");
 streamTableMapping.put("STATPEJ.POC_CLAIMHANDLER5","ClaimHandlerSt");
 }
 public static HashMap<String,String> streamTopicMapping = new HashMap();
 static {

 streamTableMapping.put("ClaimSt","STATPEJ.POC_CLAIM5");
 streamTableMapping.put("CustomerSt","STATPEJ.POC_CUSTOMER5");
 streamTableMapping.put("PaymentSt","STATPEJ.POC_CLAIMPAYMENT5");
 streamTableMapping.put("PolicySt","STATPEJ.POC_POLICY5");
 streamTableMapping.put("AddressSt","STATPEJ.POC_ADDRESS5");
 streamTableMapping.put("ClaimHandlerSt","STATPEJ.POC_CLAIMHANDLER5");
 }


**/
/**AVRO 5 WITH A2 **/
/**
 public static final String CUSTOMER_TOPIC ="STATPEJ.POC_CUSTOMER5";
 public static final String POLICY_TOPIC ="STATPEJ.POC_POLICY5";
 public static final String CLAIM_TOPIC  = "STATPEJ.POC_CLAIM5";
 public static final String PAYMENT_TOPIC = "STATPEJ.POC_CLAIMPAYMENT5";
 public static final String ADDRESS_TOPIC = "STATPEJ.POC_ADDRESS5";
    public static final String ADDRESS2_TOPIC = "STATPEJ.POC_ADDRESS52";
 public static final String CLAIMHANDLER_TOPIC = "STATPEJ.POC_CLAIMHANDLER5";
 public static ArrayList<String> SOURCE_NODES= new ArrayList<>(Arrays.asList("STATPEJ.POC_CLAIM5","STATPEJ.POC_CUSTOMER5","STATPEJ.POC_CLAIMPAYMENT5","STATPEJ.POC_POLICY5","STATPEJ.POC_ADDRESS5","STATPEJ.POC_CLAIMHANDLER5","STATPEJ.POC_ADDRESS52"));
 public static ArrayList<String> NODES= new ArrayList<>(Arrays.asList("STATPEJ.POC_CLAIM5","STATPEJ.POC_CUSTOMER5","STATPEJ.POC_CLAIMPAYMENT5","STATPEJ.POC_POLICY5","STATPEJ.POC_ADDRESS5","STATPEJ.POC_CLAIMHANDLER5","STATPEJ.POC_ADDRESS52"));
 public static ArrayList<String> STREAMNODES = new ArrayList<>(Arrays.asList("ClaimSt","CustomerSt","PaymentSt","PolicySt","AddressSt","ClaimHandlerSt"));
 static {


 // streamTableMapping.put("STATPEJ.POC_CLAIM","ClaimSt");
 // streamTableMapping.put("STATPEJ.POC_CUSTOMER","CustomerSt");
 // streamTableMapping.put("STATPEJ.POC_CLAIMPAYMENT","PaymentSt");
 // streamTableMapping.put("STATPEJ.POC_POLICY","PolicySt");


 streamTableMapping.put("STATPEJ.POC_CLAIM5","ClaimSt");
 streamTableMapping.put("STATPEJ.POC_CUSTOMER5","CustomerSt");
 streamTableMapping.put("STATPEJ.POC_CLAIMPAYMENT5","PaymentSt");
 streamTableMapping.put("STATPEJ.POC_POLICY5","PolicySt");
 streamTableMapping.put("STATPEJ.POC_ADDRESS5","AddressSt");
     streamTableMapping.put("STATPEJ.POC_ADDRESS52","AddressSt");
 streamTableMapping.put("STATPEJ.POC_CLAIMHANDLER5","ClaimHandlerSt");
 }
 public static HashMap<String,String> streamTopicMapping = new HashMap();
 static {

 streamTableMapping.put("ClaimSt","STATPEJ.POC_CLAIM5");
 streamTableMapping.put("CustomerSt","STATPEJ.POC_CUSTOMER5");
 streamTableMapping.put("PaymentSt","STATPEJ.POC_CLAIMPAYMENT5");
 streamTableMapping.put("PolicySt","STATPEJ.POC_POLICY5");
 streamTableMapping.put("AddressSt","STATPEJ.POC_ADDRESS5");
     streamTableMapping.put("AddressSt","STATPEJ.POC_ADDRESS52");
 streamTableMapping.put("ClaimHandlerSt","STATPEJ.POC_CLAIMHANDLER5");
 }

**/
    /*****LOCAL FILES ADDRESS*/
    //local
/**
   public static ArrayList<String> SOURCE_NODES= new ArrayList<>(Arrays.asList("Claim","Customer","Payment","Policy","Address"));
    public static ArrayList<String> NODES = new ArrayList<>(Arrays.asList("Claim","Customer","Payment","Policy","Address"));
    // public static ArrayList<String> STREAMNODES = new ArrayList<>(Arrays.asList("Claim","Customer","Payment","Policy","Address"));
     //with streams
     public static ArrayList<String> STREAMNODES = new ArrayList<>(Arrays.asList("ClaimSt","CustomerSt","PaymentSt","PolicySt","AddressSt"));
     public static final String CUSTOMER_TOPIC = "Customer";
     public static final String POLICY_TOPIC = "Policy";
     public static final String CLAIM_TOPIC = "Claim";
     public static final String PAYMENT_TOPIC = "Payment";
     public static final String ADDRESS_TOPIC = "Address";
     public static final String CLAIMHANDLER_TOPIC = "ClaimHandler";
       static {


        streamTableMapping.put("Claim","ClaimSt");
        streamTableMapping.put("Customer","CustomerSt");
        streamTableMapping.put("Payment","PaymentSt");
        streamTableMapping.put("Policy","PolicySt");
        streamTableMapping.put("Address","AddressSt");
}
    public static HashMap<String,String> streamTopicMapping = new HashMap();
static {


        streamTableMapping.put("ClaimSt","Claim");
        streamTableMapping.put("CustomerSt","Customer");
        streamTableMapping.put("PaymentSt","Payment");
        streamTableMapping.put("PolicySt","Policy");
        streamTableMapping.put("AddressSt","Address");
        }

**/

    /**********************************************************************/

    /**DB2 TEST **/

 public static final String CUSTOMER_TOPIC ="DB2N.TNKU016";
 public static final String POLICY_TOPIC ="DB2N.TNKU017";
// public static final String CLAIM_TOPIC  = "STATPEJ.POC_CLAIM5";
// public static final String PAYMENT_TOPIC = "STATPEJ.POC_CLAIMPAYMENT5";
// public static final String ADDRESS_TOPIC = "STATPEJ.POC_ADDRESS5";
// public static final String ADDRESS2_TOPIC = "STATPEJ.POC_ADDRESS52";
// public static final String CLAIMHANDLER_TOPIC = "STATPEJ.POC_CLAIMHANDLER5";
 public static ArrayList<String> SOURCE_NODES= new ArrayList<>(Arrays.asList("DB2N.TNKU016","DB2N.TNKU017"));
 public static ArrayList<String> NODES= new ArrayList<>(Arrays.asList("DB2N.TNKU016","DB2N.TNKU017"));

 public static ArrayList<String> STREAMNODES = new ArrayList<>(Arrays.asList("TNKU016St","TNKU017St"));
 static {


 // streamTableMapping.put("STATPEJ.POC_CLAIM","ClaimSt");
 // streamTableMapping.put("STATPEJ.POC_CUSTOMER","CustomerSt");
 // streamTableMapping.put("STATPEJ.POC_CLAIMPAYMENT","PaymentSt");
 // streamTableMapping.put("STATPEJ.POC_POLICY","PolicySt");


 streamTableMapping.put("DB2N.TNKU016","TNKU016St");
 streamTableMapping.put("DB2N.TNKU017","TNKU017St");

 }
 public static HashMap<String,String> streamTopicMapping = new HashMap();
 static {

 streamTableMapping.put("TNKU016St","DB2N.TNKU016");
 streamTableMapping.put("TNKU017St","\"DB2N.TNKU017");

 }


 /** public static ArrayList<String> NODES = new ArrayList<>(Arrays.asList("Claim","Customer","Payment","Policy"));
  public  static ArrayList<String> STREAMNODES = new ArrayList<>(Arrays.asList("ClaimSt","CustomerSt","PaymentSt","PolicySt"));
 public static ArrayList<String> SOURCE_NODES= new ArrayList<>(Arrays.asList("Claim","Customer","Payment","Policy","Address"));
  public static final String CUSTOMER_TOPIC = "Customer";
 public static final String POLICY_TOPIC = "Policy";
 public static final String CLAIM_TOPIC = "Claim";
 public static final String PAYMENT_TOPIC = "Payment";
 public static final String ADDRESS_TOPIC = "Address";

 static {


  streamTableMapping.put("Claim","ClaimSt");
  streamTableMapping.put("Customer","CustomerSt");
  streamTableMapping.put("Payment","PaymentSt");
  streamTableMapping.put("Policy","PolicySt");
  streamTableMapping.put("Address","AddressSt");
 }
 public static HashMap<String,String> streamTopicMapping = new HashMap();
 static {


  streamTableMapping.put("ClaimSt","Claim");
  streamTableMapping.put("CustomerSt","Customer");
  streamTableMapping.put("PaymentSt","Payment");
  streamTableMapping.put("PolicySt","Policy");
  streamTableMapping.put("AddressSt","Address");
 }**/


 // static ArrayList<String> tablenodes = new ArrayList<>(Arrays.asList("Claim","Customer","Payment","Policy"));

    //NOT USEDstatic ArrayList<String> streamnodes = new ArrayList<>(Arrays.asList("STATPEJ.POC_CLAIM","STATPEJ.POC_CLAIMPAYMENT","STATPEJ.POC_CUSTOMER","STATPEJ.POC_POLICY"));

    //NOTUSED public static ArrayList<String> TABLENODES = new ArrayList<>(Arrays.asList("STATPEJ.POC_CLAIM2","STATPEJ.POC_CLAIMPAYMENT2","STATPEJ.POC_CUSTOMER2","STATPEJ.POC_POLICY2","STATPEJ.POC_ADDRESS2"));

    //static ArrayList<String> nodes = new ArrayList<>(Arrays.asList("POC_CLAIMList","POC_CLAIMPAYMENTList","POC_CUSTOMERList","POC_POLICYList"));
    //static ArrayList<String> streamnodes = new ArrayList<>(Arrays.asList("POC_CLAIM","POC_CLAIMPAYMENT","POC_CUSTOMER","POC_POLICY"));
    //static ArrayList<String> tablenodes = new ArrayList<>(Arrays.asList("STATPEJ.POC_CLAIM","STATPEJ.POC_CLAIMPAYMENT","STATPEJ.POC_CUSTOMER","STATPEJ.POC_POLICY"));
    /***USE THIS for Neo512 and Neo512Clean**/
    /** ApproximataleyEqual needs to be changed to exactl =  --Builder.java ln 409 **/

/***
    public static ArrayList<String> SOURCE_NODES= new ArrayList<>(Arrays.asList("Claim","Customer","Payment","Policy","Address"));
   public static ArrayList<String> NODES = new ArrayList<>(Arrays.asList("Claim","Customer","Payment","Policy"));
    public static ArrayList<String> STREAMNODES = new ArrayList<>(Arrays.asList("ClaimSt","CustomerSt","PaymentSt","PolicySt"));
   public static ArrayList<String> tablenodes = new ArrayList<>(Arrays.asList("Claim","Customer","Payment","Policy"));
     public static final String CUSTOMER_TOPIC = "Customer";
     public static final String POLICY_TOPIC = "Policy";
     public static final String CLAIM_TOPIC = "Claim";
     public static final String PAYMENT_TOPIC = "Payment";
     public static final String ADDRESS_TOPIC = "Address";
    //static ArrayList<String> nodes = new ArrayList<>(Arrays.asList("Claim","Customer","Payment","Policy","Address"));
 ************************/
    /** USE THIS for avro schema stuff*/
    // static ArrayList<String> nodes = new ArrayList<>(Arrays.asList("STATPEJ.POC_CLAIM","STATPEJ.POC_CLAIMPAYMENT","STATPEJ.POC_CUSTOMER","STATPEJ.POC_POLICY"));
    //static ArrayList<String> streamnodes = new ArrayList<>(Arrays.asList("STATPEJ.POC_CLAIM","STATPEJ.POC_CLAIMPAYMENT","STATPEJ.POC_CUSTOMER","STATPEJ.POC_POLICY"));
    // static ArrayList<String> streamnodes = new ArrayList<>(Arrays.asList("ClaimSt","CustomerSt","PaymentSt","PolicySt"));
    // static ArrayList<String> tablenodes = new ArrayList<>(Arrays.asList("STATPEJ.POC_CLAIM","STATPEJ.POC_CLAIMPAYMENT","STATPEJ.POC_CUSTOMER","STATPEJ.POC_POLICY"));
}
