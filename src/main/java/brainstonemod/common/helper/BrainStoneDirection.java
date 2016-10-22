package brainstonemod.common.helper;

import java.util.HashMap;

import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public enum BrainStoneDirection {
	/** +Y */
	UP(),

	/** -Y */
	DOWN(),

	/** -Z */
	NORTH(),

	/** +X */
	EAST(),

	/** +Z */
	SOUTH(),

	/** -X */
	WEST();

	// opposite mapping
	private static final HashMap<BrainStoneDirection, BrainStoneDirection> oppositeMap = new HashMap<>(
            6);
	static {
		oppositeMap.put(UP, DOWN);
		oppositeMap.put(DOWN, UP);
		oppositeMap.put(NORTH, SOUTH);
		oppositeMap.put(EAST, WEST);
		oppositeMap.put(SOUTH, NORTH);
		oppositeMap.put(WEST, EAST);
	}

	// reorintated North mapping
	private static final HashMap<BrainStoneDirection, HashMap<BrainStoneDirection, BrainStoneDirection>> reorintateNorthMap = new HashMap<>(
            4);
	static {
		HashMap<BrainStoneDirection, BrainStoneDirection> tmpMap;

		// New orientations for NORTH as the new North
		tmpMap = new HashMap<>(4);

		tmpMap.put(NORTH, NORTH);
		tmpMap.put(EAST, EAST);
		tmpMap.put(SOUTH, SOUTH);
		tmpMap.put(WEST, WEST);

		reorintateNorthMap.put(NORTH, tmpMap);

		// New orientations for EAST as the new North
		tmpMap = new HashMap<>(4);

		tmpMap.put(NORTH, EAST);
		tmpMap.put(EAST, SOUTH);
		tmpMap.put(SOUTH, WEST);
		tmpMap.put(WEST, NORTH);

		reorintateNorthMap.put(EAST, tmpMap);

		// New orientations for SOUTH as the new North
		tmpMap = new HashMap<>(4);

		tmpMap.put(NORTH, SOUTH);
		tmpMap.put(EAST, WEST);
		tmpMap.put(SOUTH, NORTH);
		tmpMap.put(WEST, EAST);

		reorintateNorthMap.put(SOUTH, tmpMap);

		// New orientations for WEST as the new North
		tmpMap = new HashMap<>(4);

		tmpMap.put(NORTH, WEST);
		tmpMap.put(EAST, NORTH);
		tmpMap.put(SOUTH, EAST);
		tmpMap.put(WEST, SOUTH);

		reorintateNorthMap.put(WEST, tmpMap);
	}
	// Create the bidirectional mapping for the array indices
	private static final HashBiMap<Integer, BrainStoneDirection> fromArrayIndexMap = HashBiMap
			.create(6);
	static {
		fromArrayIndexMap.put(0, UP);
		fromArrayIndexMap.put(1, DOWN);
		fromArrayIndexMap.put(2, NORTH);
		fromArrayIndexMap.put(3, EAST);
		fromArrayIndexMap.put(4, SOUTH);
		fromArrayIndexMap.put(5, WEST);
	}
	private static final BiMap<BrainStoneDirection, Integer> toArrayIndexMap = fromArrayIndexMap
			.inverse();

	// Create the bidirectional mapping for the ForgeDirections
	private static final BiMap<ForgeDirection, BrainStoneDirection> fromForgeDirectionMap = HashBiMap
			.create(6);
	static {
		fromForgeDirectionMap.put(ForgeDirection.UP, UP);
		fromForgeDirectionMap.put(ForgeDirection.DOWN, DOWN);
		fromForgeDirectionMap.put(ForgeDirection.NORTH, NORTH);
		fromForgeDirectionMap.put(ForgeDirection.SOUTH, SOUTH);
		fromForgeDirectionMap.put(ForgeDirection.WEST, WEST);
		fromForgeDirectionMap.put(ForgeDirection.EAST, EAST);
	}
	private static final BiMap<BrainStoneDirection, ForgeDirection> toForgeDirectionMap = fromForgeDirectionMap
			.inverse();

	// Create the bidirectional mapping for the redstone connect indices
	private static final BiMap<Integer, BrainStoneDirection> fromRedstoneConnectIndexMap = HashBiMap
			.create(5);
	static {
		fromRedstoneConnectIndexMap.put(-1, UP);
		fromRedstoneConnectIndexMap.put(0, NORTH);
		fromRedstoneConnectIndexMap.put(1, EAST);
		fromRedstoneConnectIndexMap.put(2, SOUTH);
		fromRedstoneConnectIndexMap.put(3, WEST);
	}
	private static final BiMap<BrainStoneDirection, Integer> toRedstoneConnectIndexMap = fromRedstoneConnectIndexMap
			.inverse();

	// Create the bidirectional mapping for the texture directions
	private static final BiMap<Integer, BrainStoneDirection> fromTextureDirectionMap = HashBiMap
			.create(5);
	static {
		fromTextureDirectionMap.put(0, DOWN);
		fromTextureDirectionMap.put(1, UP);
		fromTextureDirectionMap.put(2, NORTH);
		fromTextureDirectionMap.put(3, SOUTH);
		fromTextureDirectionMap.put(4, WEST);
		fromTextureDirectionMap.put(5, EAST);
	}
	private static final BiMap<BrainStoneDirection, Integer> toTextureDirectionMap = fromTextureDirectionMap
			.inverse();

	public static BrainStoneDirection fromArrayIndex(int index) {
		assert fromArrayIndexMap.containsKey(index);

		return fromArrayIndexMap.get(index);
	}

	public static BrainStoneDirection fromForgeDirection(
			ForgeDirection forgeDirection) {
		assert fromForgeDirectionMap.containsKey(forgeDirection);

		return fromForgeDirectionMap.get(forgeDirection);
	}

	public static BrainStoneDirection fromPlayerYaw(float yaw) {
		yaw = ((yaw % 360.0f) + 360.0f) % 360.0f;

		assert (yaw >= 0.0f) && (yaw < 360.0f);

		if ((yaw > 135.0f) && (yaw <= 225.0f))
			return NORTH;
		else if ((yaw > 225.0f) && (yaw <= 315.0f))
			return EAST;
		else if ((yaw > 315.0f) || (yaw <= 45.0f))
			return SOUTH;
		else if ((yaw > 45.0f) && (yaw <= 135.0f))
			return WEST;

		BSP.throwIllegalArgumentException("The passed player yaw ("
				+ yaw
				+ ") is out of the valid range. This is technically impossible. If this happens report it immedeatly!");

		return null;
	}

	public static BrainStoneDirection fromRedstoneConnectIndex(int index) {
		assert fromRedstoneConnectIndexMap.containsKey(index);

		return fromRedstoneConnectIndexMap.get(index);
	}

	public static BrainStoneDirection fromTextureDirection(int side) {
		assert fromTextureDirectionMap.containsKey(side);

		return fromTextureDirectionMap.get(side);
	}

	BrainStoneDirection() {
	}

	public BrainStoneDirection getOpposite() {
		assert oppositeMap.containsKey(this);

		return oppositeMap.get(this);
	}

	public BrainStoneDirection reorintateNorth(BrainStoneDirection newNorth) {
		assert reorintateNorthMap.containsKey(newNorth);
		assert reorintateNorthMap.get(newNorth).containsKey(this);

		return reorintateNorthMap.get(newNorth).get(this);
	}

	public int toArrayIndex() {
		assert toArrayIndexMap.containsKey(this);

		return toArrayIndexMap.get(this);
	}

	public ForgeDirection toForgeDirection() {
		assert toForgeDirectionMap.containsKey(this);

		return toForgeDirectionMap.get(this);
	}

	public int toRedstoneConnectIndex() {
		assert toRedstoneConnectIndexMap.containsKey(this);

		return toRedstoneConnectIndexMap.get(this);
	}

	public int toTextureDirection() {
		assert toTextureDirectionMap.containsKey(this);

		return toTextureDirectionMap.get(this);
	}
}