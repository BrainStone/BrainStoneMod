package brainstonemod.common.tileentity;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.logicgate.Gate;
import brainstonemod.common.logicgate.Pin;
import brainstonemod.common.logicgate.PinState;
import brainstonemod.common.tileentity.template.TileEntityBrainStoneSyncBase;
<<<<<<< HEAD
=======
import brainstonemod.network.BrainStonePacketHelper;
>>>>>>> f3a966d... v2.42.1037 BETA prerelease
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

<<<<<<< HEAD
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
=======
		return "";
	}

	private static final byte getNumGates() {
		byte count;

		for (count = 0; getGateName(count) != ""; count++) {
>>>>>>> f3a966d... v2.42.1037 BETA prerelease
		}

		return -1;
	}

<<<<<<< HEAD
	private final byte GuiFocused;
=======
	private boolean correctConnect;

	private boolean ignoreIncorrectConnect;
	private boolean invertOutput;
	private boolean swapable;
	private byte direction;
	private byte GuiFocused;
	private byte mode;
	private final byte PinState[] = { 0, -1, -1, -1 };
	private byte PinType[];
	private int data;
>>>>>>> f3a966d... v2.42.1037 BETA prerelease
	private long lastUpdate;
	private final Vector<String> TASKS;

	private final Vector<String> Users;
	private ArrayList<String> PrintErrorBuff;
	private boolean PrintErrorBuffActive;

	private Gate ActiveGate;
	private int GatePos;

	public byte currentRenderDirection;

	public TileEntityBlockBrainLogicBlock() {
		TASKS = new Vector<String>();
		lastUpdate = -100L;
		GuiFocused = 0;
		Users = new Vector<String>();
		PrintErrorBuffActive = false;

		ActiveGate = Gate.getGate(Gate.GateNames[0]);
		GatePos = 0;

		ActiveGate.onGateChange(0);
		ActiveGate.onTick();
	}

	public void addTASKS(String s) {
		final Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side == Side.SERVER) {
			// We are on the server side.
			TASKS.add(s);
		} else if (side == Side.CLIENT) {
			// // We are on the client side.
			// final ByteArrayOutputStream bos = new ByteArrayOutputStream(0);
			// final DataOutputStream outputStream = new DataOutputStream(bos);
			//
			// try {
			// outputStream.writeInt(xCoord);
			// outputStream.writeInt(zCoord);
			//
			// outputStream.writeUTF(s);
			// } catch (final IOException e) {
			// BSP.logException(e);
			// }
			//
			// BrainStonePacketHandler.sendPacketToServer("BSM.TEBBLBS", bos);
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

<<<<<<< HEAD
	public boolean canBeMovedByMouse(byte pos) {
		return ActiveGate.Pins[pos].Movable
				&& ActiveGate.Pins[pos].State.isValid();
=======
	public boolean connectToRedstone(int side) {
		return PinType[convertRedstoneDirections(side)] != -1;
>>>>>>> f3a966d... v2.42.1037 BETA prerelease
	}

	private boolean canBlockConnectToGate(World world, int x, int y, int z,
			int direction, Block block) {
		return (block != null)
				&& ((block.canProvidePower()
						&& ((direction < 2) || block
								.canConnectRedstone(
										world,
										x,
										y,
										z,
										MCToInternalDirection((byte) (InternalToMCDirection((byte) direction) ^ 1)) - 2)) && (((block != Blocks.powered_repeater)
						&& (block != Blocks.unpowered_repeater)
						&& (block != Blocks.powered_comparator) && (block != Blocks.unpowered_comparator)) || ((world
						.getBlockMetadata(x, y, z) & 3) == (direction & 3)))) || world
						.getBlock(x, y, z).getMaterial().isSolid());
	}

	public boolean canPinsSwap(int pos1, int pos2) {
		return ActiveGate.canSwapWith(ActiveGate.Pins[pos1],
				ActiveGate.Pins[pos2]);
	}

	public void changeGate(int direction) {
		this.addTASKS("changeGate",
				new String[] { "int", String.valueOf(direction) });
	}

	public void changeGate(String string) {
		this.addTASKS("changeGate", new String[] { "string", string });
	}

	public void changeGate(String string, int direction) {
		this.addTASKS("changeGate",
				new String[] { "both", string, String.valueOf(direction) });
	}

	public boolean connectToRedstone(int side) {
		return ActiveGate.Pins[side + 2].State.canConnectRedstone();
	}

	public void doTASKS() {
<<<<<<< HEAD
		final int i = TASKS.size();
=======
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			final int size = TASKS.size();

			if (size != 0) {
				print((new StringBuilder()).append("doTASKS() (size: ")
						.append(String.valueOf(size)).append(")").toString());

				for (int j = 0; j < size; j++) {
					runTASK(TASKS.get(j));
				}
>>>>>>> f3a966d... v2.42.1037 BETA prerelease

		if (i != 0) {
			print((new StringBuilder()).append("doTASKS() (size: ")
					.append(String.valueOf(i)).append(")").toString());

			for (int j = 0; j < i; j++) {
				runTASK(TASKS.get(j));
			}

			TASKS.clear();
		}

		updateEntity();
	}

	public float getFactorForPinBlue(byte direction) {
		final int color = getGateColor(direction);

		final float blue = (color & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			final float red = ((color >> 16) & 255) / 255.0F;
			final float green = ((color >> 8) & 255) / 255.0F;

			return ((red * 30.0F) + (green * 70.0F)) / 100.0F;
		}

		return blue;
	}

	public float getFactorForPinGreen(byte direction) {
		final int color = getGateColor(direction);

		final float green = ((color >> 8) & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			final float red = ((color >> 16) & 255) / 255.0F;

			return ((red * 30.0F) + (green * 70.0F)) / 100.0F;
		}

		return green;
	}

	public float getFactorForPinRed(byte direction) {
		final int color = getGateColor(direction);

		final float red = ((color >> 16) & 255) / 255.0F;

<<<<<<< HEAD
		if (EntityRenderer.anaglyphEnable) {
			final float green = ((color >> 8) & 255) / 255.0F;
			final float blue = (color & 255) / 255.0F;

			return ((red * 30.0F) + (green * 59.0F) + (blue * 11.0F)) / 100.0F;
=======
	public void drawGates(GuiBrainLogicBlock guibrainlogicblock, int x, int y) {
		for (int k = 0; k < numGates; k++) {
			guibrainlogicblock.drawString(getGateName(k), x, y + (12 * k),
					k != mode ? 0x404040 : 32768);
		}

		guibrainlogicblock.drawSplitString(
				StatCollector.translateToLocal("gui.brainstone.invertOutput"),
				x + 85, y + 50, invertOutput ? 32768 : 0x404040, 80);

		if (!correctConnect && !ignoreIncorrectConnect) {
			guibrainlogicblock.drawSplitString(
					StatCollector.translateToLocal("gui.brainstone.warning1"),
					x + 75, y + 70, 0xa00000, 95);
		} else if (!correctConnect) {
			guibrainlogicblock.drawSplitString(
					StatCollector.translateToLocal("gui.brainstone.warning2"),
					x + 75, y + 70, 0xa05000, 95);
>>>>>>> f3a966d... v2.42.1037 BETA prerelease
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

	public String getGateLetter(byte direction) {
		return String.valueOf(ActiveGate.Pins[direction].Name);
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
		if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this)
			return false;
		else
			return entityplayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D,
					zCoord + 0.5D) <= 64D;
	}

	public void logIn(UUID user) {
		this.addTASKS("logInOut", new String[] { "true", user.toString() });
	}

	public void logOut(UUID user) {
<<<<<<< HEAD
		this.addTASKS("logInOut", new String[] { "false", user.toString() });
=======
		this.addTASK("logInOut", new String[] { "false", user.toString() });
	}

	private void modeUpdated() {
		swapable = true;
		ignoreIncorrectConnect = false;
		invertOutput = false;

		switch (mode) {
		case 0:
		case 1:
		case 2:
			swapable = false;
			Pins = (new String[] { "Q", "B", "A", "C" });
			PinType = (new byte[] { 10, 0, 0, 0 });
			break;

		case 3:
			Pins = (new String[] { "Q", "", "A", "B" });
			PinType = (new byte[] { 10, -1, 1, 1 });
			break;

		case 4:
			ignoreIncorrectConnect = true;
			Pins = (new String[] { "Q", "A", "", "" });
			PinType = (new byte[] { 10, 1, -1, -1 });
			break;

		case 5:
			data = 0;
			ignoreIncorrectConnect = true;
			Pins = (new String[] { "Q", "", "R", "S" });
			PinType = (new byte[] { 10, -1, 1, 1 });
			break;

		case 6:
			data = 0;
			Pins = (new String[] { "Q", "", "D", "C" });
			PinType = (new byte[] { 10, -1, 1, 1 });
			break;

		case 7:
			data = 0;
			Pins = (new String[] { "Q", "T", "", "" });
			PinType = (new byte[] { 10, 1, -1, -1 });
			break;

		case 8:
			data = 0;
			ignoreIncorrectConnect = true;
			Pins = (new String[] { "Q", "", "J", "K" });
			PinType = (new byte[] { 10, -1, 1, 1 });
			break;
		}
	}

	private void modeUpdated(byte mode) {
		this.mode = mode;
		this.modeUpdated();
>>>>>>> f3a966d... v2.42.1037 BETA prerelease
	}

	@Override
	public void onDataPacket(NetworkManager net,
			S35PacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord,
					xCoord, yCoord, zCoord);
		}
	}

	private void print(Object obj) {
		if (!BSP.debug(obj) && PrintErrorBuffActive) {
			PrintErrorBuff.add(obj.toString());
		}
	}

	private void printErrorInrunTASK(String s, String s1, String as[]) {
		print("===================================================");
		print("Error Signature in \"runTASK\":");
		print("===================================================");
		print((new StringBuilder()).append("\tOriginal Task: \"").append(s)
				.append("\"").toString());
		print((new StringBuilder()).append("\tCutted Task:   \"").append(s1)
				.append("\"").toString());
		print("\tParameters:");
		final int i = as.length;

		if (i == 0) {
			print("\t\tNo Parameters!!!");
		} else {
			for (int j = 0; j < i; j++) {
				print((new StringBuilder()).append("\t\tParameter ")
						.append(String.valueOf(j)).append(": \"").append(as[j])
						.append("\"").toString());
			}
		}

		print("===================================================");
		print("!!!\t\t\tEND\t\t\t!!!");
		print("===================================================");
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
<<<<<<< HEAD
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
		GatePos = nbttagcompound.getInteger("GatePos");

		final byte byte0 = nbttagcompound.getByte("TASKS-Size");

		for (byte byte1 = 0; byte1 < byte0; byte1++) {
=======
		direction = nbttagcompound.getByte("Direction");
		mode = nbttagcompound.getByte("Mode");
		GuiFocused = nbttagcompound.getByte("GuiFocused");
		data = nbttagcompound.getInteger("Data");
		invertOutput = nbttagcompound.getBoolean("InvertOutput");
		swapable = nbttagcompound.getBoolean("Swapable");
		correctConnect = nbttagcompound.getBoolean("CorrectConnect");
		ignoreIncorrectConnect = nbttagcompound
				.getBoolean("IgnoreIncorrectConnect");

		for (int i = 0; i < 4; i++) {
			PinState[i] = nbttagcompound.getByte((new StringBuilder())
					.append("State").append(String.valueOf(i)).toString());
			PinType[i] = nbttagcompound.getByte((new StringBuilder())
					.append("Type").append(String.valueOf(i)).toString());
			Pins[i] = nbttagcompound.getString((new StringBuilder())
					.append("Pins").append(String.valueOf(i)).toString());
		}

		final byte size = nbttagcompound.getByte("TASKS-Size");
		TASKS.clear();
		TASKS.ensureCapacity(size);

		for (byte byte1 = 0; byte1 < size; byte1++) {
>>>>>>> f3a966d... v2.42.1037 BETA prerelease
			TASKS.add(
					byte1,
					nbttagcompound.getString((new StringBuilder())
							.append("TASK").append(String.valueOf(byte1))
							.toString()));
		}
	}

<<<<<<< HEAD
	public void renderGate(FontRenderer fontrenderer, byte pos) {
		final Pin pin = ActiveGate.Pins[pos];
=======
	public void renderInOutPut(FontRenderer fontrenderer, byte byte0) {
		byte0 = transformDirection(byte0);
>>>>>>> f3a966d... v2.42.1037 BETA prerelease

		if (pin.State.shallRender()) {
			final String tmp = String.valueOf(pin.Name);

			fontrenderer.drawString(tmp, -fontrenderer.getStringWidth(tmp) / 2,
					4, getGateColor(pos));
		}
	}

	private void runTASK(String s) {
		startPrintErrorBuff();

		String as[] = s.split(" ");
		final String s1 = as[0];
		as = as[1].split(",");

<<<<<<< HEAD
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
			print("!!!\tRun Task: \"logInOut\"\t\t!!!");

			if (as.length == 2) {
=======
		if (s1.equals("swapPosition")) {
			print("!!!\tRun Task: \"swapPosition\"\t\t!!!");

			if (as.length == 2) {
				this.swapPosition(Byte.valueOf(as[0]).byteValue(), Byte
						.valueOf(as[1]).byteValue());
			} else {
				print("!!!\tError: Wrong number of parameters\t!!!\n");
				printErrorInrunTASK(s, s1, as);
				BSP.throwRuntimeException(getPrintErrorBuff()
						+ "Wrong number of parameters");
			}
		} else if (s1.equals("modeUpdated")) {
			print("!!!\tRun Task: \"modeUpdated\"\t\t\t!!!");

			if (as.length == 1) {
				this.modeUpdated(Byte.valueOf(as[0]).byteValue());
			} else {
				print("!!!\tError: Wrong number of parameters\t!!!\n");
				printErrorInrunTASK(s, s1, as);
				BSP.throwRuntimeException(getPrintErrorBuff()
						+ "Wrong number of parameters");
			}
		} else if (s1.equals("setFocused")) {
			print("!!!\tRun Task: \"setFocused\"\t\t\t!!!");

			if (as.length == 1) {
				this.setFocused(Byte.valueOf(as[0]).byteValue());
			} else {
				print("!!!\tError: Wrong number of parameters\t!!!\n");
				printErrorInrunTASK(s, s1, as);
				BSP.throwRuntimeException(getPrintErrorBuff()
						+ "Wrong number of parameters");
			}
		} else if (s1.equals("setInvertOutput")) {
			print("!!!\tRun Task: \"setInvertOutput\"\t\t!!!");

			if (as.length == 1) {
				setInvertOutput(Boolean.valueOf(as[0]).booleanValue());
			} else {
				print("!!!\tError: Wrong number of parameters\t!!!\n");
				printErrorInrunTASK(s, s1, as);
				BSP.throwRuntimeException(getPrintErrorBuff()
						+ "Wrong number of parameters");
			}
		} else if (s1.equals("logInOut")) {
			print("!!!\tRun Task: \"logInOut\"\t\t!!!");

			if (as.length == 2) {
				final UUID player = UUID.fromString(as[1]);

>>>>>>> f3a966d... v2.42.1037 BETA prerelease
				if (Boolean.valueOf(as[0]).booleanValue()) {
					if (!Users.contains(as[1])) {
						Users.add(as[1]);
					} else {
<<<<<<< HEAD
						print("User \"" + as[1] + "\" is already logged in!");
=======
						print("User with UUID \"" + as[1]
								+ "\" is already logged in!");
>>>>>>> f3a966d... v2.42.1037 BETA prerelease
					}
				} else {
					if (Users.contains(as[1])) {
						Users.remove(as[1]);
					} else {
<<<<<<< HEAD
						print("User \"" + as[1] + "\" is already logged out!");
=======
						print("User with UUID \"" + as[1]
								+ "\" is already logged out!");
>>>>>>> f3a966d... v2.42.1037 BETA prerelease
					}
				}
			} else {
				print("!!!\tError: Wrong number of parameters\t!!!\n");
				printErrorInrunTASK(s, s1, as);
				BSP.throwRuntimeException(getPrintErrorBuff()
						+ "Wrong number of parameters");
			}
<<<<<<< HEAD
		} else if (s1.equals("changeGate")) {
			print("!!!\tRun Task: \"changeGate\"\t\t!!!");
=======
		} else {
			print("!!!\t\tInvalid Task\t\t\t!!!\n");
			printErrorInrunTASK(s, s1, as);
			BSP.throwRuntimeException(getPrintErrorBuff() + "Invalid Task");
		}

		print("!!!\t\tTask Finished\t\t\t!!!\n");

		stopPrintErrorBuff();
	}

	public void setDirection(byte byte0) {
		if ((byte0 >= 0) && (byte0 < 4)) {
			direction = byte0;
		}
	}

	public void setDirection(int i) {
		this.setDirection((byte) i);
	}

	public void setFocused(byte pin) {
		final Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side == Side.SERVER) {
			// We are on the server side.

			if ((pin >= 0) && (pin < 4)) {
				GuiFocused = pin;
			}
		} else if (side == Side.CLIENT) {
			// We are on the client side.

			this.addTASK("setFocused", new String[] { String.valueOf(pin) });
		} else {
			// We are on the Bukkit server. Bukkit is a server mod used for
			// security.
		}
	}

	public void setFocused(int i) {
		this.setFocused((byte) i);
	}

	private void setInvertOutput(boolean flag) {
		invertOutput = flag;
	}

	public void setMode(byte byte0) {
		this.addTASK((new StringBuilder()).append("modeUpdated ")
				.append(String.valueOf(byte0)).toString());
	}

	private byte setOutput() {
		boolean flag = false;
		correctConnect = true;

		for (int i = 1; i < 4; i++) {
			correctConnect = correctConnect
					&& ((PinType[i] != 1) || ((PinType[i] == 1) && (PinState[i] != -1)));
		}

		correctConnect = correctConnect
				&& ((PinState[1] != -1) || (PinState[2] != -1) || (PinState[3] != -1));

		if (correctConnect || ignoreIncorrectConnect) {
			final ArrayList arraylist = new ArrayList();
			final HashMap hashmap = new HashMap();

			for (int j = 1; j < 4; j++) {
				if ((PinState[j] != -1) && (PinType[j] != -1)) {
					boolean flag1;
					arraylist.add(Boolean.valueOf(flag1 = PinState[j] == 1));
					hashmap.put(Pins[j], Boolean.valueOf(flag1));
				}

				if ((PinState[j] == -1) && ignoreIncorrectConnect
						&& !hashmap.containsKey(Pins[j])) {
					hashmap.put(Pins[j], Boolean.valueOf(PinState[j] == 1));
				}
			}

			final int k = arraylist.size();

			switch (mode) {
			default:
				break;

			case 0:
				flag = k != 0;
>>>>>>> f3a966d... v2.42.1037 BETA prerelease

			if (as.length == 2) {
				if (as[0].equals("string")) {
					ActiveGate = Gate.getGate(as[1]);

					if (ActiveGate != null) {
						for (int i = 0; i < Gate.NumberGates; i++) {
							if (Gate.GateNames[i].equals(as[1])) {
								GatePos = i;

								break;
							}
						}
					} else {
						GatePos = -1;
					}
				} else if (as[0].equals("int")) {
					ActiveGate.onGateChange(Integer.valueOf(as[1]));
					ActiveGate.onTick();
				}
			} else if (as.length == 3) {
				if (as[0].equals("both")) {
					ActiveGate = Gate.getGate(as[1]);

					if (ActiveGate != null) {
						for (int i = 0; i < Gate.NumberGates; i++) {
							if (Gate.GateNames[i].equals(as[1])) {
								GatePos = i;

								break;
							}
						}
					} else {
						GatePos = -1;
					}

					ActiveGate.onGateChange(Integer.valueOf(as[2]));
					ActiveGate.onTick();
				}
			} else {
				print("!!!\tError: Wrong number of parameters\t!!!\n");
				printErrorInrunTASK(s, s1, as);
				BSP.throwRuntimeException(getPrintErrorBuff()
						+ "Wrong number of parameters");
			}
		} else {
			print("!!!\t\tInvalid Task\t\t\t!!!\n");
			printErrorInrunTASK(s, s1, as);
			BSP.throwRuntimeException(getPrintErrorBuff() + "Invalid Task");
		}

<<<<<<< HEAD
		print("!!!\t\tTask Finished\t\t\t!!!\n");
=======
		return (byte) (flag ^ invertOutput ? 1 : 0);
	}

	public boolean setPinState(byte pinStates[]) {
		final byte[] tmpPinState = new byte[4];
		System.arraycopy(PinState, 0, tmpPinState, 0, 4);

		if (pinStates.length == 3) {
			System.arraycopy(pinStates, 0, PinState, 1, 3);
			PinState[0] = setOutput();
		}

		final Byte tmp;
>>>>>>> f3a966d... v2.42.1037 BETA prerelease

		stopPrintErrorBuff();
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

<<<<<<< HEAD
	public void tickGate(World world, int x, int y, int z, long time) {
		final Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side != Side.CLIENT) {
			time /= 2;

			if ((time % ActiveGate.getTickRate()) == 0) {
				final int xShift[] = new int[] { 0, 0, 0, 1, 0, -1 };
				final int yShift[] = new int[] { 1, -1, 0, 0, 0, 0 };
				final int zShift[] = new int[] { 0, 0, -1, 0, 1, 0 };
				int tmpX, tmpY, tmpZ;
				Block block;

				for (int i = 0; i < 6; i++) {
					if (ActiveGate.Pins[i].State.canConnectRedstone()
							&& !ActiveGate.Pins[i].Output) {
						tmpX = x + xShift[i];
						tmpY = y + yShift[i];
						tmpZ = z + zShift[i];

						block = world.getBlock(tmpX, tmpY, tmpZ);

						if (canBlockConnectToGate(world, tmpX, tmpY, tmpZ, i,
								block)) {
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
=======
	private void swapPosition(byte byte0, byte byte1) {
		if ((byte0 > 0) && (byte0 < 4) && (byte1 > 0) && (byte1 < 4)
				&& (byte0 != byte1) && swapable) {
			final byte byte2 = PinType[byte0];
			final String s = Pins[byte0];
			PinType[byte0] = PinType[byte1];
			Pins[byte0] = Pins[byte1];
			PinType[byte1] = byte2;
			Pins[byte1] = s;
			GuiFocused = byte1;
		}
	}

	public void swapPosition(int firstPos, int secondPos) {
		this.addTASK((new StringBuilder()).append("swapPosition ")
				.append(String.valueOf((byte) firstPos)).append(",")
				.append(String.valueOf((byte) secondPos)).toString());
	}

	public byte transformDirection(int i) {
		switch (direction) {
		default:
			break;

		case 1:
			switch (i) {
			case 0:
				i = 2;
				break;

			case 1:
				i = 3;
				break;

			case 2:
				i = 1;
				break;

			case 3:
				i = 0;
				break;
			}

			break;

		case 2:
			switch (i) {
			case 0:
				i = 1;
				break;

			case 1:
				i = 0;
				break;

			case 2:
				i = 3;
				break;

			case 3:
				i = 2;
				break;
			}

			break;
>>>>>>> f3a966d... v2.42.1037 BETA prerelease

				ActiveGate.onTick();
			}
		}
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
<<<<<<< HEAD
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
		nbttagcompound.setInteger("GatePos", GatePos);

		final byte byte0 = (byte) TASKS.size();
		nbttagcompound.setByte("TASKS-Size", byte0);

		for (byte byte1 = 0; byte1 < byte0; byte1++) {
=======
		nbttagcompound.setByte("Direction", direction);
		nbttagcompound.setByte("Mode", mode);
		nbttagcompound.setByte("GuiFocused", GuiFocused);
		nbttagcompound.setInteger("Data", data);
		nbttagcompound.setBoolean("InvertOutput", invertOutput);
		nbttagcompound.setBoolean("Swapable", swapable);
		nbttagcompound.setBoolean("CorrectConnect", correctConnect);
		nbttagcompound.setBoolean("IgnoreIncorrectConnect",
				ignoreIncorrectConnect);

		for (int i = 0; i < 4; i++) {
			nbttagcompound.setByte((new StringBuilder()).append("State")
					.append(String.valueOf(i)).toString(), PinState[i]);
			nbttagcompound.setByte(
					(new StringBuilder()).append("Type")
							.append(String.valueOf(i)).toString(), PinType[i]);
			nbttagcompound.setString((new StringBuilder()).append("Pins")
					.append(String.valueOf(i)).toString(), Pins[i]);
		}

		final byte size = (byte) TASKS.size();
		nbttagcompound.setByte("TASKS-Size", size);

		for (byte byte1 = 0; byte1 < size; byte1++) {
>>>>>>> f3a966d... v2.42.1037 BETA prerelease
			nbttagcompound
					.setString(
							(new StringBuilder()).append("TASK")
									.append(String.valueOf(byte1)).toString(),
							TASKS.get(byte1));
		}
	}
}