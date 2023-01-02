package me.medisant.sbpreise.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import me.medisant.sbpreise.api.ApiInteraction;
import me.medisant.sbpreise.api.ItemStatistics;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;

public class RequestPriceChangeGui extends LightweightGuiDescription {

    private final ItemStatistics itemStatistics;
    private final int currentTab;

    public RequestPriceChangeGui(ItemStatistics itemStatistics, int currentTab) {
        this.currentTab = currentTab;
        this.itemStatistics = itemStatistics;
        WGridPanel root = createMainPanel();
        root.setInsets(Insets.ROOT_PANEL);
        setFullscreen(true);
        setRootPanel(root);
        root.validate(this);
    }

    private WGridPanel createMainPanel() {
        WGridPanel mainPanel = new WGridPanel(22);

        WLabel titleLabel = new WLabel(Text.literal(I18n.translate("text.gui.title.request_change", itemStatistics.getFriendly_name())));
        titleLabel.setColor(0xbcbcbc);
        mainPanel.add(titleLabel, 0, 0, 5, 1);

        WTextField priceMinTextField = new WTextField();
        priceMinTextField.setText(String.valueOf(itemStatistics.getPrice_min()));
        mainPanel.add(priceMinTextField, 0, 1, 5, 1);

        WTextField priceMaxTextField = new WTextField();
        priceMaxTextField.setText(String.valueOf(itemStatistics.getPrice_max()));
        mainPanel.add(priceMaxTextField, 0, 2, 5, 1);

        WLabel errorLabel = new WLabel(Text.empty());
        errorLabel.setColor(0xbcbcbc);
        mainPanel.add(errorLabel, 12, 1, 20, 1);

        WButton requestPriceChangeButton = new WButton(Text.translatable("text.gui.button.request_change"));
        requestPriceChangeButton.setOnClick(() -> {
            try {
                errorLabel.setText(Text.of(new ApiInteraction().requestPriceChange(
                        itemStatistics.getSb_id(),
                        Double.parseDouble(priceMinTextField.getText()),
                        Double.parseDouble(priceMaxTextField.getText()))));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        mainPanel.add(requestPriceChangeButton, 6, 1, 5, 1);

        WButton backButton = new WButton(Text.translatable("text.gui.button.back"));
        backButton.setOnClick(() -> MinecraftClient.getInstance().setScreen(new GuiScreen(new MainGui(currentTab))));
        mainPanel.add(backButton, 6, 2, 5, 1);

        return mainPanel;
    }

}
