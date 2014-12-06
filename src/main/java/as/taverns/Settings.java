package as.taverns;

import java.util.Collection;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.VillagerRegistry;

public class Settings
{
	private static final String CATEGORY_VILLAGERS = "villagers";
	private static final String CATEGORY_COMPATIBILITY = "compatibility";

	// Settings
    public final boolean generateTaverns;
    public final boolean generateStalls;
    public final boolean generateBarns;
    public final boolean generateBakery;
    public final boolean generateChickencoop;
    
    public final int weightTaverns;
    public final int weightBasementTaverns;
    public final int weightStalls;
    public final int weightBarns;
    public final int weightBakery;
    public final int weightChickencoop;
    
    public final int barWenchID;
    public final int hostlerID;
    public final int shepherdessID;
    public final int bakerID;
    
    // Installed mods
    public final boolean minecraftComesAlive;
    public final boolean grimoireOfGaia2;
    public final boolean millenaire;
    
    private int getVillagerID(Configuration cfg, String category, String configName, int defaultID)
    {
		Collection<Integer> registeredVillagers = VillagerRegistry.getRegisteredVillagers();
		while( registeredVillagers.contains(Integer.valueOf(defaultID)) )
		{
			++ defaultID;
		}
		return cfg.get(category, configName, defaultID).getInt(defaultID);
    }
    
    public Settings(Configuration config)
	{
		config.load();
		
		config.getCategory(Configuration.CATEGORY_GENERAL).setComment(
				"Enable or disable village parts as needed.\n\n"
				+ "\"Weight\" determines how likely this structure is to spawn,\n"
				+ "useful values are in the range 1-50.\n"
				+ "A higher value means the structure is more likely to be created.\n\n"
				+ "Disabling a structure disables the corresponding custom villager.");
		config.getCategory(CATEGORY_VILLAGERS).setComment(
				"IDs of custom villagers. Set it to -1 to disable\n"
				+ "this specific villager type.");
		config.getCategory(CATEGORY_COMPATIBILITY).setComment(
				"Miscellaneous settings for compatibility with different mods.\n");
		
		generateTaverns = config.get(Configuration.CATEGORY_GENERAL, "generateTaverns", true).getBoolean(true);
		generateStalls = config.get(Configuration.CATEGORY_GENERAL, "generateStalls", true).getBoolean(true);
		generateBarns = config.get(Configuration.CATEGORY_GENERAL, "generateBarns", true).getBoolean(true);
		generateBakery = config.get(Configuration.CATEGORY_GENERAL, "generateBakery", true).getBoolean(true);
		generateChickencoop = config.get(Configuration.CATEGORY_GENERAL, "generateChickenCoop", true).getBoolean(true);
		
		weightTaverns = config.get(Configuration.CATEGORY_GENERAL, "weightTaverns", 50).getInt(50);
		weightBasementTaverns = 0; // config.get(Configuration.CATEGORY_GENERAL, "weightBasementTaverns", 3).getInt(3);
		weightStalls = config.get(Configuration.CATEGORY_GENERAL, "weightStalls", 50).getInt(50);
		weightBarns = config.get(Configuration.CATEGORY_GENERAL, "weightBarns", 50).getInt(50);
		weightBakery = config.get(Configuration.CATEGORY_GENERAL, "weightBakery", 50).getInt(50);
		weightChickencoop = config.get(Configuration.CATEGORY_GENERAL, "weightChickenCoop", 50).getInt(50);

		barWenchID = getVillagerID(config, CATEGORY_VILLAGERS, "villagerIDBarWench", 42);
		hostlerID = getVillagerID(config, CATEGORY_VILLAGERS, "villagerIDHostler", barWenchID + 1);
		shepherdessID = getVillagerID(config, CATEGORY_VILLAGERS, "villagerIDShepherdess", hostlerID + 1);
		bakerID = getVillagerID(config, CATEGORY_VILLAGERS, "villagerIDBaker", shepherdessID + 1);

		if( config.hasChanged() )
        {
            config.save();
        }
        
    	minecraftComesAlive = Loader.isModLoaded("MCA");
    	grimoireOfGaia2 = Loader.isModLoaded("GrimoireGaia2");
    	millenaire = Loader.isModLoaded("millenaire");
	}
}
