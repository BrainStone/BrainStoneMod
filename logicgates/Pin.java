package mods.brainstone.logicgates;

public class Pin {
	public static final Pin NullPin = new Pin('\0', false);

	public final char Name;
	public final boolean Movable;
	public final boolean Output;
	public final boolean Inverted;
	public short State;

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
		Name = name;
		Movable = movable;
		Output = output;
		Inverted = inverted;
		State = 0;
	}
}
