package com.extendedclip.papi.expansion.server;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.Cacheable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.util.TimeUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ServerExpansion extends PlaceholderExpansion implements Cacheable {
	
	private final Map<String, SimpleDateFormat> dateFormats = new HashMap<String, SimpleDateFormat>();
	private final int MB = 1048576;
    private final Runtime runtime = Runtime.getRuntime();
	private Object craftServer;
    private Field tps;
    private String version;

	private final String VERSION = getClass().getPackage().getImplementationVersion();

	public ServerExpansion() {
        try {
        	version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            craftServer = Class.forName("net.minecraft.server." + version + ".MinecraftServer").getMethod("getServer").invoke(null);
            tps = craftServer.getClass().getField("recentTps");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
    @Override
	public void clear() {
		craftServer = null;
		tps = null;
		version = null;
		dateFormats.clear();
	}

	
	@Override
	public String getIdentifier() {
		return "server";
	}
	
	@Override
	public String getPlugin() {
		return null;
	}
	
	@Override
	public String getAuthor() {
		return "clip";
	}

	@Override
	public String getVersion() {
		return VERSION;
	}
 
	@Override
	public String onPlaceholderRequest(Player p, String identifier) {
		
		switch(identifier) {
		case "tps":
			return getTps(null);
		case "online":
			return String.valueOf(Bukkit.getOnlinePlayers().size());
		case "max_players":
			return String.valueOf(Bukkit.getMaxPlayers());
		case "unique_joins":
			return String.valueOf(Bukkit.getOfflinePlayers().length);
		case "uptime":
			return getPlaceholderAPI().getUptime();
		case "has_whitelist":
			return Bukkit.getServer().hasWhitelist() ? PlaceholderAPIPlugin.booleanTrue() : PlaceholderAPIPlugin.booleanFalse();
		}
		
		if (identifier.startsWith("tps_")) {
			identifier = identifier.replace("tps_", "");
			return getTps(identifier);
		}
		
		if (identifier.startsWith("online_")) {
			
			identifier = identifier.replace("online_", "");
			
			int i = 0;
			
			for (Player o : Bukkit.getOnlinePlayers()) {
				if (o.getWorld().getName().equals(identifier)) {
					i = i+1;
				}
			}
			return String.valueOf(i);
		}
		
		if (identifier.startsWith("countdown_")) {
			String time = identifier.replace("countdown_", "");
			
			if (time.indexOf("_") == -1) {
				
				Date then = null;
				
				try {
					then = PlaceholderAPIPlugin.getDateFormat().parse(time);
				} catch(Exception e) {
					return null;
				}
				
				Date now = new Date();
				
				long between = then.getTime() - now.getTime();
				
				if (between <= 0) {
					return "0";
				}
				
				return TimeUtil.getTime((int)TimeUnit.MILLISECONDS.toSeconds(between));
				
			} else {
				
				String[] parts = time.split("_");
				
				if (parts.length != 2) {
					return "invalid format and time";
				}
				
				time = parts[1];
				
				String format = parts[0];
				
				SimpleDateFormat f = null;
				
				try {
					f = new SimpleDateFormat(format);
				} catch (Exception e) {
					return "invalid date format";
				}
				
				Date then = null;
				
				try {
					then = f.parse(time);
				} catch(Exception e) {
					return "invalid date";
				}
				
				long t = System.currentTimeMillis();
				
				long between = then.getTime() - t;
				
				if (between <= 0) {
					return "0";
				}
				
				return TimeUtil.getTime((int)TimeUnit.MILLISECONDS.toSeconds(between));
				
			}
		}
		
		if (identifier.startsWith("time_")) {
			
			identifier = identifier.replace("time_", "");
			
			if (dateFormats.containsKey(identifier)) {
				return dateFormats.get(identifier).format(new Date());
			}
			
			try {
				SimpleDateFormat format = new SimpleDateFormat(identifier);
				
				dateFormats.put(identifier, format);
				
				return format.format(new Date());
			} catch (NullPointerException | IllegalArgumentException ex) {
				return null;
			}
		}

		if (identifier.startsWith("ram_")) {

			if (identifier.equals("ram_used")) {
				return String.valueOf((runtime.totalMemory() - runtime.freeMemory()) / MB);
			}

			if (identifier.equals("ram_free")) {
				return String.valueOf(runtime.freeMemory() / MB);
			}

			if (identifier.equals("ram_total")) {
				return String.valueOf(runtime.totalMemory() / MB);
			}

			if (identifier.equals("ram_max")) {
				return String.valueOf(runtime.maxMemory() / MB);
			}
		}
		
		return null;
	}
	
	private double[] tps() {
		if (version == null || craftServer == null || tps == null) {
			return new double[]{0,0,0};
		}
        try {
            return((double[]) tps.get(craftServer));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new double[]{0,0,0};
	}	

	private double fix(double tps) {
		return Math.min( Math.round( tps * 100.0 ) / 100.0, 20.0 );
	}
	
    private String color(double tps) {
        return ((tps > 18.0) ? ChatColor.GREEN : (tps > 16.0) ? ChatColor.YELLOW : ChatColor.RED).toString()
                + ( ( tps > 20.0 ) ? "*" : "") + fix(tps);
    }
	
	public String getTps(String arg) {
		if (arg == null || arg.isEmpty()) {
			StringBuilder sb = new StringBuilder();
	        for (double t : tps()) {
	            sb.append(color(t));
	            sb.append(", ");
	        }
	        return sb.toString();
		}
        switch(arg) {
        case "1":
        case "one":
        	return String.valueOf(fix(tps()[0]));
        case "5":
        case "five":
        	return String.valueOf(fix(tps()[1]));
        case "15":
        case "fifteen":
        	return String.valueOf(tps()[2]);
        case "1_colored":
        case "one_colored":
        	return color(tps()[0]);
        case "5_colored":
        case "five_colored":
        	return color(tps()[1]);
        case "15_colored":
        case "fifteen_colored":
        	return color(tps()[2]);
        }
        return null;
	}

}
