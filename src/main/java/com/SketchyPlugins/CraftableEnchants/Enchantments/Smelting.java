package com.SketchyPlugins.CraftableEnchants.Enchantments;

import java.util.Iterator;

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
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import com.SketchyPlugins.CraftableEnchants.Main;
import com.SketchyPlugins.CraftableEnchants.Libraries.CustomEnchantment;
import com.SketchyPlugins.CraftableEnchants.Libraries.ItemCategories;

public class Smelting extends CustomEnchantment {

	public Smelting() {
		super("Smelting",1);
		//can enchant everything normal
		for(Material m : ItemCategories.TOOLS)
			canEnchant.add(m);
		for(Material m : ItemCategories.WEAPONS)
			canEnchant.add(m);
	}
	ItemStack smeltStack(ItemStack i) {
		Iterator<Recipe> recIter = Bukkit.recipeIterator();
		while(recIter.hasNext()) {
			Recipe rec = recIter.next();
			//check if it's a furnace recipe
			if(rec instanceof FurnaceRecipe) {
				FurnaceRecipe frec = (FurnaceRecipe) rec;
				//if it's input is similar to the given item stack
				if(frec.getInput().isSimilar(i)) {
					ItemStack toReturn = frec.getResult();
					toReturn.setAmount(i.getAmount());
					return toReturn;
				}
			}
		}
		return i;
	}
	@Override
	public void onBlockMined(Player plr, ItemStack item, Block block) {
		if(plr.getGameMode() == GameMode.CREATIVE)
			return;
		
		ItemStack[] items = block.getDrops(item).toArray(new ItemStack[0]);
		for(ItemStack is : items)
			block.getWorld().dropItemNaturally(block.getLocation().add(0.5,0,0.5),smeltStack(is));
		
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
		final Damageable e = (Damageable) target;
		if(e.getHealth() <= damage)
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, (Runnable)new Runnable() {
				 public void run() {
					 for(Entity near : e.getNearbyEntities(1, 1, 1)) {
							if(near instanceof Item)
								((Item) near).setItemStack(smeltStack(((Item) near).getItemStack()));
					 }
				 }
			 },2);
	}

	@Override
	public void onTakeDamage(Entity target, ItemStack specificItem, double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int onGainExp(Player plr, ItemStack item, int amountLeft, int originalAmount) {
		// TODO Auto-generated method stub
		return originalAmount;
	}

	@Override
	public Entity onShootBow(LivingEntity ent, ItemStack item, Entity projectile) {
		// TODO Auto-generated method stub
		return projectile;
	}

}
