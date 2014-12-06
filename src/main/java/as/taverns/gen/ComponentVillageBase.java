package as.taverns.gen;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import as.common.gen.BiomeSpecificBlock;
import as.taverns.Taverns;
import as.taverns.direction.Dir;

import com.google.common.collect.Sets;

public abstract class ComponentVillageBase extends StructureVillagePieces.Village
{
	protected StructureVillagePieces.Start startPiece;
	
	public ComponentVillageBase() {}
	
	protected ComponentVillageBase(StructureVillagePieces.Start startPiece, int type, Random rnd, StructureBoundingBox structBB, int direction)
	{
        super(startPiece, type);
        coordBaseMode = direction;
        boundingBox = structBB;
		this.startPiece = startPiece;
	}

	// Helper methods for less writing and preparation for 1.7 changes
	protected void fill(World worldObj, StructureBoundingBox structBB,
			int minX, int minY, int minZ, int maxX, int maxY, int maxZ,
			Block placeBlock, Block replaceBlock, boolean alwaysreplace)
	{
		fillWithBlocks(worldObj, structBB,
				minX, minY, minZ, maxX, maxY, maxZ,
				placeBlock, replaceBlock, alwaysreplace);
	}

	protected void fill(World worldObj, StructureBoundingBox structBB,
			int minX, int minY, int minZ, int maxX, int maxY, int maxZ,
			BiomeSpecificBlock placeBlock, BiomeSpecificBlock replaceBlock, boolean alwaysreplace)
	{
		fillWithMetadataBlocks(worldObj, structBB,
				minX, minY, minZ, maxX, maxY, maxZ,
				placeBlock.block, placeBlock.metadata, replaceBlock.block, replaceBlock.metadata, alwaysreplace);
	}

	protected void fill(World worldObj, StructureBoundingBox structBB,
			int minX, int minY, int minZ, int maxX, int maxY, int maxZ,
			Block placeBlock, Block replaceBlock)
	{
		fillWithBlocks(worldObj, structBB,
				minX, minY, minZ, maxX, maxY, maxZ,
				placeBlock, replaceBlock, false);
	}
	
	protected void fill(World worldObj, StructureBoundingBox structBB,
			int minX, int minY, int minZ, int maxX, int maxY, int maxZ,
			BiomeSpecificBlock placeBlock, BiomeSpecificBlock replaceBlock)
	{
        for( int x = minX; x <= maxX; ++ x )
        {
            for( int y = minY; y <= maxY; ++ y )
            {
                for( int z = minZ; z <= maxZ; ++ z )
                {
            		if( x != minX && x != maxX && y != minY && y != maxY && z != minZ && z != maxZ )
            		{
            			// Not on the sides
            			placeBlock(worldObj, replaceBlock, x, y, z, structBB);
            		}
            		else
            		{
            			placeBlock(worldObj, placeBlock, x, y, z, structBB);
            		}
            	}
            }
        }
	}

	protected void fillRandomly(World world, StructureBoundingBox structBB, Random rnd, float chance,
			int minX, int minY, int minZ, int maxX, int maxY, int maxZ,
			Block placeBlock, Block replaceBlock)
	{
		randomlyFillWithBlocks(world, structBB, rnd, chance,
				minX, minY, minZ, maxX, maxY, maxZ,
				placeBlock, replaceBlock, false);
	}
	
	protected void fillRandomly(World worldObj, StructureBoundingBox structBB, Random rnd, float chance,
			int minX, int minY, int minZ, int maxX, int maxY, int maxZ,
			BiomeSpecificBlock placeBlock, BiomeSpecificBlock replaceBlock)
	{
        for( int x = minX; x <= maxX; ++ x )
        {
            for( int y = minY; y <= maxY; ++ y )
            {
                for( int z = minZ; z <= maxZ; ++ z )
                {
                	if( rnd.nextFloat() <= chance )
                	{
                		if( x != minX && x != maxX && y != minY && y != maxY && z != minZ && z != maxZ )
                		{
                			// Not on the sides
                			placeBlock(worldObj, replaceBlock, x, y, z, structBB);
                		}
                		else
                		{
                			placeBlock(worldObj, placeBlock, x, y, z, structBB);
                		}
                	}
                }
            }
        }
	}

