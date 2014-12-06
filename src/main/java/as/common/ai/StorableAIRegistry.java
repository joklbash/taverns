package as.common.ai;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.nbt.NBTTagCompound;

public final class StorableAIRegistry
{
	private final static Map<String, StorableAIBuilder<?>> builderMap;
	private final static Map<Class<?>, StorableAIBuilder<?>> classMap;
	private final static StorableAIBuilder nullBuilder;
	
	static
	{
		builderMap = new HashMap<String, StorableAIBuilder<?>>();
		classMap = new HashMap<Class<?>, StorableAIBuilder<?>>();
		nullBuilder = new NullAIBuilder();
	}
	
	public static <T extends EntityAIBase & StorableAI> void registerBuilder(StorableAIBuilder<T> builder)
	{
		builderMap.put(builder.getRoutineName(), builder);
		classMap.put(builder.getType(), builder);
	}
	
	public static <T extends EntityAIBase & StorableAI> StorableAIBuilder<T> getBuilder(String id)
	{
		StorableAIBuilder<T> builder = (StorableAIBuilder<T>)builderMap.get(id);
		return (null == builder ? nullBuilder : builder);
	}

	public static <T extends EntityAIBase & StorableAI> StorableAIBuilder<T> getBuilder(Class<T> clazz)
	{
		StorableAIBuilder<T> builder = (StorableAIBuilder<T>)classMap.get(clazz);
		return (null == builder ? nullBuilder : builder);
	}

	public static <T extends EntityAIBase & StorableAI> StorableAIBuilder<T> getBuilder(T classInstance)
	{
		StorableAIBuilder<T> builder = (StorableAIBuilder<T>)classMap.get(classInstance.getClass());
		return (null == builder ? nullBuilder : builder);
	}
	
	private static class NullAI extends EntityAIBase implements StorableAI
	{
		@Override
		public void writeToNBT(NBTTagCompound data) {}

		@Override
		public boolean shouldExecute() { return false; }
	}
	
	private static class NullAIBuilder implements StorableAIBuilder<NullAI>
	{
		@Override
		public NullAI createFromNBT(EntityLiving living, NBTTagCompound data) { return null; }

		@Override
		public Class<NullAI> getType() { return NullAI.class; }

		@Override
		public String getRoutineName() { return "Vernideas:NULL"; }
	}
}
