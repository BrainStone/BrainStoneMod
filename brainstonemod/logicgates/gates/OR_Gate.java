package brainstonemod.logicgates.gates;

import brainstonemod.logicgates.Gate;
import brainstonemod.logicgates.Pin;

public class OR_Gate extends Gate {
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
		// TODO Auto-generated method stub

	}

	@Override
	public void onTick() {
		boolean out = false;

		for (char i = 'A'; i <= 'C'; i++) {
			if (this.getPinState(i).isPowered()) {
				out = true;
				break;
			}
		}

		this.setPinState('Q', out);
	}
}