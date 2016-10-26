package brainstonemod.common.api.tconstruct;

import brainstonemod.common.api.IModIntegration;

/**
 * @author The_Fireplace
 */
public class TiConCompat implements IModIntegration {
    @Override
    public void preInit() {

    }

    @Override
    public void init() {

    }

    @Override
    public void postInit() {
        TinkersContructMaterialBrainStone.initToolMaterials();
    }

    @Override
    public void serverStarting() {

    }

    @Override
    public void addAchievement() {

    }
}
