package as.taverns;

import as.common.CommonSidedProxy;
import as.taverns.gen.BakeryCreationHandler;
import as.taverns.gen.BarnCreationHandler;
import as.taverns.gen.ChickencoopCreationHandler;
import as.taverns.gen.ComponentVillageBakery;
import as.taverns.gen.ComponentVillageBarn;
import as.taverns.gen.ComponentVillageChickencoop;
import as.taverns.gen.ComponentVillageStall;
import as.taverns.gen.ComponentVillageTavern;
import as.taverns.gen.StallCreationHandler;
import as.taverns.gen.TavernCreationHandler;
import as.taverns.trader.VillageHandlerBaker;
import as.taverns.trader.VillageHandlerBarWench;
import as.taverns.trader.VillageHandlerHostler;
import as.taverns.trader.VillageHandlerShepherdess;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Taverns.modID, name = Taverns.modName, version = Taverns.modVersion)
public class Taverns
{
    @Instance public static Taverns instance;
    
    @SidedProxy(clientSide = "as.common.client.ClientProxy",
    		serverSide = "as.common.server.ServerProxy")
    public static CommonSidedProxy sidedProxy;
    
    public static final String modID = "taverns";
    public static final String modName = "Village Taverns";
    public static final String modVersion = "@@VERSION@@-@@REVISION@@";
    
    public static Settings config;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	config = new Settings(new Configuration(event.getSuggestedConfigurationFile()));
        if( config.generateTaverns )
        {
        	if( config.barWenchID > 0 )
        	{
        		VillageHandlerBarWench.init(config);
        	}
        	TavernCreationHandler.init(config);
			ComponentVillageTavern.init(config);
        	//Tavern2CreationHandler.init(config);
			//ComponentVillageTavern2.init(config);
        }
        if( config.generateBarns )
        {
        	if( config.shepherdessID > 0 )
        	{
    			VillageHandlerShepherdess.init(config);
        	}
        	BarnCreationHandler.init(config);
			ComponentVillageBarn.init(config);
        }
        if( config.generateStalls )
        {
        	if( config.hostlerID > 0 )
        	{
    			VillageHandlerHostler.init(config);
        	}
        	StallCreationHandler.init(config);
			ComponentVillageStall.init(config);
        }
        if( config.generateBakery )
        {
        	if( config.bakerID > 0 )
        	{
    			VillageHandlerBaker.init(config);
        	}
        	BakeryCreationHandler.init(config);
			ComponentVillageBakery.init(config);
        }
        if( config.generateChickencoop )
        {
        	ChickencoopCreationHandler.init(config);
        	ComponentVillageChickencoop.init(config);
        }
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	MinecraftForge.EVENT_BUS.register(new EntityEventHandler());
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        if( config.generateTaverns )
        {
			ComponentVillageTavern.postInit(config);
        }
        if( config.generateStalls )
        {
        	ComponentVillageStall.postInit(config);
        }
        if( config.generateBakery )
        {
        	ComponentVillageBakery.postInit(config);
        }
    }
}
