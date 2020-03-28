package com.SketchyPlugins.CraftableEnchants.Libraries;

import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.ItemStack;
import com.SketchyPlugins.CraftableEnchants.Main;

public class ItemEnchanter implements Listener {
	final Material toEnchant;
	final Enchantment enchantment;
	final int level;
	public ItemEnchanter(final Material _toEnchant, final Enchantment _enchantment, final int _level) {
		toEnchant = _toEnchant;
		enchantment = _enchantment;
		level = _level;
		
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.instance);
	}
	ItemStack enchant(ItemStack i) {
		if(enchantment instanceof CustomEnchantment) {
			CustomEnchantment ench = (CustomEnchantment) enchantment;
			return ench.enchant(i, level, true);
		}
		else {
			try {
				i.addEnchantment(enchantment, level);
				return i;
			}
			catch(Exception ignored) {
				i.addUnsafeEnchantment(enchantment, level);
				return i;
			}
		}
	}
	
	@EventHandler
	public void onCraft(PrepareItemCraftEvent e) {
		ItemStack i = e.getInventory().getResult();
		if(i == null)
			return;
		
		if(i.getType() != toEnchant)
			return;
		
		i = enchant(i);
		e.getInventory().setResult(i);
	}
	@EventHandler
	public void onSmelt(FurnaceSmeltEvent e) {
		if(!(e.getBlock() instanceof Furnace))
			return;
		
		Furnace block = (Furnace) e.getBlock();
		ItemStack i = e.getResult();
		if(i == null)
			return;
		
		if(i.getType() != toEnchant)
			return;
		
		i = enchant(i);
		block.getInventory().setResult(i);
	}
	@EventHandler
	public void onInventoryOpen (InventoryOpenEvent e) {
		if(e.getInventory() == null)
			return;
		
		ListIterator<ItemStack> iter = e.getInventory().iterator();
		while(iter.hasNext()) {
			ItemStack i = iter.next();
			if(i != null)
				if(i.getType() == toEnchant) {
					enchant(i);
				}
		}
	}
	@EventHandler
	public void onItemDropped (ItemSpawnEvent e) {
		ItemStack i = e.getEntity().getItemStack();
		if(i == null)
			return;
		if(i.getType() == toEnchant)
			e.getEntity().setItemStack(enchant(i));
	}
	@EventHandler
	public void onPlayerLog(PlayerJoinEvent e) {
		ItemStack[] items = e.getPlayer().getInventory().getContents();
		for(ItemStack i : items) {
			if(i != null)
				if(i.getType() == toEnchant) {
					enchant(i);
				}
		}
	}
	@EventHandler
	public void onEnable(PluginEnableEvent e) {
		for(Player plr : Bukkit.getServer().getOnlinePlayers()) {
			ItemStack[] items = plr.getInventory().getContents();
			for(ItemStack i : items) {
				if(i != null)
					if(i.getType() == toEnchant) {
						enchant(i);
					}
			}
		}
	}
}
