package as.taverns.gen;

import java.util.List;
import java.util.Random;

import as.common.ai.GuardAI;
import as.common.gen.BiomeSpecificBlock;
import as.common.util.TimePeriod;
import as.taverns.Settings;
import as.taverns.Taverns;
import as.taverns.direction.Dir;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.util.ForgeDirection;

public class ComponentVillageTavern extends ComponentVillageBase
{
    public static final String TAVERN_CHEST = "taverns_tavernChest";
    public static final WeightedRandomChestContent[] tavernChestContents = new WeightedRandomChestContent[]
    {
        new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 1),
        new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10),
        new WeightedRandomChestContent(Items.bed, 0, 1, 3, 30),
        new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15),
        new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15),
        new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 5),
        new WeightedRandomChestContent(Items.iron_sword, 0, 1, 1, 5),
        new WeightedRandomChestContent(Items.iron_chestplate, 0, 1, 1, 5),
        new WeightedRandomChestContent(Items.iron_helmet, 0, 1, 1, 5),
        new WeightedRandomChestContent(Items.iron_leggings, 0, 1, 1, 5),
        new WeightedRandomChestContent(Items.iron_boots, 0, 1, 1, 5),
        new WeightedRandomChestContent(new ItemStack(Blocks.obsidian), 1, 7, 5),
        new WeightedRandomChestContent(new ItemStack(Blocks.sapling), 3, 7, 5)
    };
    
    public static boolean initialized = false;;
    
    public static void init(Settings config)
    {
    	if( !initialized )
    	{
    		initialized = true;
    		ChestGenHooks.getInfo(TAVERN_CHEST).setMin(1);
    		ChestGenHooks.getInfo(TAVERN_CHEST).setMax(10);
            for( int i = 0; i < tavernChestContents.length; ++ i )
            {
            	ChestGenHooks.addItem(TAVERN_CHEST, tavernChestContents[i]);
            }
            MapGenStructureIO.func_143031_a(ComponentVillageTavern.class, "taverns:ViT");
    	}
    }
    
    public static boolean postInitialized = false;
    
    public static void postInit(Settings config)
    {
    	if( !postInitialized )
    	{
    		postInitialized = true;
            if( config.grimoireOfGaia2 )
            {
            	//ChestGenHooks.addItem(TAVERN_CHEST, new WeightedRandomChestContent(new ItemStack(GaiaItem.ShardEmerald), 1, 2, 2));
            }
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
                    return new ComponentVillageTavern(startPiece, type, random, structBB, direction);
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

    public ComponentVillageTavern() {}
    
	public ComponentVillageTavern(StructureVillagePieces.Start startPiece, int type, Random rnd, StructureBoundingBox structBB, int direction)
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
            averageGroundLevel = getAverageGroundLevel(world, bb);
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
                
        // floors
        fill(world, bb, 0,0,1, 14,0,7, dirt, dirt); // backside
        fill(world, bb, 0,0,0, 14,0,0, gravel, gravel); // front
        fill(world, bb, 1,0,1, 4,0,6, cobble, cobble); // main floor left
        fill(world, bb, 5,0,1, 12,0,6, planks, planks); // main floor right
        fill(world, bb, 1,4,1, 12,4,6, planks, planks); // upper floor
        fill(world, bb, 7,8,1, 12,8,6, planks, planks); // roof

        //// walls
        // wall - left side
        fill(world, bb, 1,1,1, 1,4,1, woodUp, woodUp);
        fill(world, bb, 1,1,2, 1,3,5, cobble, cobble);
        fill(world, bb, 1,1,6, 1,4,6, woodUp, woodUp);

        fill(world, bb, 2,3,1, 3,3,1, planks, planks);
        fill(world, bb, 2,1,1, 3,1,1, cobble, cobble);
        fill(world, bb, 2,2,1, 3,2,1, glassPane, glassPane);
        fill(world, bb, 4,1,1, 4,3,1, woodUp, woodUp);
        fill(world, bb, 5,3,1, 6,3,1, woodUp, woodUp);
        fill(world, bb, 7,1,1, 7,7,1, woodUp, woodUp);
        placeBlock(world, woodWest, 4, 3, 1, bb);
        placeBlock(world, woodWest, 7, 3, 1, bb);
        fill(world, bb, 8,1,1, 11,1,1, cobble, cobble);
        fill(world, bb, 8,2,1, 11,7,1, planks, planks);
        fill(world, bb, 8,2,1, 11,2,1, glassPane, glassPane);
        fill(world, bb, 12,1,1, 12,7,1, woodUp, woodUp);

        fill(world, bb, 12,1,2, 12,1,5, cobble, cobble);
        fill(world, bb, 12,2,2, 12,7,5, planks, planks);
        fill(world, bb, 12,2,3, 12,2,4, glassPane, glassPane);
        fill(world, bb, 12,6,3, 12,6,4, glassPane, glassPane);
        fill(world, bb, 12,1,6, 12,7,6, woodUp, woodUp);
        
        fill(world, bb, 7,1,6, 11,1,6, cobble, cobble);
        fill(world, bb, 7,2,6, 11,7,6, planks, planks);
        placeBlock(world, glassPane, 10,6,6, bb);
        fill(world, bb, 6,1,6, 6,8,6, woodUp, woodUp);
        placeBlock(world, woodUp, 5,3,6, bb);
        fill(world, bb, 4,1,6, 4,3,6, woodUp, woodUp);
        fill(world, bb, 2,1,6, 3,3,6, cobble, cobble);

        fill(world, bb, 7,5,2, 7,7,5, planks, planks);
        fill(world, bb, 6,5,5, 6,8,5, planks, planks);
                
        // roofing
        for( int xx = 0; xx <= 13; ++ xx )
        {
			placeBlock(world, roofNorth, xx, 4, 0, bb);
			placeBlock(world, roofSouth, xx, 4, 7, bb);
        }
        
        for( int zz = 0; zz <= 7; ++ zz )
        {
        	placeBlock(world, roofEast, 0, 4, zz, bb);
            placeBlock(world, roofWest, 13, 4, zz, bb);
        }
        
        for( int xx = 6; xx <= 13; ++ xx)
        {
        	placeBlock(world, roofNorth, xx, 7, 0, bb);
        	placeBlock(world, roofSouth, xx, 7, 7, bb);
        	placeBlock(world, roofNorth, xx, 8, 1, bb);
        	placeBlock(world, roofSouth, xx, 8, 6, bb);
        }
        
        for( int xx = 7; xx <= 13; ++ xx)
        {
        	placeBlock(world, roofNorth, xx, 9, 2, bb);
        	placeBlock(world, roofSouth, xx, 9, 5, bb);
        }
        
        for( int zz = 2; zz <= 5; ++ zz)
        {
        	placeBlock(world, roofEast, 6,8,zz, bb);
        }
        
        for( int zz = 3; zz <= 4; ++ zz )
        {
        	placeBlock(world, roofWest, 13,9,zz, bb);
        	placeBlock(world, roofEast, 12,9,zz, bb);        
        	placeBlock(world, roofEast, 7,9,zz, bb);
        	placeBlock(world, roofWest, 8,9,zz, bb);
        }
        
        for( int zz = 5; zz <= 7; ++ zz )
        {
        	placeBlock(world, roofEast, 5,7,zz, bb);
        }
        
        fillWithAir(world, bb, 6,8,4, 6,8,4);
                                
        // Interior
        fill(world, bb, 1,5,6, 5,5,6, fence, fence);
        fill(world, bb, 1,5,1, 1,5,5, fence, fence);
        fill(world, bb, 2,5,1, 6,5,1, fence, fence);
                
        for( int yy = 5; yy <= 8; ++ yy )
        {
        	placeBlock(world, Blocks.ladder, ForgeDirection.WEST, 6,yy,4, bb);
        }

        placeBlock(world, Blocks.bookshelf, 0, 11,5,2, bb);
        placeBlock(world, Blocks.torch, 0, 11,6,2, bb);

        placeBlock(world, Blocks.bed, ForgeDirection.SOUTH, 10,5,3, bb);
        placeBlock(world, Blocks.bed, Dir.getMeta(Blocks.bed, ForgeDirection.SOUTH, coordBaseMode) + 8, 10,5,2, bb);
        
        if( !hasMadeChest )
        {
        	int chestX = getXWithOffset(9, 2);
        	int chestY = getYWithOffset(5);
        	int chestZ = getZWithOffset(9, 2);
        	if( bb.isVecInside(chestX, chestY, chestZ) )
        	{
        		hasMadeChest = true;
                generateStructureChestContents(world, bb, rnd, 9,5,2,
                		ChestGenHooks.getItems(TAVERN_CHEST, rnd), ChestGenHooks.getCount(TAVERN_CHEST, rnd));
        	}
        }
        
        placeDoor(world, bb, rnd, 7,5,2, ForgeDirection.WEST);
        placeDoor(world, bb, rnd, 6,1,1, ForgeDirection.SOUTH);
        placeDoor(world, bb, rnd, 5,1,1, ForgeDirection.SOUTH);
        placeDoor(world, bb, rnd, 5,1,6, ForgeDirection.NORTH);

        fill(world, bb, 4,1,5, 4,3,5, woodUp, woodUp);
        fill(world, bb, 7,1,2, 7,3,2, woodUp, woodUp);
                
        placeBlock(world, stairsEast, 7,1,5, bb);
        placeBlock(world, stairsEast, 8,2,5, bb);
        placeBlock(world, stairsEast, 9,3,5, bb);
        placeBlock(world, stairsEast, 10,4,5, bb);
        fillWithAir(world, bb, 7,4,5, 9,4,5);
                
        placeBlock(world, planks, 8,1,5, bb);
        placeBlock(world, darkBenchNorth, 9,1,5, bb);
        placeBlock(world, darkBenchNorth, 10,1,5, bb);
        placeBlock(world, darkBenchNorth, 11,1,5, bb);
        placeBlock(world, darkBenchEast, 11,1,4, bb);
        placeBlock(world, darkBenchEast, 11,1,3, bb);
        placeBlock(world, planks, 11,1,2, bb);
        placeBlock(world, benchEast, 10,1,2, bb);
        placeBlock(world, fence, 9,1,2, bb);
        placeBlock(world, Blocks.wooden_pressure_plate, 0, 9,2,2, bb);
        placeBlock(world, benchWest, 8,1,2, bb);
                
        fill(world, bb, 4,1,2, 4,1,3, planks, planks);
        fill(world, bb, 2,1,2, 2,1,3, planks, planks);

        placeBlock(world, Blocks.brewing_stand, 0, 2,2,3, bb);
        placeBlock(world, Blocks.cauldron, rnd.nextInt(3) + 1, 3,1,5, bb);
        placeBlock(world, Blocks.trapdoor, ForgeDirection.NORTH, 4, 2, 4, bb);
        
        // Furnace sets it rotation based on neighbours - we need to enforce our direction instead, since the neighbours might
        // be in a different chunk and thus not loaded yet.
        placeBlock(world, Blocks.furnace, 0, 2,1,5, bb);
        setMetadata(world, Dir.getMeta(Blocks.furnace, ForgeDirection.SOUTH, coordBaseMode), 2,1,5, bb);

        // Carpets
        fill(world, bb, 8,1,3, 10,1,4, carpet, carpet);
        
        if( flower >= 0 )
        {
        	placeBlock(world, Blocks.flower_pot, flower, 6,6,1, bb);
        }
        
        // Torches - wall-mounted torches can "fall off" if the wall happens to be in another chunk. Let's try to prevent it
        // by putting something below them for the duration of the generation, as well as setting the direction
        // of the torch. This auto-alignment is also a problem for doors, but it's less visible there.
        placeBlock(world, Blocks.torch, 0, 1,6,6, bb);
        placeBlock(world, Blocks.torch, 0, 1,6,1, bb);
        placeBlock(world, Blocks.torch, 0, 9,9,4, bb);
        placeBlock(world, Blocks.torch, 0, 4,3,0, bb);
        placeBlock(world, Blocks.torch, 0, 7,3,0, bb);
        placeBlock(world, Blocks.torch, 0, 5,3,7, bb);
        placeBlock(world, Blocks.torch, 0, 2,3,5, bb);
        
        placeBlock(world, Blocks.stone, 0, 4,2,2, bb);
        placeBlock(world, Blocks.torch, ForgeDirection.NORTH, 4,3,2, bb);
        placeAir(world, 4,2,2, bb);

        placeBlock(world, Blocks.stone, 0, 9,2,2, bb);
        placeBlock(world, Blocks.torch, ForgeDirection.NORTH, 9,3,2, bb);
        placeBlock(world, Blocks.wooden_pressure_plate, 0, 9,2,2, bb);
        
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
        
        return true;
	}
	
	@Override
	protected int getVillagerType(int alreadySpawned)
	{
		return (Taverns.config.barWenchID > 0 ? Taverns.config.barWenchID : super.getVillagerType(alreadySpawned));
	}
}
