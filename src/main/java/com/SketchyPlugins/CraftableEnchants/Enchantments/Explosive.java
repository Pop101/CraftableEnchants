package com.SketchyPlugins.CraftableEnchants.Enchantments;

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

import com.SketchyPlugins.CraftableEnchants.Libraries.CustomEnchantment;
import com.SketchyPlugins.CraftableEnchants.Libraries.ItemCategories;

public class Explosive extends CustomEnchantment{

	public Explosive() {
		super("Explosive", 2);
		for(Material m : ItemCategories.TOOLS)
			canEnchant.add(m);
		for(Material m : ItemCategories.HOES)
			canEnchant.remove(m);
		for(Material m : ItemCategories.WEAPONS)
			canEnchant.add(m);
		conflictsWith.add(Enchantment.SILK_TOUCH);
		conflictsWith.add(Enchantment.DIG_SPEED);
	}
	void explode(Location center, float radius, ItemStack tool) {
		center.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, center, 1);
		center.getWorld().playSound(center, Sound.ENTITY_GENERIC_EXPLODE, 0.2f, 1.0f);
		for(float x = -radius; x <= radius; x++)
			for(float y = -radius; y <= radius; y++)
				for(float z = -radius; z <= radius; z++)
					if(x*x+y*y+z*z <= radius*radius)
						center.clone().add(x,y,z).getBlock().breakNaturally(tool);
		for(Entity ent : center.getWorld().getNearbyEntities(center, radius,radius,radius)) {
			if((ent instanceof org.bukkit.entity.Damageable) && !(ent instanceof Player)) {
				org.bukkit.entity.Damageable toDamage = (org.bukkit.entity.Damageable) ent;
				double distance = radius-center.distance(ent.getLocation());
				if(distance > 0)
					toDamage.damage(3*distance+radius);
			}
		}
	}
	@Override
	public void onBlockMined(Player plr, ItemStack item, Block block) {
		if(!ItemCategories.contains(item.getType(), ItemCategories.TOOLS))
			return;
		explode(block.getLocation().add(0.5,0.5,0.5),getLevelFromLore(item),item);
		if(plr.getGameMode() != GameMode.CREATIVE && plr.getGameMode() != GameMode.ADVENTURE)
			if(item.getData() instanceof org.bukkit.inventory.meta.Damageable) {
				org.bukkit.inventory.meta.Damageable d = (org.bukkit.inventory.meta.Damageable) item.getData();
				d.setDamage(d.getDamage()+(int)(Math.random()*6+2));
			}
	}

	@Override
	public void onPlayerInteract(Player plr, ItemStack item, Block block, BlockFace face) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDealDamage(Entity source, ItemStack itemSource, Entity target, double damage) {
		if(!ItemCategories.contains(itemSource.getType(), ItemCategories.WEAPONS))
			return;
		if(!(target instanceof org.bukkit.entity.Damageable))
			return;
		final org.bukkit.entity.Damageable e = (org.bukkit.entity.Damageable) target;
		if(e.getHealth() <= damage)
			explode(target.getLocation().add(0,target.getHeight(),0),getLevelFromLore(itemSource),itemSource);
		if(source instanceof Player) {
			Player plr = (Player) source;
			if(plr.getGameMode() != GameMode.CREATIVE && plr.getGameMode() != GameMode.ADVENTURE)
				if(itemSource.getData() instanceof org.bukkit.inventory.meta.Damageable) {
					org.bukkit.inventory.meta.Damageable d = (org.bukkit.inventory.meta.Damageable) itemSource.getData();
					d.setDamage(d.getDamage()+(int)(Math.random()*6+2));
				}
		}
		else
			if(itemSource.getData() instanceof org.bukkit.inventory.meta.Damageable) {
				org.bukkit.inventory.meta.Damageable d = (org.bukkit.inventory.meta.Damageable) itemSource.getData();
				d.setDamage(d.getDamage()+(int)(Math.random()*6+2));
			}
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
