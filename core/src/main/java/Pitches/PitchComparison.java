package Pitches;

import java.util.ArrayList;

/**
 * Created by Sean Murphy on 10/30/2017.
 */

public class PitchComparison {

    private float[][] pitchArray;
    private ArrayList<String> notesFlat;
    private ArrayList<String> notesSharp;
    private static boolean flatSharpToggle;
    private final int PITCHRANGE = 4;
    private NoteMap noteMap;
    private ArrayList<NoteListener> noteListeners;

    public PitchComparison(ArrayList<NoteListener> nl){
        noteMap = NoteMap.getNoteMap();
        noteListeners = nl;
    }

    public void determinePitch(float pitch) {

        pitchArray = noteMap.getPitchArray();
        notesFlat = noteMap.getNotesFlat();
        notesSharp = noteMap.getNotesSharp();
        if(validPitch(pitch).equals("yes")) {
            int range = determineRange(pitch);
            String temp = comparePitchValues(pitch, range);
            filterNote(temp, pitch);
        } else {
            System.out.println("INVALID PITCH" + pitch);
        }
    }

    private void callListener(String n, float p){
        for(NoteListener n1: noteListeners){
            //System.out.println("Calling in pc: "+n+" "+p);
            n1.noteChanged(n, p);
        }
    }

    private void filterNote(String note, float pitch){
        if(!note.equals("X")){
            //System.out.println(note);
            callListener(note, pitch);
        }
    }

    private String validPitch(float p){
        if(p<pitchArray[0][0] || p>pitchArray[pitchArray.length-1][PITCHRANGE-1]){
            return "no";
        } else {
            return "yes";
        }
    }

    private int determineRange(float p) {
        /**
         * temp = [0][n] - [pA.length-1][n-1];
         * sum = temp/2;
         * ([0][n] + sum) - 0.00001;
         * ([pA.length-1][n-1] - sum);
         */

        if(p>= 65.41 && p<=127.14999){ //[0][0] && [pitchArray.length-1][0]
            return 0;
        } else if(p>= 127.15 && p<= 254.28999){ //[0][1] && [pitchArray.length-1][1]
            return 1;
        } else if(p>= 254.29 && p<= 508.56999){ //[0][2] && [pitchArray.length-1][2]
            return 2;
        } else if(p>= 508.57 && p<= 987.77){ //[0][3] && [pitchArray.length-1][3]
            return 3;
        } else {
            return -1;
        }
    }
    /** Currently unused **/
    private float pitchAccuracy(float p, String d){
        if(d.equals("down")){
            return p * .99318182f;
        } else {
            return p * 1.00681818f;
        }
    }

    private String comparePitchValues(float p, int range){
        float pBot = 0, pTop = 0;
        int marker = -1;
        for(int i=0; i<pitchArray.length; i++) {
            if(p<pitchArray[i][range] && i>0){
                pBot = pitchArray[i-1][range];
                pTop = pitchArray[i][range];
                marker = i;
                i = pitchArray.length;
            } else if(p<pitchArray[i][range] && i==0){
                pBot = pitchArray[pitchArray.length-1][range-1];
                pTop = pitchArray[i][range];
                marker = i;
                i = pitchArray.length;
            } else {
                if(p>pitchArray[i][range] && i==pitchArray.length-1){
                    pBot = pitchArray[i][range];
                    pTop = pitchArray[0][range+1];
                    marker = 0;
                    i = pitchArray.length;
                }
            }
        }

        String note = getNoteValue(marker, p, pBot, pTop);
        return note;

    }

    private String getNoteValue(int marker, float p, float pBot, float pTop){
        ArrayList<String> temp;
        if(flatSharpToggle){
            temp = notesSharp;
        } else {
            temp = notesFlat;
        }

        float cents = 0f;

        if(marker==0){
            if ((pBot / p) >= .99090909f) {
                return temp.get(11);
            } else if ((pTop / p) <= 1.00909091f) {
                return temp.get(marker);
            } else {
                return "X";
            }
        } else {
            if ((pBot / p) >= .99090909f) {
                return temp.get(marker - 1);
            } else if ((pTop / p) <= 1.00909091f) {
                return temp.get(marker);
            } else {
                return "X";
            }
        }
    }

    public static void setFlatSharpToggle(boolean toggle){
        if(!toggle){
            flatSharpToggle = false;
        } else {
            flatSharpToggle = true;
        }
    }
}

/*enum Pitch {
    CN (65.41),
    CS (69.3),
    DF (69.3),
    DN (73.42),
    DS (77.78),
    EF (77.78),
    EN (82.41),
    FN (87.31),
    FS (92.50),
    GF (92.50),
    GN (98.0),
    GS (103.83),
    AF (103.83),
    AN (110.0),
    AS (116.54),
    BF (116.54),
    BN (123.47);

    private final double pitchBase;

    Pitch(double pitch){
        pitchBase = pitch;
    }

    // TODO: Do not use this anymore - or find a way to use it differently.
}*/


/**
 * Pitch frequency ratios, where f is the original frequency:
 *
 * whole tone 9/8
 * half tone 256/243
 *
 * minor 2nd       256/243f
 * major 2nd       9/8f
 * minor 3rd       6/5f
 * major 3rd       5/4f
 * perfect 4th     4/3f
 * tritone         64/45f OR 45/32f OR 4073/2880f (last one is most accurate)
 * perfect 5th     3/2f
 * minor 6th       8/5f
 * major 6th       5/3f OR 27/16f
 * minor 7th       16/9f OR 9/5f (first one seems to be more accurate)
 * major 7th       15/8f
 * perfect octave  2f
 *
 * RANGES:
 *
 * Bass - G1-E4
 * Tenor - A2-D5
 * Alto - C3-C6
 * Soprano - G3-Gf6
 *
 * .99318182 * f = accuracy range down
 * 1.00681818 * f = accuracy range up
 */
