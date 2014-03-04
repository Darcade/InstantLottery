/*
Notes which hash is used for which color.

Inc Sac: 0 INC_SAC
Rose Red: 1 ROSE_RED
Cactus Green: 2 CACTUS_GREEN
Cocoa Beans: 3 COCOA_BEANS
Lapis Lazuli: 4 LAPIS_LAZULI
Purple Dye: 5 PURPLE_DYE
Cyan Dye: 6 CYAN_DYE
Light Gray Dye: 7 LIGHT_GRAY_DYE
Gray Dye: 8 GRAY_DYE
Pink Dye: 9 PINK_DYE
Lime Dye: 10 LIME_DYE
Dangelion Yellow: 11 DANGELION_YELLOW
Light Blue Dye: 12 LIGHT_BLUE_DYE
Magenta Dye: 13 MAGENTA_DYE
Orange Dye: 14 ORANGE_DYE
Bone Meal: 15 BONE_MEAL

 */

package me.darcade.minecraftlottery;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ColorChecker {

	HashMap<String, Object> dyes = new HashMap<String, Object>();

	public ColorChecker(MinecraftLottery lottery) {
		int randomnum = new Random().nextInt(lottery.getConfig().getInt(
				"max-price")) + 1;

		this.dyes.put("INC_SAC", new ItemStack(Material.INK_SACK, randomnum,
				(byte) 0));
		this.dyes.put("ROSE_RED", new ItemStack(Material.INK_SACK, randomnum,
				(byte) 1));
		this.dyes.put("CACTUS_GREEN", new ItemStack(Material.INK_SACK,
				randomnum, (byte) 2));
		this.dyes.put("COCOA_BEANS", new ItemStack(Material.INK_SACK,
				randomnum, (byte) 3));
		this.dyes.put("LAPIS_LAZULI", new ItemStack(Material.INK_SACK,
				randomnum, (byte) 4));
		this.dyes.put("PURPLE_DYE", new ItemStack(Material.INK_SACK, randomnum,
				(byte) 5));
		this.dyes.put("CYAN_DYE", new ItemStack(Material.INK_SACK, randomnum,
				(byte) 6));
		this.dyes.put("LIGHT_GRAY_DYE", new ItemStack(Material.INK_SACK,
				randomnum, (byte) 7));
		this.dyes.put("GRAY_DYE", new ItemStack(Material.INK_SACK, randomnum,
				(byte) 8));
		this.dyes.put("PINK_DYE", new ItemStack(Material.INK_SACK, randomnum,
				(byte) 9));
		this.dyes.put("LIME_DYE", new ItemStack(Material.INK_SACK, randomnum,
				(byte) 10));
		this.dyes.put("DANGELION_YELLOW", new ItemStack(Material.INK_SACK,
				randomnum, (byte) 11));
		this.dyes.put("LIGHT_BLUE_DYE", new ItemStack(Material.INK_SACK,
				randomnum, (byte) 12));
		this.dyes.put("MAGENTA_DYE", new ItemStack(Material.INK_SACK,
				randomnum, (byte) 13));
		this.dyes.put("ORANGE_DYE", new ItemStack(Material.INK_SACK, randomnum,
				(byte) 14));
		this.dyes.put("BONE_MEAL", new ItemStack(Material.INK_SACK, randomnum,
				(byte) 15));

	}

	public boolean checkforcolor(String itemname) {
		boolean output = false;

		for (String dyename : dyes.keySet())
			if (dyename.equalsIgnoreCase(itemname))
				output = true;

		return output;
	}
	
	public ItemStack getColor(String itemname){
		return (ItemStack) dyes.get(itemname);
	}
}
