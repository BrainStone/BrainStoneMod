package mods.brainstone.logicgates.gates;

import mods.brainstone.logicgates.Gate;
import mods.brainstone.logicgates.Pin;

public class AND_Gate extends Gate {
	@Override
	public void onGateChange(int direction) {
		Pins[0] = Pin.NullPin;
		Pins[1] = Pin.NullPin;

		Pins[2 + direction] = new Pin('Q', true, true);
		Pins[2 + ((direction + 1) % 4)] = new Pin('A');
		Pins[2 + ((direction + 2) % 4)] = new Pin('B');
		Pins[2 + ((direction + 3) % 4)] = new Pin('C');
	}

	@Override
	public void onOptionsChange() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTick() {
		// TODO Auto-generated method stub

	}
}