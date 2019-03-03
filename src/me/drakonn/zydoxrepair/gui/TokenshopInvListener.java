package me.drakonn.zydoxrepair.gui;

import me.drakonn.zydoxrepair.ZydoxRepair;
import me.drakonn.zydoxrepair.datamanager.ItemManager;
import me.drakonn.zydoxrepair.datamanager.MessageManager;
import me.drakonn.zydoxrepair.util.Util;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class TokenshopInvListener implements Listener {

    @EventHandler
    public void onTokeninvClick(InventoryClickEvent event)
    {
        if(!(event.getWhoClicked() instanceof Player))
            return;

        if(event.getClickedInventory() == null)
            return;

        if(!event.getClickedInventory().getTitle().equals(ItemManager.getTokenPurchaseInventory().getTitle()))
            return;

        if(event.getClickedInventory().getSize() != ItemManager.getTokenPurchaseInventory().getSize())
            return;

        Player player = (Player)event.getWhoClicked();

        event.setCancelled(true);
        player.updateInventory();

        ItemStack clickedItem = event.getCurrentItem();

        if(clickedItem == null || clickedItem.getType().equals(Material.AIR))
            return;

        TokenshopItem tokenshopItem = ItemManager.getTokenshopItem(clickedItem);

        if(tokenshopItem == null)
            return;

        int cost = tokenshopItem.getCost();
        if(ZydoxRepair.getInstance().getEconomy() != null) {
            Economy economy = ZydoxRepair.getInstance().getEconomy();
            double balance = economy.getBalance(player);
            if (balance <= cost) {
                player.sendMessage(MessageManager.insufficientFunds);
                return;
            }
            economy.withdrawPlayer(player, cost);
        }

        ItemStack tokenItem = ItemManager.getTokenItem();
        tokenItem.setAmount(tokenshopItem.getAmount());
        Util.givePlayerItem(player, tokenItem);
        player.sendMessage(getBoughtMessage(tokenshopItem.getAmount(), tokenshopItem.getCost()));
    }

    private String getBoughtMessage(int amount, int cost)
    {
        String message = MessageManager.boughtToken;
        message = message.replaceAll("%amount%", Integer.toString(amount));
        message = message.replaceAll("%cost%", Integer.toString(cost));
        return message;
    }
}
