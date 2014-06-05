package brainstonemod.common.logicgate;

import net.minecraft.nbt.NBTTagCompound;

public class Pin {
	public static final Pin NullPin = new Pin('\0', false, false, false, false,
			PinState.NotExisting);
	public static final Pin MovableNullPin = new Pin('\0', false, false, false,
			true, PinState.NotExisting);

	public final static Pin readFromNBT(NBTTagCompound nbttagcompound) {
		return new Pin(nbttagcompound.getString("Name").charAt(0),
				nbttagcompound.getBoolean("Movable"),
				nbttagcompound.getBoolean("Output"),
				nbttagcompound.getBoolean("Inverted"),
				nbttagcompound.getBoolean("Replaceable"),
				PinState.valueOf(nbttagcompound.getString("State")));
	}

	public final char Name;
	public final boolean Movable;
	public final boolean Output;
	public final boolean Inverted;
	public final boolean Replaceable;

	public PinState State;

	public Pin(char name) {
		this(name, true);
	}

	public Pin(char name, boolean movable) {
		this(name, movable, false);
	}

	public Pin(char name, boolean movable, boolean output) {
		this(name, movable, output, false);
	}

	public Pin(char name, boolean movable, boolean output, boolean inverted) {
		this(name, movable, output, inverted, PinState.NotConnected);
	}

	public Pin(char name, boolean movable, boolean output, boolean inverted,
			boolean replaceable, PinState state) {
		Name = name;
		Movable = movable;
		Output = output;
		Inverted = inverted;
		State = state;
		Replaceable = replaceable;
	}

	public Pin(char name, boolean movable, boolean output, boolean inverted,
			PinState state) {
		this(name, movable, output, inverted, movable, state);
	}

	public final void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setString("Name", String.valueOf(Name));
		nbttagcompound.setBoolean("Movable", Movable);
		nbttagcompound.setBoolean("Output", Output);
		nbttagcompound.setBoolean("Inverted", Inverted);
		nbttagcompound.setBoolean("Replaceable", Replaceable);
		nbttagcompound.setString("State", State.name());
	}
}
