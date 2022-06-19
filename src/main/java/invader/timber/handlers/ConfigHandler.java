package invader.timber.handlers;

import invader.timber.Timber;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ConfigHandler {
    public static Configuration config;

    public static boolean destroyLeaves;
    public static boolean reverseShift;
    public static boolean disableShift;

    public static List<String> axe_whitelist;
    public static List<String> axe_blacklist;
    public static List<String> block_whitelist;
    public static List<String> leaf_whitelist;

    public static void init(File configFile) {
        if (config == null) {
            config = new Configuration(configFile);
            loadConfig();
        }
    }

    public static void loadConfig() {
        destroyLeaves = loadProperty_Boolean("Destroy Leaves", "Destroy leaf blocks on tree felling.", true);
        reverseShift = loadProperty_Boolean("Reverse Shift", "Requires crouch for tree felling.", false);
        disableShift = loadProperty_Boolean("Disable Shift", "Ignore crouch and always fell trees.", false);

        axe_whitelist = loadProperty_StringList(
                "Axe Whitelist",
                "List of items considered to be axes. \nExample: \"minecraft:iron_axe\"",
                new String[] {
                        "astralsorcery:itemcrystalaxe",
                        "astralsorcery:itemchargedcrystalaxe",
                        "bloodarsenal:blood_infused_wooden_axe",
                        "bloodarsenal:blood_infused_iron_axe",
                        "bloodarsenal:stasis_axe",
                        "bloodmagic:bound_axe",
                        "spartantwilight:battleaxe_fiery",
                        "spartantwilight:battleaxe_ironwood",
                        "spartantwilight:battleaxe_knightly",
                        "spartantwilight:battleaxe_steeleaf",
                        "spartanweaponry:battleaxe_bronze",
                        "spartanweaponry:battleaxe_copper",
                        "spartanweaponry:battleaxe_diamond",
                        "spartanweaponry:battleaxe_gold",
                        "spartanweaponry:battleaxe_invar",
                        "spartanweaponry:battleaxe_iron",
                        "spartanweaponry:battleaxe_lead",
                        "spartanweaponry:battleaxe_platinum",
                        "spartanweaponry:battleaxe_silver",
                        "spartanweaponry:battleaxe_steel",
                        "spartanweaponry:battleaxe_stone",
                        "spartanweaponry:battleaxe_wood",
                        "spartanweaponryarcana:battleaxe_dawnstone",
                        "spartanweaponryarcana:battleaxe_elementium",
                        "spartanweaponryarcana:battleaxe_manasteel",
                        "spartanweaponryarcana:battleaxe_terrasteel",
                        "spartanweaponryarcana:battleaxe_voidmetal",
                        "spartanweaponryarcana:battleaxe_thaumium",
                        "thermalfoundation:tool.axe_copper",
                        "thermalfoundation:tool.axe_tin",
                        "thermalfoundation:tool.axe_silver",
                        "thermalfoundation:tool.axe_lead",
                        "thermalfoundation:tool.axe_aluminum",
                        "thermalfoundation:tool.axe_nickel",
                        "thermalfoundation:tool.axe_platinum",
                        "thermalfoundation:tool.axe_steel",
                        "thermalfoundation:tool.axe_electrum",
                        "thermalfoundation:tool.axe_invar",
                        "thermalfoundation:tool.axe_bronze",
                        "thermalfoundation:tool.axe_constantan",
                        "immersiveengineering:axe_steel"
                }
        );

        axe_blacklist = loadProperty_StringList(
                "Axe Blacklist",
                "List of items not considered to be axes. \nExample: \"minecraft:iron_axe\"",
                new String[] {
                        "aetherworks:item_axe_prismarine",
                        "aetherworks:item_axe_ender",
                        "botania:terraaxe",
                        "spectrite:spectrite_axe",
                        "spectrite:spectrite_axe_special",
                        "enderio:item_dark_steel_axe",
                        "enderio:item_end_steel_axe"
                }
        );

        block_whitelist = loadProperty_StringList("Block Whitelist",
            "List of blocks considered to be logs.\nBlocks need to be listed as Unlocalized Name Strings",
            new String[] {
                "tile.log",
                "tile.log_0",
                "tile.log_1",
                "tile.log_2",
                "tile.log_3",
                "tile.log_4",
                "tile.pamCinnamon",
                "tile.pamPaperbark",
                "tile.pamMaple",
                "tile.for.pile_wood",
                "tile.for.logs.vanilla.fireproof.1",
                "tile.for.logs.vanilla.fireproof.0",
                "tile.for.logs.fireproof.7",
                "tile.for.logs.fireproof.6",
                "tile.for.logs.fireproof.5",
                "tile.for.logs.fireproof.4",
                "tile.for.logs.fireproof.3",
                "tile.for.logs.fireproof.2",
                "tile.for.logs.fireproof.1",
                "tile.for.logs.fireproof.0",
                "tile.for.logs.7",
                "tile.for.logs.6",
                "tile.for.logs.5",
                "tile.for.logs.4",
                "tile.for.logs.3",
                "tile.for.logs.2",
                "tile.for.logs.1",
                "tile.for.logs.0",
                "ic2.rubber_wood",
                "tile.terraqueous.trunk",
                "tile.techreborn.rubberlog",
                "tile.dendrology:log",
                "tile.livingwood",
                "tile.totemic:cedar_log",
                "tile.rustic.log",
                "tile.natura.nether_logs",
                "tile.natura.nether_logs2",
                "tile.natura.overworld_logs",
                "tile.natura.overworld_logs2"
            }
        );

        leaf_whitelist = loadProperty_StringList("Leaf Whitelist",
            "List of blocks considered to be leaves. \nBlocks need to be listed as Unlocalized Name Strings",
            new String[] {
                "tile.pamdate",
                "tile.pampistachio",
                "tile.pampapaya",
                "tile.pamwalnut",
                "tile.pamcherry",
                "tile.pamfig",
                "tile.pamdragonfruit",
                "tile.pamapple",
                "tile.pamlemon",
                "tile.pampear",
                "tile.pamolive",
                "tile.pamgrapefruit",
                "tile.pampomegranate",
                "tile.pamcashew",
                "tile.pamvanilla",
                "tile.pamstarfruit",
                "tile.pambanana",
                "tile.pamplum",
                "tile.pamavocadu",
                "tile.pampecan",
                "tile.pampistachio",
                "tile.pamlime",
                "tile.pampeppercorn",
                "tile.pamalmond",
                "tile.pamgooseberry",
                "tile.pampeach",
                "tile.pamchestnut",
                "tile.pamcoconut",
                "tile.pammango",
                "tile.pamapricot",
                "tile.pamorange",
                "tile.pampersimmon",
                "tile.pamnutmeg",
                "tile.pamdurian",
                "tile.pamcinnamon",
                "tile.pammaple",
                "tile.pampaperbark"
            }
        );

        if (config.hasChanged())
            config.save();
    }

    public static List<String> loadProperty_StringList(String propName, String comment, String... default_) {
        Property prop = config.get("general", propName, default_, comment);
        return Arrays.asList(prop.getStringList());
    }

    public static boolean loadProperty_Boolean(String propName, String comment, boolean default_) {
        Property prop = config.get("general", propName, default_, comment);
        return prop.getBoolean(default_);
    }

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if(eventArgs.getModID().equals(Timber.MOD_ID))
            loadConfig();
    }
}
