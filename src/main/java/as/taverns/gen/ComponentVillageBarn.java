package as.taverns.gen;

import java.util.List;
import java.util.Random;

import as.common.ai.GuardAI;
import as.common.gen.BiomeSpecificBlock;
import as.common.util.TimePeriod;
import as.taverns.Settings;
import as.taverns.Taverns;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.util.ForgeDirection;

public class ComponentVillageBarn extends ComponentVillageBase
{
    public static boolean initialized = false;;
    
    public static void init(Settings config)
    {
    	if( !initialized )
    	{
    		initialized = true;
            MapGenStructureIO.func_143031_a(ComponentVillageBarn.class, "taverns:ViB");
    	}
    }

    private static final int HEIGHT = 6;

    public static Object buildComponent(StructureVillagePieces.Start startPiece, List pieces, Random random,
    		int x, int y, int z, int direction, int type)
    {
    	boolean extendedBuilding = (random.nextInt(3) == 0);
    	StructureBoundingBox structBB = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, (extendedBuilding ? 13 : 9), HEIGHT, 7, direction);
    	if( canVillageGoDeeper(structBB) )
    	{
            if( null == StructureComponent.findIntersecting(pieces, structBB) )
            {
                    return new ComponentVillageBarn(startPiece, type, random, structBB, direction, extendedBuilding);
            }
    	}
    	return null;
    }
    
    private int averageGroundLevel = -1;
    
    private boolean hasExtension = false;
    private int woolColor = 0;
    private int sheepSpawned = 0;
    private boolean villagerSpawned = false;

    public ComponentVillageBarn() {}
    
	public ComponentVillageBarn(StructureVillagePieces.Start startPiece, int type, Random rnd, StructureBoundingBox structBB, int direction, boolean extension)
	{
		super(startPiece, type, rnd, structBB, direction);
        hasExtension = extension;
        woolColor = rnd.nextInt(16);
        sheepSpawned = rnd.nextInt(2); // Half of the time just one sheep
    }

	/** writeNBTTag */
    @Override
    protected void func_143012_a(NBTTagCompound par1NBTTagCompound)
    {
        super.func_143012_a(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("HasExtension", hasExtension);
        par1NBTTagCompound.setInteger("WoolC", woolColor);
        par1NBTTagCompound.setInteger("VSheep", sheepSpawned);
        par1NBTTagCompound.setBoolean("VillagerSpawned", villagerSpawned);
    }

	/** readNBTTag */
    @Override
    protected void func_143011_b(NBTTagCompound par1NBTTagCompound)
    {
        super.func_143011_b(par1NBTTagCompound);
        hasExtension = par1NBTTagCompound.getBoolean("HasExtension");
        woolColor = par1NBTTagCompound.getInteger("WoolC");
        sheepSpawned = par1NBTTagCompound.getInteger("VSheep");
        villagerSpawned = par1NBTTagCompound.getBoolean("VillagerSpawned");
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
        
        int width = (hasExtension ? 13 : 9);

		BiomeGenBase biome = (startPiece == null ? null : startPiece.biome);

		BiomeSpecificBlock planks = BiomeSpecificBlock.query(Blocks.planks, 0, biome);
		BiomeSpecificBlock cobble = BiomeSpecificBlock.query(Blocks.cobblestone, 0, biome);
		BiomeSpecificBlock dirt = BiomeSpecificBlock.query(Blocks.dirt, 0, biome);
		BiomeSpecificBlock grass = BiomeSpecificBlock.queryVanilla(Blocks.grass, 0, null);
		BiomeSpecificBlock gravel = BiomeSpecificBlock.query(Blocks.gravel, 0, biome);
		BiomeSpecificBlock woodUp = BiomeSpecificBlock.query(Blocks.log, 0, biome);
		BiomeSpecificBlock fence = BiomeSpecificBlock.query(Blocks.fence, 0, biome);
		BiomeSpecificBlock woodSlabDown = getWoodSlab(0, ForgeDirection.DOWN, biome);
		BiomeSpecificBlock woodSlabUp = getWoodSlab(0, ForgeDirection.UP, biome);
		BiomeSpecificBlock glassPane = BiomeSpecificBlock.query(Blocks.glass_pane, 0, biome);
		
		BiomeSpecificBlock roofEast = getSpecificBlock(Blocks.oak_stairs, ForgeDirection.EAST, biome);
		BiomeSpecificBlock roofWest = getSpecificBlock(Blocks.oak_stairs, ForgeDirection.WEST, biome);

        // Clear area for the barn building
        fill(world, bb, 0,0,0, width-1,5,6, BiomeSpecificBlock.air, BiomeSpecificBlock.air, false);
        for( int xx = 0; xx < width; ++ xx )
        {
            for( int zz = 0; zz < 7; ++ zz)
            {
                clearCurrentPositionBlocksUpwards(world, xx,0,zz, bb);
                fillDownwards(world, Blocks.cobblestone, 0, xx,-1,zz, bb);
            }
        }

        // Ground
        fill(world, bb, 0,0,0, width-1,0,6, dirt, dirt); // Ground
        fill(world, bb, 2,0,2, 6,0,4, grass, grass); // Grass for the sheep
        fill(world, bb, 3,0,0, 5,0,0, gravel, gravel); // Entrance
        
        // Torches
        placeTorch(world, bb, 3,2,1, ForgeDirection.DOWN);
        placeTorch(world, bb, 5,2,1, ForgeDirection.DOWN);
        placeTorch(world, bb, 0,2,5, ForgeDirection.WEST);
        placeTorch(world, bb, 8,2,5, ForgeDirection.EAST);
        placeTorch(world, bb, 1,2,6, ForgeDirection.NORTH);
        placeTorch(world, bb, 7,2,6, ForgeDirection.NORTH);
        
        // Enclosure
        fill(world, bb, 1,1,1, 7,1,1, fence, fence);
        fill(world, bb, 2,1,5, 6,1,5, fence, fence);
        fill(world, bb, 1,1,2, 1,1,4, fence, fence);
        fill(world, bb, 7,1,2, 7,1,4, fence, fence);
        placeBlock(world, Blocks.fence_gate, ForgeDirection.SOUTH, 4,1,1, bb);
        placeBlock(world, woodUp, 1,1,5, bb);
        placeBlock(world, woodUp, 7,1,5, bb);

        placeBlock(world, fence, 1,2,1, bb);
        placeBlock(world, fence, 1,3,1, bb);
        placeBlock(world, fence, 7,2,1, bb);
        placeBlock(world, fence, 7,3,1, bb);
        placeBlock(world, planks, 1,2,5, bb);
        placeBlock(world, planks, 7,2,5, bb);
        
        // Roofing
        placeBlock(world, woodSlabDown, 1,4,1, bb);
        placeBlock(world, Blocks.wool, 0, 2,4,1, bb);
        placeBlock(world, Blocks.wool, woolColor, 3,4,1, bb);
        placeBlock(world, Blocks.wool, 0, 4,4,1, bb);
        placeBlock(world, Blocks.wool, woolColor, 5,4,1, bb);
        placeBlock(world, Blocks.wool, 0, 6,4,1, bb);
        placeBlock(world, woodSlabDown, 7,4,1, bb);
        
        for( int xx = 1; xx <= 7; ++ xx )
        {
        	placeBlock(world, woodSlabDown, xx,4,2, bb);
        	placeBlock(world, woodSlabUp, xx,3,3, bb);
        	placeBlock(world, woodSlabDown, xx,3,4, bb);
        }

        for( int xx = 2; xx <= 6; ++ xx )
        {
        	placeBlock(world, woodSlabUp, xx,2,5, bb);
        }
        
        sheepSpawned += spawnEntity(world, bb, 3,1,3, 1 - sheepSpawned);
        sheepSpawned += spawnEntity(world, bb, 5,1,3, 2 - sheepSpawned);

        if( hasExtension )
        {
        	// Added building to the side
        	
        	// Light
        	placeTorch(world, bb, 9,4,0, ForgeDirection.SOUTH);
        	placeTorch(world, bb, 8,4,3, ForgeDirection.EAST);

        	// Ground
        	fill(world, bb, 7,1,1, 11,1,5, cobble, cobble);
        	placeBlock(world, Blocks.stone_slab, 3, 7,1,3, bb);
        	placeBlock(world, Blocks.stone_stairs, ForgeDirection.NORTH, 9,1,0, bb);
        	placeBlock(world, Blocks.stone_stairs, ForgeDirection.EAST, 8,1,0, bb);
        	placeBlock(world, Blocks.stone_stairs, ForgeDirection.WEST, 10,1,0, bb);
        	
        	// Walls
        	for( int yy = 2; yy <= 4; ++ yy )
        	{
        		placeBlock(world, woodUp, 7,yy,1, bb);
        		placeBlock(world, planks, 8,yy,1, bb);
        		placeBlock(world, planks, 10,yy,1, bb);
        		placeBlock(world, woodUp, 11,yy,1, bb);
        		placeBlock(world, woodUp, 7,yy,5, bb);
        		placeBlock(world, woodUp, 11,yy,5, bb);
        		placeBlock(world, planks, 7,yy,2, bb);
        		placeBlock(world, planks, 7,yy,4, bb);
        	}
        	fill(world, bb, 8,2,5, 10,4,5, planks, planks);
        	fill(world, bb, 11,2,2, 11,4,4, planks, planks);
        	placeBlock(world, planks, 9,4,1, bb);
        	placeBlock(world, planks, 7,4,3, bb);
        	placeAir(world, 7,3,3, bb);
        	for( int xx = 8; xx <= 10; ++ xx )
        	{
        		placeBlock(world, woodUp, xx,5,1, bb);
        		placeBlock(world, woodUp, xx,5,5, bb);
        	}
        	
        	placeBlock(world, glassPane, 9,3,5, bb);
        	placeBlock(world, glassPane, 11,3,3, bb);
        	placeDoor(world, bb, random, 9,2,1, ForgeDirection.SOUTH);
        	
        	// Roofing
        	for( int zz = 1; zz <= 5; ++ zz )
        	{
        		placeBlock(world, roofEast, 7,5,zz, bb);
        		placeBlock(world, roofWest, 11,5,zz, bb);
        		placeBlock(world, woodSlabDown, 8,6,zz, bb);
        		placeBlock(world, planks, 9,6,zz, bb);
        		placeBlock(world, woodSlabDown, 10,6,zz, bb);
        	}
        	
            if( !villagerSpawned )
            {
            	EntityVillager villager = spawnVillager(world, bb, 9,2,3);
            	if( null != villager )
            	{
            		villagerSpawned = true;
            		int globalX = getXWithOffset(9, 3);
            		int globalY = getYWithOffset(2);
            		int globalZ = getZWithOffset(9, 3);
            		villager.tasks.addTask(4, new GuardAI(villager, new ChunkCoordinates(globalX, globalY, globalZ),
            				5.0f, 48.0f, new TimePeriod(0.02, 0.45), false));
            	}
            }
        }
        
        return true;
	}

	@Override
	protected int getVillagerType(int alreadySpawned)
	{
		return (Taverns.config.shepherdessID > 0 ? Taverns.config.shepherdessID : super.getVillagerType(alreadySpawned));
	}
	
	@Override
	protected EntityLivingBase getEntity(World world, int index)
	{
		return new EntitySheep(world);
	}
}
