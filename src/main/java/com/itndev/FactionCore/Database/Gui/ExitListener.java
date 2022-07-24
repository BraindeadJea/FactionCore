package com.itndev.FactionCore.Database.Gui;

import com.itndev.FactionCore.Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExitListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        if(GuiPanel.isLoaded) {
            Server.Close = true;
        } else {
            System.exit(0);
        }
    }
}
