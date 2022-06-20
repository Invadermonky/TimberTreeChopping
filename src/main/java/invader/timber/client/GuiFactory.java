package invader.timber.client;

import invader.timber.Timber;
import invader.timber.handlers.ConfigHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.DefaultGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

public class GuiFactory extends DefaultGuiFactory {
    public GuiFactory() {
        super(Timber.MOD_ID, Timber.MOD_NAME);
    }

    public GuiScreen createConfigGui(GuiScreen guiScreen) {
        List<IConfigElement> elementList = new ArrayList<>();
        for (String s : ConfigHandler.config.getCategoryNames()) {
            elementList.add(new ConfigElement(ConfigHandler.config.getCategory(s)));
        }
        return new GuiConfig(guiScreen, elementList, modid, false, false, title);
    }
}
