package com.gmail.nimadastmalchi.tag.listeners;

import com.gmail.nimadastmalchi.tag.ActivePlayer;
import com.gmail.nimadastmalchi.tag.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

import static java.lang.Math.abs;
import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.PI;

public class PlayerInteractListener implements Listener {
    private Main plugin;

    // Height from feet to eyes (in blocks):
    private final float PLAYER_STAND_HEIGHT = (float) 1.6;
    private final float PLAYER_SNEAK_HEIGHT = (float) 1.27;

    // Height from feet to top of head (in blocks):
    private final float TARGET_STAND_HEIGHT = (float) 1.92;
    private final float TARGET_SNEAK_HEIGHT = (float) 1.54;

    public PlayerInteractListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!Main.players.containsKey(player.getName())) {
            return;
        }
        ActivePlayer ap = Main.players.get(player.getName());

        // Check if player clicked air, player is holding an item, and that item is the player's weapon:
        if (e.getAction() == Action.RIGHT_CLICK_AIR && e.getItem() != null && ap.isWeapon(e.getItem().getType())) {
            long currentTime = System.currentTimeMillis();
            if (abs(currentTime - ap.getTime()) < 500) {
                return;
            }
            ap.setTime(currentTime);

            float playerYaw = player.getLocation().getYaw();
            float playerPitch = player.getLocation().getPitch();

            // Calculate the direction vector:
            float scale = (float) abs(cos(playerPitch * PI / 180));
            float xDir = (float) -sin(playerYaw * PI / 180) * scale;
            float yDir = (float) -sin(playerPitch * PI / 180);
            float zDir = (float) cos(playerYaw * PI / 180) * scale;
            float playerHeight = player.isSneaking() ? PLAYER_SNEAK_HEIGHT : PLAYER_STAND_HEIGHT;

            // Print the particles:
            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(255, 25, 25), 1);
            for (int i = 1; i <= 50; i++) {
                Location l = player.getLocation().clone().add(i * xDir, i * yDir + playerHeight, i * zDir);
                player.getWorld().spawnParticle(Particle.REDSTONE, l, 1, dustOptions);
                if (!l.getBlock().isEmpty() && !l.getBlock().isPassable()) {
                    // store i and make sure the i found below is less than this i.
                }
            }

            Location playerLocation = player.getLocation();
            float minDistanceSquared = Float.MAX_VALUE;
            String closestTargetName = "";
            for (Map.Entry entry : Main.players.entrySet()) {
                String targetName = (String) entry.getKey();
                if (targetName.equals(player.getName())) {
                    continue;
                }
                Player target = Bukkit.getPlayer(targetName);
                Location targetLocation = target.getLocation();

                // The height and yaw of the opponent:
                float targetHeight = target.isSneaking() ? TARGET_SNEAK_HEIGHT : TARGET_STAND_HEIGHT;
                float targetYaw = targetLocation.getYaw();

                // Offsets:
                float xOffset = (float) abs(0.25 + 0.25 * sin(playerYaw - targetYaw));
                float zOffset = (float) abs(0.25 + 0.25 * cos(playerYaw - targetYaw));

                // Upper and lower bounds of i for hitting the target:
                float xLow = (float) (targetLocation.getX() - xOffset - playerLocation.getX()) / xDir;
                float xHigh = (float) (targetLocation.getX() + xOffset - playerLocation.getX()) / xDir;
                float yLow = (float) (targetLocation.getY() - playerLocation.getY() - playerHeight) / yDir;
                float yHigh = (float) (targetLocation.getY() + targetHeight - playerLocation.getY() - playerHeight) / yDir;
                float zLow = (float) (targetLocation.getZ() - zOffset - playerLocation.getZ()) / zDir;
                float zHigh = (float) (targetLocation.getZ() + zOffset - playerLocation.getZ()) / zDir;

                // To account for negative xDir, yDir, or zDir:
                if (xLow > xHigh) {
                    xLow += xHigh - (xHigh = xLow);
                }
                if (yLow > yHigh) {
                    yLow += yHigh - (yHigh = yLow);
                }
                if (zLow > zHigh) {
                    zLow += zHigh - (zHigh = zLow);
                }

                float maxLow = max(xLow, yLow, zLow);
                float minHigh = min(xHigh, yHigh, zHigh);

                // maxLow <= minHigh if:
                // xLow <= i <= xHigh
                // yLow <= i <= yHigh
                // zLow <= i <= zHigh

                // if iExists, max(xLow, yLow, zLow) >= 0 is true if there exists at least one i that is positive:
                if (maxLow <= minHigh && maxLow >= 0) {
                    float currentDistance = (float) targetLocation.distanceSquared(playerLocation);
                    if (currentDistance < minDistanceSquared) {
                        minDistanceSquared = currentDistance;
                        closestTargetName = targetName;
                    }
                }
            }
            // Check if a target is hit:
            if (closestTargetName != "") {
                Player target = Bukkit.getPlayer(closestTargetName);
                target.damage(ap.getDmg());
                if (target.getHealth() == 0) {
                    player.setHealth(20);
                    ap.upgradeWeapon();
                }
                /*
                double health = target.getHealth();
                if (health <= 0) {
                    return;
                }
                health -= ap.getDmg();
                if (health < 0) {
                    health = 0;
                    player.setHealth(20);
                    ap.upgradeWeapon();
                    ActivePlayer targetAP = Main.players.get(target.getName());
                    targetAP.giveWeapon();
                }
                target.setHealth(health);
                */
            }
        }
    }

    public static float max(float a, float b, float c) {
        return Math.max(Math.max(a, b), c);
    }

    public static float min(float a, float b, float c) {
        return Math.min(Math.min(a, b), c);
    }

}
