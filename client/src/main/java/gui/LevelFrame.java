package gui;

import Pitches.LevelData;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by Sean Murphy on 5/1/2018.
 */
public class LevelFrame extends JFrame implements ListSelectionListener, ActionListener {

    private JTextArea descriptionArea;
    private JButton startButton;
    private JList<String> levelsList;
    private JPanel mainPanel;
    private JPanel bottomPanel;
    private JSplitPane splitPane;

    private ArrayList<String> levelData;
    private String[] levelListItems;
    private LevelData ld;
    private int lnum;
    private String exercise;

    public LevelFrame(String e) {

        exercise = e;
        ld = new LevelData();
        System.out.println("inside level frame " + e);
        createList(exercise);

        setTitle("Choose level of difficulty");

        splitPane = new JSplitPane();

        descriptionArea = new JTextArea();
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);

        startButton = new JButton("Start");
        startButton.addActionListener(this);
        startButton.setContentAreaFilled(false);
        startButton.setPreferredSize(new Dimension(150, 40));

        mainPanel = new JPanel(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setSize(new Dimension(300, 250));
        mainPanel.add(descriptionArea);
        mainPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        mainPanel.add(startButton);

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
        bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        bottomPanel.add(levelsList);
        bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        bottomPanel.add(mainPanel);
        bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        add(bottomPanel);

        setSize(new Dimension(400, 350));
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void createList(String exercise){
        levelData = ld.getLevelData(exercise);
        String levels = levelData.get(0);
        int size = levelData.size()-1;
        System.out.println("array size " + size);
        levelListItems = ld.parseLevels(levels, size);
        levelsList = new JList<>(levelListItems);

        levelsList.setVisibleRowCount(size);
        levelsList.setFixedCellHeight(30);
        levelsList.setFixedCellWidth(100);
        levelsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        levelsList.addListSelectionListener(this);
    }

    private void setTextArea(int levelNum){
        descriptionArea.setText(levelData.get(levelNum));
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        String levelName = levelsList.getSelectedValue();
        char levelNum = levelName.charAt(levelName.length()-1);
        if(Character.isDigit(levelNum)){
            int temp = Character.getNumericValue(levelNum);
            lnum = temp;
            setTextArea(temp);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==startButton){
            new ExerciseFrame(exercise, lnum);
            this.dispose();
        }
    }
}
