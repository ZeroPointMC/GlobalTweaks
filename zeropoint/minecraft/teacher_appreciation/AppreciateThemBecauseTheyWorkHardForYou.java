// May 7, 2014 10:07:34 AM
package zeropoint.minecraft.teacher_appreciation;


import java.util.logging.Logger;

import zeropoint.minecraft.core.util.Log;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;


@SuppressWarnings("javadoc")
@Mod(modid = "gt-teacherAppreciation", name = "Teacher Appreciation", version = "appreciative")
public class AppreciateThemBecauseTheyWorkHardForYou {
	@SuppressWarnings({
		"unused",
		"static-method"
	})
	@EventHandler
	public void init(FMLInitializationEvent event) {
		final Logger l = Log.getLogger("[GlobalTweaks] Teacher Appreciation");
		l.severe("Remember, your teachers work hard for you!");
		l.severe("Their jobs aren't easy as it is, so please don't make it harder for them!");
	}
}
