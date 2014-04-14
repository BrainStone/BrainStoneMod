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

public class OR_Gate extends Gate {
	@Override
	public void onGateChange(BrainStoneDirection direction) {
		Pins[UP.toArrayIndex()] = Pin.MovableNullPin;
		Pins[DOWN.toArrayIndex()] = Pin.MovableNullPin;

		Pins[NORTH.reorintateNorth(direction).toArrayIndex()] = new Pin('Q',
				true, true);
		Pins[EAST.reorintateNorth(direction).toArrayIndex()] = new Pin('A');
		Pins[SOUTH.reorintateNorth(direction).toArrayIndex()] = new Pin('B');
		Pins[WEST.reorintateNorth(direction).toArrayIndex()] = new Pin('C');
	}

	@Override
	public void onOptionsChange() {
		// TODO What to do when options change?
	}

	@Override
	public void onTick() {
		boolean out = false;

		for (char i = 'A'; i <= 'C'; i++) {
			if (getPinState(i).isPowered()) {
				out = true;
				break;
			}
		}

		this.setPinState('Q', out);
	}
}