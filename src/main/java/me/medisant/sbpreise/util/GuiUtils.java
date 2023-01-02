package me.medisant.sbpreise.util;

import me.medisant.sbpreise.config.ModConfig;
import net.minecraft.client.MinecraftClient;

public class GuiUtils {

    public static int getWidth() {
        return (int) Math.floor(MinecraftClient.getInstance().getWindow().getWidth() / ModConfig.INSTANCE.widthDivisor);
    }

    public static int getHeigth() {
        return (int) Math.floor(MinecraftClient.getInstance().getWindow().getHeight() / ModConfig.INSTANCE.heigthDivisor);
    }
}
