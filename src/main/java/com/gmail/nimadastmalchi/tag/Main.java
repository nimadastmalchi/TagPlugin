package com.gmail.nimadastmalchi.tag;

import com.gmail.nimadastmalchi.tag.commands.TagCommand;
import com.gmail.nimadastmalchi.tag.listeners.EntityDamageListener;
import com.gmail.nimadastmalchi.tag.listeners.PlayerInteractListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Main extends JavaPlugin {
    public static HashMap<String, ActivePlayer> players;

    @Override
    public void onEnable() {
        players = new HashMap<>();

        new TagCommand(this);
        new PlayerInteractListener(this);
        new EntityDamageListener(this);
    }
}
