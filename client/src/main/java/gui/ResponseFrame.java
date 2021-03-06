package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Sean Murphy on 4/25/2018.
 */
public class ResponseFrame extends JFrame implements ActionListener{

    /**  **/
    private String response;
    private ExerciseFrame ef;

    /**  **/
    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JLabel label;
    private JButton button1, button2, button3;

    /**  **/
    private final Font LABELFONT = new Font(
            "Arial", Font.BOLD, 30);

    public ResponseFrame(String r, ExerciseFrame f) {
        response = r;
        ef=f;

        mainPanel = new JPanel(new GridLayout(1,1,10,0));
        buttonPanel = new JPanel(new FlowLayout());
        label = new JLabel();

        setFrame();

        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(LABELFONT);
        mainPanel.add(label);

        add(mainPanel);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(new Dimension(450, 350));
        setLocationRelativeTo(null);
        //setResizable(false);
        setVisible(true);
    }

    private void setFrame(){

        if(response.equals("warning")){
            this.setTitle("Warning");
            label.setText("<html><p style=\"text-align:center\">" +
                    "Please move on to the next note in the exercise!</p></html>");
            mainPanel.setBackground(Color.BLUE);
            buttonPanel.setBackground(Color.BLUE);
            addButton1(button1, "OK");
        } else if(response.equals("success")){
            this.setTitle("Correct!");
            label.setText("<html><p style=\"text-align:center\">" +
                    "Good job! You passed the exercise!<br>On to the next one!</p></html>");
            mainPanel.setBackground(Color.GREEN);
            buttonPanel.setBackground(Color.GREEN);
            addButton1(button1, "I'm ready");
        } else if(response.equals("fail")){
            this.setTitle("Wrong");
            label.setText("<html><p style=\"text-align:center\">" +
                    "You got the exercise wrong.<br>Would you like to try again?</p></html>");
            mainPanel.setBackground(Color.RED);
            buttonPanel.setBackground(Color.RED);
            addButton1(button1, "Try Again");
            addButton2(button2, "Move On");
            addButton2(button3, "See Results");
        }
    }

    private void addButton1(JButton b1, String text){
        b1 = new JButton(text);
        createButton(b1);
        buttonPanel.add(b1, BorderLayout.SOUTH);
    }

    private void addButton2(JButton b2, String text){
        System.out.println("creating button2 " + text);
        b2 = new JButton(text);
        createButton(b2);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(b2, BorderLayout.SOUTH);

    }

    private void createButton(JButton b){
        b.addActionListener(this);
        b.setPreferredSize(new Dimension(110, 40));
        b.setContentAreaFilled(true);
        b.setBackground(Color.WHITE);
        b.setFocusPainted(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        if(e.getActionCommand().equals("Try Again")){
            ef.resetCurrentExercise();
            this.dispose();
        } else if(e.getActionCommand().equals("OK")) {
            this.setVisible(false);
            MainWindow.callCoreCommand("Notes");
            this.dispose();
        } else if(e.getActionCommand().equals("See Results")){
            createResultsFrame();
        } else {
            ef.newExercise();
            this.dispose();
        }
    }

    private void createResultsFrame(){
        ArrayList<String> input = ef.getInputList();
        String original = ef.getNoteList();
        new ResultsFrame(input, original);
    }
}
