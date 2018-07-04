package com.nereus;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.javatuples.Sextet;
import org.javatuples.Triplet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TestClass {

    public static int septet = 948039636;
    public static int superset = 1415173982;
    public static int subset = 766958739;
    public static int overlap = 1121347462;
    public static int jointriplet = 356105058;
    public static int identifiedBy = -1717279801;
    public static int tempRegrouped = 1;

    public static MultiMap<String,String> identifiedByT = new MultiValueMap<>();
    public static MultiMap<String,Sextet> supersetT = new MultiValueMap<>();
    public static MultiMap<String,Sextet> subsetT = new MultiValueMap<>();
    public static MultiMap<String,Sextet<String,Set<String>,Long,String,Set<String>,Long>> properSubsetT = new MultiValueMap<>();
    public static MultiMap<String,Sextet> overlapT = new MultiValueMap<>();
    public static String septeS ="{Policy=[[Policy, [policystarttime], 597, Customer, [customertime], 493], [Policy, [policystarttime, policy], 597, Customer, [customertime, policy], 493]], Payment1&Claim=[[Claim, [policy], 156, Policy, [policy], 300]], Payment=[[Payment, [claimnumber, claimcounter], 193, Claim, [claimnumber, claimcounter], 448]], Customer&Policy=[[Policy, [policy], 300, Claim, [policy], 156]], Payment1&Claim1=[[Claim, [policy], 156, Policy, [policy], 300]], Customer=[[Customer, [customertime], 493, Policy, [policystarttime], 597], [Customer, [customertime, policy], 493, Policy, [policystarttime, policy], 597], [Customer, [customertime, policy], 493, Policy, [policystarttime, policy], 597]], Claim=[[Claim, [claimnumber], 448, Payment, [claimnumber], 193], [Claim, [claimnumber, claimcounter], 448, Payment, [claimnumber, claimcounter], 193]], Payment1=[[Payment, [claimnumber, claimcounter], 193, Claim, [claimnumber, claimcounter], 448]]}";

    public static String joinTripletS = "{Policy=[Policy, Customer, false], Payment1&Claim=[Payment1&Claim, Customer&Policy, true], Payment=[Payment, Claim, true], Payment1&Claim1=[Payment1&Claim1, Customer&Policy1, false], Customer&Policy=[Customer&Policy, Payment1&Claim, true], Customer=[Customer, Policy, false], Customer&Policy1=[Customer&Policy1, Payment1&Claim1, false], Claim=[Claim, Payment1, false], Payment1=[Payment1, Claim, false]}";

    public static void main(String[] arg)  {

        Set<String> test = new HashSet();
        test.add("customer");
        String customer = test.toString().replace("[","").replace("]","");

        System.out.println(test.toString().replace("[","").replace("]",""));
        System.out.println(customer.equals("customer"));

        StringBuilder builder = new StringBuilder();
        System.out.println(builder.append(test.toString()).append(" "));
        int i=1;
        i++;
        System.out.println("integer i "+i);
        String customer1 = "customer";
        System.out.println(customer1.toUpperCase());


    }

}
