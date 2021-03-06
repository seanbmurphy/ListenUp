package Pitches;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Sean Murphy on 11/26/2017.
 */

public class NoteMap{

    private static NoteMap n;
    private ArrayList<String> notesFlat;
    private ArrayList<String> notesSharp;
    private float[][] pitchArray;
    private final int PITCHRANGE = 4;
    private final int NOTEAMOUNT = 12;

    public NoteMap(){
        n = this;
    }

    public static NoteMap getNoteMap() {return n;}
    public float[][] getPitchArray() {return pitchArray;}
    public ArrayList<String> getNotesFlat() {return notesFlat;}
    public ArrayList<String> getNotesSharp() {return notesSharp;}

    public void create(String filename){
        try {
            Scanner scan = new Scanner(new File(filename));
            createPitchMap(scan);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createPitchMap(Scanner scan){
        notesFlat = new ArrayList<>();
        notesSharp = new ArrayList<>();
        ArrayList<Float> frequencies = new ArrayList<>();
        int temp = 0;
        while(scan.hasNext()){
            String s = scan.next();
            if(s.substring(0,1).matches("\\d+")){
                frequencies.add(Float.parseFloat(s));
            } else {
                if(s.charAt(1)=='F'){
                    notesFlat.add(s);
                } else if(s.charAt(1)=='S'){
                    notesSharp.add(s);
                } else {
                    notesFlat.add(s);
                    notesSharp.add(s);
                }
            }
        }
        pitchArray = new float[NOTEAMOUNT][PITCHRANGE];
        for(int i=0; i<pitchArray.length; i++){
            for(int j=0; j<pitchArray[i].length; j++){
                pitchArray[i][j] = frequencies.get(temp);
                temp++;
            }
        }
    }
}
