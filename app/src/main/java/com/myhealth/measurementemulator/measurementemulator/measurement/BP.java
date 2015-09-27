package com.myhealth.measurementemulator.measurementemulator.measurement;

import java.util.Random;

/**
 * @author Jitse
 * A class to generate random blood pressure values.
 */
public class BP {
    private static final int MINIMUM_SYSTOLIC_BP = 70;
    private static final int MAXIMUM__SYSTOLIC_BP = 150;
    private static final int MAXIMUM_DEVIATION_DIASTOLIC_BP = 10;

    /**
     * generates random blood pressure values.
     * @return randomly generated BP value
     */
    public int[] getNewBP(){
        Random random = new Random();
        int[] bloodPressure = new int[2];
        int systolic = MINIMUM_SYSTOLIC_BP + random.nextInt(MAXIMUM__SYSTOLIC_BP - MINIMUM_SYSTOLIC_BP);
        int diastolic =  (int)(MAXIMUM_DEVIATION_DIASTOLIC_BP/2 + (0.64*systolic)
                - random.nextInt(MAXIMUM_DEVIATION_DIASTOLIC_BP));
        bloodPressure[0] = systolic;
        bloodPressure[1] = diastolic;
        return bloodPressure;
    }
}

