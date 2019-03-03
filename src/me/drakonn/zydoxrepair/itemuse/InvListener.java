package me.drakonn.zydoxrepair.itemuse;

import me.drakonn.zydoxrepair.datamanager.ItemManager;
import me.drakonn.zydoxrepair.datamanager.MessageManager;
import me.drakonn.zydoxrepair.util.Util;
import me.drakonn.zydoxweapons.weapons.ZydoxWeapon;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InvListener implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent event)
    {
        if(!(event.getWhoClicked() instanceof Player))
            return;

        if(event.getCursor() == null || event.getCurrentItem() == null)
            return;

        if(event.getCursor().getType() == Material.AIR || event.getCurrentItem().getType() == Material.AIR)
            return;

        if(!event.getClickedInventory().equals(event.getWhoClicked().getInventory()))
            return;
        ItemStack cursorItem = event.getCursor();
        ItemStack clickedItem = event.getCurrentItem();

        if(cursorItem == null || !cursorItem.hasItemMeta()
                || !cursorItem.getItemMeta().hasDisplayName() || !cursorItem.getItemMeta().hasLore())
            return;

        if(!cursorItem.isSimilar(ItemManager.getTokenItem()))
            return;

        Player player = (Player)event.getWhoClicked();
        ZydoxWeapon weapon = ZydoxWeapon.getZydoxWeapon(clickedItem);

        if(!Util.isRepairable(clickedItem.getType()) && weapon == null)
        {
            player.sendMessage(MessageManager.notRepairable);
                return;
        }

        if(weapon == null)
        {
            if (clickedItem.getDurability() == 0) {
                player.sendMessage(MessageManager.alreadyRepaired);
                return;
            }

            clickedItem.setDurability((short) 0);
        }
        else
        {
            int uses = me.drakonn.zydoxweapons.util.Util.getUses(clickedItem);
            if(uses == weapon.getUses())
            {
                player.sendMessage(MessageManager.alreadyRepaired);
                return;
            }

            uses = uses + weapon.repairTokensDo();
            if(uses > weapon.getUses())
                uses = weapon.uses;

            event.getClickedInventory().setItem(event.getSlot(), weapon.setNbtData(uses, me.drakonn.zydoxweapons.util.Util.getAmmo(clickedItem)));

        }
        player.setItemOnCursor(null);

        event.setCancelled(true);
        if (cursorItem.getAmount() != 1) {
            cursorItem.setAmount(cursorItem.getAmount() - 1);
            Util.givePlayerItem(player, cursorItem);
        }
        player.updateInventory();

        player.sendMessage(MessageManager.tokenUsed);
    }
}
