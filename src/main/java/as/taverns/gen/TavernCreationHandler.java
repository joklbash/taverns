package as.taverns.gen;

import java.util.List;
import java.util.Random;

import as.taverns.Settings;
import as.taverns.Taverns;
import net.minecraft.util.MathHelper;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageCreationHandler;

public class TavernCreationHandler implements IVillageCreationHandler
{
	public static TavernCreationHandler instance = null;
	
	public static void init(Settings config)
	{
		if( null == instance )
		{
			instance = new TavernCreationHandler();
			VillagerRegistry.instance().registerVillageCreationHandler(instance);
		}
	}
	
	@Override
	public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int i)
	{
        return new StructureVillagePieces.PieceWeight(ComponentVillageTavern.class, Taverns.config.weightTaverns,
        		MathHelper.getRandomIntegerInRange(random, 0, 1));
	}

	@Override
	public Class<?> getComponentClass()
	{
		return ComponentVillageTavern.class;
	}

	/**
	 * What happens here:
	 * <p>
	 * When the result of this method is not <i>null</i>, the following code
	 *  is run in StructureVillagePieces:
	 * <pre>
if (componentvillage != null)
{
    ++villagePiece.villagePiecesSpawned;
    startPiece.structVillagePieceWeight = villagePiece;

    if (!villagePiece.canSpawnMoreVillagePieces())
    {
        startPiece.structureVillageWeightedPieceList.remove(villagePiece);
    }

    return componentvillage;
}

	 * </pre>
	 * This only removes the component from the list of possible ones so that
	 * the limits are taken into account. Hacking around that means that we need
	 * to do those operations yourself. We have access to the objects, so no problem here.
	 * <p>
	 * The result of this then gets added to the buildings list, still in StructureVillagePieces:
	 * <pre>
if (componentvillage != null)
{
    int middleX = (componentvillage.boundingBox.minX + componentvillage.boundingBox.maxX) / 2;
    int middleZ = (componentvillage.boundingBox.minZ + componentvillage.boundingBox.maxZ) / 2;
    int sizeX = componentvillage.boundingBox.maxX - componentvillage.boundingBox.minX;
    int sizeZ = componentvillage.boundingBox.maxZ - componentvillage.boundingBox.minZ;
    int size = sizeX > sizeZ ? sizeX : sizeZ;

    if (par0ComponentVillageStartPiece.getWorldChunkManager().areBiomesViable(middleX, middleZ, size / 2 + 4, MapGenVillage.villageSpawnBiomes))
    {
        par1List.add(componentvillage);
        par0ComponentVillageStartPiece.field_74932_i.add(componentvillage);
        return componentvillage;
    }
}
	 * </pre>
	 * Again, we have access to all of this, so we can do the checks and manipulations ourselves.
	 * <p>
	 * For roads (paths), the only change is that we need to modify (append to) the paths
	 * list, <b>field_74930_j</b>.
	 * <p>
	 * Finally, in ComponentVillagePathGen the following happens with the result:
	 * <pre>
if (componentvillage != null)
{
    posAlongRoad += Math.max(componentvillage.boundingBox.getXSize(), componentvillage.boundingBox.getZSize());
    buildingGenerated = true;
}
	 * </pre>
	 * With <i>posAlongRoad</i> being used to space buildings apart and <i>buildingGenerated</i>
	 * determined if there is a point in continuing the road network (no buildings = road runs out).
	 * <p>
	 * After all the buildings are generated along the road, the road network gets extended like this:
	 * <pre>
	 * StructureVillagePieces.getNextStructureComponentVillagePath(startPiece, pieces, random, x,y,z, orientation, componentType)
	 * </pre>
	 * The componentType is typically 0 for village roads/paths.
	 */
	@Override
	public Object buildComponent(StructureVillagePieces.PieceWeight villagePiece,
			StructureVillagePieces.Start startPiece, List pieces, Random random,
			int p1, int p2, int p3, int p4, int p5)
	{
        return ComponentVillageTavern.buildComponent(startPiece, pieces, random, p1, p2, p3, p4, p5);
    }
}
