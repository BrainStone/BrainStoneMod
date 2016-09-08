package brainstonemod.common.tileentity;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.World;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.helper.BrainStoneDirection;
import brainstonemod.common.logicgate.Gate;
import brainstonemod.common.logicgate.Pin;
import brainstonemod.common.logicgate.PinState;
import brainstonemod.common.tileentity.template.TileEntityBrainStoneSyncBase;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityBlockBrainLogicBlock extends
		TileEntityBrainStoneSyncBase {
	public static BrainStoneDirection guiDirection;

	public static int getColorForPowerLevel(byte powerLevel) {
		final float ratio = powerLevel / 15.0F;
		return (powerLevel != -1.0F) ? ((((int) (0xC6 * ratio)) << 16)
				+ (((int) (0x05 * ratio)) << 8) + ((int) (0x05 * ratio)))
				: 0x888888;
	}

	private final byte GuiFocused;
	private long lastUpdate;
	private final Vector<String> TASKS;

	private final Vector<String> Users;
	private ArrayList<String> PrintErrorBuff;
	private boolean PrintErrorBuffActive;

	private Gate ActiveGate;
	private int GatePos;

	public BrainStoneDirection currentRenderDirection;

	public TileEntityBlockBrainLogicBlock() {
		TASKS = new Vector<String>();
		lastUpdate = -100L;
		GuiFocused = 0;
		Users = new Vector<String>();
		PrintErrorBuffActive = false;

		ActiveGate = Gate.getGate(Gate.GateNames[0]);
		GatePos = 0;

		ActiveGate.onGateChange(BrainStoneDirection.NORTH);
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

	public boolean canBeMovedByMouse(BrainStoneDirection direction) {
		return ActiveGate.Pins[direction.toArrayIndex()].Movable
				&& ActiveGate.Pins[direction.toArrayIndex()].State.isValid();
	}

	private boolean canBlockConnectToGate(World world, int x, int y, int z,
			BrainStoneDirection direction, Block block) {
		final int side = direction.toArrayIndex();

		return (block != null)
				&& (block.getMaterial().isSolid() || ((side > 1) && block
						.canConnectRedstone(world, x, y, z, side - 2)));
	}

	public boolean canPinsSwap(int pos1, int pos2) {
		return ActiveGate.canSwapWith(ActiveGate.Pins[pos1],
				ActiveGate.Pins[pos2]);
	}

	public void changeGate(BrainStoneDirection direction) {
		this.addTASKS("changeGate",
				new String[] { "direction", String.valueOf(direction) });
	}

	public void changeGate(String string) {
		this.addTASKS("changeGate", new String[] { "string", string });
	}

	public void changeGate(String string, BrainStoneDirection direction) {
		this.addTASKS("changeGate",
				new String[] { "both", string, String.valueOf(direction) });
	}

	public boolean connectToRedstone(BrainStoneDirection direction) {
		return ActiveGate.Pins[direction.toArrayIndex()].State
				.canConnectRedstone();
	}

	public void doTASKS() {
		final int i = TASKS.size();

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

	public float getFactorForPinBlue(BrainStoneDirection direction) {
		final int color = getGateColor(direction);

		final float blue = (color & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			final float red = ((color >> 16) & 255) / 255.0F;
			final float green = ((color >> 8) & 255) / 255.0F;

			return ((red * 30.0F) + (green * 70.0F)) / 100.0F;
		}

		return blue;
	}

	public float getFactorForPinGreen(BrainStoneDirection direction) {
		final int color = getGateColor(direction);

		final float green = ((color >> 8) & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable) {
			final float red = ((color >> 16) & 255) / 255.0F;

			return ((red * 30.0F) + (green * 70.0F)) / 100.0F;
		}

		return green;
	}

	public float getFactorForPinRed(BrainStoneDirection direction) {
		final int color = getGateColor(direction);

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

	public int getGateColor(BrainStoneDirection direction) {
		return getColorForPowerLevel(ActiveGate.Pins[direction.toArrayIndex()].State
				.getPowerLevel());
	}

	public String getGateLetter(BrainStoneDirection direction) {
		return String.valueOf(ActiveGate.Pins[direction.toArrayIndex()].Name);
	}

	public int getGatePos() {
		return GatePos;
	}

	public byte getPowerLevel(BrainStoneDirection direction) {
		return ActiveGate.Pins[direction.toArrayIndex()].State.getPowerLevel();
	}

	public byte getPowerLevel(int pos) {
		return this.getPowerLevel((byte) pos);
	}

	private byte getPowerLevelFromBlock(int x, int y, int z,
			BrainStoneDirection direction) {
		final Block block = worldObj.getBlock(x, y, z);

		if (block == Blocks.redstone_wire)
			return (byte) Math.max(
					worldObj.getIndirectPowerLevelTo(x, y, z,
							direction.toTextureDirection()),
					worldObj.getBlockMetadata(x, y, z));
		else
			return (byte) worldObj.getIndirectPowerLevelTo(x, y, z,
					direction.toTextureDirection());
	}

	public byte getPowerOutputLevel(BrainStoneDirection direction) {
		final Pin pin = ActiveGate.Pins[direction.toArrayIndex()];

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
		this.addTASKS("logInOut", new String[] { "false", user.toString() });
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
			TASKS.add(
					byte1,
					nbttagcompound.getString((new StringBuilder())
							.append("TASK").append(String.valueOf(byte1))
							.toString()));
		}
	}

	@SideOnly(Side.CLIENT)
	public void renderGate(FontRenderer fontrenderer,
			BrainStoneDirection direction) {
		final Pin pin = ActiveGate.Pins[direction.ordinal()];

		if (pin.State.shallRender()) {
			final String tmp = String.valueOf(pin.Name);

			fontrenderer.drawString(tmp, -fontrenderer.getStringWidth(tmp) / 2,
					4, getGateColor(direction));
		}
	}

	private void runTASK(String s) {
		startPrintErrorBuff();

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
			print("!!!\tRun Task: \"logInOut\"\t\t!!!");

			if (as.length == 2) {
				if (Boolean.valueOf(as[0]).booleanValue()) {
					if (!Users.contains(as[1])) {
						Users.add(as[1]);
					} else {
						print("User \"" + as[1] + "\" is already logged in!");
					}
				} else {
					if (Users.contains(as[1])) {
						Users.remove(as[1]);
					} else {
						print("User \"" + as[1] + "\" is already logged out!");
					}
				}
			} else {
				print("!!!\tError: Wrong number of parameters\t!!!\n");
				printErrorInrunTASK(s, s1, as);
				BSP.throwRuntimeException(getPrintErrorBuff()
						+ "Wrong number of parameters");
			}
		} else if (s1.equals("changeGate")) {
			print("!!!\tRun Task: \"changeGate\"\t\t!!!");

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
				} else if (as[0].equals("direction")) {
					ActiveGate.onGateChange(BrainStoneDirection.valueOf(as[1]));
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

					ActiveGate.onGateChange(BrainStoneDirection.valueOf(as[2]));
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

		print("!!!\t\tTask Finished\t\t\t!!!\n");

		stopPrintErrorBuff();
	}

	public boolean shallDoUpdate(long l) {
		if (l > (lastUpdate + 1)) {
			lastUpdate = l;
			return true;
		} else
			return false;
	}

	public boolean shallRenderPin(BrainStoneDirection direction) {
		return ActiveGate.Pins[direction.toArrayIndex()].State.shallRender();
	}

	private void startPrintErrorBuff() {
		PrintErrorBuffActive = true;
		PrintErrorBuff = new ArrayList<String>();
	}

	private void stopPrintErrorBuff() {
		PrintErrorBuffActive = false;
		PrintErrorBuff = null;
	}

	public void tickGate(int x, int y, int z, long time) {
		final Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side != Side.CLIENT) {
			time /= 2;

			if ((time % ActiveGate.getTickRate()) == 0) {
				final int xShift[] = new int[] { 0, 0, 0, 1, 0, -1 };
				final int yShift[] = new int[] { 1, -1, 0, 0, 0, 0 };
				final int zShift[] = new int[] { 0, 0, -1, 0, 1, 0 };
				int tmpX, tmpY, tmpZ;
				Block block;

				BrainStoneDirection direction;

				for (int i = 0; i < 6; i++) {
					direction = BrainStoneDirection.fromArrayIndex(i);

					if (ActiveGate.Pins[i].State.canConnectRedstone()
							&& !ActiveGate.Pins[i].Output) {
						tmpX = x + xShift[i];
						tmpY = y + yShift[i];
						tmpZ = z + zShift[i];

						block = worldObj.getBlock(tmpX, tmpY, tmpZ);

						if (canBlockConnectToGate(worldObj, tmpX, tmpY, tmpZ,
								direction, block)) {
							ActiveGate.Pins[i].State = PinState
									.getPinState(getPowerLevelFromBlock(tmpX,
											tmpY, tmpZ, direction));
						} else {
							ActiveGate.Pins[i].State = PinState.NotConnected;
						}
					}
				}

				ActiveGate.onTick();
			}

			worldObj.markBlockForUpdate(x, y, z);
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
		nbttagcompound.setInteger("GatePos", GatePos);

		final byte byte0 = (byte) TASKS.size();
		nbttagcompound.setByte("TASKS-Size", byte0);

		for (byte byte1 = 0; byte1 < byte0; byte1++) {
			nbttagcompound
					.setString(
							(new StringBuilder()).append("TASK")
									.append(String.valueOf(byte1)).toString(),
							TASKS.get(byte1));
		}
	}
}