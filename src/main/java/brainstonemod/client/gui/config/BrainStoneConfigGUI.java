package brainstonemod.client.gui.config;

import java.util.ArrayList;
import java.util.List;

import brainstonemod.BrainStone;
import brainstonemod.common.api.BrainStoneModules;
import brainstonemod.common.helper.BrainStoneConfigHelper;
import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.gui.GuiScreen;

public class BrainStoneConfigGUI extends GuiConfig {
	public BrainStoneConfigGUI(GuiScreen parent) {
		super(parent, getConfigElements(), BrainStone.MOD_ID, false, false, BrainStone.NAME + " Config");
	}

	/** Compiles a list of config elements */
	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<>();

		// Add categories to config GUI
		list.add(categoryElement(BrainStoneConfigHelper.getDisplayCategory(), "Display",
				"gui.brainstone.config.cat.display"));
		list.add(categoryElement(BrainStoneConfigHelper.getWorldgenCategory(), "Worldgen",
				"gui.brainstone.config.cat.worldgen"));

		if (BrainStoneModules.energy())
			list.add(categoryElement(BrainStoneConfigHelper.getBrainStoneLiveCapacitorCategory(),
					"BrainStoneLiveCapacitor", "gui.brainstone.config.cat.brainstonelivecapacitor"));

		return list;
	}

	/**
	 * Creates a button linking to another screen where all options of the
	 * category are available
	 */
	private static IConfigElement categoryElement(List<IConfigElement> elements, String name, String tooltip_key) {
		return new DummyConfigElement.DummyCategoryElement(name, tooltip_key, elements);
	}
}
