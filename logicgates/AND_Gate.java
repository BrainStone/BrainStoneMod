package mods.brainstone.logicgates;

import java.util.ArrayList;
import java.util.HashMap;

public class AND_Gate extends LogicGate {
	public AND_Gate() {
		Gates = new ArrayList<Pin>();

		Localizations = new HashMap<String, HashMap<String, String[]>>();

		// English

		HashMap<String, String[]> tempLocalizations = new HashMap<String, String[]>();

		tempLocalizations.put("option.test", new String[] { "Test",
				"This is a test." });

		Localizations.put("en_US", tempLocalizations);

		// Deutsch

		tempLocalizations = new HashMap<String, String[]>();

		tempLocalizations.put("option.test", new String[] { "Test",
				"Das ist ein Test." });

		Localizations.put("de_DE", tempLocalizations);
	}

	@Override
	public boolean evaluateGate() {
		return false;
	}

	@Override
	public void onOptionsChange() {
		;
	}

	@Override
	public void setStartOptions(short direction) {
		;
	}
}