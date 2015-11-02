package org.cyclops.evilcraft.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.entity.item.EntityItemDarkStick;
import org.lwjgl.opengl.GL11;

/**
 * Renderer for a dark stick
 * 
 * @author rubensworks
 *
 */
public class RenderDarkStick extends Render {

    /**
     * Make a new instance.
     * @param renderManager The render manager
     * @param config The config.
     */
	public RenderDarkStick(RenderManager renderManager, ExtendedConfig<EntityConfig> config) {
	    super(renderManager);
	}

    protected ItemStack getItemStack(Entity entity) {
        return ((EntityItemDarkStick) entity).getEntityItem();
    }

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime) {
        EntityItemDarkStick darkStick = (EntityItemDarkStick) entity;
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + 0.2F, z);

        float rotation;
        if(darkStick.isValid()) {
            rotation = darkStick.getAngle();
        } else {
            rotation = (((float)darkStick.getAge()) / 20.0F + darkStick.hoverStart) * (180F / (float)Math.PI);
        }

        GL11.glRotatef(rotation, 0, 1, 0);
        GL11.glRotatef(-90F, 0, 1, 0);
        GL11.glRotatef(25F, 1, 0, 0);
        
        bindEntityTexture(entity);

        Minecraft.getMinecraft().getRenderItem().renderItemModel(getItemStack(entity));
        
        GlStateManager.popMatrix();
	}

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }

}
