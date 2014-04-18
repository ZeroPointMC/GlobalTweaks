package zeropoint.minecraft.core.util;


import java.util.logging.Level;
import java.util.logging.Logger;

import zeropoint.core.StackTrace;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;


/**
 * Retrieve and configure a Logger in one go
 * 
 * @author Zero Point
 */
public class Log {
	/**
	 * Get a logger for the Mod owning the caller
	 * 
	 * @return the configured Logger
	 * @deprecated Should use getLogger(String name) instead.
	 */
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
	/**
	 * Get a Logger for the given name
	 * 
	 * @param name
	 *            - the name of the Logger
	 * @return the configured Logger
	 */
	public static final Logger getLogger(String name) {
		return getLogger(name, null);
	}
	/**
	 * Get a Logger for the given name, using the given resource bundle
	 * 
	 * @param name
	 *            - the name of the Logger
	 * @param resBundle
	 *            - the resource bundle to use for the Logger
	 * @return the configured Logger
	 */
	public static final Logger getLogger(String name, String resBundle) {
		FMLLog.makeLog(name);
		Logger logger;
		if (resBundle == null) {
			logger = Logger.getLogger(name, resBundle);
		}
		else {
			logger = Logger.getLogger(name);
		}
		logger.setLevel(Level.ALL);
		logger.config("Logger initialized");
		return logger;
	}
}
