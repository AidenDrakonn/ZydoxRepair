package me.drakonn.zydoxrepair.command;

import me.drakonn.zydoxrepair.datamanager.ItemManager;
import me.drakonn.zydoxrepair.datamanager.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BlacksmithCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!command.getLabel().equalsIgnoreCase("blacksmith"))
            return  true;

        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("[ZydoxRepair] /blacksmith can only be used ingame, try /zrepair");
            return true;
        }

        Player player = (Player)commandSender;
        if(!player.hasPermission("zrepair.blacksmith"))
        {
            player.sendMessage(MessageManager.noPermission);
            return true;
        }

        player.openInventory(ItemManager.getTokenPurchaseInventory());
        return true;
    }
}
