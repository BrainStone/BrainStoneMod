package brainstonemod.common.tileentity.template;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityBrainStoneSyncBase extends TileEntity {

	/*@Override
	public boolean canUpdate() {
		return false;
	}*/

	@Override
	public Packet getDescriptionPacket() {
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, getBlockMetadata(), getUpdateTag());
	}

    public NBTTagCompound getUpdateTag(){
        NBTTagCompound compound = new NBTTagCompound();
        writeToNBT(compound);
        return compound;
    }

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());//func_148857_g=getNBTCompound()
	}
}