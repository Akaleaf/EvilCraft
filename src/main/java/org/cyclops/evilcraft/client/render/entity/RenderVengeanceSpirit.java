package org.cyclops.evilcraft.client.render.entity;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.MobConfig;
import org.cyclops.evilcraft.entity.monster.VengeanceSpirit;
import org.lwjgl.opengl.GL11;

import java.util.Map;

/**
 * Renderer for a vengeance spirit
 * 
 * @author rubensworks
 *
 */
public class RenderVengeanceSpirit extends Render {

	private final RenderPlayerSpirit playerRenderer;
	private final Map<GameProfile, GameProfile> checkedProfiles = Maps.newHashMap();

	/**
     * Make a new instance.
	 * @param renderManager The render manager.
     * @param config Then config.
     */
    public RenderVengeanceSpirit(RenderManager renderManager, ExtendedConfig<MobConfig> config) {
        super(renderManager);
		playerRenderer = new RenderPlayerSpirit(renderManager);
    }

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime) {
		VengeanceSpirit spirit = (VengeanceSpirit) entity;
		EntityLivingBase innerEntity = spirit.getInnerEntity();
		if(innerEntity != null && spirit.isVisible()) {
			Render render = (Render) renderManager.entityRenderMap.get(innerEntity.getClass());
			if(render != null && !spirit.isSwarm()) {
				GlStateManager.enableBlend();
				if(!spirit.isFrozen()) {
					GlStateManager.blendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
				} else {
					GlStateManager.blendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
				}
                float c = Math.min(1F - (float) (spirit.getBuildupDuration()) / 30, 0.65F);
                GlStateManager.color(c, c, c);
				//GL14.glBlendColor(0, 0, 0, 0);
				//GlStateManager.blendFunc(GL11.GL_SRC_COLOR, GL11.GL_CONSTANT_COLOR);
				//GlStateManager.blendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE_MINUS_DST_COLOR);
				
				try {
					if(spirit.isPlayer()) {
						GameProfile gameProfile = new GameProfile(spirit.getPlayerUUID(), spirit.getPlayerName());
						ResourceLocation resourcelocation = DefaultPlayerSkin.getDefaultSkinLegacy();
						Minecraft minecraft = Minecraft.getMinecraft();
						// Check if we have loaded the (texturized) profile before, otherwise we load it and cache it.
						if(!checkedProfiles.containsKey(gameProfile)) {
							Property property = (Property) Iterables.getFirst(gameProfile.getProperties().get("textures"), (Object) null);
							if (property == null) {
								// The game profile enchanced with texture information.
								GameProfile newGameProfile = Minecraft.getMinecraft().getSessionService().fillProfileProperties(gameProfile, true);
								checkedProfiles.put(gameProfile, newGameProfile);
							}
						} else {
							Map map = minecraft.getSkinManager().loadSkinFromCache(checkedProfiles.get(gameProfile));
							if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
								resourcelocation = minecraft.getSkinManager().loadSkin((MinecraftProfileTexture) map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
							}
						}
						playerRenderer.setPlayerTexture(resourcelocation);
						playerRenderer.doRender(innerEntity, x, y, z, yaw, 0);
					} else {
						render.doRender(innerEntity, x, y, z, yaw, 0);
					}
				} catch (Exception e) {
					// Invalid entity, so set as swarm.
					spirit.setIsSwarm(true);
					spirit.setPlayerId(""); // Just in case the crash was caused by a player spirit.
				}
				GlStateManager.disableBlend();
			}
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity var1) {
		return null;
	}

	public static class RenderPlayerSpirit extends RenderBiped {

		@Setter
		private ResourceLocation playerTexture;

		public RenderPlayerSpirit(RenderManager renderManager) {
			super(renderManager, new ModelBiped(0.0F), 0.5F);
		}

		protected ResourceLocation getEntityTexture(EntityLiving entity) {
			return playerTexture;
		}

	}

}
