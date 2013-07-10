package brainstonemod.logicgates;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;

public class Pin {
	public static final Pin NullPin = new Pin('\0', false, false, false,
			PinState.NotExisting);
	public static final Pin MovableNullPin = new Pin('\0', true, false, false,
			PinState.NotExisting);

	public final static Pin readFromInputStream(DataInputStream inputStream)
			throws IOException {
		return new Pin(inputStream.readChar(), inputStream.readBoolean(),
				inputStream.readBoolean(), inputStream.readBoolean(),
				PinState.valueOf(inputStream.readUTF()));
	}

	public final static Pin readFromNBT(NBTTagCompound nbttagcompound) {
		return new Pin(nbttagcompound.getString("Name").charAt(0),
				nbttagcompound.getBoolean("Movable"),
				nbttagcompound.getBoolean("Output"),
				nbttagcompound.getBoolean("Inverted"),
				PinState.valueOf(nbttagcompound.getString("State")));
	}

	public final char Name;
	public final boolean Movable;
	public final boolean Output;
	public final boolean Inverted;

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
			PinState state) {
		Name = name;
		Movable = movable;
		Output = output;
		Inverted = inverted;
		State = state;
	}

	public final void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setString("Name", String.valueOf(Name));
		nbttagcompound.setBoolean("Movable", Movable);
		nbttagcompound.setBoolean("Output", Output);
		nbttagcompound.setBoolean("Inverted", Inverted);
		nbttagcompound.setString("State", State.name());
	}

	public final void writeToOutputStream(DataOutputStream outputStream)
			throws IOException {
		outputStream.writeChar(Name);
		outputStream.writeBoolean(Movable);
		outputStream.writeBoolean(Output);
		outputStream.writeBoolean(Inverted);
		outputStream.writeUTF(State.name());
	}
}
