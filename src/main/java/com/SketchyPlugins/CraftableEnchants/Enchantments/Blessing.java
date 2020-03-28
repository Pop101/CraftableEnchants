package com.SketchyPlugins.CraftableEnchants.Enchantments;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.SketchyPlugins.CraftableEnchants.Main;
import com.SketchyPlugins.CraftableEnchants.Libraries.CustomEnchantment;
import com.SketchyPlugins.CraftableEnchants.Libraries.ItemCategories;

public class Blessing extends CustomEnchantment{

	public Blessing() {
		super("Minor Blessing", 50);
		for(Material m : ItemCategories.TOOLS)
			canEnchant.add(m);
		for(Material m : ItemCategories.WEAPONS)
			canEnchant.add(m);
	}
	private void playEffect(Location loc) {
		loc.getWorld().playSound(loc, Sound.BLOCK_BEACON_POWER_SELECT, 1.0f, 1.0f);
		loc.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, loc.clone().add(0,0.5,0), 15, 1.0, 2.0, 1.0);
	}
	private void enchantFlash(ItemStack stack) {		
		//get list of all possible enchantments
		LinkedList<Enchantment> possibleEnchs = new LinkedList<Enchantment>();
		for(Enchantment ench : Enchantment.values()) possibleEnchs.add(ench);
		
		//remove all enchantments the item has
		for(Enchantment ench : stack.getEnchantments().keySet()) possibleEnchs.remove(ench);
		
		//pick a random one
		Enchantment toFlash = possibleEnchs.get((int) (Math.random()*possibleEnchs.size()));
		
		//pick a random level
		int level = (int) (Math.random()* (toFlash.getMaxLevel()-toFlash.getStartLevel()))+toFlash.getStartLevel();
		
		//add it
		if(toFlash instanceof CustomEnchantment) {
			((CustomEnchantment) toFlash).enchant(stack, level);
		}
		else {
			stack.addEnchantment(toFlash, level);
		}
		
		//queue removal
		remEnch(stack, toFlash);
	}
	private void remEnch(final ItemStack stack, final Enchantment ench) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, (Runnable)new Runnable() {
			public void run() {
				if(ench instanceof CustomEnchantment) {
					((CustomEnchantment) ench).disenchant(stack);
				}
				else {
					stack.removeEnchantment(ench);
				}
			}
		 },3);
	}
	@Override
	public void onBlockMined(Player plr, ItemStack item, Block block) {
		if(plr.getGameMode() == GameMode.CREATIVE)
			return;
		if(this.getLevelFromLore(item)*0.01 > Math.random())
			return;
		enchantFlash(item);
		playEffect(plr.getLocation());
	}

	@Override
	public void onPlayerInteract(Player plr, ItemStack item, Block block, BlockFace face) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDealDamage(Entity source, ItemStack itemSource, Entity target, double damage) {
		if(source instanceof Player)
			if(((Player) source).getGameMode() == GameMode.CREATIVE)
				return;
		if(this.getLevelFromLore(itemSource)*0.01 > Math.random())
			return;
		enchantFlash(itemSource);
		playEffect(source.getLocation());
	}

	@Override
	public double onTakeDamage(Entity target, ItemStack specificItem, double amount) {
		return amount;
	}

	@Override
	public int onGainExp(Player plr, ItemStack item, int amountLeft, int originalAmount) {
		// TODO Auto-generated method stub
		return originalAmount;
	}

	@Override
	public Entity onShootBow(LivingEntity ent, ItemStack item, Entity projectile) {
		if(ent instanceof Player)
			if(((Player) ent).getGameMode() == GameMode.CREATIVE)
				return projectile;
		if(this.getLevelFromLore(item)*0.01 > Math.random())
			return projectile;
		enchantFlash(item);
		playEffect(ent.getLocation());
		return projectile;
	}

}
