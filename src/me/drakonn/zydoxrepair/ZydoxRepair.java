package me.drakonn.zydoxrepair;

import me.drakonn.zydoxrepair.command.BlacksmithCommand;
import me.drakonn.zydoxrepair.command.ZydoxRepairCommand;
import me.drakonn.zydoxrepair.datamanager.ItemManager;
import me.drakonn.zydoxrepair.datamanager.MessageManager;
import me.drakonn.zydoxrepair.gui.TokenshopInvListener;
import me.drakonn.zydoxrepair.itemuse.InvListener;
import me.drakonn.zydoxrepair.util.Glow;
import me.drakonn.zydoxrepair.util.Util;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class ZydoxRepair extends JavaPlugin {

    private ItemManager itemManager = new ItemManager(this);
    private MessageManager messageManager = new MessageManager(this);
    private BlacksmithCommand blacksmithCommand = new BlacksmithCommand();
    private ZydoxRepairCommand zydoxRepairCommand = new ZydoxRepairCommand();
    private static ZydoxRepair instance;
    private Economy economy;

    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        economy = Util.setupEconomy(this);
        registerGlow();
        itemManager.loadItems();
        messageManager.loadMessages();
        getServer().getPluginManager().registerEvents(new InvListener(), this);
        getServer().getPluginManager().registerEvents(new TokenshopInvListener(), this);
        getCommand("blacksmith").setExecutor(blacksmithCommand);
        getCommand("zrepair").setExecutor(zydoxRepairCommand);
    }

    public void registerGlow() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Glow glow = new Glow(70);
            Enchantment.registerEnchantment(glow);
        }
        catch (IllegalArgumentException e){
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static ZydoxRepair getInstance()
    {
        return instance;
    }

    public Economy getEconomy() {
        return economy;
    }
}
