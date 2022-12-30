package me.medisant.sbpreise;

import me.medisant.sbpreise.api.DataProvider;
import me.medisant.sbpreise.api.ItemStatistics;
import me.medisant.sbpreise.config.ModConfig;
import net.fabricmc.api.ModInitializer;

import java.util.List;

public class Sbpreise implements ModInitializer {

    public static List<ItemStatistics> cache;

    @Override
    public void onInitialize() {
        ModConfig.init();
        initializeCache();
    }

    private void initializeCache() {
        cache = new DataProvider().getItemStatistics(true);
    }
}
