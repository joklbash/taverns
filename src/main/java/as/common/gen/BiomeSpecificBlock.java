package as.common.gen;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.BiomeEvent;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import cpw.mods.fml.common.eventhandler.Event;

public class BiomeSpecificBlock
{
	public final int metadata;
	public final Block block;
	
	public final static BiomeSpecificBlock air = new BiomeSpecificBlock(Blocks.air, 0);
	
	private final static Map<Pair<Block, Integer>, Pair<Block, Integer>> desertReplaceMap;
	
	private static BiomeSpecificBlock getDesertReplacement(Block original, Integer originalMeta)
	{
		Pair<Block,Integer> replacement = desertReplaceMap.get(ImmutablePair.<Block,Integer>of(original, originalMeta));
		if( null == replacement )
		{
			// Try the wildcard (-1)
			replacement = desertReplaceMap.get(ImmutablePair.<Block,Integer>of(original, -1));
		}
		// Found anything?
		if( null != replacement )
		{
			return new BiomeSpecificBlock(replacement.getLeft(), replacement.getRight() >= 0 ? replacement.getRight() : originalMeta);
		}
		else
		{
			return new BiomeSpecificBlock(original, originalMeta);
		}
	}
	
	/**
	 * Return vanilla replacements only
	 */
	public static BiomeSpecificBlock queryVanilla(Block block, int meta, BiomeGenBase biome)
	{
    	if( biome == BiomeGenBase.desert || biome == BiomeGenBase.desertHills )
    	{
    		return getDesertReplacement(block, meta);
    	}
    	else
    	{
    		return new BiomeSpecificBlock(block, meta);
    	}
	}
	
	public static BiomeSpecificBlock query(Block block, int meta, BiomeGenBase biome)
	{
		Block newBlock = null;
		int newMeta = -1;
		// Determine modded block ID
        BiomeEvent.GetVillageBlockID eventBlock = new BiomeEvent.GetVillageBlockID(biome, block, meta);
        MinecraftForge.TERRAIN_GEN_BUS.post(eventBlock);
        if( eventBlock.getResult() == Event.Result.DENY )
        {
        	newBlock = eventBlock.replacement;
        }
		// Determine modded block metadata
        BiomeEvent.GetVillageBlockMeta eventMeta = new BiomeEvent.GetVillageBlockMeta(biome, block, meta);
        MinecraftForge.TERRAIN_GEN_BUS.post(eventMeta);
        if( eventMeta.getResult() == Event.Result.DENY )
        {
        	newMeta = eventMeta.replacement;
        }
        
        if( null != newBlock && newMeta >= 0 )
        {
        	// Mod-based replacement
            return new BiomeSpecificBlock(newBlock, newMeta);
        }
        else if( null == newBlock && newMeta < 0 )
        {
        	// Default replacement
        	if( biome == BiomeGenBase.desert || biome == BiomeGenBase.desertHills )
        	{
        		return getDesertReplacement(block, meta);
        	}
        	else
        	{
        		return new BiomeSpecificBlock(block, meta);
        	}
        }
        else
        {
        	// Hard cases - events replaced one value but not the other
        	if( null != newBlock )
        	{
        		// Replaced the ID, keep the meta
        		if( biome == BiomeGenBase.desert || biome == BiomeGenBase.desertHills )
        		{
        			BiomeSpecificBlock desertReplacement = getDesertReplacement(block, meta);
        			return new BiomeSpecificBlock(newBlock, desertReplacement.metadata);
        		}
        		else
        		{
        			return new BiomeSpecificBlock(newBlock, meta);
        		}
        	}
        	else
        	{
        		// Replaced the meta, kept the ID
        		if( biome == BiomeGenBase.desert || biome == BiomeGenBase.desertHills )
        		{
        			BiomeSpecificBlock desertReplacement = getDesertReplacement(block, meta);
        			return new BiomeSpecificBlock(desertReplacement.block, newMeta);
        		}
        		else
        		{
        			return new BiomeSpecificBlock(block, newMeta);
        		}
        	}
        }
	}
	
	protected BiomeSpecificBlock(Block blockInstance, int meta)
	{
		metadata = meta;
		block = blockInstance;
	}
	
	private static void addDesertReplacement(Block from, Integer fromMeta, Block to, Integer toMeta)
	{
		desertReplaceMap.put(
				ImmutablePair.<Block, Integer>of(from, fromMeta),
				ImmutablePair.<Block, Integer>of(to, toMeta));
	}
	static
	{
		desertReplaceMap = new HashMap<Pair<Block,Integer>, Pair<Block,Integer>>();
		
		// Fill in default Minecraft replacements.
		addDesertReplacement(Blocks.log, -1, Blocks.sandstone, 0);
		addDesertReplacement(Blocks.planks, -1, Blocks.sandstone, 0);
		addDesertReplacement(Blocks.cobblestone, -1, Blocks.sandstone, 0);
		addDesertReplacement(Blocks.mossy_cobblestone, -1, Blocks.sandstone, 1);
		addDesertReplacement(Blocks.gravel, -1, Blocks.sandstone, 0);
		addDesertReplacement(Blocks.oak_stairs, -1, Blocks.sandstone_stairs, -1);
		addDesertReplacement(Blocks.jungle_stairs, -1, Blocks.sandstone_stairs, -1);
		addDesertReplacement(Blocks.stone_stairs, -1, Blocks.sandstone_stairs, -1);
		addDesertReplacement(Blocks.birch_stairs, -1, Blocks.quartz_stairs, -1);
		addDesertReplacement(Blocks.spruce_stairs, -1, Blocks.stone_brick_stairs, -1);
		addDesertReplacement(Blocks.acacia_stairs, -1, Blocks.sandstone_stairs, -1);
		addDesertReplacement(Blocks.dark_oak_stairs, -1, Blocks.stone_brick_stairs, -1);
		// Stone and cobblestone slabs
		addDesertReplacement(Blocks.stone_slab, 0, Blocks.stone_slab, 1);
		addDesertReplacement(Blocks.stone_slab, 3, Blocks.stone_slab, 1);
		addDesertReplacement(Blocks.stone_slab, 8, Blocks.stone_slab, 9);
		addDesertReplacement(Blocks.stone_slab, 11, Blocks.stone_slab, 9);
		addDesertReplacement(Blocks.double_stone_slab, 0, Blocks.double_stone_slab, 1);
		addDesertReplacement(Blocks.double_stone_slab, 3, Blocks.double_stone_slab, 1);
		// Wooden slabs
		for( int meta = 0; meta <= 5; ++ meta )
		{
			addDesertReplacement(Blocks.wooden_slab, meta, Blocks.stone_slab, 1);
			addDesertReplacement(Blocks.wooden_slab, meta + 8, Blocks.stone_slab, 9);
			addDesertReplacement(Blocks.double_wooden_slab, meta, Blocks.double_stone_slab, 1);
		}
		// Dirt to sand
		addDesertReplacement(Blocks.dirt, -1, Blocks.sand, 9);
	}
}
