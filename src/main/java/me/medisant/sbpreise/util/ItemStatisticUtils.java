package me.medisant.sbpreise.util;

import me.medisant.sbpreise.api.ApiInteraction;
import me.medisant.sbpreise.api.ItemStatistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemStatisticUtils {

    public static Map<String, List<ItemStatistics>> getItemStatisticsInCategories() {
        Map<String, List<ItemStatistics>> outputMap = new HashMap<>();
        for (ItemStatistics itemStatistics: new ApiInteraction().getItemStatistics(false)) {
            String category = itemStatistics.getCategory();

            if (!outputMap.containsKey(category)) outputMap.put(category, new ArrayList<>());
            outputMap.get(category).add(itemStatistics);
        }
        return outputMap;
    }

}
