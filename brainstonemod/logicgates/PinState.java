package brainstonemod.logicgates;

public enum PinState {
	NotExisting((byte) -1), NotConnected((byte) -1), Unpowered((byte) 0), Powered(
			(byte) 15),

	// PowerLevels

	Level0((byte) 0), Level1((byte) 1), Level2((byte) 2), Level3((byte) 3), Level4(
			(byte) 4), Level5((byte) 5), Level6((byte) 6), Level7((byte) 7), Level8(
			(byte) 8), Level9((byte) 9), Level10((byte) 10), Level11((byte) 11), Level12(
			(byte) 12), Level13((byte) 13), Level14((byte) 14), Level15(
			(byte) 15);

	public static PinState getPinState(byte powerLevel) {
		switch (powerLevel) {
		case 15:
			return Level15;
		case 14:
			return Level14;
		case 13:
			return Level13;
		case 12:
			return Level12;
		case 11:
			return Level11;
		case 10:
			return Level10;
		case 9:
			return Level9;
		case 8:
			return Level8;
		case 7:
			return Level7;
		case 6:
			return Level6;
		case 5:
			return Level5;
		case 4:
			return Level4;
		case 3:
			return Level3;
		case 2:
			return Level2;
		case 1:
			return Level1;
		case 0:
			return Level0;
		default:
			return NotConnected;
		}
	}

	private final byte PowerLevel;

	private PinState(byte PowerLevel) {
		this.PowerLevel = PowerLevel;
	}

	public boolean canConnectRedstone() {
		return this != NotExisting;
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

	public boolean shallRender() {
		return this != NotExisting;
	}
}