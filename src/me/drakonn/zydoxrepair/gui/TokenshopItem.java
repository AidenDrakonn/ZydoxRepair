package me.drakonn.zydoxrepair.gui;

import org.bukkit.inventory.ItemStack;

public class TokenshopItem {

    private int invSlot;
    private ItemStack item;
    private int cost;
    private int amount;


    public TokenshopItem(ItemStack item, int invSlot, int cost, int amount) {
        this.item = item;
        this.invSlot = invSlot;
        this.cost = cost;
        this.amount = amount;
    }


    public int getInvSlot() { return invSlot; }

    public ItemStack getItem() { return item.clone(); }

    public int getCost() { return cost; }

    public int getAmount() { return amount; }
}
