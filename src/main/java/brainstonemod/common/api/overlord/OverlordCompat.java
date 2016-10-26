package brainstonemod.common.api.overlord;

import brainstonemod.common.api.IModIntegration;

/**
 * @author The_Fireplace
 */
public class OverlordCompat implements IModIntegration {
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
}
