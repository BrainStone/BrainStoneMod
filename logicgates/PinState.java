package mods.brainstone.logicgates;

public enum PinState {
	NotExisting, NotConnected, Unpowered, Powered;

	public boolean isPowered() {
		return this == Powered;
	}

	public boolean isValid() {
		return (this != NotConnected) && (this != NotExisting);
	}
}