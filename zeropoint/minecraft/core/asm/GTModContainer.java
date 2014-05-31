// May 18, 2014 8:27:48 AM
package zeropoint.minecraft.core.asm;


import java.util.Arrays;
import java.util.logging.Logger;

import net.minecraftforge.event.EventBus;
import zeropoint.minecraft.core.util.Log;

import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;


@SuppressWarnings("javadoc")
public class GTModContainer extends DummyModContainer {
	public static final String name = "GlobalTweaks|Coremod";
	private final Logger log = Log.getLogger(name);
	public GTModContainer() {
		super(new ModMetadata());
		ModMetadata myMeta = this.getMetadata();
		myMeta.modId = "gt-coremod";
		myMeta.name = name;
		myMeta.version = "release";
		myMeta.authorList = Arrays.asList("ZeroPoint");
		myMeta.description = "Access transformer for GlobalTweaks";
		myMeta.url = "http://zeropointmc.tumblr.com/";
		myMeta.updateUrl = "";
		myMeta.screenshots = new String[0];
		myMeta.logoFile = "";
	}
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		this.log.info("Current state: " + controller.getModState(this).toString());
		return true;
	}
	@SuppressWarnings("unused")
	@Subscribe
	public void modConstruction(FMLConstructionEvent evt) {
		this.log.info("Mod constructed");
	}
	@SuppressWarnings("unused")
	@Subscribe
	public void preInit(FMLPreInitializationEvent evt) {
		this.log.info("Mod preinitialized");
	}
	@SuppressWarnings("unused")
	@Subscribe
	public void init(FMLInitializationEvent evt) {
		this.log.info("Mod initialized");
	}
	@SuppressWarnings("unused")
	@Subscribe
	public void postInit(FMLPostInitializationEvent evt) {
		this.log.info("Mod postinitialized");
	}
}
