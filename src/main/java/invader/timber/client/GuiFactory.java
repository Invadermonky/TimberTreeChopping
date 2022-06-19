package invader.timber.client;

import invader.timber.Timber;
import invader.timber.handlers.ConfigHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.DefaultGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;

public class GuiFactory extends DefaultGuiFactory {
    public GuiFactory() {
        super(Timber.MOD_ID, Timber.MOD_NAME);
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen guiScreen) {
        return (GuiScreen) new GuiConfig(guiScreen, (new ConfigElement(ConfigHandler.config.getCategory("general"))).getChildElements(), modid, false, false, title);
    }
}
