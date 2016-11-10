package brainstonemod.common.compat.overlord;

import brainstonemod.common.compat.IModIntegration;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import the_fireplace.overlord.entity.EntityArmyMember;

/**
 * @author The_Fireplace
 */
public class OverlordCompat implements IModIntegration, IOverlordCompat {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		// Do nothing
	}

	@Override
	public void init(FMLInitializationEvent event) {
		new BrainStoneAugment();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		// Do nothing
	}

	@Override
	public void addAchievement() {
		// Do nothing
	}

	@Override
	public boolean exemptEntity(Entity entity) {
		return (entity instanceof EntityArmyMember && ((EntityArmyMember) entity).getAugment() != null
				&& ((EntityArmyMember) entity).getAugment().augmentId().equals("pulsatingbrainstone"));
	}
}