	protected void fillDownwards(World world,
			Block block, int par3, int xx, int par5, int zz,
			StructureBoundingBox structBB)
	{
		func_151554_b/*fillDownwards*/(world, block, par3, xx, par5, zz, structBB);
	}

	protected void placeBlock(World world, Block block,
			int metadata, int posX, int posY, int posZ,
			StructureBoundingBox structBB)
	{
		placeBlockAtCurrentPosition(world, block, metadata, posX, posY, posZ, structBB);
	}
	
	protected void placeBlock(World world, Block block, ForgeDirection dir,
			int posX, int posY, int posZ, StructureBoundingBox structBB)
	{
		placeBlockAtCurrentPosition(world, block, Dir.getMeta(block, dir, coordBaseMode), posX, posY, posZ, structBB);
	}
	
	protected void placeBlock(World world, BiomeSpecificBlock block,
			int posX, int posY, int posZ, StructureBoundingBox structBB)
	{
        int globalX = this.getXWithOffset(posX, posZ);
        int globalY = this.getYWithOffset(posY);
        int globalZ = this.getZWithOffset(posX, posZ);

        if( structBB.isVecInside(globalX, globalY, globalZ) )
        {
        	world.setBlock(globalX, globalY, globalZ, block.block, block.metadata, 2);
        }
	}
	
	protected void placeAir(World world, int posX, int posY, int posZ, StructureBoundingBox structBB)
	{
		placeBlockAtCurrentPosition(world, Blocks.air, 0, posX, posY, posZ, structBB);
	}

	protected void placeDoor(World world, StructureBoundingBox structBB,
			Random rnd, int posX, int posY, int posZ, ForgeDirection dir)
	{
		placeDoorAtCurrentPosition(world, structBB, rnd, posX, posY, posZ, Dir.getMeta(Blocks.wooden_door, dir, coordBaseMode));
	}
	
	/**
	 * Warning: Sets the position *below* this one to air!
	 */
	protected void placeTorch(World world, StructureBoundingBox structBB,
			int posX, int posY, int posZ, ForgeDirection dir)
	{
        placeBlock(world, Blocks.stone, 0, posX, posY - 1, posZ, structBB);
        placeBlock(world, Blocks.torch, dir, posX, posY, posZ, structBB);
        placeAir(world, posX, posY - 1, posZ, structBB);
	}
	
	protected void setMetadata(World world, int metadata,
			int posX, int posY, int posZ, StructureBoundingBox structBB)
	{
		int globalX = getXWithOffset(posX, posZ);
		int globalY = getYWithOffset(posY);
		int globalZ = getZWithOffset(posX, posZ);
		
		if( structBB.isVecInside(globalX, globalY, globalZ) )
		{
			world.setBlockMetadataWithNotify(globalX, globalY, globalZ, metadata, 2);
		}
	}
	
	/**
	 * Does nothing if amount <= 0.
	 * Else tries to spawn entities by calling getEntity(index) as often as needed.
	 * 
	 * @return Amount of entities spawned
	 */
    protected int spawnEntity(World world, StructureBoundingBox structBB, int posX, int posY, int posZ, int amount)
    {
    	int spawned = 0;
    	for( int idx = 0; idx < amount; ++ idx )
    	{
    		int globalX = getXWithOffset(posX, posZ);
    		int globalY = getYWithOffset(posY);
    		int globalZ = getZWithOffset(posX, posZ);
    		
    		if( structBB.isVecInside(globalX, globalY, globalZ) )
    		{
    			EntityLivingBase entity = getEntity(world, idx);
    			if( null != entity )
    			{
    				++ spawned;
    				entity.setLocationAndAngles((double)globalX + 0.5d, (double)globalY, (double)globalZ + 0.5d, 0.5f, 0.0f);
    				world.spawnEntityInWorld(entity);
    			}
    		}
    	}
    	
    	return spawned;
    }

