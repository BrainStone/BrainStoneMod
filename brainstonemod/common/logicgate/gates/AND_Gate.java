package brainstonemod.common.logicgate.gates;

import brainstonemod.common.logicgate.Gate;
import brainstonemod.common.logicgate.Pin;
import brainstonemod.common.logicgate.PinState;

public class AND_Gate extends Gate {
	@Override
	public void onGateChange(int direction) {
		Pins[0] = Pin.MovableNullPin;
		Pins[1] = Pin.MovableNullPin;

		Pins[2 + direction] = new Pin('Q', true, true);
		Pins[2 + ((direction + 1) % 4)] = new Pin('A');
		Pins[2 + ((direction + 2) % 4)] = new Pin('B');
		Pins[2 + ((direction + 3) % 4)] = new Pin('C');
	}

	@Override
	public void onOptionsChange() {
		// TODO What to do when options change?
	}

	@Override
	public void onTick() {
		boolean out = true;
		boolean connected = false;
		PinState tmp;

		for (char i = 'A'; i <= 'C'; i++) {
			connected = (tmp = this.getPinState(i)).isValid() || connected;
			out = tmp.isValid() ? (out && tmp.isPowered()) : out;
		}

		this.setPinState('Q', out && connected);
	}
}