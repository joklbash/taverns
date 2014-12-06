package as.taverns.direction;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class Dir
{
	public final static Map<Pair<Block, ForgeDirection>, Integer> map;

	public final static Map<Pair<ForgeDirection, Integer>, ForgeDirection> orientation;
	
	public static int getMeta(Block bl, ForgeDirection dir)
	{
		Integer val = map.get(ImmutablePair.<Block, ForgeDirection>of(bl, dir));
		return (null != val ? val.intValue() : dir.ordinal() );
	}
	
	public static int getMeta(Block bl, ForgeDirection dir, int orient)
	{
		ForgeDirection newDir = orientation.get(ImmutablePair.<ForgeDirection, Integer>of(dir, Integer.valueOf(orient)));
		return (null != newDir ? getMeta(bl, newDir) : getMeta(bl, dir));
	}
	
	private static void add(Block bl, ForgeDirection dir, int val)
	{
		map.put(ImmutablePair.<Block, ForgeDirection>of(bl, dir), Integer.valueOf(val));
	}
	
	private static void addCardinal(Block bl, int valNorth, int valSouth, int valWest, int valEast)
	{
		map.put(ImmutablePair.<Block, ForgeDirection>of(bl, ForgeDirection.NORTH), Integer.valueOf(valNorth));
		map.put(ImmutablePair.<Block, ForgeDirection>of(bl, ForgeDirection.SOUTH), Integer.valueOf(valSouth));
		map.put(ImmutablePair.<Block, ForgeDirection>of(bl, ForgeDirection.WEST), Integer.valueOf(valWest));
		map.put(ImmutablePair.<Block, ForgeDirection>of(bl, ForgeDirection.EAST), Integer.valueOf(valEast));
	}
	
	static
	{
		map = new HashMap<Pair<Block,ForgeDirection>, Integer>();
		
		add(Blocks.log, ForgeDirection.UP, 0);
		add(Blocks.log, ForgeDirection.DOWN, 0);
		addCardinal(Blocks.log, 8, 8, 4, 4);

		add(Blocks.log2, ForgeDirection.UP, 0);
		add(Blocks.log2, ForgeDirection.DOWN, 0);
		addCardinal(Blocks.log2, 8, 8, 4, 4);
		
		add(Blocks.hay_block, ForgeDirection.UP, 0);
		add(Blocks.hay_block, ForgeDirection.DOWN, 0);
		addCardinal(Blocks.hay_block, 8, 8, 4, 4);

		addCardinal(Blocks.bed, 2, 0, 1, 3);

		add(Blocks.piston, ForgeDirection.UP, 1);
		add(Blocks.piston, ForgeDirection.DOWN, 0);
		addCardinal(Blocks.piston, 2, 3, 4, 5);
		add(Blocks.sticky_piston, ForgeDirection.UP, 1);
		add(Blocks.sticky_piston, ForgeDirection.DOWN, 0);
		addCardinal(Blocks.sticky_piston, 2, 3, 4, 5);
		add(Blocks.piston_extension, ForgeDirection.UP, 1);
		add(Blocks.piston_extension, ForgeDirection.DOWN, 0);
		addCardinal(Blocks.piston_extension, 2, 3, 4, 5);
		
		addCardinal(Blocks.brick_stairs, 3, 2, 1, 0);
		addCardinal(Blocks.stone_stairs, 3, 2, 1, 0); // Actually, cobblestone stairs
		addCardinal(Blocks.nether_brick_stairs, 3, 2, 1, 0);
		addCardinal(Blocks.sandstone_stairs, 3, 2, 1, 0);
		addCardinal(Blocks.stone_brick_stairs, 3, 2, 1, 0);
		addCardinal(Blocks.birch_stairs, 3, 2, 1, 0);
		addCardinal(Blocks.jungle_stairs, 3, 2, 1, 0);
		addCardinal(Blocks.oak_stairs, 3, 2, 1, 0);
		addCardinal(Blocks.spruce_stairs, 3, 2, 1, 0);
		addCardinal(Blocks.acacia_stairs, 3, 2, 1, 0);
		addCardinal(Blocks.dark_oak_stairs, 3, 2, 1, 0);
		
		addCardinal(Blocks.iron_door, 1, 3, 0, 2);
		addCardinal(Blocks.wooden_door, 1, 3, 0, 2);
		
		addCardinal(Blocks.fence_gate, 2, 0, 1, 3);
		
		addCardinal(Blocks.tripwire_hook, 2, 0, 1, 3);
		
		addCardinal(Blocks.ladder, 2, 3, 4, 5);
		addCardinal(Blocks.wall_sign, 2, 3, 4, 5);
		addCardinal(Blocks.furnace, 2, 3, 4, 5);
		addCardinal(Blocks.lit_furnace, 2, 3, 4, 5);
		addCardinal(Blocks.chest, 2, 3, 4, 5);
		
		addCardinal(Blocks.trapdoor, 1, 0, 3, 2);
		
		add(Blocks.torch, ForgeDirection.DOWN, 5);
		addCardinal(Blocks.torch, 4, 3, 2, 1);
		add(Blocks.unlit_redstone_torch, ForgeDirection.DOWN, 5);
		addCardinal(Blocks.unlit_redstone_torch, 4, 3, 2, 1);
		add(Blocks.redstone_torch, ForgeDirection.DOWN, 5);
		addCardinal(Blocks.redstone_torch, 4, 3, 2, 1);
		
		orientation = new HashMap<Pair<ForgeDirection,Integer>, ForgeDirection>();
		// Orientation 0 - reflection (switch north and south)
		orientation.put(ImmutablePair.<ForgeDirection, Integer>of(ForgeDirection.NORTH, Integer.valueOf(0)), ForgeDirection.SOUTH);
		orientation.put(ImmutablePair.<ForgeDirection, Integer>of(ForgeDirection.SOUTH, Integer.valueOf(0)), ForgeDirection.NORTH);
		orientation.put(ImmutablePair.<ForgeDirection, Integer>of(ForgeDirection.WEST, Integer.valueOf(0)), ForgeDirection.WEST);
		orientation.put(ImmutablePair.<ForgeDirection, Integer>of(ForgeDirection.EAST, Integer.valueOf(0)), ForgeDirection.EAST);
		// Orientation 1 - rotation clockwise, then reflection (switch north and south)
		orientation.put(ImmutablePair.<ForgeDirection, Integer>of(ForgeDirection.NORTH, Integer.valueOf(1)), ForgeDirection.WEST);
		orientation.put(ImmutablePair.<ForgeDirection, Integer>of(ForgeDirection.SOUTH, Integer.valueOf(1)), ForgeDirection.EAST);
		orientation.put(ImmutablePair.<ForgeDirection, Integer>of(ForgeDirection.WEST, Integer.valueOf(1)), ForgeDirection.NORTH);
		orientation.put(ImmutablePair.<ForgeDirection, Integer>of(ForgeDirection.EAST, Integer.valueOf(1)), ForgeDirection.SOUTH);
		// Orientation 2 - no change
		orientation.put(ImmutablePair.<ForgeDirection, Integer>of(ForgeDirection.NORTH, Integer.valueOf(2)), ForgeDirection.NORTH);
		orientation.put(ImmutablePair.<ForgeDirection, Integer>of(ForgeDirection.SOUTH, Integer.valueOf(2)), ForgeDirection.SOUTH);
		orientation.put(ImmutablePair.<ForgeDirection, Integer>of(ForgeDirection.WEST, Integer.valueOf(2)), ForgeDirection.WEST);
		orientation.put(ImmutablePair.<ForgeDirection, Integer>of(ForgeDirection.EAST, Integer.valueOf(2)), ForgeDirection.EAST);
		// Orientation 3 - rotation counter-clockwise
		orientation.put(ImmutablePair.<ForgeDirection, Integer>of(ForgeDirection.NORTH, Integer.valueOf(3)), ForgeDirection.EAST);
		orientation.put(ImmutablePair.<ForgeDirection, Integer>of(ForgeDirection.SOUTH, Integer.valueOf(3)), ForgeDirection.WEST);
		orientation.put(ImmutablePair.<ForgeDirection, Integer>of(ForgeDirection.WEST, Integer.valueOf(3)), ForgeDirection.NORTH);
		orientation.put(ImmutablePair.<ForgeDirection, Integer>of(ForgeDirection.EAST, Integer.valueOf(3)), ForgeDirection.SOUTH);
	}
}
