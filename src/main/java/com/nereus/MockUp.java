package com.nereus;



import org.javatuples.Sextet;

import java.util.HashSet;
import java.util.Set;

import static com.nereus.HLLDB.*;
import static com.nereus.MetadataAccessor.putProperSubsetTuple;
import static com.nereus.MetadataAccessor.putSupersetTuple;

public class MockUp {

    public static void clearSets(){
        SUPERSET.clear();
        SUBSET.clear();
        PROPERSUBSET.clear();
        OVERLAP.clear();
    }

    public static void populateSetsAvro()
    {
        Set<String> mfe_nr = new HashSet<>();
        mfe_nr.add("MFE_NR");
        Set<String> mfe_nr1 = new HashSet<>();
        mfe_nr1.add("MFE_NR1");

        Sextet<String, Set<String>, Long, String, Set<String>, Long> t17a =
                Sextet.with("DB2N.TNKU017", mfe_nr, 7509060L, "DB2N.TNKU016", mfe_nr1, 3066377L);
        putSupersetTuple("DB2N.TNKU017",t17a);

        Sextet<String, Set<String>, Long, String, Set<String>, Long> t16a =
                Sextet.with("DB2N.TNKU016", mfe_nr1, 3066377L, "DB2N.TNKU017", mfe_nr,7509060L );
        putProperSubsetTuple("DB2N.TNKU016",t16a);

    }
    public static void populateSets ()
    {

        Set<String> customer= new HashSet<>();
        customer.add("customer");
        Set<String> claimnumber= new HashSet<>();
        claimnumber.add("claimnumber");
        Set<String> claimcounter = new HashSet<>();
        claimcounter.add("claimnumber, claimcounter");
        Set<String> policy = new HashSet<>();
        policy.add("policy");


        Sextet<String, Set<String>, Long, String, Set<String>, Long> tupleC1 =
                Sextet.with("Customer", customer, 9971L, "Policy", customer, 9971L);
        Sextet<String, Set<String>, Long, String, Set<String>, Long> tupleC2 =
                Sextet.with("Customer", customer, 9971L, "Address", customer, 9971L);
        putSupersetTuple("Customer",tupleC1);
        putSupersetTuple("Customer",tupleC2);

        Sextet<String, Set<String>, Long, String, Set<String>, Long> tupleCl1 =
                Sextet.with("Claim", claimnumber, 12283L, "Payment", claimnumber, 5295L);
        Sextet<String, Set<String>, Long, String, Set<String>, Long> tupleCl2 =
                Sextet.with("Claim", claimcounter, 12357L, "Payment", claimcounter, 5307L);
        putSupersetTuple("Claim",tupleCl1);
        putSupersetTuple("Claim",tupleCl2);

        Sextet<String, Set<String>, Long, String, Set<String>, Long> tuplePol1 =
                Sextet.with("Policy", customer, 9971L, "Customer", customer, 9971L);
        Sextet<String, Set<String>, Long, String, Set<String>, Long> tuplePol2 =
                Sextet.with("Policy", policy, 10060L, "Claim", policy, 5055L);
        Sextet<String, Set<String>, Long, String, Set<String>, Long> tuplePol3 =
                Sextet.with("Policy", customer, 9971L, "Address", customer, 9971L);
        putProperSubsetTuple("Policy",tuplePol1);
        putProperSubsetTuple("Policy",tuplePol2);
        putProperSubsetTuple("Policy",tuplePol3);

        Sextet<String, Set<String>, Long, String, Set<String>, Long> Pay1 =
                Sextet.with("Payment", claimcounter, 5307L, "Claim", claimcounter, 12357L);
        Sextet<String, Set<String>, Long, String, Set<String>, Long> Pay2 =
                Sextet.with("Payment", claimnumber, 5295L, "Claim", claimnumber, 12283L);
        putProperSubsetTuple("Payment",Pay1);
        putProperSubsetTuple("Payment",Pay2);

        Sextet<String, Set<String>, Long, String, Set<String>, Long> Addr1 =
                Sextet.with("Address", customer, 9971L, "Customer", customer, 9971L);
        Sextet<String, Set<String>, Long, String, Set<String>, Long> Addr2 =
                Sextet.with("Address", customer, 9971L, "Policy", customer, 9971L);
        putProperSubsetTuple("Address",Addr1);
        putProperSubsetTuple("Address",Addr2);

        Sextet<String, Set<String>, Long, String, Set<String>, Long> Clm1 =
                Sextet.with("Claim", policy, 5055L, "Policy", policy, 10060L);
        putProperSubsetTuple("Claim",Clm1);
        System.out.println("##########SUPERSET");

    }
    public static void printSets()
    {
        System.out.println("############SUPERSET");
        SUPERSET.forEach((k,v)->{
            System.out.println("Key "+k+" Value "+v);

        });
        System.out.println("############SUBSET");
        SUBSET.forEach((k,v)->{
            System.out.println("Key "+k+" Value "+v);
        });
        System.out.println("############PROPERSUBSET");
        PROPERSUBSET.forEach((k,v)->{
            System.out.println("Key "+k+" Value "+v);
        });

     /*   System.out.println("############OVERLAP");
        OVERLAP.forEach((k,v)->{
            System.out.println("Key "+k+" Value "+v);
        });
*/

    }
}