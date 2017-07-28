package com.SirBlobman.notify;

import com.SirBlobman.combatlogx.config.Config;
import com.SirBlobman.combatlogx.utility.Util;

import org.bukkit.Bukkit;
import org.bukkit.boss.*;
import org.bukkit.entity.Player;

import java.util.*;

public class CustomBoss {
    private static Map<Player, BossBar> BOSS = Util.newMap();
    
    public static BossBar getBossBar(Player p) {
        if(BOSS.containsKey(p)) {
            BossBar bb = BOSS.get(p);
            return bb;
        } else {
            int def = Config.OPTION_TIMER;
            List<String> l1 = Util.newList("{time_left}");
            List<Object> l2 = Util.newList(def);
            String title = Util.formatMessage(Config.MESSAGE_BOSS_BAR, l1, l2);
            BarStyle bs = BarStyle.SOLID;
            BarColor bc = null;
            String color = Config.OPTION_BOSS_BAR_COLOR;
            try {
                bc = BarColor.valueOf(color);
            } catch(Throwable ex) {
                bc = null;
            } finally {
                if(bc == null) {
                    String error = "Invalid Boss Bar Color in config '" + color + "'. Defaulting to YELLOW";
                    Util.print(error);
                    bc = BarColor.YELLOW;
                }
            }
            BossBar bb = Bukkit.createBossBar(title, bc, bs);
            bb.setVisible(true);
            bb.addPlayer(p);
            BOSS.put(p, bb); return getBossBar(p);
        }
    }
    
    public static void changeTime(Player p, long time) {
        if(Config.OPTION_BOSS_BAR) {
            List<String> l1 = Util.newList("{time_left}");
            List<Object> l2 = Util.newList(time);
            String title = Util.formatMessage(Config.MESSAGE_BOSS_BAR, l1, l2);
            BossBar bb = getBossBar(p);
            double top = time;
            double bot = Config.OPTION_TIMER;
            double div = (top / bot);
            bb.setProgress(div);
            bb.setTitle(title);
        }
    }
    
    public static void remove(Player p) {
        if(Config.OPTION_BOSS_BAR) {
            BossBar bb = getBossBar(p);
            bb.setVisible(false);
            bb.removePlayer(p);
            BOSS.remove(p);
        }
    }
}