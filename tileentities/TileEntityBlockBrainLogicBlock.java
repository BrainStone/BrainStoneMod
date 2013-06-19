package mods.brainstone.tileentities;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import mods.brainstone.handlers.BrainStonePacketHandler;
import mods.brainstone.logicgates.Gate;
import mods.brainstone.logicgates.Pin;
import mods.brainstone.logicgates.PinState;
import mods.brainstone.templates.BSP;
import mods.brainstone.templates.TileEntityBrainStoneSyncBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityBlockBrainLogicBlock extends
		TileEntityBrainStoneSyncBase {

	@SideOnly(Side.CLIENT)
	public static byte guiDirection;

	public static int getColorForPowerLevel(byte powerLevel) {
		final float ratio = powerLevel / 15.0F;
		return (powerLevel != -1.0F) ? ((((int) (0xC6 * ratio)) << 16)
				+ (((int) (0x05 * ratio)) << 8) + ((int) (0x05 * ratio)))
				: 0x888888;
	}

	public static String getInvName() {
		return "container.brainstonetrigger";
	}

	public static byte InternalToMCDirection(byte Internal_Direction) {
		switch (Internal_Direction) {
		case 0:
		case 1:
			return (byte) (Internal_Direction ^ 1);
		case 2:
			return Internal_Direction;
		case 3:
			return (byte) (Internal_Direction + 2);
		case 4:
		case 5:
			return (byte) (Internal_Direction - 1);
		}

		return -1;
	}

	public static byte MCToInternalDirection(byte MC_Direction) {
		switch (MC_Direction) {
		case 0:
		case 1:
			return (byte) (MC_Direction ^ 1);
		case 2:
			return MC_Direction;
		case 3:
		case 4:
			return (byte) (MC_Direction + 1);
		case 5:
			return (byte) (MC_Direction - 2);
		}

		return -1;
	}

	private byte GuiFocused;
	private long lastUpdate;
	private final Vector TASKS;

	private final Vector Users;
	private ArrayList<String> PrintErrorBuff;

	private boolean PrintErrorBuffActive;

	private Gate ActiveGate;
	private int GatePos;

	public byte currentRenderDirection;

	public TileEntityBlockBrainLogicBlock() {
		TASKS = new Vector();
		lastUpdate = -100L;
		GuiFocused = 0;
		Users = new Vector();
		PrintErrorBuffActive = false;

		this.changeGate("AND_Gate", 0);
	}

	public void addTASKS(String s) {
		final Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side == Side.SERVER) {
			// We are on the server side.
			TASKS.add(s);
		} else if (side == Side.CLIENT) {
			// We are on the client side.
			final ByteArrayOutputStream bos = new ByteArrayOutputStream(0);
			final DataOutputStream outputStream = new DataOutputStream(bos);

			try {
				outputStream.writeInt(xCoord);
				outputStream.writeInt(yCoord);
				outputStream.writeInt(zCoord);

				outputStream.writeUTF(s);
			} catch (final IOException e) {
				BSP.printException(e);
			}

			BrainStonePacketHandler.sendPacketToServer("BSM.TEBBLBS", bos);
		} else {
			// We are on the Bukkit server. Bukkit is a server mod used for
			// security.
		}
	}

	public void addTASKS(String s, String as[]) {
		String s1 = (new StringBuilder()).append(s).append(" ").toString();
		final int i = as.length - 1;

		for (int j = 0; j < i; j++) {
			s1 = (new StringBuilder()).append(s1).append(as[j]).append(",")
					.toString();
		}

		s1 = (new StringBuilder()).append(s1).append(as[i]).toString();

		this.addTASKS(s1);
	}

	private boolean canBlockConnectToGate(World world, int x, int y, int z,
			int direction, Block block, int blockId) {
		return (block != null)
				&& ((block.canProvidePower()
						&& ((direction < 2) || block
								.canConnectRedstone(
										world,
										x,
										y,
										z,
										MCToInternalDirection((byte) (InternalToMCDirection((byte) direction) ^ 1)) - 2)) && (((blockId != 93)
						&& (blockId != 149) && (blockId != 150)) || ((world
						.getBlockMetadata(x, y, z) & 3) == (direction & 3)))) || world
						.getBlockMaterial(x, y, z).isSolid());
	}

	public void changeGate(int direction) {
		ActiveGate.onGateChange(direction);
		ActiveGate.onTick();
	}

	public void changeGate(String string) {
		ActiveGate = Gate.getGate(string);

		if (ActiveGate != null) {
			for (int i = 0; i < Gate.NumberGates; i++) {
				if (Gate.GateNames[i].equals(string)) {
					GatePos = i;

					return;
				}
			}
		}

		GatePos = -1;
	}

	public void changeGate(String string, int direction) {
		this.changeGate(string);
		this.changeGate(direction);
	}

	public boolean connectToRedstone(int side) {
		return ActiveGate.Pins[side + 2].State.canConnectRedstone();
	}

	public void doTASKS() {
		final int i = TASKS.size();

		if (i != 0) {
			this.print((new StringBuilder()).append("doTASKS() (size: ")
					.append(String.valueOf(i)).append(")").toString());

			for (int j = 0; j < i; j++) {
				this.runTASK((String) TASKS.get(j));
			}

			TASKS.clear();
		}

		try {
			this.update(false);
		} catch (final IOException e) {
			BSP.printException(e);
		}
	}

	@Override
	protected void generateOutputStream(DataOutputStream outputStream)
			throws IOException {
		outputStream.writeInt(xCoord);
		outputStream.writeInt(yCoord);
		outputStream.writeInt(zCoord);

		outputStream.writeByte(GuiFocused);
		outputStream.writeLong(lastUpdate);

		ActiveGate.writeToOutputStream(outputStream);
	}

	public float getFactorForPinBlue(byte direction) {
		final int color = this.getGateColor(direction);

		final float blue = (color & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			final float red = ((color >> 16) & 255) / 255.0F;
			final float green = ((color >> 8) & 255) / 255.0F;

			return ((red * 30.0F) + (green * 70.0F)) / 100.0F;
		}

		return blue;
	}

	public float getFactorForPinGreen(byte direction) {
		final int color = this.getGateColor(direction);

		final float green = ((color >> 8) & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			final float red = ((color >> 16) & 255) / 255.0F;

			return ((red * 30.0F) + (green * 70.0F)) / 100.0F;
		}

		return green;
	}

	public float getFactorForPinRed(byte direction) {
		final int color = this.getGateColor(direction);

		final float red = ((color >> 16) & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			final float green = ((color >> 8) & 255) / 255.0F;
			final float blue = (color & 255) / 255.0F;

			return ((red * 30.0F) + (green * 59.0F) + (blue * 11.0F)) / 100.0F;
		}

		return red;
	}

	public byte getFocused() {
		return GuiFocused;
	}

	public int getGateColor(byte direction) {
		return getColorForPowerLevel(ActiveGate.Pins[direction].State
				.getPowerLevel());
	}

	public int getGatePos() {
		return GatePos;
	}

	public byte getPowerLevel(byte pos) {
		return ActiveGate.Pins[pos].State.getPowerLevel();
	}

	public byte getPowerLevel(int pos) {
		return this.getPowerLevel((byte) pos);
	}

	public byte getPowerOutputLevel(byte MC_Direction) {
		final Pin pin = ActiveGate.Pins[MCToInternalDirection(MC_Direction)];

		return pin.Output ? pin.State.getPowerLevel() : 0;
	}

	private String getPrintErrorBuff() {
		if (!PrintErrorBuffActive)
			return "";

		String tmp = "";
		final int size = PrintErrorBuff.size();

		for (int i = 0; i < size; i++) {
			tmp += PrintErrorBuff.get(i) + "\n";
		}

		tmp += "\n";

		return tmp;
	}

	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
			return false;
		else
			return entityplayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D,
					zCoord + 0.5D) <= 64D;
	}

	public void logIn(String user) {
		this.addTASKS("logInOut", new String[] { "true", user });
	}

	public void logOut(String user) {
		this.addTASKS("logInOut", new String[] { "false", user });
	}

	private void print(Object obj) {
		if (!BSP.finest(obj) && PrintErrorBuffActive) {
			PrintErrorBuff.add(obj.toString());
		}
	}

	private void printErrorInrunTASK(String s, String s1, String as[]) {
		this.print("===================================================");
		this.print("Error Signature in \"runTASK\":");
		this.print("===================================================");
		this.print((new StringBuilder()).append("\tOriginal Task: \"")
				.append(s).append("\"").toString());
		this.print((new StringBuilder()).append("\tCutted Task:   \"")
				.append(s1).append("\"").toString());
		this.print("\tParameters:");
		final int i = as.length;

		if (i == 0) {
			this.print("\t\tNo Parameters!!!");
		} else {
			for (int j = 0; j < i; j++) {
				this.print((new StringBuilder()).append("\t\tParameter ")
						.append(String.valueOf(j)).append(": \"").append(as[j])
						.append("\"").toString());
			}
		}

		this.print("===================================================");
		this.print("!!!\t\t\tEND\t\t\t!!!");
		this.print("===================================================");
	}

	@Override
	public void readFromInputStream(DataInputStream inputStream)
			throws IOException {
		GuiFocused = inputStream.readByte();
		lastUpdate = inputStream.readLong();

		ActiveGate = Gate.readFromInputStream(inputStream);
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		// direction = nbttagcompound.getByte("Direction");
		// mode = nbttagcompound.getByte("Mode");
		// data = nbttagcompound.getInteger("Data");
		// invertOutput = nbttagcompound.getBoolean("InvertOutput");
		// swapable = nbttagcompound.getBoolean("Swapable");
		//
		// for (int i = 0; i < 4; i++) {
		// PinState[i] = nbttagcompound.getByte((new StringBuilder())
		// .append("State").append(String.valueOf(i)).toString());
		// PinType[i] = nbttagcompound.getByte((new StringBuilder())
		// .append("Type").append(String.valueOf(i)).toString());
		// Pins[i] = nbttagcompound.getString((new StringBuilder())
		// .append("Pins").append(String.valueOf(i)).toString());
		// }

		ActiveGate = Gate.readFromNBT(nbttagcompound);

		final byte byte0 = nbttagcompound.getByte("TASKS-Size");

		for (byte byte1 = 0; byte1 < byte0; byte1++) {
			TASKS.add(
					byte1,
					nbttagcompound.getString((new StringBuilder())
							.append("TASK").append(String.valueOf(byte1))
							.toString()));
		}
	}

	public void renderGate(FontRenderer fontrenderer, byte pos) {
		final Pin pin = ActiveGate.Pins[pos];

		if (pin.State.shallRender()) {
			final String tmp = String.valueOf(pin.Name);
			final float powerLevel = pin.State.getPowerLevel();

			fontrenderer.drawString(tmp, -fontrenderer.getStringWidth(tmp) / 2,
					4, this.getGateColor(pos));
		}
	}

	private void runTASK(String s) {
		this.startPrintErrorBuff();

		String as[] = s.split(" ");
		final String s1 = as[0];
		as = as[1].split(",");

		// if (s1.equals("swapPosition")) {
		// this.print("!!!\tRun Task: \"swapPosition\"\t\t!!!");
		//
		// if (as.length == 2) {
		// this.swapPosition(Byte.valueOf(as[0]).byteValue(), Byte
		// .valueOf(as[1]).byteValue());
		// } else {
		// this.print("!!!\tError: Wrong number of parameters\t!!!\n");
		// this.printErrorInrunTASK(s, s1, as);
		// BSP.throwRuntimeException(this.getPrintErrorBuff()
		// + "Wrong number of parameters");
		// }
		// } else
		if (s1.equals("logInOut")) {
			this.print("!!!\tRun Task: \"logInOut\"\t\t!!!");

			if (as.length == 2) {
				if (Boolean.valueOf(as[0]).booleanValue()) {
					if (!Users.contains(as[1])) {
						Users.add(as[1]);
					} else {
						this.print("User \"" + as[1]
								+ "\" is already logged in!");
					}
				} else {
					if (Users.contains(as[1])) {
						Users.remove(as[1]);
					} else {
						this.print("User \"" + as[1]
								+ "\" is already logged out!");
					}
				}
			} else {
				this.print("!!!\tError: Wrong number of parameters\t!!!\n");
				this.printErrorInrunTASK(s, s1, as);
				BSP.throwRuntimeException(this.getPrintErrorBuff()
						+ "Wrong number of parameters");
			}
		} else {
			this.print("!!!\t\tInvalid Task\t\t\t!!!\n");
			this.printErrorInrunTASK(s, s1, as);
			BSP.throwRuntimeException(this.getPrintErrorBuff() + "Invalid Task");
		}

		this.print("!!!\t\tTask Finished\t\t\t!!!\n");

		this.stopPrintErrorBuff();
	}

	public boolean shallDoUpdate(long l) {
		if (l > (lastUpdate + 1)) {
			lastUpdate = l;
			return true;
		} else
			return false;
	}

	public boolean shallRenderPin(byte direction) {
		return ActiveGate.Pins[direction].State.shallRender();
	}

	private void startPrintErrorBuff() {
		PrintErrorBuffActive = true;
		PrintErrorBuff = new ArrayList<String>();
	}

	private void stopPrintErrorBuff() {
		PrintErrorBuffActive = false;
		PrintErrorBuff = null;
	}

	public void tickGate(World world, int x, int y, int z, long time) {
		final Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side != Side.CLIENT) {
			time /= 2;

			if ((time % ActiveGate.getTickRate()) == 0) {
				final int xShift[] = new int[] { 0, 0, 0, 1, 0, -1 };
				final int yShift[] = new int[] { 1, -1, 0, 0, 0, 0 };
				final int zShift[] = new int[] { 0, 0, -1, 0, 1, 0 };
				int tmpX, tmpY, tmpZ, blockId;
				Block block;

				for (int i = 0; i < 6; i++) {
					if (ActiveGate.Pins[i].State.canConnectRedstone()
							&& !ActiveGate.Pins[i].Output) {
						tmpX = x + xShift[i];
						tmpY = y + yShift[i];
						tmpZ = z + zShift[i];

						blockId = world.getBlockId(tmpX, tmpY, tmpZ);
						block = Block.blocksList[blockId];

						if (this.canBlockConnectToGate(world, tmpX, tmpY, tmpZ,
								i, block, blockId)) {
							if (block instanceof BlockRedstoneWire) {
								ActiveGate.Pins[i].State = PinState
										.getPinState((byte) world
												.getBlockMetadata(tmpX, tmpY,
														tmpZ));
							} else {
								ActiveGate.Pins[i].State = PinState
										.getPinState((byte) world
												.getIndirectPowerLevelTo(
														tmpX,
														tmpY,
														tmpZ,
														InternalToMCDirection((byte) i) ^ 1));
							}
						} else {
							ActiveGate.Pins[i].State = PinState.NotConnected;
						}
					}
				}

				ActiveGate.onTick();
			}
		}
	}

	@Override
	public void update(boolean sendToServer) throws IOException {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(0);
		final DataOutputStream outputStream = new DataOutputStream(bos);

		this.generateOutputStream(outputStream);

		if (sendToServer) {
			BrainStonePacketHandler.sendPacketToServer("BSM.TEBBLBS", bos);
		} else {
			BrainStonePacketHandler.sendPacketToClosestPlayers(this,
					"BSM.TEBBLBC", bos);
		}
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		// nbttagcompound.setByte("Direction", direction);
		// nbttagcompound.setByte("Mode", mode);
		// nbttagcompound.setInteger("Data", data);
		// nbttagcompound.setBoolean("InvertOutput", invertOutput);
		// nbttagcompound.setBoolean("Swapable", swapable);
		//
		// for (int i = 0; i < 4; i++) {
		// nbttagcompound.setByte((new StringBuilder()).append("State")
		// .append(String.valueOf(i)).toString(), PinState[i]);
		// nbttagcompound.setByte(
		// (new StringBuilder()).append("Type")
		// .append(String.valueOf(i)).toString(), PinType[i]);
		// nbttagcompound.setString((new StringBuilder()).append("Pins")
		// .append(String.valueOf(i)).toString(), Pins[i]);
		// }

		ActiveGate.writeToNBT(nbttagcompound);

		final byte byte0 = (byte) TASKS.size();
		nbttagcompound.setByte("TASKS-Size", byte0);

		for (byte byte1 = 0; byte1 < byte0; byte1++) {
			nbttagcompound.setString((new StringBuilder()).append("TASK")
					.append(String.valueOf(byte1)).toString(),
					(String) TASKS.get(byte1));
		}
	}
}