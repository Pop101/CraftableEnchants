package com.SketchyPlugins.CraftableEnchants.Enchantments;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.SketchyPlugins.CraftableEnchants.Libraries.CustomEnchantment;
import com.SketchyPlugins.CraftableEnchants.Libraries.ItemCategories;

public class Warping extends CustomEnchantment {

	public Warping() {
		super("Warping", 1);
		//conflictsWith.add(Enchantment.PROTECTION_ENVIRONMENTAL);
		//conflictsWith.add(Enchantment.PROTECTION_EXPLOSIONS);
		//conflictsWith.add(Enchantment.PROTECTION_FIRE);
		//conflictsWith.add(Enchantment.PROTECTION_PROJECTILE);
		//can enchant everything normal
		for(Material m : ItemCategories.ARMOR)
			canEnchant.add(m);
		for(Material m : ItemCategories.WEAPONS)
			canEnchant.add(m);
	}
	void randomWarp(Entity e) {
		Location warp = e.getLocation().add(Math.random()*16-8, Math.random()*16-9, Math.random()*16-8);
		boolean safe = false;
		for(int i = 0; i < 16; i++) {
			warp.add(0, 1, 0);
			if(warp.getBlock().isPassable() || warp.getBlock().isEmpty()) {
				Location up = warp.clone().add(0,1,0);
				if(up.getBlock().isPassable() || up.getBlock().isEmpty()) {
					safe = true;
					break;
				}
			}
		}
		if(safe) {
			e.getWorld().playSound(e.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1.0f, 1.0f);
			
			DustOptions d = new DustOptions(Color.PURPLE,1.5f);
			e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0, e.getHeight()/2, 0), 20, 0.25, e.getHeight()/4, 0.25,0.8,d);
			
			e.teleport(warp);
		}
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
		if(!ItemCategories.contains(itemSource.getType(), ItemCategories.WEAPONS))
			return;
		
		if(Math.random() <= 0.10)
			randomWarp(target);
	}

	@Override
	public void onTakeDamage(Entity target, ItemStack specificItem, double amount) {
		//don't check armor only
		if(Math.random() <= 0.0625) //with 4 pieces of warping armor, 25% chance to warp
			randomWarp(target);
	}
	
	@Override
	public int onGainExp(Player plr, ItemStack item, int amountLeft, int originalAmount) {
		// TODO Auto-generated method stub
		return amountLeft;
	}

	@Override
	public Entity onShootBow(LivingEntity ent, ItemStack item, Entity projectile) {
		// TODO Auto-generated method stub
		return projectile;
	}

}
