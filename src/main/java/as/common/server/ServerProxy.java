package as.common.server;

import as.common.CommonSidedProxy;
import net.minecraft.item.Item;
import net.minecraftforge.client.IItemRenderer;

public class ServerProxy implements CommonSidedProxy
{
	@Override
	public void registerItemRenderer(Item item, IItemRenderer itemRenderer)
	{
		// NoOp for server
	}

	@Override
	public void registerVillagerSkin(int villagerId, String resourceDomain, String resourcePath)
	{
		// NoOp for server
	}

}
