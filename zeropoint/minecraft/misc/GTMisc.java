// May 4, 2014 1:52:45 PM
package zeropoint.minecraft.misc;


import java.util.logging.Logger;

import zeropoint.minecraft.core.Config;
import zeropoint.minecraft.core.GTCore;
import zeropoint.minecraft.core.util.Log;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;


@SuppressWarnings("javadoc")
@Mod(modid = GTMisc.modid, name = GTMisc.name, version = GTMisc.version, dependencies = "required-after:gtweaks-core")
public class GTMisc {
	public static final String modid = "gtweaks-misc";
	public static final String name = "GlobalTweaks|Misc";
	public static final String version = "release";
	private static Config cfg;
	public static final Logger LOG = Log.getLogger(name);
	private static QuillAndInk quill;
	@SuppressWarnings("unused")
	@EventHandler
	public void init(FMLInitializationEvent event) {
		cfg = GTCore.getConfig();
		if ( !GTCore.Modules.miscEnabled()) {
			LOG.warning("Module disabled!");
			return;
		}
		this.quill = new QuillAndInk(cfg.item("misc", "quillAndInk.id", 26048, "ID of the Quill and Ink item"), cfg.integer("misc", "quillAndInk.uses", 8, "Maximum number of uses of the Quill and Ink item") - 1);
		GameRegistry.registerItem(quill, "quillAndInk");
		LanguageRegistry.addName(quill, "Quill and Ink");
	}
}
