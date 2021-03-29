package com.gmail.nimadastmalchi.tag;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ActivePlayer {
    private final static Material ITEM1 = Material.WOODEN_HOE;
    private final static Material ITEM2 = Material.STONE_HOE;
    private final static Material ITEM3 = Material.IRON_HOE;
    private final static Material ITEM4 = Material.GOLDEN_HOE;
    private final static Material ITEM5 = Material.DIAMOND_HOE;
    private final static Material ITEM6 = Material.NETHERITE_HOE;
    private final static Material WEAPONS[] = {ITEM1, ITEM2, ITEM3, ITEM4, ITEM5, ITEM6};

    String name;
    private int weaponIndex;
    private boolean aimbotStatus;
    private long lastTime;

    public ActivePlayer(String name) {
        this.name = name;
        weaponIndex = 0;
        aimbotStatus = false;
        lastTime = System.currentTimeMillis();
    }

    // Accessors:
    public double getDmg() {
        return 5 + 3 * weaponIndex;
    }

    public boolean hasAimbot() {
        return aimbotStatus;
    }

    public boolean isWeapon(Material m) {
        return WEAPONS[weaponIndex] == m;
    }

    public boolean hasWon() {
        return weaponIndex == WEAPONS.length - 1;
    }

    public long getTime() {
        return lastTime;
    }

    // Mutators:
    public void upgradeWeapon() {
        weaponIndex = (weaponIndex < WEAPONS.length - 1) ? weaponIndex + 1 : weaponIndex;
        giveWeapon();
    }

    public void downgradeWeapon() {
        weaponIndex = (weaponIndex >= 1) ? weaponIndex - 1: weaponIndex;
        giveWeapon();
    }

    public void setAimbotStatus(boolean aimbotStatus) {
        this.aimbotStatus = aimbotStatus;
    }

    public void setTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public void giveWeapon() {
        Player p = Bukkit.getPlayer(name);
        p.getInventory().setItem(0, new ItemStack(WEAPONS[weaponIndex]));
    }
}