    /**
     * Spawn a single villager. Return that villager or <i>null</i> if spawning failed, for further modification.
     */
    protected EntityVillager spawnVillager(World world, StructureBoundingBox structBB, int posX, int posY, int posZ)
    {
    	return spawnVillager(world, structBB, posX, posY, posZ, 0);
    }

    /**
     * Spawn a single villager. Return that villager or <i>null</i> if spawning failed, for further modification.
     */
    protected EntityVillager spawnVillager(World world, StructureBoundingBox structBB, int posX, int posY, int posZ, int index)
    {
		int globalX = getXWithOffset(posX, posZ);
		int globalY = getYWithOffset(posY);
		int globalZ = getZWithOffset(posX, posZ);

		EntityVillager entityvillager = null;
		if( structBB.isVecInside(globalX, globalY, globalZ) )
		{
			entityvillager = new EntityVillager(world, getVillagerType(index));
            entityvillager.setLocationAndAngles((double)globalX + 0.5D, (double)globalY, (double)globalZ + 0.5D, 0.0F, 0.0F);
            world.spawnEntityInWorld(entityvillager);
        }
		
		return entityvillager;
    }

    /**
     * Override this method for custom (non-villager) entity spawning.
     * 
     * @param world the World to spawn the entity in
     * @param index the index number of the entity
     * @return the EntityLivingBase to spawn, or null if we don't spawn any
     */
    protected EntityLivingBase getEntity(World world, int index)
    {
    	return null;
    }
    
    public BiomeSpecificBlock getSpecificBlock(Block block, ForgeDirection dir, BiomeGenBase biome)
    {
    	return BiomeSpecificBlock.query(block, Dir.getMeta(block, dir, coordBaseMode), biome);
    }
    
    // Mod-specific area
    
    private Set<Block> stairBlocks = Sets.<Block>newHashSet(
    		Blocks.brick_stairs, Blocks.stone_stairs, Blocks.nether_brick_stairs, Blocks.quartz_stairs, Blocks.sandstone_stairs,
    		Blocks.stone_brick_stairs, Blocks.birch_stairs, Blocks.jungle_stairs, Blocks.oak_stairs, Blocks.spruce_stairs,
    		Blocks.acacia_stairs, Blocks.dark_oak_stairs
    		);
    
    /**
     * Stairs to walk on
     */
    public BiomeSpecificBlock getStairs(Block wood, ForgeDirection dir, BiomeGenBase biome)
    {
		// Check which planks we'll get
		BiomeSpecificBlock planks = BiomeSpecificBlock.query(Blocks.planks, 0, biome);

    	// Check what we get for default wood replacement
    	BiomeSpecificBlock stairs = BiomeSpecificBlock.query(wood, Dir.getMeta(Blocks.oak_stairs, dir, coordBaseMode), biome);
    	// If it's not a stair and we can derive one from the plank replacement, do that.
    	if( !stairBlocks.contains(stairs.block) )
    	{
    		Block replacementStairBlock = null;
    		// Planks to corresponding stairs
    		if( planks.block == Blocks.planks )
    		{
    			switch( planks.metadata )
    			{
    				case 0: replacementStairBlock = Blocks.oak_stairs; break;
    				case 1: replacementStairBlock = Blocks.spruce_stairs; break;
    				case 2: replacementStairBlock = Blocks.birch_stairs; break;
    				case 3: replacementStairBlock = Blocks.jungle_stairs; break;
    				case 4: replacementStairBlock = Blocks.acacia_stairs; break;
    				case 5: replacementStairBlock = Blocks.dark_oak_stairs; break;
    			}
    		}
    		if( planks.block == Blocks.brick_block )
    		{
    			replacementStairBlock = Blocks.brick_stairs;
    		}
    		if( planks.block == Blocks.cobblestone || planks.block == Blocks.mossy_cobblestone )
    		{
    			replacementStairBlock = Blocks.stone_stairs;
    		}
    		if( planks.block == Blocks.nether_brick || planks.block == Blocks.netherrack )
    		{
    			replacementStairBlock = Blocks.nether_brick_stairs;
    		}
    		if( planks.block == Blocks.quartz_block )
    		{
    			replacementStairBlock = Blocks.quartz_stairs;
    		}
    		if( planks.block == Blocks.sandstone )
    		{
    			replacementStairBlock = Blocks.sandstone_stairs;
    		}
    		if( planks.block == Blocks.stonebrick || planks.block == Blocks.stone )
    		{
    			replacementStairBlock = Blocks.stone_brick_stairs;
    		}
    		
    		if( null != replacementStairBlock )
    		{
    			return BiomeSpecificBlock.queryVanilla(replacementStairBlock, Dir.getMeta(Blocks.oak_stairs, dir, coordBaseMode), null);
    		}
    	}
    	// Just use default wood
    	return stairs;
    }
    
