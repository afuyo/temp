package com.nereus;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.javatuples.Sextet;

import java.util.*;

import static com.nereus.HLLDB.*;

public class SandBox2 {


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
        // System.out.println(diff);
        float tolerance = tolerancePercentage/100*desiredValue;
        // System.out.println(tolerance);

        return diff<tolerance;
    }

    public static boolean approximatelyLess(float desiredValue, float actualValue, float tolerancePercentage)
    {
        float diff = Math.abs(desiredValue-actualValue);
        // System.out.println(diff);
        float tolerance = tolerancePercentage/100*desiredValue;
        // System.out.println(tolerance);

        return diff<tolerance;
    }

    public static Boolean isInProperSubsetSuperset (String leftName, String rightName)
    {
        Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>> leftProperSubset= (Collection) PROPERSUBSET.get(leftName);
        Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>> rightSuperSet= (Collection) SUPERSET.get(rightName);
        HashMap<Integer,Boolean> isInProperSubsetSuperset = new HashMap<>();
        isInProperSubsetSuperset.put(1,false);


        leftProperSubset.forEach((e)->{

            rightSuperSet.forEach((g)->{
                if(e.getValue3().equals(g.getValue0()))
                {
                    isInProperSubsetSuperset.put(1,true);
                }
            });
        });

        return rightSuperSet.stream().flatMap(x->leftProperSubset.stream().filter(s->x.getValue3().equals(s.getValue0()))).findAny().isPresent();
        // return isInProperSubsetSuperset.get(1);

    }

    public static Sextet getMatchMaxCardinalityTuple2 (String leftName, String rightName){
        /**Use this for inherited relationships when name do not longer match */

        //System.err.println("DEBUG MAXXXXXXXXX "+leftname+" "+rightName);
        Sextet properSub= null;

        Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>> leftProperSubset= (Collection) PROPERSUBSET.get(leftName);
        Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>> rightProperSubset= (Collection) PROPERSUBSET.get(rightName);
        Collection<Sextet<String,Set<String>,Long,String,Set<String>,Long>> rightSuperSet= (Collection) SUPERSET.get(rightName);
        boolean isInProperSubsetSuperset= false;
        final Comparator<Sextet<String, Set<String>, Long, String, Set<String>, Long>> comp = (s1, s2) -> Long.compare(s1.getValue2(), s2.getValue2());

        if(rightSuperSet!=null && leftProperSubset!=null) {
            //this didn't work
            // isInProperSubsetSuperset = rightSuperSet.stream().anyMatch(s -> s.getValue0().equals(leftProperSubset.iterator().next().getValue3())&& !s.getValue1().isEmpty());
            isInProperSubsetSuperset = isInProperSubsetSuperset(leftName,rightName);

            if(isInProperSubsetSuperset){
                System.out.println(leftName+rightName+" Hello");
                properSub = leftProperSubset.stream().filter(s-> s.getValue3().equals(rightSuperSet.iterator().next().getValue0())).max(comp).get();

                //  properSub = leftProperSubset.stream().flatMap(x->leftProperSubset.stream().filter(s->x.getValue3().equals(s.getValue0()))).max(comp).get();
                //


            }
        }
        /*if(!rightProperSubset.stream().anyMatch(s-> s.getValue3().equals(leftProperSubset.iterator().next().getValue0())))
        {
            rightProperSubset=(Collection) superset.get(rightName);
        }*/
        //TODO add !isInProperSubset check the if it is in superset  ?

        else  if(leftProperSubset!=null && rightProperSubset!=null  ) {

            try {
                // properSub = leftProperSubset.stream().filter(s -> s.getValue3().equals(rightName)).max(comp).get();
                //properSub = rightProperSubset.stream().filter(s-> s.getValue3().equals(leftProperSubset.iterator().next().getValue0())).max(comp).get();
                properSub = leftProperSubset.stream().filter(s-> s.getValue3().equals(rightProperSubset.iterator().next().getValue0())).max(comp).get();

            } catch (NoSuchElementException e) {
                properSub = leftProperSubset.stream().max(comp).get();
                //TODO handle NoSucheElementException for Claim Payment1&Claim
                // System.err.println("StackTrace"+leftname+" "+rightName);
                //System.err.println("ProperSub"+properSub.toString());
                //e.printStackTrace();
                return properSub;

            }
        }
        //System.out.println("GET MAXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        //System.out.println(leftProperSubset.stream().max(comp).get().toString());
        return properSub;
    }

    public static void main(String[] arg)  {

       // public static MultiMap<String,Sextet<String,Set<String>,Long,String,Set<String>,Long>> PROPERSUBSET = new MultiValueMap<>();
        Set<String> set1 = new HashSet<>();
        set1.add("Something");
        Set<String> set2 = new HashSet<>();
        set2.add("Something else");

        Sextet  c1 = Sextet.with("Customer",set1,10L,"Policy",set1,10L);
        Sextet c2 = Sextet.with("Customer",set2,20L,"Policy",set2,20L);
        Sextet c21 = Sextet.with("Customer",set2,25L,"Policy",set2,25L);
        Sextet c3 = Sextet.with("Customer",set1,30L,"Address",set1,30L);

        Sextet p1 = Sextet.with("Policy",set1,10L,"Customer",set1,10L);
        Sextet p2 = Sextet.with("Policy",set2,20L,"Customer",set2,20L);
        Sextet p21 = Sextet.with("Policy",set2,25L,"Customer",set2,25L);
        Sextet p3 = Sextet.with("Policy",set1,40L,"Claim",set1,40L);

        Sextet a1 = Sextet.with("Address",set1,10L,"Customer",set1,10L);

        SUPERSET.put("P",p3);
        SUPERSET.put("C",c3);
        SUPERSET.put("C",c2);
        SUPERSET.put("C",c21);
        SUPERSET.put("C",c1);


        PROPERSUBSET.put("A",a1);
        PROPERSUBSET.put("P",p1);
        PROPERSUBSET.put("P",p2);
        PROPERSUBSET.put("P",p21);

        Sextet sextet = getMatchMaxCardinalityTuple2("P","C");
        System.out.println(sextet);

        boolean temp = approximatelyLess(6023,5939,1);
        System.out.println(temp);

        System.out.println("20112 20166 1 ");
        System.out.println(approximatelyEqual(20112,20166,1));
        System.out.println("9971 10060 1 ");
        System.out.println(approximatelyEqual(9971,10060,1));

    }
}
