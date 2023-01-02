package me.medisant.sbpreise.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WScrollPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import me.medisant.sbpreise.api.ItemStatistics;
import me.medisant.sbpreise.util.GuiUtils;
import me.medisant.sbpreise.util.ItemStatisticUtils;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.registry.Registry;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainGui extends LightweightGuiDescription {

    private final int tab;

    public MainGui(int tab) {
        this.tab = tab;
        WGridPanel root = createMainPanel();
        root.setInsets(Insets.ROOT_PANEL);
        setFullscreen(true);
        setRootPanel(root);
        root.validate(this);
    }

    private WGridPanel createMainPanel() {
        WGridPanel mainPanel = new WGridPanel(22);

        TabPanel tabPanel = createTabPanel();
        tabPanel.setTab(tab);
        WScrollPanel scrollPanel = new WScrollPanel(tabPanel);
        scrollPanel.setScrollingHorizontally(TriState.TRUE);
        //scrollPanel.setScrollingVertically(TriState.DEFAULT);

        mainPanel.add(scrollPanel, 0, 0, GuiUtils.getWidth(), GuiUtils.getHeigth());

        return mainPanel;
    }

    private TabPanel createTabPanel() {

        Map<String, List<ItemStatistics>> dataMap = ItemStatisticUtils.getItemStatisticsInCategories();
        ArrayList<TabPanel.Tab> tabs = new ArrayList<>();

        for (Map.Entry<String, List<ItemStatistics>> entry : dataMap.entrySet()) {
            WGridPanel panel = new WGridPanel();
            for (int i = 0; i < entry.getValue().size(); i++) {
                ItemStatistics itemStatistics = entry.getValue().get(i);
                //button & icon
                ItemStack itemStack = new ItemStack(Items.AIR);
                try {
                    itemStack = new ItemStack(Registry.ITEM.get(new Identifier("minecraft:" + itemStatistics.getMinecraft_name())), 1);
                } catch (InvalidIdentifierException e) {
                    e.printStackTrace();
                }
                WButton iconButton = new WButton(new ItemIcon(itemStack));

                iconButton.setEnabled(false);
                panel.add(iconButton, 0, i, 1, 1);

                //text with name
                WButton itemNameButton = new WButton(Text.of(" " + itemStatistics.getFriendly_name()));
                itemNameButton.setEnabled(false);
                itemNameButton.setAlignment(HorizontalAlignment.LEFT);
                panel.add(itemNameButton, 1, i, 10, 1);

                //text with last update
                String lastChanged = "";
                if (itemStatistics.getLastchangedate() != 0) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy").withZone(ZoneId.systemDefault());
                    lastChanged = formatter.format(Instant.ofEpochMilli(System.currentTimeMillis() - itemStatistics.getLastchangedate()));
                }
                WButton lastChangedButton = new WButton(Text.of(lastChanged));
                lastChangedButton.setEnabled(false);
                panel.add(lastChangedButton, 11, i, 5, 1);

                //text with price
                WButton priceButton = new WButton(Text.of(itemStatistics.getPrice_min() + "-" + itemStatistics.getPrice_max()));
                priceButton.setOnClick(() ->
                        MinecraftClient.getInstance().setScreen(new GuiScreen(new RequestPriceChangeGui(itemStatistics, tab))));
                panel.add(priceButton, 16, i, 10, 1);
            }

            tabs.add(new TabPanel.Tab(panel, entry.getKey()));
        }

        return new TabPanel(tabs);
    }

}
