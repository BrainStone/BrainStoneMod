package brainstone;

import cpw.mods.fml.common.Mod;

@Mod(modid="BrainStoneMod_TemplateAddOn", name="BSM - Template Add-On", version="for ????")
@BSMAddOn(texture={"/???.png"}, foreignTexture={"???.png"}, side={0}, bottom={0})
public class TemplateAddon
{
  public TemplateAddon()
  {
    BrainStone.addAddOn(TemplateAddon.class);
  }
}