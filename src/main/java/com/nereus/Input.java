package com.nereus;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import static com.nereus.HLLDB.*;
public class Input {

 /**   public static Set<Set<String>> createTags(String tagsType){

        ArrayList<String> tagsArray = new ArrayList<>();
        String type = tagsType;
        switch(type)
        {
            case CUSTOMER_TOPIC:
                tagsArray.add("customer");
                tagsArray.add("policy");
                tagsArray.add("customertime");
                tagsArray.add("address");
                break;
            case POLICY_TOPIC:
                tagsArray.add("policy");
                tagsArray.add("policystarttime");
                tagsArray.add("policyendtime");
                tagsArray.add("pvar0");
                tagsArray.add("pvar1");
                break;
            case CLAIM_TOPIC:
                tagsArray.add("claimnumber");
                tagsArray.add("claimtime");
                tagsArray.add("claimreporttime");
                tagsArray.add("claimcounter");
                tagsArray.add("policy");
                break;
            case PAYMENT_TOPIC:
                tagsArray.add("claimnumber");
                tagsArray.add("claimcounter");
                tagsArray.add("paytime");
                tagsArray.add("payment");
            default:
                break;

        }


        Set<String> tags =  new HashSet<>();
        tags.addAll(tagsArray);
        Set<Set<String>> tagsSets = Sets.powerSet(tags);
        return tagsSets;

        //for (String hashtag : tagsForQuery) {
        //  System.out.println(hashtag);
        //  }
    }
  **/
}
