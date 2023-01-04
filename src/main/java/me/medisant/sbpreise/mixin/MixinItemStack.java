package me.medisant.sbpreise.mixin;

import me.medisant.sbpreise.api.ApiInteraction;
import me.medisant.sbpreise.api.ItemStatistics;
import me.medisant.sbpreise.client.SbpreiseClient;
import me.medisant.sbpreise.config.ModConfig;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mixin(value = ItemStack.class, priority = 500)
public abstract class MixinItemStack {

    @Shadow
    public abstract boolean isEmpty();

    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract Text getName();

    @Inject(method = "getTooltip", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void getToolTip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, List<Text> list) {
        try {
            //if should only shown on skyblock (config) and the tablist contains "Skyblock", true
            //if config = false, true
            boolean addTooltip = !ModConfig.INSTANCE.onlySkyblock || SbpreiseClient.isOnSkyblock;

            if (!this.isEmpty() && addTooltip) { //checks if the itemStack is an item and it should be added

                String itemName = this.getName().getString(); //name of the item
                String id = this.getItem().toString(); //id of the item
                ItemStatistics itemStatistics = new ApiInteraction().getItemStatistics(id, itemName); //try to get the itemStatistics of the item, returns null if not present
                if (itemStatistics != null) { //if the itemStatistics are present...
                    DecimalFormat decimalFormat = new DecimalFormat("#,###,###,##0.00");
                    list.add(Text.literal(I18n.translate("sbpreise.lore.price", decimalFormat.format(itemStatistics.getPrice_min()), decimalFormat.format(itemStatistics.getPrice_max()))));
                    list.add(Text.literal(I18n.translate("sbpreise.lore.item", itemStatistics.getFriendly_name())));
                    if (itemStatistics.getLastchangedate() != 0) { //if a last changed date was provided by the api, add it
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy").withZone(ZoneId.systemDefault());
                        String lastChanged = formatter.format(Instant.ofEpochMilli(System.currentTimeMillis() - itemStatistics.getLastchangedate()));
                        list.add(Text.literal(I18n.translate("sbpreise.lore.last_changed", lastChanged)));
                    }
                    list.add(Text.literal(I18n.translate("sbpreise.lore.credits")));
                }

            }
        } catch (NullPointerException ex) {
            System.out.println("NPE in getTooltipdone");
            try {
                Item item = this.getItem();
                if (item == null) {
                    System.out.println("item is null");
                } else {
                    System.out.println("item is " + this.getItem().getTranslationKey());
                }
            } catch (NullPointerException ignored) {

            }
        }

    }

}
