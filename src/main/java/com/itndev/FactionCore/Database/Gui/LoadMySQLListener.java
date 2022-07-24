package com.itndev.FactionCore.Database.Gui;

import com.itndev.FactionCore.Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoadMySQLListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        Server.FromMYSQL = true;
        GuiPanel.isLoaded = true;
    }
}
