package com.nereus;
import java.util.HashMap;
import java.util.Set;

import static com.nereus.HLLDB.*;

public class SetInfo {
   public static Integer customerLines = 0;//493;
   public static Integer policyLines = 0;// 597;
   public static Integer claimLines = 0;//448;
    public static Integer paymentLines = 0;// 395;
    public static Integer addressLines = 0;
    public static Integer claimHandlerLines = 0;
    public static Integer address2Lines = 0;

    static int getCardinality(String dataSetName)
    {

        switch(dataSetName){
            case CUSTOMER_TOPIC: return customerLines;

            case POLICY_TOPIC: return policyLines;

          //  case CLAIM_TOPIC: return claimLines;

          //  case PAYMENT_TOPIC: return paymentLines;

            default: return 0;

        }

    }

   public static int getCardinalityA(String dataSetName) {

        switch (dataSetName) {
            case CUSTOMER_TOPIC:
                return customerLines;

            case POLICY_TOPIC:
                return policyLines;

         /**   case CLAIM_TOPIC:
                return claimLines;

            case PAYMENT_TOPIC:
                return paymentLines;

            case ADDRESS_TOPIC:
                return addressLines;

            case CLAIMHANDLER_TOPIC:
            return claimHandlerLines;
          **/

            default:
                return 0;

        }
    }



    }
