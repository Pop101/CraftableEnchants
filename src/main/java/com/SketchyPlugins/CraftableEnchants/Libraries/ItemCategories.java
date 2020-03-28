package com.SketchyPlugins.CraftableEnchants.Libraries;

import org.bukkit.Material;

public class ItemCategories {
	public static boolean contains(Material o, Material...objects) {
		for(Object test : objects)
			if(o.equals(test))
				return true;
		
		return false;
	}
	public static final Material[] SWORDS = new Material[]{
			Material.DIAMOND_SWORD,
			Material.IRON_SWORD,
			Material.GOLDEN_SWORD,
			Material.STONE_SWORD,
			Material.WOODEN_SWORD};
	public static final Material[] AXES = new Material[]{
			Material.DIAMOND_AXE,
			Material.IRON_AXE,
			Material.GOLDEN_AXE,
			Material.STONE_AXE,
			Material.WOODEN_AXE};
	public static final Material[] HOES = new Material[]{
			Material.DIAMOND_HOE,
			Material.IRON_HOE,
			Material.GOLDEN_HOE,
			Material.STONE_HOE,
			Material.WOODEN_HOE};
	public static final Material[] SHOVELS = new Material[]{
			Material.DIAMOND_SHOVEL,
			Material.IRON_SHOVEL,
			Material.GOLDEN_SHOVEL,
			Material.STONE_SHOVEL,
			Material.WOODEN_SHOVEL};
	public static final Material[] PICKAXES = new Material[]{
			Material.DIAMOND_PICKAXE,
			Material.IRON_PICKAXE,
			Material.GOLDEN_PICKAXE,
			Material.STONE_PICKAXE,
			Material.WOODEN_PICKAXE};
	public static final Material[] ARMOR = new Material[]{
			Material.DIAMOND_HELMET,
			Material.GOLDEN_HELMET,
			Material.IRON_HELMET,
			Material.LEATHER_HELMET,
			Material.DIAMOND_CHESTPLATE,
			Material.GOLDEN_CHESTPLATE,
			Material.IRON_CHESTPLATE,
			Material.LEATHER_CHESTPLATE,
			Material.DIAMOND_LEGGINGS,
			Material.GOLDEN_LEGGINGS,
			Material.IRON_LEGGINGS,
			Material.LEATHER_LEGGINGS,
			Material.DIAMOND_BOOTS,
			Material.GOLDEN_BOOTS,
			Material.IRON_BOOTS,
			Material.LEATHER_BOOTS};
	public static final Material[] WEAPONS = new Material[]{
			Material.DIAMOND_AXE,
			Material.IRON_AXE,
			Material.GOLDEN_AXE,
			Material.STONE_AXE,
			Material.WOODEN_AXE,
			Material.DIAMOND_SWORD,
			Material.IRON_SWORD,
			Material.GOLDEN_SWORD,
			Material.STONE_SWORD,
			Material.WOODEN_SWORD,
			Material.BOW};
	public static final Material[] TOOLS = new Material[]{
			Material.DIAMOND_AXE,
			Material.IRON_AXE,
			Material.GOLDEN_AXE,
			Material.STONE_AXE,
			Material.WOODEN_AXE,
			Material.DIAMOND_PICKAXE,
			Material.IRON_PICKAXE,
			Material.GOLDEN_PICKAXE,
			Material.STONE_PICKAXE,
			Material.WOODEN_PICKAXE,
			Material.DIAMOND_HOE,
			Material.IRON_HOE,
			Material.GOLDEN_HOE,
			Material.STONE_HOE,
			Material.WOODEN_HOE,
			Material.DIAMOND_SHOVEL,
			Material.IRON_SHOVEL,
			Material.GOLDEN_SHOVEL,
			Material.STONE_SHOVEL,
			Material.WOODEN_SHOVEL,
			Material.SHEARS,
			Material.FISHING_ROD};
	public static final Material[] GLEAMING = new Material[] {
			Material.GOLD_BLOCK,
			Material.GOLD_INGOT,
			Material.GOLDEN_PICKAXE,
			Material.GOLDEN_AXE,
			Material.GOLDEN_SHOVEL,
			Material.GOLDEN_SWORD,
			Material.GOLDEN_HOE,
			Material.GOLDEN_HELMET,
			Material.GOLDEN_LEGGINGS,
			Material.GOLDEN_CHESTPLATE,
			Material.GOLDEN_BOOTS};
}
