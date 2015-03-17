package evilcraft.client.render.tileentity;

import evilcraft.tileentity.EvilCraftBeaconTileEntity;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

/**
 * EvilCraft's version of a beacon renderer, this allows us to have custom colors
 * and customize other stuff without being dependend on vanilla code
 * 
 * @author immortaleeb
 *
 */
public class RenderTileEntityBeacon extends TileEntitySpecialRenderer {
	
	private static final ResourceLocation BEACON_TEXTURE = new ResourceLocation("textures/entity/beacon_beam.png");
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTickTime, int partialDamage) {
		renderBeacon((EvilCraftBeaconTileEntity)tileentity, x, y, z, partialTickTime, partialDamage);
	}
	
	protected void renderBeacon(EvilCraftBeaconTileEntity tileentity, double x, double y, double z, float partialTickTime, int partialDamage) {
		float f1 = tileentity.getBeamRenderVariable();
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		
        if (tileentity.isBeamActive())
        {
        	Vector4f beamInnerColor = tileentity.getBeamInnerColor();
        	Vector4f beamOuterColor = tileentity.getBeamOuterColor();
        	
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            this.bindTexture(BEACON_TEXTURE);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDepthMask(true);
            OpenGlHelper.glBlendFunc(770, 1, 1, 0);
            float f2 = (float)tileentity.getWorld().getTotalWorldTime() + partialTickTime;
            float f3 = -f2 * 0.2F - (float)MathHelper.floor_float(-f2 * 0.1F);
            byte b0 = 1;
            double d3 = (double)f2 * 0.025D * (1.0D - (double)(b0 & 1) * 2.5D);
            worldRenderer.startDrawingQuads();
            worldRenderer.func_178960_a(beamInnerColor.x, beamInnerColor.y, beamInnerColor.z, beamInnerColor.w);
            double d4 = (double)b0 * 0.2D;
            double d5 = 0.5D + Math.cos(d3 + 2.356194490192345D) * d4;
            double d6 = 0.5D + Math.sin(d3 + 2.356194490192345D) * d4;
            double d7 = 0.5D + Math.cos(d3 + (Math.PI / 4D)) * d4;
            double d8 = 0.5D + Math.sin(d3 + (Math.PI / 4D)) * d4;
            double d9 = 0.5D + Math.cos(d3 + 3.9269908169872414D) * d4;
            double d10 = 0.5D + Math.sin(d3 + 3.9269908169872414D) * d4;
            double d11 = 0.5D + Math.cos(d3 + 5.497787143782138D) * d4;
            double d12 = 0.5D + Math.sin(d3 + 5.497787143782138D) * d4;
            double d13 = (double)(256.0F * f1);
            double d14 = 0.0D;
            double d15 = 1.0D;
            double d16 = (double)(-1.0F + f3);
            double d17 = (double)(256.0F * f1) * (0.5D / d4) + d16;
            worldRenderer.addVertexWithUV(x + d5, y + d13, z + d6, d15, d17);
            worldRenderer.addVertexWithUV(x + d5, y, z + d6, d15, d16);
            worldRenderer.addVertexWithUV(x + d7, y, z + d8, d14, d16);
            worldRenderer.addVertexWithUV(x + d7, y + d13, z + d8, d14, d17);
            worldRenderer.addVertexWithUV(x + d11, y + d13, z + d12, d15, d17);
            worldRenderer.addVertexWithUV(x + d11, y, z + d12, d15, d16);
            worldRenderer.addVertexWithUV(x + d9, y, z + d10, d14, d16);
            worldRenderer.addVertexWithUV(x + d9, y + d13, z + d10, d14, d17);
            worldRenderer.addVertexWithUV(x + d7, y + d13, z + d8, d15, d17);
            worldRenderer.addVertexWithUV(x + d7, y, z + d8, d15, d16);
            worldRenderer.addVertexWithUV(x + d11, y, z + d12, d14, d16);
            worldRenderer.addVertexWithUV(x + d11, y + d13, z + d12, d14, d17);
            worldRenderer.addVertexWithUV(x + d9, y + d13, z + d10, d15, d17);
            worldRenderer.addVertexWithUV(x + d9, y, z + d10, d15, d16);
            worldRenderer.addVertexWithUV(x + d5, y, z + d6, d14, d16);
            worldRenderer.addVertexWithUV(x + d5, y + d13, z + d6, d14, d17);
            tessellator.draw();
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glDepthMask(false);
            worldRenderer.startDrawingQuads();
            worldRenderer.func_178960_a(beamOuterColor.x, beamOuterColor.y, beamOuterColor.z, beamOuterColor.w);
            double d18 = 0.2D;
            double d19 = 0.2D;
            double d20 = 0.8D;
            double d21 = 0.2D;
            double d22 = 0.2D;
            double d23 = 0.8D;
            double d24 = 0.8D;
            double d25 = 0.8D;
            double d26 = (double)(256.0F * f1);
            double d27 = 0.0D;
            double d28 = 1.0D;
            double d29 = (double)(-1.0F + f3);
            double d30 = (double)(256.0F * f1) + d29;
            worldRenderer.addVertexWithUV(x + d18, y + d26, z + d19, d28, d30);
            worldRenderer.addVertexWithUV(x + d18, y, z + d19, d28, d29);
            worldRenderer.addVertexWithUV(x + d20, y, z + d21, d27, d29);
            worldRenderer.addVertexWithUV(x + d20, y + d26, z + d21, d27, d30);
            worldRenderer.addVertexWithUV(x + d24, y + d26, z + d25, d28, d30);
            worldRenderer.addVertexWithUV(x + d24, y, z + d25, d28, d29);
            worldRenderer.addVertexWithUV(x + d22, y, z + d23, d27, d29);
            worldRenderer.addVertexWithUV(x + d22, y + d26, z + d23, d27, d30);
            worldRenderer.addVertexWithUV(x + d20, y + d26, z + d21, d28, d30);
            worldRenderer.addVertexWithUV(x + d20, y, z + d21, d28, d29);
            worldRenderer.addVertexWithUV(x + d24, y, z + d25, d27, d29);
            worldRenderer.addVertexWithUV(x + d24, y + d26, z + d25, d27, d30);
            worldRenderer.addVertexWithUV(x + d22, y + d26, z + d23, d28, d30);
            worldRenderer.addVertexWithUV(x + d22, y, z + d23, d28, d29);
            worldRenderer.addVertexWithUV(x + d18, y, z + d19, d27, d29);
            worldRenderer.addVertexWithUV(x + d18, y + d26, z + d19, d27, d30);
            tessellator.draw();
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDepthMask(true);
        }
        
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);
	}

}
