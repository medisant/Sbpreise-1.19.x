package me.medisant.sbpreise.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.WTabPanel;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import me.medisant.sbpreise.api.ItemStatistics;
import me.medisant.sbpreise.util.ItemStatisticUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class MainGui extends LightweightGuiDescription {

    public MainGui() {
        WGridPanel root = createMainPanel();
        root.setInsets(Insets.ROOT_PANEL);
        setFullscreen(true);
        setRootPanel(root);
        root.validate(this);
    }

    private WGridPanel createMainPanel() {
        WGridPanel mainPanel = new WGridPanel(22);
        mainPanel.add(createTabPanel(), 1, 1, 28, 11);
        return mainPanel;
    }

    private WTabPanel createTabPanel() {
        Map<String, List<ItemStatistics>> dataMap = ItemStatisticUtils.getItemStatisticsInCategories();
        WTabPanel tabs = new WTabPanel();

        for (Map.Entry<String, List<ItemStatistics>> entry: dataMap.entrySet()) {
            WGridPanel panel = new WGridPanel(22);
            panel.add(reportListPanel(entry.getValue()), 0, 0, 28, 11);
            tabs.add(panel, tab -> tab.title(Text.of(entry.getKey())));
        }

        return tabs;
    }

    private WListPanel<ItemStatistics, ItemStatisticsListEntry> reportListPanel(List<ItemStatistics> itemStatisticsList) {
        BiConsumer<ItemStatistics, ItemStatisticsListEntry> configurator = (ItemStatistics itemStatistics, ItemStatisticsListEntry itemStatisticsListEntry) -> {
            itemStatisticsListEntry.getItem().setText(Text.of(itemStatistics.getFriendly_name()));
            itemStatisticsListEntry.getPrice_min().setText(Text.of(String.valueOf(itemStatistics.getPrice_min())));
            itemStatisticsListEntry.getPrice_max().setText(Text.of(String.valueOf(itemStatistics.getPrice_max())));
            itemStatisticsListEntry.getChangePriceButton().setOnClick(() -> {
                MinecraftClient.getInstance().setScreen(new GuiScreen(new RequestPriceChangeGui(itemStatistics.getFriendly_name(), itemStatistics.getPrice_min(), itemStatistics.getPrice_max(), itemStatistics.getSb_id())));
            });
        };
        return new WListPanel<>(itemStatisticsList, ItemStatisticsListEntry::new, configurator).setListItemHeight(24);
    }

}
