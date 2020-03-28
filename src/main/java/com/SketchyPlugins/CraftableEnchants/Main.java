package com.SketchyPlugins.CraftableEnchants;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import com.SketchyPlugins.CraftableEnchants.Enchantments.Blessing;
import com.SketchyPlugins.CraftableEnchants.Enchantments.Explosive;
import com.SketchyPlugins.CraftableEnchants.Enchantments.Gleaming;
import com.SketchyPlugins.CraftableEnchants.Enchantments.Hardness;
import com.SketchyPlugins.CraftableEnchants.Enchantments.Infusion;
import com.SketchyPlugins.CraftableEnchants.Enchantments.Lifesteal;
import com.SketchyPlugins.CraftableEnchants.Enchantments.Persistence;
import com.SketchyPlugins.CraftableEnchants.Enchantments.Powering;
import com.SketchyPlugins.CraftableEnchants.Enchantments.Smelting;
import com.SketchyPlugins.CraftableEnchants.Enchantments.Telekinesis;
import com.SketchyPlugins.CraftableEnchants.Enchantments.Unknown;
import com.SketchyPlugins.CraftableEnchants.Enchantments.Villainy;
import com.SketchyPlugins.CraftableEnchants.Enchantments.Warping;
import com.SketchyPlugins.CraftableEnchants.Libraries.ItemCategories;
import com.SketchyPlugins.CraftableEnchants.Libraries.ItemEnchanter;

public final class Main extends JavaPlugin{
	public static JavaPlugin instance;
	@Override
	public void onEnable() {
		getLogger().info(ChatColor.GREEN+"Enabled!");
		
		Main.instance = this;
		
		new ItemEnchanter(Material.FLINT_AND_STEEL, Enchantment.FIRE_ASPECT, 1);
		new ItemEnchanter(Material.MAGMA_CREAM, Enchantment.PROTECTION_FIRE, 1);
		new ItemEnchanter(Material.BLAZE_POWDER, Enchantment.ARROW_FIRE, 1);
		
		Enchantment unknown = new Unknown();
		new ItemEnchanter(Material.LAPIS_LAZULI, unknown, 1);
		new ItemEnchanter(Material.GOLD_INGOT, unknown, 1);
		new ItemEnchanter(Material.EMERALD, unknown, 1);
		
		Enchantment powering = new Powering();
		new ItemEnchanter(Material.LEVER, powering, 1);
		
		Enchantment infusion = new Infusion();
		new ItemEnchanter(Material.LAPIS_BLOCK, infusion, 1);
		
		Enchantment lifesteal = new Lifesteal();
		new ItemEnchanter(Material.GHAST_TEAR, lifesteal, 1);
		
		Enchantment warping = new Warping();
		new ItemEnchanter(Material.ENDER_PEARL, warping, 1);
		
		Enchantment persistence = new Persistence();
		new ItemEnchanter(Material.DEAD_BUSH, persistence, 1);
		
		Enchantment telekinesis = new Telekinesis();
		new ItemEnchanter(Material.ENDER_EYE, telekinesis, 1);
		
		Enchantment cooking = new Smelting();
		new ItemEnchanter(Material.BLAZE_ROD, cooking, 1);
		
		Enchantment explosive = new Explosive();
		new ItemEnchanter(Material.TNT, explosive, 1);
		
		Enchantment villainy = new Villainy();
		new ItemEnchanter(Material.EMERALD_BLOCK, villainy, 1);
		
		Enchantment minorBlessing = new Blessing();
		new ItemEnchanter(Material.OXEYE_DAISY, minorBlessing, 1);
		
		Enchantment hardness = new Hardness();
		new ItemEnchanter(Material.SCUTE, hardness, 1);
		
		//gleaming should be defined last
		Enchantment gleaming = new Gleaming();
		for(Material m : ItemCategories.GLEAMING)
			new ItemEnchanter(m, gleaming, 1);
		
		new EnchantmentCrafting(this);
	}
}
