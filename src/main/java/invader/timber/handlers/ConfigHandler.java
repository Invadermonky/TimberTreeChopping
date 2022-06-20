package invader.timber.handlers;

import invader.timber.Timber;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigHandler {
    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_TWEAKS= "tweaks";
    public static final String CATEGORY_INTEGRATIONS = "integrations";

    public static Configuration config;

    public static boolean destroyLeaves;
    public static boolean invertCrouch;
    public static boolean disableShift;
    public static boolean dynamicBreakSpeed;

    public static double toolDamage;
    public static int maxTreeSize;

    public static List<String> axe_whitelist;
    public static List<String> axe_blacklist;
    public static List<String> block_whitelist;
    public static List<String> block_blacklist;
    public static List<String> leaf_whitelist;
    public static List<String> leaf_blacklist;

    private static Property prop_destroyLeaves;
    private static Property prop_invertCrouch;
    private static Property prop_disableShift;
    private static Property prop_dynamicBreakSpeed;
    private static Property prop_toolDamage;
    private static Property prop_maxTreeSize;
    private static Property prop_axeWhitelist;
    private static Property prop_axeBlacklist;
    private static Property prop_blockWhitelist;
    private static Property prop_blockBlacklist;
    private static Property prop_leafWhitelist;
    private static Property prop_leafBlacklist;

    public static void init(File configFile) {
        config = new Configuration(configFile);
        config.load();
        loadConfig();
        setPropertyOrder();;
    }

    private static void setPropertyOrder() {
        List<String> propOrder_general = new ArrayList<>();
        propOrder_general.add(prop_invertCrouch.getName());
        propOrder_general.add(prop_disableShift.getName());
        propOrder_general.add(prop_destroyLeaves.getName());
        propOrder_general.add(prop_dynamicBreakSpeed.getName());

        config.setCategoryPropertyOrder(CATEGORY_GENERAL, propOrder_general);

        List<String> propOrder_tweaks = new ArrayList<>();
        propOrder_tweaks.add(prop_toolDamage.getName());
        propOrder_tweaks.add(prop_maxTreeSize.getName());

        config.setCategoryPropertyOrder(CATEGORY_GENERAL, propOrder_tweaks);

        List<String> propOrder_integrations = new ArrayList<>();
        propOrder_integrations.add(prop_axeWhitelist.getName());
        propOrder_integrations.add(prop_axeBlacklist.getName());
        propOrder_integrations.add(prop_blockWhitelist.getName());
        propOrder_integrations.add(prop_blockBlacklist.getName());
        propOrder_integrations.add(prop_leafWhitelist.getName());
        propOrder_integrations.add(prop_leafBlacklist.getName());

        config.setCategoryPropertyOrder(CATEGORY_GENERAL, propOrder_integrations);
    }

    public static void loadConfig() {

        prop_destroyLeaves = config.get(CATEGORY_GENERAL, "Destroy Leaves", true);
        prop_destroyLeaves.setComment("Destroy leaf blocks on tree felling.");
        destroyLeaves = prop_destroyLeaves.getBoolean();

        prop_invertCrouch = config.get(CATEGORY_GENERAL, "Invert Crouch", false);
        prop_invertCrouch.setComment("Requires crouch for tree felling.");
        invertCrouch = prop_invertCrouch.getBoolean();

        prop_disableShift = config.get(CATEGORY_GENERAL, "Disable Crouch", false);
        prop_disableShift.setComment("When true will always fell trees.");
        disableShift = prop_disableShift.getBoolean();

        prop_dynamicBreakSpeed = config.get(CATEGORY_GENERAL, "Dynamic Break Speed", true);
        prop_dynamicBreakSpeed.setComment("Enable dynamic break speed based on tree size.");
        dynamicBreakSpeed = prop_dynamicBreakSpeed.getBoolean();

        prop_toolDamage = config.get(CATEGORY_TWEAKS, "Tool Damage", 1.5);
        prop_toolDamage.setComment("Tool damage multiplier for breaking multiple blocks.\nEach block broken is multiplied by this value.");
        toolDamage = prop_toolDamage.getDouble();

        prop_maxTreeSize = config.get(CATEGORY_TWEAKS, "Max Tree Size", 192);
        prop_maxTreeSize.setComment("The maximum tree size that can be cut down at once.\nTrees larger than this value will not be felled.");
        prop_maxTreeSize.setMinValue(2);
        prop_maxTreeSize.setMaxValue(512);
        maxTreeSize = prop_maxTreeSize.getInt();

        prop_axeWhitelist = config.get(CATEGORY_INTEGRATIONS, "Axe Whitelist", new String[] {
                "actuallyadditions:item_axe_emerald",
                "actuallyadditions:item_axe_obsidian",
                "actuallyadditions:item_axe_quartz",
                "actuallyadditions:item_axe_crystal_white",
                "actuallyadditions:item_axe_crystal_red",
                "actuallyadditions:item_axe_crystal_blue",
                "actuallyadditions:item_axe_crystal_light_blue",
                "actuallyadditions:item_axe_crystal_black",
                "actuallyadditions:item_axe_crystal_green",
                "bloodarsenal:blood_infused_wooden_axe",
                "bloodarsenal:blood_infused_iron_axe",
                "bloodarsenal:stasis_axe",
                "bloodmagic:bound_axe",
                "immersiveengineering:axe_steel",
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
                "tconstruct:hatchet"
        });
        prop_axeWhitelist.setComment("List of items considered to be axes. \nExample: \"minecraft:iron_axe\"");
        axe_whitelist = Arrays.asList(prop_axeWhitelist.getStringList());

        prop_axeBlacklist = config.get(CATEGORY_INTEGRATIONS, "Axe Blacklist", new String[] {
                "aetherworks:item_axe_prismarine",
                "aetherworks:item_axe_ender",
                "botania:terraaxe",
                "spectrite:spectrite_axe",
                "spectrite:spectrite_axe_special",
                "enderio:item_dark_steel_axe",
                "enderio:item_end_steel_axe"});
        prop_axeBlacklist.setComment("List of items not considered to be axes. \nExample: \"minecraft:iron_axe\"");
        axe_blacklist = Arrays.asList(prop_axeBlacklist.getStringList());

        prop_blockWhitelist = config.get(CATEGORY_INTEGRATIONS, "Block Whitelist", new String[] {});
        prop_blockWhitelist.setComment("List of blocks considered to be logs.\nBExample: \"minecraft:log\" or \"minecraft:log:0\"");
        block_whitelist = Arrays.asList(prop_blockWhitelist.getStringList());

        prop_blockBlacklist = config.get(CATEGORY_INTEGRATIONS, "Block Blacklist", new String[] {});
        prop_blockBlacklist.setComment("List of blocks not to be considered as logs.\nExample: \"minecraft:log\" or \"minecraft:log:0\"");
        block_blacklist = Arrays.asList(prop_blockBlacklist.getStringList());

        prop_leafWhitelist = config.get(CATEGORY_INTEGRATIONS, "Leaf Whitelist", new String[] {
                "harvestcraft:pamdate",
                "harvestcraft:pampistachio",
                "harvestcraft:pampapaya",
                "harvestcraft:pamwalnut",
                "harvestcraft:pamcherry",
                "harvestcraft:pamfig",
                "harvestcraft:pamdragonfruit",
                "harvestcraft:pamapple",
                "harvestcraft:pamlemon",
                "harvestcraft:pampear",
                "harvestcraft:pamolive",
                "harvestcraft:pamgrapefruit",
                "harvestcraft:pampomegranate",
                "harvestcraft:pamcashew",
                "harvestcraft:pamvanilla",
                "harvestcraft:pamstarfruit",
                "harvestcraft:pambanana",
                "harvestcraft:pamplum",
                "harvestcraft:pamavocadu",
                "harvestcraft:pampecan",
                "harvestcraft:pampistachio",
                "harvestcraft:pamlime",
                "harvestcraft:pampeppercorn",
                "harvestcraft:pamalmond",
                "harvestcraft:pamgooseberry",
                "harvestcraft:pampeach",
                "harvestcraft:pamchestnut",
                "harvestcraft:pamcoconut",
                "harvestcraft:pammango",
                "harvestcraft:pamapricot",
                "harvestcraft:pamorange",
                "harvestcraft:pampersimmon",
                "harvestcraft:pamnutmeg",
                "harvestcraft:pamdurian",
                "harvestcraft:pamcinnamon",
                "harvestcraft:pammaple",
                "harvestcraft:pampaperbark"
        });
        prop_leafWhitelist.setComment("List of blocks considered to be leaves. \nExample: \"minecraft:log\" or \"minecraft:log:0\"");
        leaf_whitelist = Arrays.asList(prop_leafWhitelist.getStringList());

        prop_leafBlacklist = config.get(CATEGORY_INTEGRATIONS, "Leaf Blacklist", new String[] {});
        prop_leafBlacklist.setComment("List of blocks not considered to be leaves.\nExample: \"minecraft:log\" or \"minecraft:log:0\"");
        leaf_blacklist = Arrays.asList(prop_leafBlacklist.getStringList());

        if (config.hasChanged())
            config.save();
    }

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if(eventArgs.getModID().equals(Timber.MOD_ID))
            loadConfig();
    }
}
