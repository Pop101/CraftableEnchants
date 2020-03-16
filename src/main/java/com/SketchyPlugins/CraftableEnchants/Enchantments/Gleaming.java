package com.SketchyPlugins.CraftableEnchants.Enchantments;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.SketchyPlugins.CraftableEnchants.Main;
import com.SketchyPlugins.CraftableEnchants.Libraries.CustomEnchantment;

public class Gleaming extends CustomEnchantment {

	public Gleaming() {
		super("Gleaming", 1);
		super.canCraftWith = false;
		//conflicts with everything
		for(Enchantment ench : Enchantment.values())
			conflictsWith.add(ench);
	}

	@EventHandler
	public void inventoryCheck(InventoryMoveItemEvent e) {
		updateInventory(e.getSource());
		updateInventory(e.getDestination());
	}
	@EventHandler
	public void inventoryInteract(InventoryInteractEvent e) {
		updateInventory(e.getInventory());
	}
	@EventHandler
	public void inventoryClick(InventoryClickEvent e) {
		if(e.getSlot() == -1)
			return;
		doubleUpdateInventory(e.getInventory());
		if(!e.getInventory().equals(e.getClickedInventory()))
			doubleUpdateInventory(e.getClickedInventory());
		
		doubleUpdateInventory(e.getWhoClicked().getInventory());
	}
	@EventHandler
	public void inventoryDrag(InventoryDragEvent e) {
		if(getLevelFromLore(e.getOldCursor()) <= 0)
			return;
		
		final int maxSlots = e.getInventory().getSize();
		for(int slot : e.getRawSlots()) {
			if(slot > maxSlots) //if outside inventory
				e.setCancelled(true);
		}
	}
	@EventHandler
	public void pickupItem(InventoryPickupItemEvent e) {
		if(e.getItem() == null)
			return;
		ItemStack i = e.getItem().getItemStack();
		if(i == null)
			return;
		updateInventory(e.getInventory());		
	}
	@EventHandler
	public void pickupItem(EntityPickupItemEvent e) {
		if(e.getItem() == null)
			return;
		ItemStack i = e.getItem().getItemStack();
		if(i == null)
			return;
		if(e.getEntity() instanceof InventoryHolder)
			updateInventory(((InventoryHolder)e.getEntity()).getInventory());		
	}
	@EventHandler
	public void dropItem(EntitySpawnEvent e) {
		if(!(e.getEntity() instanceof Item))
			return;
		Item i = (Item) e.getEntity();
		//update all nearby inventory holders that could have dropped this
		for(Entity ent : i.getNearbyEntities(1, 2, 1)) {
			if(ent instanceof InventoryHolder) {
				updateInventory(((InventoryHolder) ent).getInventory());
			}
		}
		//if this item has gleaming, clear all enchants off it
		if(!i.getItemStack().containsEnchantment(this))
			return;
		ItemStack toChange = i.getItemStack();
		clearEnchants(toChange);
		i.setItemStack(toChange);
	}
	void updateInventory(final Inventory i) {
		updateInventory(i, 1);
	}
	void doubleUpdateInventory(final Inventory i) {
		updateInventory(i, 1);
		updateInventory(i, 5);
	}
	void updateInventory(final Inventory i, int delay) {
		if(i == null)
			return;
		if(i instanceof AnvilInventory)
			return;
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, (Runnable)new Runnable() {
            public void run() {
            	for(ItemStack it : i.getContents()) {
            		if(it != null) {
	        			if(getLevelFromLore(it) > 0) {
	        				clearEnchants(it);
	        				applyEnchantsFromInventory(it,i);
	        			}
            		}
        		}
            }
        },delay);
	}
	void clearEnchants(ItemStack i) {
		Set<Enchantment> toRemove = i.getEnchantments().keySet();
		for(Enchantment ench : toRemove) {
			if((ench instanceof CustomEnchantment) && !(ench instanceof Gleaming))
				((CustomEnchantment) ench).disenchant(i);
			else if(!(ench instanceof Gleaming))
				i.removeEnchantment(ench);
		}
		//re-apply this, which was (probably) removed
		//enchant(i);
	}
	void applyEnchantsFromInventory(ItemStack og, Inventory inv) {
		HashMap<Enchantment, Integer> toAdd = new HashMap<Enchantment, Integer>();
		for(ItemStack i : inv.getContents()) {
			if(i != null) {
				for(Enchantment ench : i.getEnchantments().keySet()) {
					if((ench instanceof CustomEnchantment) && !(ench instanceof Gleaming)) {
						CustomEnchantment cEnch = (CustomEnchantment) ench;
						if(cEnch.canCraftWith) {
							int level = cEnch.getLevelFromLore(i);
							if(level > cEnch.getLevelFromLore(og))
								cEnch.enchant(og,level,true);
						}
					}
					else if(i.getEnchantmentLevel(ench) > toAdd.getOrDefault(ench, 0)) {
						toAdd.put(ench, i.getEnchantmentLevel(ench));
					}
				}
			}
		}
		for(Enchantment ench : toAdd.keySet()) {
			if(ench.canEnchantItem(og))
				og.addUnsafeEnchantment(ench, toAdd.get(ench));
		}
		enchant(og);
	}
	@Override
	public void onBlockMined(Player plr, ItemStack item, Block block) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlayerInteract(Player plr, ItemStack item, Block block, BlockFace face) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDealDamage(Entity source, ItemStack itemSource, Entity target, double damage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTakeDamage(Entity target, ItemStack specificItem, double amount) {
		// TODO Auto-generated method stub

	}

	@Override
	public int onGainExp(Player plr, ItemStack item, int amountLeft, int originalAmount) {
		// TODO Auto-generated method stub
		return (int) (amountLeft*1.5);
	}

	@Override
	public Entity onShootBow(LivingEntity ent, ItemStack item, Entity projectile) {
		// TODO Auto-generated method stub
		return projectile;
	}

}
