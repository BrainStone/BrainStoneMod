package mods.brainstone.logicgates;

public enum PinState {
	NotExisting((byte) -1), NotConnected((byte) -1), Unpowered((byte) 0), Powered(
			(byte) 15),

	// PowerLevels

	Level0((byte) 0), Level1((byte) 1), Level2((byte) 2), Level3((byte) 3), Level4(
			(byte) 4), Level5((byte) 5), Level6((byte) 6), Level7((byte) 7), Level8(
			(byte) 8), Level9((byte) 9), Level10((byte) 10), Level11((byte) 11), Level12(
			(byte) 12), Level13((byte) 13), Level14((byte) 14), Level15(
			(byte) 15);

	private final byte PowerLevel;

	private PinState(byte PowerLevel) {
		this.PowerLevel = PowerLevel;
	}

	public byte getPowerLevel() {
		return PowerLevel;
	}

	public boolean isNotPowered() {
		return PowerLevel == 0;
	}

	public boolean isPowered() {
		return PowerLevel > 0;
	}

	public boolean isValid() {
		return (this != NotConnected) && (this != NotExisting);
	}
}