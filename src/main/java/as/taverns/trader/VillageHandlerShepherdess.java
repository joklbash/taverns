package as.taverns.trader;

import java.util.Random;

import as.taverns.Settings;
import as.taverns.Taverns;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;

public class VillageHandlerShepherdess extends VillageHandlerBase implements IVillageTradeHandler
{
	public static VillageHandlerShepherdess villageHandler = null;
	
	public static void init(Settings config)
	{
		if( null == villageHandler )
		{
			villageHandler = new VillageHandlerShepherdess();
			VillagerRegistry.instance().registerVillagerId(config.shepherdessID);
			VillagerRegistry.instance().registerVillageTradeHandler(config.shepherdessID, villageHandler);
			Taverns.sidedProxy.registerVillagerSkin(config.shepherdessID, Taverns.modID, "textures/entity/village_shepherdess.png");
		}
	}

	@Override
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random)
	{
		ItemStack emeraldShard = null;
		if( Taverns.config.grimoireOfGaia2 )
		{
			//emeraldShard = new ItemStack(GaiaItem.ShardEmerald, 1);
		}

		// Wool to string
		recipeList.add(new MerchantRecipe(new ItemStack(Blocks.wool, 1), new ItemStack(Items.string, 4)));
		
		// Merchant recipes treat items with different damageValue but same itemID as the same, so
		// we can only use one of those trades, sadly ...
		switch( random.nextInt(5) )
		{
			case 0:
				// White
				recipeList.add(new MerchantRecipe(emerald, randomItem(Blocks.wool, 14, 18, 0, random)));
				break;
			case 1:
				// Light grey
				recipeList.add(new MerchantRecipe(emerald, randomItem(Blocks.wool, 10, 14, 8, random)));
				break;
			case 2:
				// Dark grey
				recipeList.add(new MerchantRecipe(emerald, randomItem(Blocks.wool, 10, 14, 7, random)));
				break;
			case 3:
				// Black
				recipeList.add(new MerchantRecipe(emerald, randomItem(Blocks.wool, 6, 12, 15, random)));
				break;
			case 4:
				// Any random
				recipeList.add(new MerchantRecipe(emerald, randomItem(Blocks.wool, 6, 12, random.nextInt(16), random)));
				break;
			default:
				// This shouldn't happen.
				break;
		}
        
		/*
        if( Taverns.config.grimoireOfGaia2 )
        {
        	// Use GoG2's emerald shard (=1/4th of an emerald) for some trades too.
            recipeList.add(new MerchantRecipe(emeraldShard, new ItemStack(Items.string, 6)));
    		switch( random.nextInt(5) )
    		{
    			case 0:
    				// White
    				recipeList.add(new MerchantRecipe(emeraldShard, randomItem(Blocks.wool, 3, 5, 0, random)));
    				break;
    			case 1:
    				// Light grey
    				recipeList.add(new MerchantRecipe(emeraldShard, randomItem(Blocks.wool, 2, 3, 8, random)));
    				break;
    			case 2:
    				// Dark grey
    				recipeList.add(new MerchantRecipe(emeraldShard, randomItem(Blocks.wool, 2, 3, 7, random)));
    				break;
    			case 3:
    				// Black
    				recipeList.add(new MerchantRecipe(emeraldShard, new ItemStack(Blocks.wool, 2, 15)));
    				break;
    			case 4:
    				// Any random
    				recipeList.add(new MerchantRecipe(emeraldShard, new ItemStack(Blocks.wool, 2, random.nextInt(16))));
    				break;
    			default:
    				// This shouldn't happen.
    				break;
    		}
    		addRandomTrade(recipeList, new ItemStack(Items.shears, 1, 0), emeraldShard, 0.5, random);
        }
        */
	}

}
