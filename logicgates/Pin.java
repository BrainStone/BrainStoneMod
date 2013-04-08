package mods.brainstone.logicgates;

public class Pin {
	public boolean Output;
	public short Position;
	public char Letter;
	public boolean Inverted;
	public boolean Movable;

	public boolean EMPTY;

	public Pin(boolean output, short position, char letter) {
		this(output, position, letter, false);
	}

	public Pin(boolean output, short position, char letter, boolean inverted) {
		this(output, position, letter, inverted, true);
	}

	public Pin(boolean output, short position, char letter, boolean inverted,
			boolean movable) {
		Output = output;
		Position = position;
		Letter = letter;
		Inverted = inverted;
		Movable = movable;

		EMPTY = false;
	}

	public Pin(short position) {
		this(false, position, ' ');

		EMPTY = true;
	}

	public Pin setMobility(boolean movable) {
		Movable = movable;

		return this;
	}
}