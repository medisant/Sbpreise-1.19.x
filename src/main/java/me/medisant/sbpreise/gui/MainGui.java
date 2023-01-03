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

//the default main menu, a tab panel showing the different categories and the related itemStatistics
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
        WScrollPanel scrollPanel = new WScrollPanel(tabPanel); //scroll panel to make it scrollable
        scrollPanel.setScrollingHorizontally(TriState.TRUE); //set the horizontally scrolling to always active

        mainPanel.add(scrollPanel, 0, 0, GuiUtils.getWidth(), GuiUtils.getHeigth()); //set the size of the gui depending on the settings set in the config

        return mainPanel;
    }

    //create the TabPanel which is later added to the mainGUI
    private TabPanel createTabPanel() {

        Map<String, List<ItemStatistics>> dataMap = ItemStatisticUtils.getItemStatisticsInCategories(); //fetch the data from the gui and already parse it to a map of categories and their itemStatistics
        ArrayList<TabPanel.Tab> tabs = new ArrayList<>(); //the tabs which are later added to the TabPanel

        for (Map.Entry<String, List<ItemStatistics>> entry : dataMap.entrySet()) {
            WGridPanel panel = new WGridPanel();
            for (int i = 0; i < entry.getValue().size(); i++) {
                ItemStatistics itemStatistics = entry.getValue().get(i);
                //button & icon
                ItemStack itemStack = new ItemStack(Items.AIR); //air if the item id is invalid
                try {
                    itemStack = new ItemStack(Registry.ITEM.get(new Identifier("minecraft:" + itemStatistics.getMinecraft_name())), 1); //set the item icon to a itemStack of the provided item id
                } catch (InvalidIdentifierException e) {
                    e.printStackTrace();
                }
                WButton iconButton = new WButton(new ItemIcon(itemStack)); //the item icon is a disabled button (sprites can't show an itemStack)
                iconButton.setEnabled(false);
                panel.add(iconButton, 0, i, 1, 1);

                //text with name
                WButton itemNameButton = new WButton(Text.of(" " + itemStatistics.getFriendly_name())); //the text is showed on disabled buttons instead of labels, looks fancier
                itemNameButton.setEnabled(false);
                itemNameButton.setAlignment(HorizontalAlignment.LEFT);
                panel.add(itemNameButton, 1, i, 10, 1);

                //text with last update
                String lastChanged = "";
                if (itemStatistics.getLastchangedate() != 0) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy").withZone(ZoneId.systemDefault());
                    lastChanged = formatter.format(Instant.ofEpochMilli(System.currentTimeMillis() - itemStatistics.getLastchangedate())); //format the last changed date (dd.MM.yyyy)
                }
                WButton lastChangedButton = new WButton(Text.of(lastChanged));
                lastChangedButton.setEnabled(false);
                panel.add(lastChangedButton, 11, i, 5, 1);

                //text with price
                WButton priceButton = new WButton(Text.of(itemStatistics.getPrice_min() + "-" + itemStatistics.getPrice_max()));
                priceButton.setOnClick(() ->
                        MinecraftClient.getInstance().setScreen(new GuiScreen(new RequestPriceChangeGui(itemStatistics, tab)))); //when the price button is clicked, open the gui to request a price change for this item
                panel.add(priceButton, 16, i, 10, 1);
            }

            tabs.add(new TabPanel.Tab(panel, entry.getKey())); //add the tab to the list
        }

        return new TabPanel(tabs); //create and return a TabPanel with the tabs added to the list
    }

}
