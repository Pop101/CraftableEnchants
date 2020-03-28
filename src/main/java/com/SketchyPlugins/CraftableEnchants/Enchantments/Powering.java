package com.SketchyPlugins.CraftableEnchants.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.block.data.type.RedstoneRail;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import com.SketchyPlugins.CraftableEnchants.Main;
import com.SketchyPlugins.CraftableEnchants.Libraries.CustomEnchantment;
import com.SketchyPlugins.CraftableEnchants.Libraries.ItemCategories;

public class Powering extends CustomEnchantment{

	public Powering() {
		super("Actuation", 1);
		conflictsWith.add(Enchantment.SILK_TOUCH);
		//can enchant everything normal
		for(Material m : ItemCategories.TOOLS)
			canEnchant.add(m);
		for(Material m : ItemCategories.WEAPONS)
			canEnchant.add(m);
	}
	@Override
	public void onBlockMined(Player plr, ItemStack item, Block block) {		
		powerBlock(block);
		final Block fBlock = block;
		for(int i = 0; i < 3; i++)
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, (Runnable)new Runnable() {
	            public void run() {
	            	DustOptions d = new DustOptions(Color.RED,1.5f);
	            	fBlock.getWorld().spawnParticle(Particle.REDSTONE, fBlock.getLocation().add(0.5, 0.5, 0.5), 2, 0.25, 0.25, 0.25,0,d);
	            }
	        },(long) (Math.random()*10));
	}

	public void onPlayerInteract(ItemStack item, Block block, BlockFace face) {
		Bukkit.getLogger().info("Powering block: "+block.getBlockData());
		powerBlock(block);
		powerBlock(block.getRelative(face));
		powerBlock(block.getRelative(face.getOppositeFace()));
	}
	void powerBlock(final Block b) {
		final BlockData bd = b.getBlockData();
		if(bd instanceof Dispenser) {
			Dispenser disp = (Dispenser) bd;
			disp.setTriggered(false);
			b.setBlockData(disp,false);
		}
		else if(bd instanceof RedstoneRail) {
			RedstoneRail rail = (RedstoneRail) bd;
			rail.setPowered(true);
			b.setBlockData(rail, false);
			
			queueUnpower(b.getLocation());
		}
		else if (bd instanceof AnaloguePowerable) {
			AnaloguePowerable powerable = (AnaloguePowerable) bd;
            powerable.setPower(15);
            b.setBlockData(powerable, false);
            for(int x = -1; x <= 1; x++)
            	for(int z = -1; z <= 1; z++)
            		if(x != z && z != 0)
            			b.getLocation().add(x, 0, z).getBlock().getState().update();
            
            queueUnpower(b.getLocation());
        }
		else if (bd instanceof Powerable) {
			Powerable powerable = (Powerable) bd;
            powerable.setPowered(true);
            b.setBlockData(powerable, false);
            
            queueUnpower(b.getLocation());
        }
	}
	void queueUnpower(final Location loc) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, (Runnable)new Runnable() {
            public void run() {
            	if(loc.getBlock().getBlockData() instanceof RedstoneRail) {
            		final RedstoneRail rail = (RedstoneRail) loc.getBlock().getBlockData();
	            	rail.setPowered(false);
	            	loc.getBlock().setBlockData(rail);
            	}
            	else if (loc.getBlock().getBlockData() instanceof Powerable) {
            		final Powerable powerable = (Powerable) loc.getBlock().getBlockData();
	            	powerable.setPowered(false);
	            	loc.getBlock().setBlockData(powerable);
            	}
            	else if (loc.getBlock().getBlockData() instanceof AnaloguePowerable) {
            		final AnaloguePowerable powerable = (AnaloguePowerable) loc.getBlock().getBlockData();
	            	powerable.setPower(0);
	            	loc.getBlock().setBlockData(powerable);
            	}
            }
        },(long) (Math.random()*30)+20);
	}
	@Override
	public void onDealDamage(Entity source, ItemStack itemSource, Entity target, double damage) {
		// TODO Auto-generated method stub
		
	}

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
	@Override
	public void onPlayerInteract(Player plr, ItemStack item, Block block, BlockFace face) {
		// TODO Auto-generated method stub
		
	}

}
