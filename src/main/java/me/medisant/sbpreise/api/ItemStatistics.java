package me.medisant.sbpreise.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ItemStatistics {

    private String minecraft_name;
    private String friendly_name;
    private double price_min;
    private double price_max;
    private long lastchangedate;
    private String category;

    public static ItemStatistics ERROR = new ItemStatistics(
            "error",
            "error",
            0.0,
            0.0,
            0,
            "error");
}
