package com.SketchyPlugins.CraftableEnchants.Enchantments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.SketchyPlugins.CraftableEnchants.Libraries.CustomEnchantment;
import com.SketchyPlugins.CraftableEnchants.Libraries.ItemCategories;

public class Persistence extends CustomEnchantment {

	HashMap<Player, List<ItemStack>> respawn;
	public Persistence() {
		super("Persistence", 1);
		respawn = new HashMap<Player, List<ItemStack>>();
		
		//can enchant everything normal
		for(Material m : ItemCategories.TOOLS)
			canEnchant.add(m);
		for(Material m : ItemCategories.WEAPONS)
			canEnchant.add(m);
	}
	@EventHandler
	public void onPlayerDeath(EntityDeathEvent e) {
		if(!(e.getEntity() instanceof Player))
			return;
		PlayerInventory inv = ((Player)e.getEntity()).getInventory();
		for(ItemStack i : inv.getContents()) {
			if(getLevelFromLore(i) > 0) {
				List<ItemStack> items = respawn.getOrDefault(((Player)e.getEntity()), new ArrayList<ItemStack>());
				items.add(i);
				respawn.put((Player)e.getEntity(), items);
			}
		}
		if(respawn.get((Player)e.getEntity()) != null)
			for(ItemStack toRemove : respawn.get((Player)e.getEntity()))
				inv.remove(toRemove);
	}
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player plr = e.getPlayer();
		if(plr == null)
			return;
		
		ItemStack[] items = respawn.getOrDefault(plr, new ArrayList<ItemStack>()).toArray(new ItemStack[0]);
		for(ItemStack i : items) {
			disenchant(i);
		}
		plr.getInventory().addItem(items);
		respawn.remove(plr);
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
