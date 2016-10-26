package brainstonemod.common.api;

/**
 * @author The_Fireplace
 */
public interface IModIntegration {
    void preInit();
    void init();
    void postInit();
    void serverStarting();
    void addAchievement();
}
