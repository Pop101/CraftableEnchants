package com.SketchyPlugins.CraftableEnchants.Enchantments;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.SketchyPlugins.CraftableEnchants.Libraries.CustomEnchantment;

public class Villainy extends CustomEnchantment {

	public Villainy() {
		super("Villainy",1);
	}
	void makeSound(Location loc) {
		if(Math.random() < 0.3f)
			loc.getWorld().playSound(loc, Sound.ENTITY_VILLAGER_AMBIENT, (float) (0.8*Math.random()+0.7f), (float) (0.75+0.5*Math.random()));
	}
	@Override
	public void onBlockMined(Player plr, ItemStack item, Block block) {
		makeSound(plr.getLocation());
	}

	@Override
	public void onPlayerInteract(Player plr, ItemStack item, Block block, BlockFace face) {
		makeSound(plr.getLocation());
	}

	@Override
	public void onDealDamage(Entity source, ItemStack itemSource, Entity target, double damage) {
		makeSound(source.getLocation());
	}

	@Override
	public void onTakeDamage(Entity target, ItemStack specificItem, double amount) {
		makeSound(target.getLocation());
	}

	@Override
	public int onGainExp(Player plr, ItemStack item, int amountLeft, int originalAmount) {
		makeSound(plr.getLocation());
		return amountLeft;
	}

	@Override
	public Entity onShootBow(LivingEntity ent, ItemStack item, Entity projectile) {
		makeSound(ent.getLocation());
		return projectile;
	}

}
