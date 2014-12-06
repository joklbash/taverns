package as.taverns.gen;

import java.util.List;
import java.util.Random;

import as.taverns.Settings;
import as.taverns.Taverns;
import net.minecraft.util.MathHelper;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageCreationHandler;

public class StallCreationHandler implements IVillageCreationHandler
{
	public static StallCreationHandler instance = null;
	
	public static void init(Settings config)
	{
		if( null == instance )
		{
			instance = new StallCreationHandler();
			VillagerRegistry.instance().registerVillageCreationHandler(instance);
		}
	}

	@Override
	public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int i)
	{
        return new StructureVillagePieces.PieceWeight(ComponentVillageStall.class, Taverns.config.weightStalls,
        		MathHelper.getRandomIntegerInRange(random, 0, 1));
	}

	@Override
	public Class<?> getComponentClass()
	{
		return ComponentVillageStall.class;
	}

	@Override
	public Object buildComponent(StructureVillagePieces.PieceWeight villagePiece,
			StructureVillagePieces.Start startPiece, List pieces, Random random,
			int p1, int p2, int p3, int p4, int p5)
	{
        return ComponentVillageStall.buildComponent(startPiece, pieces, random, p1, p2, p3, p4, p5);
	}

}
