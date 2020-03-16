package com.SketchyPlugins.CraftableEnchants.Enchantments;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.SketchyPlugins.CraftableEnchants.Main;
import com.SketchyPlugins.CraftableEnchants.Libraries.CustomEnchantment;
import com.SketchyPlugins.CraftableEnchants.Libraries.ItemCategories;

public class Telekinesis extends CustomEnchantment {

	public Telekinesis() {
		super("Telekinesis", 1);
		//can enchant everything normal
		for(Material m : ItemCategories.TOOLS)
			canEnchant.add(m);
		for(Material m : ItemCategories.WEAPONS)
			canEnchant.add(m);
	}
	void addToPlayer(Player plr, ItemStack... items) {
		HashMap<Integer, ItemStack> toDrop =  plr.getInventory().addItem(items);
		
		for(Integer i : toDrop.keySet()) {
			plr.getWorld().dropItem(plr.getLocation(), toDrop.getOrDefault(i, null));
		}
		
	}
	@Override
	public void onBlockMined(Player plr, ItemStack item, Block block) {
		if(plr.getGameMode() == GameMode.CREATIVE)
			return;
		
		ItemStack[] items = block.getDrops(item).toArray(new ItemStack[0]);
		addToPlayer(plr, items);
		
		block.setType(Material.AIR);
	}

	@Override
	public void onPlayerInteract(Player plr, ItemStack item, Block block, BlockFace face) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDealDamage(Entity source, ItemStack itemSource, Entity target, double damage) {
		if(!(target instanceof Damageable))
			return;
		if(!(source instanceof Player))
			return;
		final Player plr = (Player) source;
		final Damageable e = (Damageable) target;
		if(e.getHealth() <= damage)
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, (Runnable)new Runnable() {
				 public void run() {
					 for(Entity near : e.getNearbyEntities(1, 1, 1)) {
							if(near instanceof Item) {
								addToPlayer(plr, ((Item) near).getItemStack());
								near.remove();
							}
					 }
				 }
			 },3);
	}
	@Override
	public void onTakeDamage(Entity target, ItemStack specificItem, double amount) {
		// TODO Auto-generated method stub
		
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
