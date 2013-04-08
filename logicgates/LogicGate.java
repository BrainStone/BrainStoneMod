package mods.brainstone.logicgates;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class LogicGate {
	public ArrayList<Pin> Gates;
	public HashMap<String, HashMap<String, String[]>> Localizations;
	public ArrayList<HashMap<String, Option>> Options;

	public LogicGate() {
		;
	}

	public abstract boolean evaluateGate();

	public abstract void onOptionsChange();

	public abstract void setStartOptions(short direction);
}