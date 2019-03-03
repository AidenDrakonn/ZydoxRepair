package me.drakonn.zydoxrepair.datamanager;

import me.drakonn.zydoxrepair.ZydoxRepair;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class MessageManager {

    public static String tokenUsed;
    public static String givenToken;
    public static String notRepairable;
    public static String alreadyRepaired;
    public static String noPermission;
    public static String insufficientFunds;
    public static String boughtToken;
    public static List<String> help = new ArrayList<>();


    private ZydoxRepair plugin;
    public MessageManager(ZydoxRepair plugin)
    {
        this.plugin = plugin;
    }

    public void loadMessages()
    {
        tokenUsed = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.tokenused"));
        givenToken = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.giventoken"));
        notRepairable = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.notrepairable"));
        alreadyRepaired = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.alreadyrepaired"));
        noPermission = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.nopermission"));
        insufficientFunds = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.insufficientfunds"));
        boughtToken = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.boughttoken"));
        setHelp();
    }

    public void setHelp()
    {
        help.add("§7-----------------§bZydoxRepair§7-----------------");
        help.add("§b/zrepair give (player) [amount] §8- §fGive a player repair tokens");
        help.add("§b/zrepair reload §8- §fReload the plugin and apply all config changes");
        help.add("§b/zrepair help §8- §fShow this help message");
        help.add("§b/blacksmith §8- §fOpens the blacksmith gui");
    }
}
