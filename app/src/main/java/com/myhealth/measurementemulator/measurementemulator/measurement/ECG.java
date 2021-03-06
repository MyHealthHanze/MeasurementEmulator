package com.myhealth.measurementemulator.measurementemulator.measurement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * @author Jitse
 * A class to generate random ECG values.
 */
public class ECG {

    public static final int MEASURES_PER_DECISECOND = 10;
    public static final int MINIMUM_PAUSE = 3*MEASURES_PER_DECISECOND;
    public static final int EVERAGE_P_WAVE = 4;
    public static final int P_WAVE_SPEED = 4;
    public static final int MINIMUM_PR_SEGMENT = (int)(0.2*MEASURES_PER_DECISECOND);
    public static final int EVERAGE_QRS = 12;
    public static final int MINIMUM_ST_SEGMENT = (int)(0.2*MEASURES_PER_DECISECOND);
    public static final int EVERAGE_T_WAVE = 6;
    public static final int T_WAVE_SPEED = 6;
    public static final int EVERAGE_U_WAVE = 1;
    public static final int U_WAVE_SPEED = 8;

    private int maxDuration;
    private List<Double> values = new ArrayList<>();
    private Random random = new Random();

    /**
     * generates a new random ECG
     *
     * @param maxDuration The maximum duration of the ECG
     * @return a randomly generated ECG
     */
    public List<Double> getNewECG(int maxDuration){
        this.maxDuration = maxDuration * MEASURES_PER_DECISECOND;
        generatePause(MINIMUM_PAUSE);
        while(values.size() < this.maxDuration){
            generatePWave();
            generatePause(MINIMUM_PR_SEGMENT);
            generateQRSComplex();
            generatePause(MINIMUM_ST_SEGMENT);
            generateTWave();
            generateUWave();
            generatePause(MINIMUM_PAUSE);
        }
        return this.values;
    }

    /**
     * Adds a pause to the current timeline.
     *
     * @param minimumLength The minimum lenght of the pause
     */
    private void generatePause(int minimumLength) {
        double pauseLength = minimumLength + random.nextDouble()*10;
        int i = 0;
        while(values.size() < maxDuration && i < pauseLength){
            values.add(0.0);
            i++;
        }
    }

    /**
     * Adds a U.wave to the current timeline.
     */
    private void generateUWave( ) {
        createWave(U_WAVE_SPEED, EVERAGE_U_WAVE);
    }

    /**
     * Adds a T wave to the current timeline.
     */
    private void generateTWave( ) {
        createWave(T_WAVE_SPEED, EVERAGE_T_WAVE);
    }

    /**
     * Adds a QRS complex to the current timeline saved in values.
     */
    private void generateQRSComplex( ) {
        double height;
        int speed = 14 - random.nextInt(6);
        int n = 0;//counting how many times the line y=0 has been crossed
        for(double i = 0.0; i<50; i++){
            height = EVERAGE_QRS * Math.sin(speed * (i / MEASURES_PER_DECISECOND)) + (0.75 * EVERAGE_QRS);
            if(values.size() < maxDuration){
                if (height <= 0 && n < 2) { //searching first drop
                    values.add(height);
                    n = 1;
                } else if (height >= 0 && n < 3 && n > 0) {//searching first spike
                    values.add(height);
                    n = 2;
                } else if (height <= 0 && n < 4 && n > 1) {//searching second drop*/
                    values.add(height - (0.15 * EVERAGE_QRS));
                    n = 3;
                } else if (height >= 0 && n >= 3) {
                    values.add(0.0);
                    break;
                }
            }
        }
    }

    /**
     * Adds a P.wave to the current timeline.
     */
    private void generatePWave(){
        createWave(P_WAVE_SPEED, EVERAGE_P_WAVE);
    }

    /**
     * Adds a wave with a given hight and speed to the timeline.
     *
     * @param speed the speed of the wave (duration).
     * @param waveHeight hight of the wave.
     */
    private void createWave(int speed, int waveHeight) {
        double height;
        speed = 14 - random.nextInt(speed) - 3;
        for(double i = 0.0; i<20; i++){
            height = waveHeight * Math.sin(speed * (i / MEASURES_PER_DECISECOND));
            if(values.size() < maxDuration){
                if (height >= 0) {
                    values.add(height);
                } else{
                    values.add(0.0);
                    i = 20;
                }
            }
        }
    }
}
