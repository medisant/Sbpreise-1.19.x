package me.medisant.sbpreise.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.medisant.sbpreise.Sbpreise;
import me.medisant.sbpreise.config.ModConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ApiInteraction {

    public List<ItemStatistics> getItemStatistics(boolean ignoreCache) {
        if (ignoreCache || Sbpreise.cache == null || Sbpreise.cache.size() == 0) {

            try {
                //reading the raw json data from the api
                URL url = new URL(ModConfig.INSTANCE.api_url);
                InputStream input = url.openStream();
                String json = new String(input.readAllBytes());
                input.close();

                //parsing the data to a list of ItemStatistics
                return new Gson().fromJson(json, new TypeToken<List<ItemStatistics>>() {
                }.getType());
            } catch (IOException e) {
                e.printStackTrace();
                return Collections.emptyList();
            }

        } else return Sbpreise.cache; //returning the cache
    }

    public ItemStatistics getItemStatistics(String id, String name) {
        if (id.equalsIgnoreCase("enchanted_book")) return null;

        ItemStatistics itemStatistics = getItemStatisticsFromName(name); //first trying to get the stats using the name of the item
        if (itemStatistics == null)
            itemStatistics = getItemStatisticsFromId(id); //if this does not work, try using the id
        //if (itemStatistics == null) return ItemStatistics.ERROR; //returning an error if there is no result

        return itemStatistics;
    }

    public ItemStatistics getItemStatisticsFromId(String minecraft_name) {
        for (ItemStatistics itemStatistics : getItemStatistics(false)) {
            if (itemStatistics.getMinecraft_name().equalsIgnoreCase(minecraft_name)) return itemStatistics;
        }
        return null;
    }

    public ItemStatistics getItemStatisticsFromName(String friendly_name) {
        for (ItemStatistics itemStatistics : getItemStatistics(false)) {
            if (itemStatistics.getFriendly_name().equalsIgnoreCase(friendly_name)) return itemStatistics;
        }
        return null;
    }

    public String requestPriceChange(int id, double price_min, double price_max) {
        try {
            String urlString = "https://sbpreise.de/api/insert.php?id=" + id + "&price_min=" + price_min + "&price_max=" + price_max;
            //reading the raw json data from the api
            URL url = new URL(urlString);
            InputStream input = url.openStream();
            String json = new String(input.readAllBytes());
            Map<String, Object> result = new Gson().fromJson(json, new TypeToken<Map<String, Object>>() {
            }.getType());
            input.close();
            if (result.containsKey("error")) return (String) result.get("error");
            if (result.containsKey("success")) return (String) result.get("success");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