    /**
     * Benches to sit on (in vanilla: stairs)
     */
    public BiomeSpecificBlock getBench(Block wood, ForgeDirection dir, BiomeGenBase biome)
    {
    	return getStairs(wood, dir, biome);
    }
    
    public BiomeSpecificBlock getWoodSlab(int plankMeta, ForgeDirection dir, BiomeGenBase biome)
    {
		// Check which planks we'll get
		BiomeSpecificBlock planks = BiomeSpecificBlock.query(Blocks.planks, plankMeta, biome);

    	BiomeSpecificBlock slab = BiomeSpecificBlock.query(Blocks.wooden_slab, plankMeta + (dir == ForgeDirection.UP ? 8 : 0), biome);
    	// If we get a vanilla wood slab, see if we can make some better guesses as to the replacement
    	if( slab.block == Blocks.stone_slab )
    	{
    		// For planks being replaced by vanilla wood, use the same kind of wood
    		if( planks.block == Blocks.planks )
    		{
    			return BiomeSpecificBlock.queryVanilla(Blocks.wooden_slab, planks.metadata + (dir == ForgeDirection.UP ? 8 : 0), biome);
    		}
    		if( planks.block == Blocks.stone )
    		{
    			return BiomeSpecificBlock.queryVanilla(Blocks.stone_slab, 0 + (dir == ForgeDirection.UP ? 8 : 0), biome);
    		}
    		if( planks.block == Blocks.sandstone )
    		{
    			return BiomeSpecificBlock.queryVanilla(Blocks.stone_slab, 1 + (dir == ForgeDirection.UP ? 8 : 0), biome);
    		}
    		if( planks.block == Blocks.cobblestone || planks.block == Blocks.mossy_cobblestone )
    		{
    			return BiomeSpecificBlock.queryVanilla(Blocks.stone_slab, 3 + (dir == ForgeDirection.UP ? 8 : 0), biome);
    		}
    		if( planks.block == Blocks.brick_block )
    		{
    			return BiomeSpecificBlock.queryVanilla(Blocks.stone_slab, 4 + (dir == ForgeDirection.UP ? 8 : 0), biome);
    		}
    		if( planks.block == Blocks.stonebrick )
    		{
    			return BiomeSpecificBlock.queryVanilla(Blocks.stone_slab, 5 + (dir == ForgeDirection.UP ? 8 : 0), biome);
    		}
    		if( planks.block == Blocks.nether_brick || planks.block == Blocks.netherrack )
    		{
    			return BiomeSpecificBlock.queryVanilla(Blocks.stone_slab, 6 + (dir == ForgeDirection.UP ? 8 : 0), biome);
    		}
    		if( planks.block == Blocks.quartz_block )
    		{
    			return BiomeSpecificBlock.queryVanilla(Blocks.stone_slab, 7 + (dir == ForgeDirection.UP ? 8 : 0), biome);
    		}
    	}
    	// Just use default wood
    	return slab;
    }
    
    public BiomeSpecificBlock getMossyCobble(BiomeGenBase biome)
    {
		return BiomeSpecificBlock.query(Blocks.mossy_cobblestone, 0, biome);
    }
}
