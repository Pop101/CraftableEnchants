package com.SketchyPlugins.CraftableEnchants.Enchantments;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.SketchyPlugins.CraftableEnchants.Libraries.CustomEnchantment;

public class Unknown extends CustomEnchantment {

	public Unknown() {
		super("???", 1);
		canCraftWith = false;
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
		return amountLeft;
	}

	@Override
	public Entity onShootBow(LivingEntity ent, ItemStack item, Entity projectile) {
		// TODO Auto-generated method stub
		return projectile;
	}


}
