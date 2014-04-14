package brainstonemod.common.logicgate.gate;

import net.minecraftforge.common.util.ForgeDirection;
import brainstonemod.common.logicgate.Gate;
import brainstonemod.common.logicgate.Pin;

public class OR_Gate extends Gate {
	@Override
	public void onGateChange(ForgeDirection direction) {
		Pins[0] = Pin.MovableNullPin;
		Pins[1] = Pin.MovableNullPin;

		Pins[alterDirection(ForgeDirection.NORTH, direction).ordinal()] = new Pin(
				'Q', true, true);
		Pins[alterDirection(ForgeDirection.EAST, direction).ordinal()] = new Pin(
				'A');
		Pins[alterDirection(ForgeDirection.SOUTH, direction).ordinal()] = new Pin(
				'B');
		Pins[alterDirection(ForgeDirection.WEST, direction).ordinal()] = new Pin(
				'C');
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