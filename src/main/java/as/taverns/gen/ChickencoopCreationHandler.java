package as.taverns.gen;

import java.util.List;
import java.util.Random;

import as.taverns.Settings;
import as.taverns.Taverns;
import net.minecraft.util.MathHelper;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageCreationHandler;

public class ChickencoopCreationHandler implements IVillageCreationHandler
{
	public static ChickencoopCreationHandler instance = null;
	
	public static void init(Settings config)
	{
		if( null == instance )
		{
			instance = new ChickencoopCreationHandler();
			VillagerRegistry.instance().registerVillageCreationHandler(instance);
		}
	}
	
	@Override
	public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int i)
	{
        return new StructureVillagePieces.PieceWeight(ComponentVillageChickencoop.class, Taverns.config.weightChickencoop,
        		MathHelper.getRandomIntegerInRange(random, 0, 1));
	}

	@Override
	public Class<?> getComponentClass()
	{
		return ComponentVillageChickencoop.class;
	}

	@Override
	public Object buildComponent(StructureVillagePieces.PieceWeight villagePiece,
			StructureVillagePieces.Start startPiece, List pieces, Random random,
			int p1, int p2, int p3, int p4, int p5)
	{
        return ComponentVillageChickencoop.buildComponent(startPiece, pieces, random, p1, p2, p3, p4, p5);
    }
}
