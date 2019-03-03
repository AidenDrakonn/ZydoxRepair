package me.drakonn.zydoxrepair.datamanager;

import me.drakonn.zydoxrepair.ZydoxRepair;
import me.drakonn.zydoxrepair.gui.TokenshopItem;
import me.drakonn.zydoxrepair.util.Glow;
import me.drakonn.zydoxrepair.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemManager {

    private static List<TokenshopItem> items = new ArrayList<>();
    private static ItemStack tokenItem;
    private static Inventory tokenPurchaseInventory;
    private static ItemStack fillItem;

    private ZydoxRepair plugin;
    public ItemManager(ZydoxRepair plugin)
    {
        this.plugin = plugin;
    }

    public void loadItems()
    {
        loadTokenItem(plugin.getConfig().getConfigurationSection("tokenitem"));
        fillItem = loadItem(plugin.getConfig().getConfigurationSection("fillitem"));
        ConfigurationSection topSection = plugin.getConfig().getConfigurationSection("guiitems");

        for(String key : topSection.getKeys(false))
        {
            ConfigurationSection section = topSection.getConfigurationSection(key);
            loadGuiItem(section);
        }

        int invSize = plugin.getConfig().getInt("gui.size");
        String title = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("gui.title"));
        Inventory inv = Bukkit.createInventory(null, invSize, title);

        for(TokenshopItem tokenshopItem : items)
        {
            inv.setItem(tokenshopItem.getInvSlot(), tokenshopItem.getItem());
        }

        for(int i =0; i < inv.getSize(); i++)
        {
            if(inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR))
            {
                inv.setItem(i, fillItem);
            }
        }
        tokenPurchaseInventory = inv;
    }

    private void loadGuiItem(ConfigurationSection section)
    {
        int cost = getCost(section);
        int amount = getAmount(section);
        int invSlot = getInvSlot(section);
        List<String> lore = getlore(section);
        String name = ChatColor.translateAlternateColorCodes('&',section.getString("name"));
        ItemStack item = tokenItem.clone();
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        if(amount <= 64)
            item.setAmount(amount);
        items.add(new TokenshopItem(item, invSlot, cost, amount));
    }

    private void loadTokenItem(ConfigurationSection section)
    {
        ItemStack item = loadItem(section);
        tokenItem = item;
    }

    private ItemStack loadItem(ConfigurationSection section)
    {
        if(section.getKeys(false).containsAll(Arrays.asList("material", "name"))) {
            String materialData = section.getString("material");
            Material material;
            String materialID = materialData.split(":")[0];
            if(Util.isInt(materialID))
                material = Material.getMaterial(Integer.valueOf(materialID));
            else material = Material.getMaterial(materialID);

            if(material == null) {
                System.out.println("[ZydoxRepair] Material for " + section.getName() +" invalid, set to stone");
                material = Material.STONE;
            }
            String name = ChatColor.translateAlternateColorCodes('&', section.getString("name"));
            List<String> lore = getlore(section);
            ItemStack item = Util.createItem(material, name, lore);
            if (materialData.split(":").length == 2) {
                item.setDurability(Short.valueOf(materialData.split(":")[1]));
            }

            ItemMeta meta = item.getItemMeta();
            if(section.getBoolean("enchanted"))
            {
                meta.addEnchant(new Glow(70), 1, true);
                item.setItemMeta(meta);
            }

            return item;
        }

        System.out.println("[ZydoxRepair] " + section.getName() + " Did not contain a material or item name, has been set to stone");
        return new ItemStack(Material.STONE);
    }

    private List<String> getlore(ConfigurationSection section)
    {
        List<String> lore;
        List<String> newLore = new ArrayList<>();
        if(section.getKeys(false).contains("lore"))
            lore = section.getStringList("lore");
        else
        {
            System.out.println("[ZydoxRepair] No lore found for " + section.getName() + " please set in config");
            lore = new ArrayList<>();
        }


        for(String loreString : lore)
        {
            loreString = ChatColor.translateAlternateColorCodes('&', loreString);
            newLore.add(loreString);
        }

        return newLore;
    }

    private int getCost(ConfigurationSection section)
    {
        if(section.getKeys(false).contains("cost"))
            return section.getInt("cost");
        System.out.println("[ZydoxRepair] No cost set for " + section.getName() + " setting cost as 0");
        return 0;
    }

    private int getInvSlot(ConfigurationSection section)
    {
        if(section.getKeys(false).contains("invslot"))
        {
            return section.getInt("invslot");
        }

        System.out.println("[ZydoxRepair] No invslot set for " + section.getName() + " setting to 0");
        return 0;
    }
    
    private int getAmount(ConfigurationSection section)
    {
        if(section.getKeys(false).contains("amount"))
        {
            return section.getInt("amount");
        }

        System.out.println("[ZydoxRepair] No amount set for " + section.getName() + " setting to 1");
        return 1;
    }

    public static TokenshopItem getTokenshopItem(ItemStack item) {
        return items.stream().filter(tokenshopItem -> tokenshopItem.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(item.getItemMeta().getDisplayName())
                && tokenshopItem.getItem().getItemMeta().getLore().equals(item.getItemMeta().getLore()))
                .findFirst().orElse(null);
    }

    public static List<TokenshopItem> getItems() {
        return items;
    }

    public static ItemStack getTokenItem() {
        return tokenItem.clone();
    }

    public static ItemStack getFillItem()
    {
        return fillItem.clone();
    }

    public static Inventory getTokenPurchaseInventory() {
        return tokenPurchaseInventory;
    }
}
