package invader.timber;

import invader.timber.network.ClientSettingsMessage;
import invader.timber.network.ServerSettingsMessage;
import invader.timber.proxy.CommonProxy;
import invader.timber.handlers.ConfigHandler;
import invader.timber.handlers.EventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;


import static invader.timber.Timber.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = MOD_VERSION, guiFactory = GUI_FACTORY)
public class Timber {

    public static final String MOD_ID = "timber";
    public static final String MOD_NAME = "Timber!";
    public static final String MOD_VERSION = "1.1.0";
    public static final String GUI_FACTORY = "invader.timber.client.GuiFactory";
    public static final String CLIENT_PROXY = "invader.timber.proxy.CommonProxy";
    public static final String SERVER_PROXY = "invader.timber.proxy.ServerProxy";

    public static SimpleNetworkWrapper network;

    @SidedProxy(serverSide = SERVER_PROXY, clientSide = CLIENT_PROXY)
    private static CommonProxy commonProxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
        network.registerMessage(ServerSettingsMessage.MsgHandler.class, ServerSettingsMessage.class, 0, Side.CLIENT);
        network.registerMessage(ClientSettingsMessage.MsgHandler.class, ClientSettingsMessage.class, 1, Side.SERVER);
    }

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(commonProxy);
        MinecraftForge.EVENT_BUS.register(new ConfigHandler());
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {}
}
