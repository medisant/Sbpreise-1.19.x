package me.medisant.sbpreise.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

@Config(name = "sbpreise")
public class ModConfig implements ConfigData {

    @ConfigEntry.Gui.Excluded
    public static ModConfig INSTANCE;

    public String api_url = "https://sbpreise.de/api/all.php";
    public double widthDivisor = 67d; //only active if useAbsolutGuiSize = false
    public double heigthDivisor = 67d; //only active if useAbsolutGuiSize = false
    public int absoluteWidth = 38; //only active if useAbsolutGuiSize = true
    public int absoluteHeigth = 21; //only active if useAbsolutGuiSize = true
    public boolean useAbsoluteGuiSize = false;

    public static void init() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        INSTANCE = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

}
