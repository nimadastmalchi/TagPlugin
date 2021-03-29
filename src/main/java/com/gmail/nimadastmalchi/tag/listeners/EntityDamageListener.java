package com.gmail.nimadastmalchi.tag.listeners;

import com.gmail.nimadastmalchi.tag.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Random;

public class EntityDamageListener implements Listener {
    private Main plugin;
    private float offset = 10;

    public EntityDamageListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        /*
        Entity en = e.getEntity();
        if (en instanceof Player) {
            Player p = (Player) en;
            if (Main.players.containsKey(p.getName()) && p.getHealth() - e.getDamage() <= 0) {
                p.setHealth(20);
                Location l = p.getLocation();
                Random rand = new Random();
                Location newLoc = new Location(p.getWorld(),l.getX() + rand.nextFloat() * offset, l.getY(), l.getZ() + rand.nextFloat() * offset);
                while (!newLoc.clone().getBlock().isPassable() || !newLoc.clone().add(0,1,0).getBlock().isPassable()) {
                    newLoc.add(0,1,0);
                }
                p.teleport(newLoc);
                Bukkit.broadcastMessage(ChatColor.DARK_BLUE + p.getName() + " died.");
            }
            e.setCancelled(true);
        }
         */
    }
}
