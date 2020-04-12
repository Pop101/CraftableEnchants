package com.SketchyPlugins.CraftableEnchants;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.SketchyPlugins.CraftableEnchants.Libraries.CustomEnchantment;
import com.SketchyPlugins.CraftableEnchants.Libraries.ItemCategories;

public class EnchantmentCrafting implements Listener{
	JavaPlugin plugin;
	public EnchantmentCrafting(JavaPlugin main) {
		plugin = main;
		main.getServer().getPluginManager().registerEvents(this, main);
	}
	@EventHandler
	public void onAnvil(PrepareAnvilEvent e) {
		if(!(e.getInventory() instanceof AnvilInventory))
			return;
		AnvilInventory inv = (AnvilInventory) e.getInventory();
		
		ItemStack first = inv.getItem(0);
		ItemStack second = inv.getItem(1);
		//Bukkit.getLogger().info("In anvil: "+first+", "+second);
		if(first == null || second == null)
			return;
		if(second.getType() == Material.ENCHANTED_BOOK)
			return;
		//no gleaming
		if(ItemCategories.contains(first.getType(), ItemCategories.GLEAMING) || ItemCategories.contains(second.getType(), ItemCategories.GLEAMING))
			return;
		
		ItemStack result = isAnvilRecipe(first,second);
		//Bukkit.getLogger().info("Setting output, "+!result.equals(first)+", to:"+result);
		if(result.isSimilar(first))
			return;
		
		e.setResult(result);
		inv.setItem(2, result);
	}
	@EventHandler
	public void onAnvilClick(InventoryClickEvent e) {
		if(e.getSlot() == -1)
			return;
		if(!(e.getClickedInventory() instanceof AnvilInventory))
			return;
		//Bukkit.getLogger().info("clicked anvil slot" + e.getSlot());
		if(e.getSlot() != 2)
			return;
		//Bukkit.getLogger().info("Cursor "+e.getCursor());
		if(e.getCursor().getType() != Material.AIR)
			return;
		
		ItemStack first = e.getClickedInventory().getItem(0);
		ItemStack second = e.getClickedInventory().getItem(1);
		//Bukkit.getLogger().info("In anvil: "+first+", "+second);
		if(first == null || second == null)
			return;
		if(second.getType() == Material.ENCHANTED_BOOK)
			return;
		ItemStack result = isAnvilRecipe(first, second);
		//Bukkit.getLogger().info("Anvil result: "+result);
		if(result.equals(first))
			return;
		e.getClickedInventory().setItem(0, removeItems(first,1));
		e.getClickedInventory().setItem(1, removeItems(second,1));
		e.getClickedInventory().setItem(2, null);
		//Bukkit.getLogger().info("Player inv?: "+e.getWhoClicked().getInventory());
		e.setCursor(result);
		
		e.getWhoClicked().getWorld().playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_ANVIL_USE, 1,1);
	}
	ItemStack removeItems(ItemStack i, int amount) {
		if(i.getAmount() <= amount)
			return null;
		i.setAmount(i.getAmount()-amount);
		return i;
	}
	ItemStack isAnvilRecipe(ItemStack first, ItemStack second) {
		ItemStack result = first.clone();
		
		for(Enchantment ench : second.getEnchantments().keySet()) {
			if(ench instanceof CustomEnchantment) {
				CustomEnchantment cEnch = (CustomEnchantment) ench;
				if(cEnch.canCraftWith) {
					int level = cEnch.getLevelFromLore(second);
					//Bukkit.getLogger().info("Enchantment of item 1:"+cEnch.getLevelFromLore(first)+", item 2:"+level);
					if(level != 0 && level == cEnch.getLevelFromLore(first)) {
						result = cEnch.disenchant(result);
						result = cEnch.enchant(result, level+1, true);
						//Bukkit.getLogger().info("Trying upgrade. Result: "+result);
					}
					else if(level > cEnch.getLevelFromLore(first)) {
						result = cEnch.disenchant(result);
						result = cEnch.enchant(result,level);
					}
				}
			}
			else if(result.getEnchantments().getOrDefault(ench, 0) < second.getEnchantments().get(ench)) {
				boolean canAdd = ench.canEnchantItem(result);
				for(Enchantment conflict : Enchantment.values())
					if(ench.conflictsWith(conflict) && first.containsEnchantment(conflict)) canAdd = false;
				
				if(canAdd)
					result.addEnchantment(ench, second.getEnchantments().get(ench));
			}
				
		}
		/*if(!result.equals(first) && result.getType() == Material.BOOK) {
			result.setType(Material.ENCHANTED_BOOK);
			ItemMeta im = result.getItemMeta();
			if(im.hasLocalizedName())
				im.setLocalizedName(ChatColor.YELLOW+im.getLocalizedName());
			result.setItemMeta(im);
		}*/
		result.setAmount(1);
		return result;
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onCraft(PrepareItemCraftEvent e) {
		if(e.getInventory().getResult() == null)
			return;
		if(e.getInventory().getResult() == null)
			return;
		
		ItemStack result = e.getInventory().getResult();
		HashMap<LevelledEnchant, Integer> enchs = new HashMap<LevelledEnchant, Integer>();
		for(int a = 1; a <= 9; a ++) //might be <, not <=
			if(e.getInventory().getItem(a) != null) {
				ItemStack st = e.getInventory().getItem(a);
				for(Enchantment ench : st.getEnchantments().keySet()) {
					if(!(ench instanceof CustomEnchantment) || (ench instanceof CustomEnchantment) && ((CustomEnchantment) ench).canCraftWith) { 
						LevelledEnchant lvl = new LevelledEnchant(ench, st.getEnchantments().get(ench));
						//Bukkit.getLogger().info("Contains ench?: "+enchs.containsKey(lvl));
						enchs.put(lvl, enchs.getOrDefault(lvl, 0)+1);
						//Bukkit.getLogger().info("found "+lvl.enchantment.getName()+" at lvl "+lvl.level +" found x"+enchs.get(lvl)+" map size: "+enchs.size());
					}
				}
			}
		
		for(LevelledEnchant lvl : enchs.keySet()) {
			int occurances = enchs.get(lvl);
			if(occurances >= 2)
				try {
					//Bukkit.getLogger().info("applying "+lvl.enchantment.getName()+" at lvl "+lvl.level+" with "+occurances+" occurances");
					if(lvl.enchantment instanceof CustomEnchantment)
						result = ((CustomEnchantment) lvl.enchantment).enchant(result, lvl.level);
					else if(lvl.enchantment.canEnchantItem(result) && result.getEnchantmentLevel(lvl.enchantment) <= lvl.level)
						result.addUnsafeEnchantment(lvl.enchantment, lvl.level);
				}
				catch(Exception ignored) {
					//if the enchantment is incompatible, just don't add it
				}
		}
		
		if(!result.equals(e.getInventory().getResult()))
			e.getInventory().setResult(result);
	}
}
class LevelledEnchant{
	public final Enchantment enchantment;
	public final int level;
	public LevelledEnchant(Enchantment _enchantment, int _level) {
		enchantment = _enchantment;
		level = _level;
	}
	@Override
	public boolean equals(Object in) {
		if(!(in instanceof LevelledEnchant))
			return false;
		LevelledEnchant in2 = (LevelledEnchant) in;
		if(in2.enchantment.getKey().getKey().equalsIgnoreCase(enchantment.getKey().getKey()))
			if(in2.level == level)
				return true;
		return false;
	}
	@Override
	public int hashCode() {
		return (enchantment.getKey().getKey()+""+level).hashCode();
	}
}