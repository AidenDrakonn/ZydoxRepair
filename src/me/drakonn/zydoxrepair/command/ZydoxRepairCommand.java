package me.drakonn.zydoxrepair.command;

import me.drakonn.zydoxrepair.ZydoxRepair;
import me.drakonn.zydoxrepair.datamanager.ItemManager;
import me.drakonn.zydoxrepair.datamanager.MessageManager;
import me.drakonn.zydoxrepair.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ZydoxRepairCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!command.getLabel().equalsIgnoreCase("zrepair"))
            return  true;

        if(commandSender instanceof Player)
        {
            Player player = (Player)commandSender;
            if(!player.hasPermission("zrepair.admin"))
            {
                player.sendMessage(MessageManager.noPermission);
                return true;
            }
        }

        if(args.length == 1 && args[0].equalsIgnoreCase("help"))
        {
            for(String string : MessageManager.help)
                commandSender.sendMessage(string);

            return true;
        }

        if(args.length == 1 && args[0].equalsIgnoreCase("reload"))
        {
            ZydoxRepair.getInstance().onEnable();
            commandSender.sendMessage("§8§l[§b§lZydoxRepair§8§l] §fConfig has been reloaded");
            return true;
        }

        if(args.length >= 2 && args[0].equalsIgnoreCase("give")) {
            Player target = Bukkit.getPlayer(args[1]);
            if(target == null)
            {
                commandSender.sendMessage("§8§l[§b§lZydoxRepair§8§l] §b"+args[1]+" §fis not online");
                return true;
            }

            ItemStack item = ItemManager.getTokenItem();
            if(args.length == 3 && Util.isInt(args[2]))
            {
                item.setAmount(Integer.valueOf(args[2]));
            }

            Util.givePlayerItem(target, item);
            target.sendMessage(MessageManager.givenToken);
            commandSender.sendMessage("§8§l[§b§lZydoxRepair§8§l] §fGiven " + Integer.toString(item.getAmount())+" repair token to §b"+args[1]);
            return true;
        }

        for(String string : MessageManager.help)
            commandSender.sendMessage(string);

        return true;
    }
}
