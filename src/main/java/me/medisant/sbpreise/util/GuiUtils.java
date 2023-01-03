package me.medisant.sbpreise.util;

import me.medisant.sbpreise.config.ModConfig;
import net.minecraft.client.MinecraftClient;

//just an util to get the gui size depending on the settings set in the config
public class GuiUtils {

    public static int getWidth() {
        if (ModConfig.INSTANCE.useAbsoluteGuiSize) return ModConfig.INSTANCE.absoluteWidth;
        return (int) Math.floor(MinecraftClient.getInstance().getWindow().getWidth() / ModConfig.INSTANCE.widthDivisor);
    }

    public static int getHeigth() {
        if (ModConfig.INSTANCE.useAbsoluteGuiSize) return ModConfig.INSTANCE.absoluteHeigth;
        return (int) Math.floor(MinecraftClient.getInstance().getWindow().getHeight() / ModConfig.INSTANCE.heigthDivisor);
    }
}
