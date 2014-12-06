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

public class VillageHandlerHostler extends VillageHandlerBase implements IVillageTradeHandler
{
	public static VillageHandlerHostler villageHandler = null;
	
	public static void init(Settings config)
	{
		if( null == villageHandler )
		{
			villageHandler = new VillageHandlerHostler();
			VillagerRegistry.instance().registerVillagerId(config.hostlerID);
			VillagerRegistry.instance().registerVillageTradeHandler(config.hostlerID, villageHandler);
			Taverns.sidedProxy.registerVillagerSkin(config.hostlerID, Taverns.modID, "textures/entity/village_hostler.png");
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
		
		// Sell
		recipeList.add(new MerchantRecipe(emerald, randomItem(Items.lead, 12, 16, random)));
		addRandomTrade(recipeList, emerald, randomItem(Blocks.hay_block, 3, 5, random), 0.9, random);
		addRandomTrade(recipeList, emerald, randomItem(Items.apple, 8, 12, random), 0.9, random);
		addRandomTrade(recipeList, emerald, new ItemStack(Items.saddle, 1), 0.9, random);
		addRandomTrade(recipeList, new ItemStack(Items.emerald, 3), new ItemStack(Items.iron_horse_armor), 0.5, random);
		addRandomTrade(recipeList, emerald, new ItemStack(Items.name_tag, 1), 0.5, random);
		addRandomTrade(recipeList, emerald, new ItemStack(Items.golden_carrot, 1), 0.3, random);
		
		// Buy
		addRandomTrade(recipeList, randomItem(Items.slime_ball, 12, 20, random), emerald, 0.9, random);
		addRandomTrade(recipeList, randomItem(Items.wheat, 18, 22, random), emerald, 0.9, random);
		
		/*
		if( Taverns.config.grimoireOfGaia2 )
		{
			addRandomTrade(recipeList, emeraldShard,  randomItem(Items.lead, 3, 4, random), 0.9, random);
			addRandomTrade(recipeList, emeraldShard, new ItemStack(Blocks.hay_block, 1), 0.9, random);
			addRandomTrade(recipeList, emeraldShard, randomItem(Items.apple, 2, 3, random), 0.9, random);
			addRandomTrade(recipeList, randomItem(Items.slime_ball, 3, 5, random), emeraldShard, 0.9, random);
			addRandomTrade(recipeList, randomItem(Items.wheat, 4, 6, random), emeraldShard, 0.9, random);
		}
		*/
	}

}
