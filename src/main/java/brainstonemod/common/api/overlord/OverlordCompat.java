package brainstonemod.common.api.overlord;

import brainstonemod.common.api.IModIntegration;
import net.minecraft.entity.Entity;
import the_fireplace.overlord.entity.EntityArmyMember;

/**
 * @author The_Fireplace
 */
public class OverlordCompat implements IModIntegration, IOverlordCompat {
    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        new BrainStoneAugment();
    }

    @Override
    public void postInit() {

    }

    @Override
    public void serverStarting() {

    }

    @Override
    public void addAchievement() {

    }

    @Override
    public boolean exemptEntity(Entity entity) {
        if(entity instanceof EntityArmyMember && ((EntityArmyMember) entity).getAugment() != null && ((EntityArmyMember) entity).getAugment().augmentId().equals("pulsatingbrainstone"))
            return true;
        return false;
    }
}
