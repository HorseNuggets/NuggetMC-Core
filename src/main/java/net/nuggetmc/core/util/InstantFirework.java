package net.nuggetmc.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class InstantFirework {
	public void create(FireworkEffect fe, Location loc) {
		Firework f = (Firework) loc.getWorld().spawn(loc, Firework.class);
		FireworkMeta fm = f.getFireworkMeta();
		fm.addEffect(fe);
		f.setFireworkMeta(fm);
		try {
			Class<?> entityFireworkClass = getClass("net.minecraft.server.", "EntityFireworks");
			Class<?> craftFireworkClass = getClass("org.bukkit.craftbukkit.", "entity.CraftFirework");
			Object firework = craftFireworkClass.cast(f);
			Method handle = firework.getClass().getMethod("getHandle");
			Object entityFirework = handle.invoke(firework);
			Field expectedLifespan = entityFireworkClass.getDeclaredField("expectedLifespan");
			Field ticksFlown = entityFireworkClass.getDeclaredField("ticksFlown");
			ticksFlown.setAccessible(true);
			ticksFlown.setInt(entityFirework, expectedLifespan.getInt(entityFirework) - 1);
			ticksFlown.setAccessible(false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private Class<?> getClass(String prefix, String nmsClassString) throws ClassNotFoundException {
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
		String name = prefix + version + nmsClassString;
		Class<?> nmsClass = Class.forName(name);
		return nmsClass;
	}

	public void firework(Location loc, Player player) {
		Firework f = (Firework) player.getWorld().spawn(loc, Firework.class);

		FireworkMeta fm = f.getFireworkMeta();
		fm.addEffect(FireworkEffect.builder().flicker(true).trail(true).with(Type.BURST).withColor(Color.YELLOW)
				.withFade(Color.WHITE).build());
		fm.setPower(3);

		FireworkEffect fe = FireworkEffect.builder().flicker(true).trail(true).with(Type.BURST).withColor(Color.YELLOW)
				.withFade(Color.WHITE).build();

		create(fe, loc);

		return;
	}
}
