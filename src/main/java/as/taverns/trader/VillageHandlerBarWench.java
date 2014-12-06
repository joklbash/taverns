package as.taverns.trader;

import java.util.Random;

import as.taverns.Settings;
import as.taverns.Taverns;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;

public class VillageHandlerBarWench extends VillageHandlerBase implements IVillageTradeHandler
{
	public static VillageHandlerBarWench villageHandler = null;
	
	public static void init(Settings config)
	{
		if( null == villageHandler )
		{
			villageHandler = new VillageHandlerBarWench();
			VillagerRegistry.instance().registerVillagerId(config.barWenchID);
			VillagerRegistry.instance().registerVillageTradeHandler(config.barWenchID, villageHandler);
			Taverns.sidedProxy.registerVillagerSkin(config.barWenchID, Taverns.modID, "textures/entity/barwench.png");
		}
	}
	
	@Override
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random)
	{
		ItemStack emeraldShard = null;
		/*
		if( Taverns.config.grimoireOfGaia2 )
		{
			emeraldShard = new ItemStack(GaiaItem.ShardEmerald, 1);
		}
		*/
		
		// Awkward potions aren't stackable, so we have two single-item stacks of them
        recipeList.add(new MerchantRecipe(
                new ItemStack(Items.potionitem, 1, 16), new ItemStack(Items.potionitem, 1, 16),
                new ItemStack(Items.emerald, 1)));
        // Night vision
        addRandomTrade(recipeList, emerald, new ItemStack(Items.potionitem, 2, 33), 0.5, random);
        addRandomTrade(recipeList, emerald, randomItem(Items.baked_potato, 28, 40, random), 0.9, random);
        addRandomTrade(recipeList, emerald, randomItem(Items.magma_cream, 4, 8, random), 0.3, random);

        // Quest type trades
        addRandomTrade(recipeList, new ItemStack(Items.bone, 12), emerald, 0.5, random);
        addRandomTrade(recipeList, new ItemStack(Items.glowstone_dust, 12), emerald, 0.5, random);
        addRandomTrade(recipeList, new ItemStack(Items.blaze_powder, 12), emerald, 0.5, random);
        
        /*
        if( Taverns.config.grimoireOfGaia2 )
        {
        	// Use GoG2's emerald shard (=1/4th of an emerald) for some trades too.
        	addRandomTrade(recipeList, new ItemStack(Items.potionitem, 1, 16), emeraldShard, 0.7, random);
            addRandomTrade(recipeList, new ItemStack(GaiaItem.ShardEmerald, 2), new ItemStack(Items.potion, 1, 33), 0.5, random); // night vision
            addRandomTrade(recipeList, emeraldShard, randomItem(Items.baked_potato, 7, 10, random), 0.9, random);
            addRandomTrade(recipeList, new ItemStack(Items.bone, 3), emeraldShard, 0.5, random);
            addRandomTrade(recipeList, new ItemStack(Items.glowstone_dust, 3), emeraldShard, 0.5, random);
            addRandomTrade(recipeList, new ItemStack(Items.blaze_powder, 3), emeraldShard, 0.5, random);
        }
        */
        
        /*
        if( Taverns.config.biomesOPlenty )
        {
        	if( Items.food.isPresent() )
        	{
	        	// Trades don't differentiate between same itemID but different damageValues, so for now
	        	// (until implementing own EntityVillager) it's one or the other.
	        	if( random.nextBoolean() || MinecraftServer.getServer().getMinecraftVersion().equals("1.6.2") )
	        	{
	        		// Berries
	                recipeList.add(new MerchantRecipe(randomItem(Items.food.get(), 28, 36, 0, random), emerald));
	        	}
	        	else
	        	{
	        		// Persimmons
	                recipeList.add(new MerchantRecipe(randomItem(Items.food.get(), 20, 28, 8, random), emerald));
	        	}
	        	
	           	// GoG2 variants on some trades
	           	if( Taverns.config.grimoireOfGaia2 )
	           	{
	            	if( random.nextBoolean() || MinecraftServer.getServer().getMinecraftVersion().equals("1.6.2") )
	            	{
	                    recipeList.add(new MerchantRecipe(randomItem(Items.food.get(), 7, 9, 0, random), emeraldShard));
	            	}
	            	else
	            	{
	                    recipeList.add(new MerchantRecipe(randomItem(Items.food.get(), 5, 7, 8, random), emeraldShard));
	            	}
	           	}
        	}
        }
        */
	}
}
