package brainstonemod.common.logicgate.gate;

import static brainstonemod.common.helper.BrainStoneDirection.DOWN;
import static brainstonemod.common.helper.BrainStoneDirection.EAST;
import static brainstonemod.common.helper.BrainStoneDirection.NORTH;
import static brainstonemod.common.helper.BrainStoneDirection.SOUTH;
import static brainstonemod.common.helper.BrainStoneDirection.UP;
import static brainstonemod.common.helper.BrainStoneDirection.WEST;
import brainstonemod.common.helper.BrainStoneDirection;
import brainstonemod.common.logicgate.Gate;
import brainstonemod.common.logicgate.Pin;
import brainstonemod.common.logicgate.PinState;

public class AND_Gate extends Gate {
	@Override
	public void onGateChange(BrainStoneDirection direction) {
		Pins[UP.toArrayIndex()] = Pin.MovableNullPin;
		Pins[DOWN.toArrayIndex()] = Pin.MovableNullPin;

		Pins[SOUTH.reorintateNorth(direction).toArrayIndex()] = new Pin('Q',
				true, true);
		Pins[EAST.reorintateNorth(direction).toArrayIndex()] = new Pin('A');
		Pins[NORTH.reorintateNorth(direction).toArrayIndex()] = new Pin('B');
		Pins[WEST.reorintateNorth(direction).toArrayIndex()] = new Pin('C');
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
			connected = (tmp = getPinState(i)).isValid() || connected;
			out = tmp.isValid() ? (out && tmp.isPowered()) : out;
		}

		this.setPinState('Q', out && connected);
	}
}