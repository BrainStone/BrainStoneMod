package brainstonemod.common.api.enderio;

import brainstonemod.common.api.IModIntegration;

/**
 * @author The_Fireplace
 */
public class EIOCompat implements IModIntegration {
    @Override
    public void serverStarting() {

    }

    @Override
    public void addAchievement() {

    }

    @Override
    public void preInit() {

    }

    @Override
    public void init() {

    }

    @Override
    public void postInit() {
        EnderIORecipies.registerEnderIORecipies();
    }
}
