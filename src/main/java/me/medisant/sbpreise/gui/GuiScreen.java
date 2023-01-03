package me.medisant.sbpreise.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;

public class GuiScreen extends CottonClientScreen {

    //required to open a screen via the LibGUI library
    public GuiScreen(GuiDescription description) {
        super(description);
    }
}
