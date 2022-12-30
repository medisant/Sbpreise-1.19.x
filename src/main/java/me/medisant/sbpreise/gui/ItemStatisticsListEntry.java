package me.medisant.sbpreise.gui;

import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import lombok.Getter;
import net.minecraft.text.Text;

@Getter
public class ItemStatisticsListEntry extends WGridPanel {

    private final WLabel item;
    private final WLabel price_min;
    private final WLabel price_max;
    private final WButton changePriceButton;

    public ItemStatisticsListEntry() {
        super(12);
        this.item = new WLabel(Text.of("error"));
        this.add(item, 0, 0, 7, 1);

        this.price_min = new WLabel(Text.of("error"));
        this.add(price_min, 0, 1, 5, 1);

        this.price_max = new WLabel(Text.of("error"));
        this.add(price_max, 5, 1, 3, 1);

        this.changePriceButton = new WButton(Text.translatable("text.gui.button.request_change"));
        this.add(changePriceButton, 12, 0, 10, 2);
    }
}
