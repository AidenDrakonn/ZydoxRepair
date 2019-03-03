package me.drakonn.zydoxrepair.util;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.*;

public class Util
{

    public static ItemStack createItem(final Material mat, final int amt, final int durability, final String name,
            final List<String> lore) {
        final ItemStack item = new ItemStack(mat, amt);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Util.color(name));
        meta.setLore(Util.color(lore));
        if (durability != 0)
            item.setDurability((short) durability);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(final Material mat, final String name, final List<String> lore) {
        return createItem(mat, 1, 0, name, lore);
    }

    public static String color(final String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> color(final List<String> list) {
        final List<String> colored = new ArrayList<String>();
        for (final String s : list)
            colored.add(color(s));
        return colored;
    }

    public static Economy setupEconomy(final Plugin p) {
        if (p.getServer().getPluginManager().getPlugin("Vault") == null)
            return null;
        final RegisteredServiceProvider<Economy> rsp = p.getServer().getServicesManager()
                .getRegistration(Economy.class);
        return rsp == null ? null : rsp.getProvider();
    }

    public static Location stringToLocation(final Plugin p, final String s) {
        if (p == null || s == null || s.isEmpty())
            return null;
        final String[] args = s.split(",");
        try {
            return new Location(p.getServer().getWorld(args[0].trim()), Double.parseDouble(args[1].trim()),
                    Double.parseDouble(args[2].trim()), Double.parseDouble(args[3].trim()),
                    (float) Double.parseDouble(args[4].trim()), (float) Double.parseDouble(args[5].trim()));
        } catch (final NullPointerException e) {
            return new Location(
                    p.getServer().getWorlds().stream().filter(w -> w.getEnvironment() == World.Environment.NORMAL).findFirst()
                            .get(),
                    Double.parseDouble(args[1].trim()), Double.parseDouble(args[2].trim()),
                    Double.parseDouble(args[3].trim()), (float) Double.parseDouble(args[4].trim()),
                    (float) Double.parseDouble(args[5].trim()));
        } catch (final NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public static String locationToString(final Location l) {
        try {
            return l.getWorld().getName() + "," + round(l.getX()) + "," + round(l.getY()) + "," + round(l.getZ()) + ","
                    + round(l.getYaw()) + "," + round(l.getPitch());
        } catch (final NullPointerException e) {
            return "";
        }
    }

    public static double round(final double num) {
        return (int) (num * 100) / 100.0;
    }

    public static boolean isInt(final String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    public static boolean isRepairable(final Material m)
    {
        return (Enchantment.DURABILITY.canEnchantItem(new ItemStack(m)));
    }

    public static void givePlayerItem(Player player, ItemStack item)
    {
        if (player.getInventory().firstEmpty() != -1)
        {
            player.getInventory().addItem(item);
        }
        else if (getSlot(player, item.getType()) != -1)
        {
            player.getInventory().addItem(item);
        }
        else
        {
            player.sendMessage( "§c§l(!) §cYour inventory is full, dropping item");
            player.getWorld().dropItem(player.getLocation(), item);
        }
    }

    public static int getSlot(Player p, Material type)
    {
        for (int i = 0; i < p.getInventory().getSize(); i++) {
            if ((p.getInventory().getItem(i).getType() == type) && (p.getInventory().getItem(i).getAmount() < p.getInventory().getItem(i).getMaxStackSize())) {
                return i;
            }
        }
        return -1;
    }

}
