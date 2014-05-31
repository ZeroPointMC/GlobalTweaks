// May 18, 2014 8:12:02 AM
package zeropoint.minecraft.core.asm;


import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.Name;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;


@TransformerExclusions({
	"zeropoint.minecraft.core.asm"
})
@MCVersion("1.6.4")
@SuppressWarnings("javadoc")
@Name("GT|Coremod")
public class GTLoadingPlugin implements IFMLLoadingPlugin {
	private static boolean isDevEnviron;
	@Override
	@Deprecated
	public String[] getLibraryRequestClass() {
		return null;
	}
	@Override
	public String[] getASMTransformerClass() {
		return new String[] {
			GTAccessTransformer.class.getName()
		};
	}
	@Override
	public String getModContainerClass() {
		// return GTModContainer.class.getName();
		return null;
	}
	@Override
	public String getSetupClass() {
		return null;
	}
	@Override
	public void injectData(Map<String, Object> data) {
		this.isDevEnviron = !(Boolean) data.get("runtimeDeobfuscationEnabled");
	}
	public static final boolean isDevEnrivonment() {
		return isDevEnviron;
	}
}
