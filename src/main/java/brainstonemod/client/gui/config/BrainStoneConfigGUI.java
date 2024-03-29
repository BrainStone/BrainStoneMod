package brainstonemod.client.gui.config;

import java.util.ArrayList;
import java.util.List;

import brainstonemod.BrainStone;
import brainstonemod.common.config.BrainStoneConfigWrapper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class BrainStoneConfigGUI extends GuiConfig {
	public BrainStoneConfigGUI(GuiScreen parent) {
		super(parent, getConfigElements(), BrainStone.MOD_ID, false, false, BrainStone.NAME + " Config");
	}

	/** Compiles a list of config elements */
	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<>();

		// Add categories to config GUI
		list.add(categoryElement(BrainStoneConfigWrapper.getModulesCategory(), "Modules",
				"gui.brainstone.config.cat.modules"));
		list.add(categoryElement(BrainStoneConfigWrapper.getMiscCategory(), "Miscellaneous",
				"gui.brainstone.config.cat.miscellaneous"));
		list.add(categoryElement(BrainStoneConfigWrapper.getBrainStoneLifeCapacitorCategory(),
				"BrainStoneLifeCapacitor", "gui.brainstone.config.cat.brainstonelifecapacitor"));
		list.add(categoryElement(BrainStoneConfigWrapper.getWorldgenCategory(), "Worldgen",
				"gui.brainstone.config.cat.worldgen"));

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
