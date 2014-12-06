package as.taverns.gen;

import java.util.List;
import java.util.Random;

import as.common.gen.BiomeSpecificBlock;
import as.taverns.Settings;
import as.taverns.Taverns;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Variant on the default tavern
 */
public class ComponentVillageTavern2 extends ComponentVillageBase
{
    public static boolean initialized = false;;
    
    public static void init(Settings config)
    {
    	if( !initialized )
    	{
    		initialized = true;
    		// Make sure the base tavern is initalised too.
    		// We need it for the tavern chest
    		ComponentVillageTavern.init(config);
    		
            MapGenStructureIO.func_143031_a(ComponentVillageTavern2.class, "taverns:ViT2");
    	}
    }
    
    private static final int HEIGHT = 10;
    
    public static Object buildComponent(StructureVillagePieces.Start startPiece, List pieces, Random random,
    		int x, int y, int z, int direction, int type)
    {
    	StructureBoundingBox structBB = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 14, HEIGHT, 8, direction);
    	if( canVillageGoDeeper(structBB) )
    	{
            if( null == StructureComponent.findIntersecting(pieces, structBB) )
            {
                    return new ComponentVillageTavern2(startPiece, type, random, structBB, direction);
            }
    	}
    	return null;
    }
    
    private int averageGroundLevel = -1;

    // Because Minecraft likes to call the generation algorithm four times at the same location due to chunk boundaries ...
    private boolean hasMadeChest;
    private int carpetColor;
    private int flower;
    private boolean villagerSpawned = false;

    public ComponentVillageTavern2() {}
    
	public ComponentVillageTavern2(StructureVillagePieces.Start startPiece, int type, Random rnd, StructureBoundingBox structBB, int direction)
	{
		super(startPiece, type, rnd, structBB, direction);
        carpetColor = rnd.nextInt(16);
        flower = rnd.nextInt(16) - 4; // Negative numbers = no flowerpot
    }

	/** writeNBTTag */
    @Override
    protected void func_143012_a(NBTTagCompound par1NBTTagCompound)
    {
        super.func_143012_a(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("Chest", hasMadeChest);
        par1NBTTagCompound.setInteger("CarpetC", carpetColor);
        par1NBTTagCompound.setInteger("Flower", flower);
        par1NBTTagCompound.setBoolean("VillagerSpawned", villagerSpawned);
    }

	/** readNBTTag */
    @Override
    protected void func_143011_b(NBTTagCompound par1NBTTagCompound)
    {
        super.func_143011_b(par1NBTTagCompound);
        hasMadeChest = par1NBTTagCompound.getBoolean("Chest");
        carpetColor = par1NBTTagCompound.getInteger("CarpetC");
        flower = par1NBTTagCompound.getInteger("Flower");
        villagerSpawned = par1NBTTagCompound.getBoolean("VillagerSpawned");
    }

	@Override
	public boolean addComponentParts(World world, Random rnd, StructureBoundingBox bb)
	{
        if( averageGroundLevel < 0 )
        {
            averageGroundLevel = getAverageGroundLevel(world, bb) + 5;
            if (averageGroundLevel < 0)
            {
                return true;
            }
            boundingBox.offset(0, this.averageGroundLevel - boundingBox.maxY + HEIGHT - 2, 0);
        }

		BiomeGenBase biome = (startPiece == null ? null : startPiece.biome);
		
		// Blocks used (for the most part)
		BiomeSpecificBlock planks = BiomeSpecificBlock.query(Blocks.planks, 0, biome);
		BiomeSpecificBlock cobble = BiomeSpecificBlock.query(Blocks.cobblestone, 0, biome);
		BiomeSpecificBlock dirt = BiomeSpecificBlock.query(Blocks.dirt, 0, biome);
		BiomeSpecificBlock gravel = BiomeSpecificBlock.query(Blocks.gravel, 0, biome);
		BiomeSpecificBlock woodUp = BiomeSpecificBlock.query(Blocks.log, 0, biome);
		BiomeSpecificBlock woodWest = getSpecificBlock(Blocks.log, ForgeDirection.WEST, biome);
		BiomeSpecificBlock glassPane = BiomeSpecificBlock.query(Blocks.glass_pane, 0, biome);
		BiomeSpecificBlock fence = BiomeSpecificBlock.query(Blocks.fence, 0, biome);
		BiomeSpecificBlock carpet = BiomeSpecificBlock.query(Blocks.carpet, carpetColor, biome);
		
		// Stairs, two variants - roof and actual proper stairs (also for benches)
		BiomeSpecificBlock roofNorth = getSpecificBlock(Blocks.oak_stairs, ForgeDirection.NORTH, biome);
		BiomeSpecificBlock roofSouth = getSpecificBlock(Blocks.oak_stairs, ForgeDirection.SOUTH, biome);
		BiomeSpecificBlock roofWest = getSpecificBlock(Blocks.oak_stairs, ForgeDirection.WEST, biome);
		BiomeSpecificBlock roofEast = getSpecificBlock(Blocks.oak_stairs, ForgeDirection.EAST, biome);
		
		BiomeSpecificBlock stairsEast = getStairs(Blocks.oak_stairs, ForgeDirection.EAST, biome);

		BiomeSpecificBlock benchWest = getBench(Blocks.oak_stairs, ForgeDirection.WEST, biome);
		BiomeSpecificBlock benchEast = getBench(Blocks.oak_stairs, ForgeDirection.EAST, biome);
		BiomeSpecificBlock darkBenchNorth = getBench(Blocks.spruce_stairs, ForgeDirection.NORTH, biome);
		BiomeSpecificBlock darkBenchEast = getBench(Blocks.spruce_stairs, ForgeDirection.EAST, biome);
		
        // Clear area for the tavern building
        fill(world, bb, 0,0,0, 14,9,7, BiomeSpecificBlock.air, BiomeSpecificBlock.air);
        for( int xx = 0; xx < 14; ++ xx )
        {
            for( int zz = 0; zz < 8; ++ zz)
            {
                clearCurrentPositionBlocksUpwards(world, xx,0,zz, bb);
                fillDownwards(world, Blocks.cobblestone, 0, xx,-1,zz, bb);
            }
        }
                
        /*
        if( !hasMadeChest )
        {
        	int chestX = getXWithOffset(9, 2);
        	int chestY = getYWithOffset(5);
        	int chestZ = getZWithOffset(9, 2);
        	if( bb.isVecInside(chestX, chestY, chestZ) )
        	{
        		hasMadeChest = true;
                generateStructureChestContents(world, bb, rnd, 9,5,2,
                		ChestGenHooks.getItems(ComponentVillageTavern.TAVERN_CHEST, rnd),
                		ChestGenHooks.getCount(ComponentVillageTavern.TAVERN_CHEST, rnd));
        	}
        }
        */
        
        /*
        if( !villagerSpawned )
        {
        	EntityVillager villager = spawnVillager(world, bb, 2,1,2);
        	if( null != villager )
        	{
        		villagerSpawned = true;
        		int globalX = getXWithOffset(2, 2);
        		int globalY = getYWithOffset(1);
        		int globalZ = getZWithOffset(2, 2);
        		villager.tasks.addTask(4, new GuardAI(villager, new ChunkCoordinates(globalX, globalY, globalZ),
        				1.0f, 48.0f, new TimePeriod(0.15, 0.75), false));
        	}
        }
        */
        
        return true;
	}
	
	@Override
	protected int getVillagerType(int alreadySpawned)
	{
		return (Taverns.config.barWenchID > 0 ? Taverns.config.barWenchID : super.getVillagerType(alreadySpawned));
	}
}
