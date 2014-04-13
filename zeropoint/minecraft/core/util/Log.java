package zeropoint.minecraft.core.util;


import java.util.logging.Logger;

import zeropoint.core.StackTrace;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;


public class Log {
	@Deprecated
	public static final Logger getLogger() {
		try {
			Class<?> cl = Class.forName(StackTrace.getCallingClass());
			Mod anno = cl.getAnnotation(Mod.class);
			return getLogger(anno.name());
		}
		catch (ClassNotFoundException e) {
			return null;
		}
	}
	public static final Logger getLogger(String name) {
		return getLogger(name, null);
	}
	public static final Logger getLogger(String name, String resBundle) {
		FMLLog.makeLog(name);
		if (resBundle == null) {
			return Logger.getLogger(name, resBundle);
		}
		return Logger.getLogger(name);
	}
}
