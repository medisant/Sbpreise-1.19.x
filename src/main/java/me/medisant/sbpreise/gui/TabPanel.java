package me.medisant.sbpreise.gui;

import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.medisant.sbpreise.util.GuiUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TabPanel extends WGridPanel {

    private final ArrayList<Tab> tabs;
    private List<WButton> headers;

    public TabPanel(ArrayList<Tab> tabs) {
        this.tabs = tabs;
        createHeaders();
    }

    private void createHeaders() {
        WGridPanel headerPanel = new WGridPanel();
        headers = new ArrayList<>();
        int x = 0;
        int i = 0;
        for (Tab tab : tabs) {
            WButton button = new WButton(Text.of(tab.name));
            int finalI = i;
            button.setOnClick(() -> MinecraftClient.getInstance().setScreen(new GuiScreen(new MainGui(finalI))));
            headers.add(button);
            headerPanel.add(button, x, 0, 5, 1); //Click events missing
            x = x + 5;
            i++;
        }
        this.add(headerPanel, 0, 0, GuiUtils.getWidth(), 1);
    }

    public void setTab(int tab) {
        try {
            this.add(tabs.get(tab).panel, 0, 2, GuiUtils.getWidth(), GuiUtils.getHeigth());
            headers.get(tab).setEnabled(false);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }


    @Getter
    @Setter
    @AllArgsConstructor
    public static class Tab {

        private WGridPanel panel;
        private String name;

    }

}
