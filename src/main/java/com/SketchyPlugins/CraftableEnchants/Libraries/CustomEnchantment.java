package com.SketchyPlugins.CraftableEnchants.Libraries;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.SketchyPlugins.CraftableEnchants.Main;

public abstract class CustomEnchantment extends Enchantment implements Listener {

	
	public final String name;
	public final int maxLevel;
	public boolean canCraftWith = true;
	protected final List<Enchantment> conflictsWith;
	protected final List<Material> canEnchant;
	public CustomEnchantment(String _name, int _maxLevel) {
		super(getNamespacedKey(_name));
		name = _name;		
		maxLevel = _maxLevel;
		
		conflictsWith = new ArrayList<Enchantment>();
		canEnchant = new ArrayList<Material>();
		
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.instance);
		CustomEnchantment.RegisterEnchantement(this);
	}
	@EventHandler
	public void minedBlockListener(BlockBreakEvent event) {
		if(event.getPlayer() == null)
			return;
		if(event.getBlock() == null)
			return;
		ItemStack it = event.getPlayer().getInventory().getItemInMainHand();
		if(it == null)
			return;
		if(getLevelFromLore(it) > 0)
			onBlockMined(event.getPlayer(), it, event.getBlock());
	}
	public abstract void onBlockMined(Player plr, ItemStack item, Block block);
	
	@EventHandler
	public void interactListener(PlayerInteractEvent event) {
		if(event.getPlayer() == null)
			return;
		//if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
			ItemStack mh = event.getPlayer().getInventory().getItemInMainHand();
			if(mh != null)
				if(getLevelFromLore(mh) > 0)
					onPlayerInteract(event.getPlayer(),mh, event.getClickedBlock(), event.getBlockFace());
		//}
		//if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			ItemStack oh = event.getPlayer().getInventory().getItemInOffHand();
			if(oh != null)
				if(getLevelFromLore(oh) > 0)
					onPlayerInteract(event.getPlayer(),oh, event.getClickedBlock(), event.getBlockFace());
		//}
	}
	public abstract void onPlayerInteract(Player plr, ItemStack item, Block block, BlockFace face);
	
	
	@EventHandler
	public void attackListener(EntityDamageByEntityEvent event) {
		if(!(event.getDamager() instanceof LivingEntity))
			return;
		
		LivingEntity dmgr = (LivingEntity) event.getDamager();
		if(dmgr.getEquipment() == null)
			return;
		
		ItemStack inHand = dmgr.getEquipment().getItemInMainHand();
		if(inHand == null)
			return;
		if(getLevelFromLore(inHand) > 0)
			onDealDamage(event.getDamager(), inHand, event.getEntity(), event.getFinalDamage());
	}
	public abstract void onDealDamage(Entity source, ItemStack itemSource, Entity target, final double damage);
	
	@EventHandler
	public void recieveDamageListener(EntityDamageEvent event) {
		if(!(event.getEntity() instanceof LivingEntity))
			return;
		
		LivingEntity dmgr = (LivingEntity) event.getEntity();
		if(dmgr.getEquipment() == null)
			return;
		
		ItemStack[] armor = dmgr.getEquipment().getArmorContents();
		for(ItemStack i : armor)
			if(i != null)
				if(getLevelFromLore(i) > 0)
					event.setDamage(onTakeDamage(event.getEntity(), i, event.getDamage()));	
		if(event.getDamage() == 0) event.setCancelled(true);
	}
	public abstract double onTakeDamage(Entity target, ItemStack specificItem, double amount);

	@EventHandler
	public void expListener(PlayerExpChangeEvent event) {
		if(event.getPlayer() == null)
			return;
		Player plr = event.getPlayer();
		ItemStack[] inv = plr.getInventory().getContents();
		int exp = event.getAmount();
		for(ItemStack i : inv)
			if(i != null)
				if(getLevelFromLore(i) > 0)
					exp = onGainExp(plr, i, exp, event.getAmount());
		event.setAmount(exp);
	}
	public abstract int onGainExp(Player plr, ItemStack item, int amountLeft, int originalAmount);
	
	@EventHandler
	public void shootListener(EntityShootBowEvent event) {	
		if(event.getEntity() == null)
			return;
		
		ItemStack inHand = event.getBow();
		if(inHand == null)
			return;
		Entity proj = event.getProjectile();
		if(proj == null)
			return;
		if(getLevelFromLore(inHand) > 0)
			event.setProjectile(onShootBow(event.getEntity(), inHand,proj));
	}
	public abstract Entity onShootBow(LivingEntity ent, ItemStack item, Entity projectile);
	
	//consider adding health-changing event
	
	@Override
	public boolean canEnchantItem(ItemStack arg0) {
		for(Enchantment e : arg0.getEnchantments().keySet()) {
			if(conflictsWith(e)) {
				//Bukkit.getLogger().info("Can't enchant,"+getName()+" conflicts with "+e.getName());
				return false;
			}
		}
		//TODO: fix whatever's wrong here.
		if(canEnchant.size() > 0)
			if(!canEnchant.contains(arg0.getType())) {
				//Bukkit.getLogger().info("Can't enchant,"+getName()+" conflicts with "+arg0.getType());
				return false;
			}
		
		
		return true;
	}

	@Override
	public boolean conflictsWith(Enchantment arg0) {
		if(conflictsWith.size() > 0)
			if(conflictsWith.contains(arg0))
				return true;
		
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return null;
	}

	@Override
	public int getMaxLevel() {
		return maxLevel;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getStartLevel() {
		return 1;
	}

	@Override
	public boolean isCursed() {
		return false;
	}

	@Override
	public boolean isTreasure() {
		return false;
	}
	//helper methods for enchanting items
	public final int getLevelFromLore(String lore) {
		lore = ChatColor.stripColor(lore);
		if(!lore.startsWith(name))
			return 0;
		if(maxLevel == 1) //if it's 1, we don't need numerals
			return 1;
		
		lore = lore.substring(name.length());
		while(lore.startsWith(" "))
			lore = lore.substring(1);
		return RomanNumerals.toDecimal(lore);
	}
	public final int getLevelFromLore(ItemStack i) {
		if(i == null)
			return 0;
		ItemMeta im = i.getItemMeta();
		if(im == null)
			return 0;
		if(!im.hasLore())
			return 0;
		
		for(String str : im.getLore()) {
			if(getLevelFromLore(str) > 0)
				return getLevelFromLore(str);
		}
		return 0;
	}
	public final String getLoreFromLevel(int level) {
		if(maxLevel > 1)
			return ChatColor.GRAY + name + " " + RomanNumerals.toRomanNumberal(level);
		return ChatColor.GRAY + name;
	}
	public final ItemStack enchant(ItemStack i) {
		return enchant(i,1);
	}
	public final ItemStack enchant(ItemStack i, boolean force) {
		return enchant(i,1, force);
	}
	public final ItemStack enchant(ItemStack i, int level) {
		return enchant(i,1, false);
	}
	public final ItemStack enchant(ItemStack i, int level, boolean force) {
		if(level == 0)
			return i;
		
		if(!force)
			if(!canEnchantItem(i))
				return i;
		
		disenchant(i);
		
		if(level <= maxLevel)
			return CustomEnchantment.forceEnchantItem(i, this, level);
		else return CustomEnchantment.forceEnchantItem(i, this, maxLevel);
	}
	public final ItemStack disenchant(ItemStack i) {
		if(i == null)
			return null;
		
		ItemMeta im = i.getItemMeta();
		
		if(im.hasLore()) {
			List<String> toRemove = new ArrayList<String>();
			for(String str : im.getLore()) {
				if(getLevelFromLore(str) > 0)
					toRemove.add(str);
			}
			List<String> newLore = im.getLore();
			newLore.removeAll(toRemove);
			im.setLore(newLore);
		}
		i.setItemMeta(im);
		
		i.removeEnchantment(this);
		
		return i;
	}
	public final ItemStack enchantBasedOnLore(ItemStack i) {
		if(i == null)
			return null;
		
		ItemMeta im = i.getItemMeta();
		if(!im.hasLore())
			return i;
		
		int currentLevel = 0;
		for(String str : im.getLore()) {
			if(getLevelFromLore(str) > currentLevel) {
				currentLevel = getLevelFromLore(str);
				i = enchant(i,currentLevel);
			}
		}
		return i;
	}
	
	
	//automatically registers this enchantment
	@EventHandler
	public void onEnable(PluginEnableEvent event) {
		registerEnchant(true);
		//scans and applies to all Item entities
		for(World w : Bukkit.getServer().getWorlds()) {
			for(Entity e : w.getEntities()) {
				if(e instanceof Item) {
					Item i = (Item) e;
					i.setItemStack(enchantBasedOnLore(i.getItemStack()));
				}
			}
		}
	}
	void registerEnchant(boolean allowRetry) {
		try { //registers
			try {
				Field f = Enchantment.class.getDeclaredField("acceptingNew");
				f.setAccessible(true);
				f.set(null, true);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				Enchantment.registerEnchantment(this);
			} catch (IllegalArgumentException e) {
				unregisterEnchantment();
				if(allowRetry) registerEnchant(false);
				else e.printStackTrace();
			}
		} catch (Exception e) {
			unregisterEnchantment();
			if(allowRetry) registerEnchant(false);
			else e.printStackTrace();
		}
	}
	//automatically cleans up enchantment
	@EventHandler
	public void onDisable(PluginDisableEvent event) {
		unregisterEnchantment();
	}
	void unregisterEnchantment() {
		try {
            Field keyField = Enchantment.class.getDeclaredField("byKey");

            keyField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>) keyField.get(null);

            if(byKey.containsKey(this.getKey())) byKey.remove(this.getKey());

            Field nameField = Enchantment.class.getDeclaredField("byName");

            nameField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) nameField.get(null);

            if(byName.containsKey(this.getName())) byName.remove(this.getName());
        } catch (Exception e) {
        	Bukkit.getLogger().info("Error un-setting enchantment");
        	e.printStackTrace();
        }
	}
	//re-apply enchantment (because its removed on a server restart
	@EventHandler
	public void handleInventories (InventoryOpenEvent e) {
		ListIterator<ItemStack> iter = e.getInventory().iterator();
		while(iter.hasNext()) {
			ItemStack i = iter.next();
			enchantBasedOnLore(i);
		}
	}
	//re-apply enchantment when item becomes dropped
	@EventHandler
	public void handleDroppedItem (ItemSpawnEvent e) {
		Item i = e.getEntity();
		i.setItemStack(enchantBasedOnLore(i.getItemStack()));
	}
	public static NamespacedKey getNamespacedKey(String name) {
		final char[] allowedChars = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5','6','7','8','9','.','_','-'};
		String corrected = "";
		for(char c : name.toLowerCase().toCharArray()) {
			if(String.valueOf(allowedChars).indexOf(c) == -1)
				corrected = corrected + '-';
			else corrected = corrected + c;
		}
		return new NamespacedKey(Main.instance, corrected.toUpperCase());
	}
	public static ItemStack forceEnchantItem(ItemStack i, CustomEnchantment enchantment, int level) {
		ItemMeta meta = i.getItemMeta();
		
		List<String> lore = meta.getLore();
		if(lore == null)
			lore = new ArrayList<String>();
		
		lore.add(enchantment.getLoreFromLevel(level));
		meta.setLore(lore);
		
		i.setItemMeta(meta);
		
		i.addUnsafeEnchantment(enchantment, level);
		
		return i;
	}
	private static HashMap<String, CustomEnchantment> registeredEnchants;
	public static void RegisterEnchantement(CustomEnchantment cEnch) {
		if(registeredEnchants == null) registeredEnchants = new HashMap<String, CustomEnchantment>();
		if(registeredEnchants.containsKey(cEnch.name)) throw new IllegalArgumentException("Enchantment already registered!");
		registeredEnchants.put(cEnch.name, cEnch);
	}
	public static CustomEnchantment valueOf(String str) {
		return registeredEnchants.get(str);
	}
}
