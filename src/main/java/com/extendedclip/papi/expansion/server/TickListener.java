package com.extendedclip.papi.expansion.server;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class TickListener implements Listener {

    private boolean enabled = true;

    private int[] indices = new int[]{0, 0, 0};
    private double[] averages = new double[]{0d, 0d, 0d};
    private final ConcurrentHashMap<Integer, Double> durations1s = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Double> durations10s = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Double> durations1m = new ConcurrentHashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void event(ServerTickEndEvent event) {

        if (!this.enabled) {
            return;
        }

        if (this.indices[0] >= 20) {
            this.indices[0] = 1;
        }
        if (this.indices[1] >= 200) {
            this.indices[1] = 1;
        }
        if (this.indices[2] >= 1200) {
            this.indices[2] = 1;
        }

        double duration = event.getTickDuration();

        this.indices[0]++;
        this.indices[1]++;
        this.indices[2]++;

        this.durations1s.put(this.indices[0], duration);
        this.durations10s.put(this.indices[1], duration);
        this.durations1m.put(this.indices[2], duration);

        double totalOneSecond = 0d;
        double totalTenSeconds = 0d;
        double TotalOneMinute = 0d;
        for (double d : this.durations1s.values()) {
            totalOneSecond += d;
        }
        for (double d : this.durations10s.values()) {
            totalTenSeconds += d;
        }
        for (double d : this.durations1m.values()) {
            TotalOneMinute += d;
        }
        this.averages[0] = totalOneSecond / ((double) this.durations1s.size());
        this.averages[1] = totalTenSeconds / ((double) this.durations10s.size());
        this.averages[2] = TotalOneMinute / ((double) this.durations1m.size());
    }

    public double[] getAverages() {
        return this.averages;
    }
}
