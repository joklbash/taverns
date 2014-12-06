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

public class VillageHandlerBaker extends VillageHandlerBase implements IVillageTradeHandler
{
	public static VillageHandlerBaker villageHandler = null;
	
	public static void init(Settings config)
	{
		if( null == villageHandler )
		{
			villageHandler = new VillageHandlerBaker();
			VillagerRegistry.instance().registerVillagerId(config.bakerID);
			VillagerRegistry.instance().registerVillageTradeHandler(config.bakerID, villageHandler);
			Taverns.sidedProxy.registerVillagerSkin(config.bakerID, Taverns.modID, "textures/entity/village_baker.png");
		}
	}

	@Override
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random)
	{
		ItemStack emeraldShard = null;
		if( Taverns.config.grimoireOfGaia2 )
		{
			// emeraldShard = new ItemStack(GaiaItem.ShardEmerald, 1);
		}
		
		// Sell
		recipeList.add(new MerchantRecipe(emerald, randomItem(Items.sugar, 24, 32, random)));
		addRandomTrade(recipeList, emerald, randomItem(Items.bread, 4, 8, random), 0.9, random);
		addRandomTrade(recipeList, emerald, randomItem(Blocks.cake, 1, 3, random), 0.4, random);
		addRandomTrade(recipeList, emerald, randomItem(Items.pumpkin_pie, 3, 5, random), 0.5, random);
		addRandomTrade(recipeList, emerald, randomItem(Items.cookie, 18, 22, random), 0.5, random);
		
		// Buy
		addRandomTrade(recipeList, randomItem(Items.wheat, 20, 28, random), emerald, 0.9, random);
		addRandomTrade(recipeList, randomItem(Items.reeds, 4, 8, random), emerald, 0.9, random);
		addRandomTrade(recipeList, randomItem(Items.egg, 12, 16, random), emerald, 0.5, random);
		addRandomTrade(recipeList, randomItem(Items.coal, 16, 24, random), emerald, 0.5, random);
		
		/*
		if( Taverns.config.grimoireOfGaia2 )
		{
			addRandomTrade(recipeList, emeraldShard,  randomItem(Items.sugar, 6, 8, random), 0.9, random);
			addRandomTrade(recipeList, emeraldShard, randomItem(Items.bread, 1, 2, random), 0.8, random);
			addRandomTrade(recipeList, new ItemStack(GaiaItem.ShardEmerald, 2), new ItemStack(Blocks.cake, 1), 0.35, random);
			addRandomTrade(recipeList, emeraldShard, new ItemStack(Items.pumpkin_pie, 1), 0.4, random);
			addRandomTrade(recipeList, emeraldShard, randomItem(Items.cookie, 4, 6, random), 0.4, random);
			// Extra sell, same as regular GoG2 price - 6 to 10 emerald shards for one
			addRandomTrade(recipeList, randomItem(GaiaItem.ShardEmerald, 6, 12, random), new ItemStack(GaiaItem.FoodChocolateMousse, 1), 0.2, random);
			addRandomTrade(recipeList, randomItem(Items.emerald, 2, 3, random), new ItemStack(GaiaItem.FoodChocolateMousse, 1), 0.1, random);

			addRandomTrade(recipeList, randomItem(Items.wheat, 5, 7, random), emeraldShard, 0.8, random);
			addRandomTrade(recipeList, randomItem(Items.reeds, 1, 2, random), emeraldShard, 0.8, random);
			addRandomTrade(recipeList, randomItem(Items.egg, 3, 4, random), emeraldShard, 0.4, random);
			addRandomTrade(recipeList, randomItem(Items.coal, 4, 6, random), emeraldShard, 0.4, random);
		}
		*/
		
	}

}
