package as.taverns.gen;

import java.util.List;
import java.util.Random;

import as.common.gen.BiomeSpecificBlock;
import as.taverns.Settings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.util.ForgeDirection;

public class ComponentVillageChickencoop extends ComponentVillageBase
{
    public static boolean initialized = false;;
    
    public static void init(Settings config)
    {
    	if( !initialized )
    	{
    		initialized = true;
            MapGenStructureIO.func_143031_a(ComponentVillageChickencoop.class, "taverns:ViCC");
    	}
    }

    public static boolean postInitialized = false;
    
    private static final int HEIGHT = 5;

    public static Object buildComponent(StructureVillagePieces.Start startPiece, List pieces, Random random,
    		int x, int y, int z, int direction, int type)
    {
    	StructureBoundingBox structBB = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 7, HEIGHT, 5, direction);
    	if( canVillageGoDeeper(structBB) )
    	{
            if( null == StructureComponent.findIntersecting(pieces, structBB) )
            {
                    return new ComponentVillageChickencoop(startPiece, type, random, structBB, direction);
            }
    	}
    	return null;
    }
    
    private int averageGroundLevel = -1;
    private int chckenSpawned;

    public ComponentVillageChickencoop() {}
    
    public ComponentVillageChickencoop(StructureVillagePieces.Start startPiece, int type, Random rnd, StructureBoundingBox structBB, int direction)
	{
		super(startPiece, type, rnd, structBB, direction);
        chckenSpawned = rnd.nextInt(2);
    }

	/** writeNBTTag */
    @Override
    protected void func_143012_a(NBTTagCompound par1NBTTagCompound)
    {
        super.func_143012_a(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("ChickenSpawned", chckenSpawned);
    }

	/** readNBTTag */
    @Override
    protected void func_143011_b(NBTTagCompound par1NBTTagCompound)
    {
        super.func_143011_b(par1NBTTagCompound);
        chckenSpawned = par1NBTTagCompound.getInteger("ChickenSpawned");
    }

	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox bb)
	{
		if( averageGroundLevel < 0 )
        {
            averageGroundLevel = getAverageGroundLevel(world, bb);
            if (averageGroundLevel < 0)
            {
                return true;
            }
            boundingBox.offset(0, this.averageGroundLevel - boundingBox.maxY + HEIGHT - 2, 0);
        }
        
		BiomeGenBase biome = (startPiece == null ? null : startPiece.biome);

		BiomeSpecificBlock planks = BiomeSpecificBlock.query(Blocks.planks, 0, biome);
		BiomeSpecificBlock dirt = BiomeSpecificBlock.query(Blocks.dirt, 0, biome);
		BiomeSpecificBlock gravel = BiomeSpecificBlock.query(Blocks.gravel, 0, biome);
		BiomeSpecificBlock woodUp = BiomeSpecificBlock.query(Blocks.log, 0, biome);
		BiomeSpecificBlock woodNorth = getSpecificBlock(Blocks.log, ForgeDirection.NORTH, biome);
		BiomeSpecificBlock woodWest = getSpecificBlock(Blocks.log, ForgeDirection.WEST, biome);
		BiomeSpecificBlock fence = BiomeSpecificBlock.query(Blocks.fence, 0, biome);
		BiomeSpecificBlock woodSlab = getWoodSlab(0, ForgeDirection.DOWN, biome);
		BiomeSpecificBlock woodSlabUp = getWoodSlab(0, ForgeDirection.UP, biome);

		BiomeSpecificBlock roofNorth = getSpecificBlock(Blocks.oak_stairs, ForgeDirection.NORTH, biome);
		BiomeSpecificBlock roofSouth = getSpecificBlock(Blocks.oak_stairs, ForgeDirection.SOUTH, biome);

		// Clear area for the coop
        fill(world, bb, 0,0,0, 7,HEIGHT+1,5, BiomeSpecificBlock.air, BiomeSpecificBlock.air);
        for( int xx = 0; xx < 8; ++ xx )
        {
            for( int zz = 0; zz < 6; ++ zz)
            {
                clearCurrentPositionBlocksUpwards(world, xx,0,zz, bb);
                fillDownwards(world, Blocks.dirt, 0, xx,-1,zz, bb);
            }
        }
        
        // Ground
        fill(world, bb, 0,0,0, 7,0,5, dirt, dirt); // Dirt around the place
        fill(world, bb, 3,0,2, 6,0,3, gravel, gravel); // Chicken area

        // Torches
        placeTorch(world, bb, 0,2,0, ForgeDirection.SOUTH);
        placeTorch(world, bb, 3,2,0, ForgeDirection.SOUTH);
        
        // Wood structure
        for( int yy = 1; yy <= 2; ++ yy )
        {
        	placeBlock(world, woodUp, 0,yy,1, bb);
        	placeBlock(world, woodUp, 3,yy,1, bb);
        	placeBlock(world, woodUp, 7,yy,1, bb);
        	placeBlock(world, woodUp, 0,yy,4, bb);
        	placeBlock(world, woodUp, 3,yy,4, bb);
        	placeBlock(world, woodUp, 7,yy,4, bb);
        }
        for( int zz = 1; zz <= 4; ++ zz )
        {
        	placeBlock(world, woodNorth, 0,3,zz, bb);
        	placeBlock(world, woodNorth, 3,3,zz, bb);
        	placeBlock(world, woodNorth, 7,3,zz, bb);
        }
        
        // Fences/Walls
        fill(world, bb, 4,1,1, 6,2,1, fence, fence);
        fill(world, bb, 4,1,4, 6,2,4, fence, fence);
        placeBlock(world, planks, 7,1,2, bb);
        placeBlock(world, planks, 7,1,3, bb);
        placeBlock(world, fence, 7,2,2, bb);
        placeBlock(world, fence, 7,2,3, bb);
        placeBlock(world, fence, 2,1,1, bb);
        placeBlock(world, fence, 0,1,2, bb);
        placeBlock(world, fence, 0,1,3, bb);
        placeBlock(world, fence, 1,1,4, bb);
        placeBlock(world, fence, 2,1,4, bb);
        placeBlock(world, Blocks.fence_gate, ForgeDirection.NORTH, 1,1,1, bb);

        // Roofing
        for( int xx = 0; xx <= 7; ++ xx )
        {
        	placeBlock(world, roofNorth, xx,3,0, bb);
        	placeBlock(world, roofNorth, xx,4,1, bb);
        	placeBlock(world, woodSlab, xx,5,2, bb);
        	placeBlock(world, woodSlab, xx,5,3, bb);
        	placeBlock(world, woodSlabUp, xx,4,2, bb);
        	placeBlock(world, woodSlabUp, xx,4,3, bb);
        	placeBlock(world, roofSouth, xx,4,4, bb);
        	placeBlock(world, roofSouth, xx,3,5, bb);
        }
        
        // Decoration
        placeBlock(world, Blocks.hay_block, random.nextInt(3) * 4, 4,1,3, bb);
        placeBlock(world, Blocks.hay_block, random.nextInt(3) * 4, 5,1,3, bb);
        placeBlock(world, Blocks.hay_block, random.nextInt(3) * 4, 6,1,3, bb);

        // Chicken
        chckenSpawned += spawnEntity(world, bb, 4,2,3, 1 - chckenSpawned);
        chckenSpawned += spawnEntity(world, bb, 5,2,3, 2 - chckenSpawned);
        chckenSpawned += spawnEntity(world, bb, 6,2,3, 3 - chckenSpawned);
        
        return true;
	}

	@Override
	protected EntityLivingBase getEntity(World world, int index)
	{
		return new EntityChicken(world);
	}

}
