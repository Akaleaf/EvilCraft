package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link EntityItemUndespawnable}.
 * @author rubensworks
 *
 */
public class EntityItemUndespawnableConfig extends EntityConfig<EntityItem> {
    
    /**
     * The unique instance.
     */
    public static EntityItemUndespawnableConfig _instance;

    /**
     * Make a new instance.
     */
    public EntityItemUndespawnableConfig() {
        super(
                EvilCraft._instance,
        	true,
            "entity_item_undespawnable",
            null,
            EntityItemUndespawnable.class
        );
    }
    
    @Override
    public boolean sendVelocityUpdates() {
        return true;
    }

    @SideOnly(Side.CLIENT)
	@Override
	public Render<EntityItem> getRender(RenderManager renderManager, RenderItem renderItem) {
        return new RenderEntityItem(renderManager, renderItem);
	}
}
