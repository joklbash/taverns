package as.taverns;

import as.common.entity.properties.GuardAIProperties;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EntityEventHandler
{
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event)
	{
		if( event.entity instanceof EntityVillager )
		{
			EntityVillager villager = (EntityVillager)event.entity;
			if( null == GuardAIProperties.get(villager) )
			{
				GuardAIProperties.register(villager);
			}
		}
	}
}
