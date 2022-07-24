package com.itndev.FactionCore.Database.Gui;

import javax.swing.*;
import java.awt.*;

public class GuiPanel {
    public static JFrame frame = null;
    public static JPanel panel = null;
    public static Boolean isLoaded = false;

    public GuiPanel() {
        JButton ExitButton = new JButton("Exit");
        JButton LoadButton = new JButton("Load From MySQL");
        JButton Load2Button = new JButton("Load From YAML");
        JLabel label1 = new JLabel("");
        JLabel label2 = new JLabel("");

        ExitButton.addActionListener(new ExitListener());
        LoadButton.addActionListener(new LoadMySQLListener());
        Load2Button.addActionListener(new LoadYAMLListener());
        frame = new JFrame();
        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(100, 100, 40 ,100));
        panel.setLayout(new GridLayout(0, 1));
        if(!isLoaded) {
            panel.add(LoadButton);
            panel.add(label1);
            panel.add(Load2Button);
            panel.add(label2);
            panel.add(ExitButton);
        }
        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setTitle("FactionCore Gui Panel");
        frame.pack();
        frame.setVisible(true);
    }
}
