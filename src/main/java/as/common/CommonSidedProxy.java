package as.common;

import net.minecraft.item.Item;
import net.minecraftforge.client.IItemRenderer;

public interface CommonSidedProxy
{
	void registerItemRenderer(Item item, IItemRenderer itemRenderer);
	void registerVillagerSkin(int villagerId, String resourceDomain, String resourcePath);
}
