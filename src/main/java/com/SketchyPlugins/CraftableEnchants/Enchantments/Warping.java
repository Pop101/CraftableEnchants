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
import com.SketchyPlugins.CraftableEnchants.Libraries.LocationUtils;

public class Warping extends CustomEnchantment {

	public Warping() {
		super("Warping", 1);
		conflictsWith.add(Enchantment.PROTECTION_ENVIRONMENTAL);
		conflictsWith.add(Enchantment.PROTECTION_EXPLOSIONS);
		conflictsWith.add(Enchantment.PROTECTION_FIRE);
		conflictsWith.add(Enchantment.PROTECTION_PROJECTILE);
		//can enchant everything normal
		for(Material m : ItemCategories.ARMOR)
			canEnchant.add(m);
		for(Material m : ItemCategories.WEAPONS)
			canEnchant.add(m);
	}
	void randomWarp(Entity e) {
		Location warp = LocationUtils.randomSafeLoc(e.getLocation(), 16);
		if(warp != null) {
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
	public double onTakeDamage(Entity target, ItemStack specificItem, double amount) {
		//armor only
		if(!ItemCategories.contains(specificItem.getType(), ItemCategories.ARMOR))
			return amount;
		if(Math.random() <= 0.0625) { //with 4 pieces of warping armor, 25% chance to warp
			randomWarp(target);
			return 0;
		}
		return amount;
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
