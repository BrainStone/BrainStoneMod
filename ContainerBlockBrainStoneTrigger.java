public class ContainerBlockBrainStoneTrigger extends dd
{
  private TileEntityBlockBrainStoneTrigger trigger;

  public ContainerBlockBrainStoneTrigger(aak par1InventoryPlayer, TileEntityBlockBrainStoneTrigger par2TileEntity)
  {
    this.trigger = par2TileEntity;
    a(new SlotBlockBrainStoneTrigger(this.trigger, 0, 115, 58));

    for (int i = 0; i < 3; i++)
    {
      for (int k = 0; k < 9; k++)
      {
        a(new yu(par1InventoryPlayer, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
      }
    }

    for (int j = 0; j < 9; j++)
    {
      a(new yu(par1InventoryPlayer, j, 8 + j * 18, 142));
    }
  }

  public boolean b(yw par1EntityPlayer)
  {
    return this.trigger.a_(par1EntityPlayer);
  }
}