package com.SketchyPlugins.CraftableEnchants.Enchantments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.SketchyPlugins.CraftableEnchants.Libraries.CustomEnchantment;
import com.SketchyPlugins.CraftableEnchants.Libraries.ItemCategories;

public class Infusion extends CustomEnchantment{

	public Infusion() {
		super("Infusion", 1);
		//can enchant everything normal
		for(Material m : ItemCategories.TOOLS)
			canEnchant.add(m);
		for(Material m : ItemCategories.WEAPONS)
			canEnchant.add(m);
		for(Material m : ItemCategories.ARMOR)
			canEnchant.add(m);
	}

	@Override
	public void onBlockMined(Player plr, ItemStack item, Block block) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDealDamage(Entity source, ItemStack itemSource, Entity target, double damage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTakeDamage(Entity target,ItemStack specificItem, double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int onGainExp(Player plr, ItemStack item, int amountLeft, int originalAmount) {
		if(Math.random()/amountLeft > 0.02/100)
			return amountLeft;
		
		boolean appliedNextEnchantment = false;
		
		Set<Enchantment> has = item.getEnchantments().keySet();
		List<Enchantment> enchs = new ArrayList<Enchantment>();
		Collections.addAll(enchs, Enchantment.values());
		Collections.shuffle(enchs);
		
		for(Enchantment canGet : enchs) {
			appliedNextEnchantment = tryEnchantItem(item, canGet);
			if(appliedNextEnchantment) {
				break;
			}
		}
		if(appliedNextEnchantment)
			return -30;
		
		return amountLeft;
	}
	boolean tryEnchantItem(ItemStack i, Enchantment ench) {
		if(ench instanceof CustomEnchantment) {
			CustomEnchantment cEnch = (CustomEnchantment) ench;
			int level = cEnch.getLevelFromLore(i);
			if(!canEnchant(i, ench) || !cEnch.canCraftWith)
				return false;
			cEnch.enchant(i,level+1);
			return true;
		}
		else {
			int level = i.getEnchantmentLevel(ench);
			if(canEnchant(i, ench) && level+1 <= ench.getMaxLevel()) {
				try {
					i.addEnchantment(ench, level+1);
					return true;
				}
				catch(Exception ignored) {}
			}
		}
		return false;
	}
	boolean canEnchant(ItemStack i, Enchantment ench) {
		if(ench instanceof CustomEnchantment)
			if(!((CustomEnchantment) ench).canEnchantItem(i)) {
				return false;
			}
		else if(!ench.canEnchantItem(i))
			return false;
		for(Enchantment toCheck : i.getEnchantments().keySet())
			if(ench.conflictsWith(toCheck))
				return false;
		return true;
	}
	@Override
	public Entity onShootBow(LivingEntity ent, ItemStack item, Entity projectile) {
		// TODO Auto-generated method stub
		return projectile;
	}

	@Override
	public void onPlayerInteract(Player plr, ItemStack item, Block block, BlockFace face) {
		// TODO Auto-generated method stub
		
	}

	
}
