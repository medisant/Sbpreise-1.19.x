package me.medisant.sbpreise.util;

import me.medisant.sbpreise.api.ApiInteraction;
import me.medisant.sbpreise.api.ItemStatistics;

import java.util.*;

public class ItemStatisticUtils {

    public static Map<String, List<ItemStatistics>> getItemStatisticsInCategories() {
        Map<String, List<ItemStatistics>> unsortedOutputMap = new HashMap<>();
        for (ItemStatistics itemStatistics : new ApiInteraction().getItemStatistics(false)) {
            String category = itemStatistics.getCategory();

            if (!unsortedOutputMap.containsKey(category)) unsortedOutputMap.put(category, new ArrayList<>());
            unsortedOutputMap.get(category).add(itemStatistics);
        }

        Map<String, List<ItemStatistics>> sortedOutputMap = new TreeMap<>();

        for (Map.Entry<String, List<ItemStatistics>> entry : unsortedOutputMap.entrySet()) { //for every category
            List<ItemStatistics> unsortedItemStatistics = entry.getValue(); //the raw itemStatistics

            Map<String, ItemStatistics> unsortedTempMap = new HashMap<>(); //unsorted map of the key to sort and the itemStatistics
            unsortedItemStatistics.forEach(itemStatistics -> unsortedTempMap.put(itemStatistics.getFriendly_name(), itemStatistics));

            Map<String, ItemStatistics> sortedTempMap = new TreeMap<>(unsortedTempMap); //sorted map of the names(keys) and itemStatistics

            sortedOutputMap.put(entry.getKey(), sortedTempMap.values().stream().toList());
        }
        return sortedOutputMap;
    }

}
