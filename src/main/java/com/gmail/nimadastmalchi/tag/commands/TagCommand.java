package com.gmail.nimadastmalchi.tag.commands;

import com.gmail.nimadastmalchi.tag.ActivePlayer;
import com.gmail.nimadastmalchi.tag.Main;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagCommand implements CommandExecutor {
    private Main plugin;

    public TagCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("tag").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players may execute this command.");
            return true;
        }

        Player p = (Player) sender;
        if (args.length != 0) {
            p.sendMessage(ChatColor.RED + "Incorrect number of arguments for this command. Try again.");
            return true;
        }

        if (Main.players.containsKey(p.getName())) {
            p.sendMessage(ChatColor.WHITE + "Tag:" + ChatColor.RED + " [ON]" + ChatColor.GREEN + " [OFF]");
            Main.players.remove(p.getName());
        } else {
            p.sendMessage(ChatColor.WHITE + "Tag:" + ChatColor.GREEN + " [ON]" + ChatColor.RED + " [OFF]");
            ActivePlayer ap = new ActivePlayer(p.getName());
            Main.players.put(p.getName(), ap);
            p.setGameMode(GameMode.SURVIVAL);
            ap.giveWeapon();
        }
        return true;
    }
}
