// May 18, 2014 8:14:11 AM
package zeropoint.minecraft.core.asm;


import java.io.IOException;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;


@SuppressWarnings("javadoc")
public class GTAccessTransformer extends AccessTransformer {
	public GTAccessTransformer() throws IOException {
		super("gtweaks_at.cfg");
	}
}
