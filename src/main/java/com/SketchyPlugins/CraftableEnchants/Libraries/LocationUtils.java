package com.SketchyPlugins.CraftableEnchants.Libraries;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class LocationUtils {
	public static int limit = 80;
	
	public static Location makeSafe(Location loc) {
		//first, move it to the ground
		while(!loc.getBlock().getType().isSolid())
			loc.add(0,-1,0);
		
		//now, try and move it above ground
		while(loc.getBlock().getType().isSolid())
			loc.add(0,1,0);
		
		return loc;
	}
	public static boolean isSafe(Location loc) {
		return loc.getBlock().getType() == Material.AIR && loc.clone().add(0,1,0).getBlock().getType() == Material.AIR;
	}
	public static boolean makeSafeWithLimits(Location loc) {
		try{
			//first, move it to the ground like normal
	
			while(!loc.getBlock().getType().isSolid() && loc.getZ() > 0)
				loc.add(0,-1,0);
			
			if(loc.getZ() <= 0) return false;
			if(!loc.getChunk().isLoaded()) return false;
			
			//now, try and move it up with a max of 5 blocks
			int moved = 0;
			while(!isSafe(loc) && moved < limit/5) {
				loc.add(0,1,0);
				moved++;
			}
			//if it failed, move it back down
			if(moved == limit/5) {
				loc.subtract(0, moved, 0);
				return false;
			}
			
			//if the location ended up being safe, return true
			return !loc.getBlock().getType().isSolid();
		}
		catch(Exception e) {return false;}
	}
	
	public static Location randomSafeLoc(Location original, float radius) {
		Location toReturn = original.clone();
		int attempts = 0;
		do {
			float x = (float) (2*radius*(Math.random()-0.5)), z = (float) (2*radius*(Math.random()-0.5));
			//regenerate as long as they're outside of the radius
			while(x*x+z*z >= radius*radius) {
				x = (float) (2*radius*(Math.random()-0.5));
				z = (float) (2*radius*(Math.random()-0.5));
			}
			attempts++;
			toReturn = original.clone().add(x,0,z);
		}
		while(!makeSafeWithLimits(toReturn) && attempts < limit); //repeat if unsafe, stop if limit reached
		
		//return the location ends up safe
		return toReturn;
	}
	
	public static Location randomLocAwayFromPlayers(Player center, float minRadius, float maxRadius) {
		//get a random point on a circle
		double angle = 2*Math.PI*Math.random();
		double radius = minRadius+(Math.random()*(maxRadius-minRadius));
		Location toTest = center.getLocation().add(radius*Math.cos(angle), 0, radius*Math.cos(angle));
		makeSafeWithLimits(toTest);
		
		//check the point and regenerate if if it's wrong
		int attempts = 0;
		while(arePlayersNearPoint(toTest,minRadius, center) || attempts > limit) {
			angle = 2*Math.PI*Math.random();
			radius = minRadius+(Math.random()*(maxRadius-minRadius));
			toTest = center.getLocation().add(radius*Math.cos(angle), 0, radius*Math.cos(angle));
			makeSafeWithLimits(toTest);
		}
		//return it
		return toTest;
	}
	
	private static boolean arePlayersNearPoint(Location loc, float radius, Player toIgnore) {
		for(Player plr : loc.getWorld().getPlayers()) {
			if(!plr.equals(toIgnore) && plr.getLocation().distanceSquared(loc) > radius)
				return true;
		}
		return false;
	}
}
