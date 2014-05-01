// May 1, 2014 9:01:56 AM
package zeropoint.minecraft.effect;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import zeropoint.minecraft.core.Config;
import zeropoint.minecraft.core.GTCore;
import zeropoint.minecraft.core.util.Log;
import zeropoint.minecraft.effect.potion.PotionBleed;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;


@SuppressWarnings("javadoc")
@Mod(modid = GTEffect.modid, name = GTEffect.name, version = GTEffect.version, dependencies = "required-after:gtweaks-core")
public class GTEffect {
	public static final String modid = "gtweaks-effect";
	public static final String name = "GlobalTweaks|Effects";
	public static final String version = "release";
	private static Config cfg;
	private static final Logger LOG = Log.getLogger(name);
	private static Potion potBleed;
	@SuppressWarnings("unused")
	@EventHandler
	public static void init(FMLInitializationEvent event) {
		cfg = GTCore.getConfig();
		if ( !GTCore.Modules.effectsEnabled()) {
			LOG.warning("Module disabled!");
			return;
		}
		prepare();
		register();
		hook();
	}
	private static void hook() {
		LOG.info("Registering potion handlers");
		MinecraftForge.EVENT_BUS.register(potBleed);
	}
	private static void register() {
		LOG.info("Registering potions");
		potBleed = new PotionBleed(cfg.integer("effects.bleed", "id", 32, "ID for the Bleed effect"), cfg.integer("effects.bleed", "damage", 2, "The damage that the Bleed effect will inflict"));
		LOG.info("Registering items");
	}
	private static void prepare() {
		final int lim = 256;
		LOG.info("Attempting to extend potion effect array to " + lim);
		Potion[] potionTypes = null;
		for (Field f : Potion.class.getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getName().equals("potionTypes") || f.getName().equals("field_76425_a")) {
					Field modfield = Field.class.getDeclaredField("modifiers");
					modfield.setAccessible(true);
					modfield.setInt(f, f.getModifiers() & ~Modifier.FINAL);
					potionTypes = (Potion[]) f.get(null);
					final Potion[] newPotionTypes = new Potion[lim];
					System.arraycopy(potionTypes, 0, newPotionTypes, 0, potionTypes.length);
					f.set(null, newPotionTypes);
				}
			}
			catch (Exception e) {
				LOG.log(Level.WARNING, "Failed to extend potion effect array", e);
			}
		}
	}
	public static int bleedID() {
		return potBleed.id;
	}
}
