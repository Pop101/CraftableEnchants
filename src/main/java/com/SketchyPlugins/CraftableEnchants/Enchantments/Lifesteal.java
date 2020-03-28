package com.SketchyPlugins.CraftableEnchants.Enchantments;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.SketchyPlugins.CraftableEnchants.Libraries.CustomEnchantment;
import com.SketchyPlugins.CraftableEnchants.Libraries.ItemCategories;

public class Lifesteal extends CustomEnchantment {

	public Lifesteal() {
		super("Leeching", 3);
		for(Material m : ItemCategories.WEAPONS)
			canEnchant.add(m);
		//conflictsWith.add(Explosive);
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
		if(!(source instanceof Damageable))
			return;
		if(target instanceof Player)
			return;
		Damageable d = (Damageable) source;
		
		int level = getLevelFromLore(itemSource);
		for(int i = 0; i < level; i++) {
			if(d.getHealth() >= d.getMaxHealth()) {
				if(source instanceof Player) {
					Player plr = (Player) source;
					plr.setFoodLevel(plr.getFoodLevel()+1);
				}
			}
			else {
				if(d.getHealth()+1 > d.getMaxHealth())
					d.setHealth(d.getMaxHealth());
				else d.setHealth(d.getHealth()+1);
			}
		}
	}

	@Override
	@Override
	public double onTakeDamage(Entity target, ItemStack specificItem, double amount) {
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
