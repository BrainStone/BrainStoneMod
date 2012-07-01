public class TileEntityBlockBrainLightSensor extends kw
{
  private int lightLevel;
  private boolean direction;
  private boolean powerOn;

  public TileEntityBlockBrainLightSensor()
  {
    this.lightLevel = 8;
    this.direction = true;
    this.powerOn = false;
  }

  public int getLightLevel()
  {
    return this.lightLevel;
  }

  public boolean getDirection()
  {
    return this.direction;
  }

  public boolean getPowerOn()
  {
    return this.powerOn;
  }

  public void setLightLevel(int lightLevel)
  {
    this.lightLevel = lightLevel;
  }

  public void setDirection(boolean direction)
  {
    this.direction = direction;
  }

  public void setPowerOn(boolean powerOn)
  {
    this.powerOn = powerOn;
  }

  public void a(ady par1NBTTagCompound)
  {
    super.a(par1NBTTagCompound);
    this.lightLevel = par1NBTTagCompound.e("TEBBLS_lightLevel");
    this.direction = par1NBTTagCompound.o("TEBBLS_direction");
  }

  public void b(ady par1NBTTagCompound)
  {
    super.b(par1NBTTagCompound);
    par1NBTTagCompound.a("TEBBLS_lightLevel", (short)this.lightLevel);
    par1NBTTagCompound.a("TEBBLS_direction", this.direction);
  }
}