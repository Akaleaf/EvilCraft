package org.cyclops.evilcraft.client.render.tileentity;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.evilcraft.tileentity.TileEntangledChalice;
import org.lwjgl.opengl.GL11;

/**
 * Renderer for the {@link org.cyclops.evilcraft.block.EntangledChalice}.
 * @author rubensworks
 *
 */
public class RenderTileEntityEntangledChalice extends TileEntitySpecialRenderer<TileEntangledChalice> implements RenderHelpers.IFluidContextRender {

    private TileEntangledChalice lastTile = null;

    @Override
    public void render(final TileEntangledChalice tile, double x, double y, double z, float partialTick, int destroyStage, float alpha) {
        if(tile != null && tile.getTank().getFluid() != null && tile.getTank().getFluid().getFluid() != null) {
            lastTile = tile;
            try {
                RenderHelpers.renderTileFluidContext(tile.getTank().getFluid(), x, y, z, tile, this);
            } catch (NullPointerException e) {
                // This can happen because worlds are multi-threaded, and the fluid suddenly may become null while out fluid context is being executed.
                // Unfortunately we can't lock on worlds, so there isn't really a cleaner solution for this.
            }
        }
    }

    @Override
    public void renderFluid(FluidStack fluid) {
        float height = Math.min(0.95F, ((float) fluid.amount / (float) lastTile.getTank().getCapacity())) * 0.1875F + 0.8125F;
        int brightness = lastTile.getWorld().getCombinedLight(lastTile.getPos(), fluid.getFluid().getLuminosity(fluid));
        int l2 = brightness >> 0x10 & 0xFFFF;
        int i3 = brightness & 0xFFFF;

        TextureAtlasSprite icon = RenderHelpers.getFluidIcon(lastTile.getTank().getFluid(), EnumFacing.UP);

        Tessellator t = Tessellator.getInstance();
        BufferBuilder worldRenderer = t.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);

        worldRenderer.pos(0.1875F, height, 0.1875F).tex(icon.getMinU(), icon.getMaxV()).lightmap(l2, i3).color(1F, 1, 1, 1).endVertex();
        worldRenderer.pos(0.1875F, height, 0.8125F).tex(icon.getMinU(), icon.getMinV()).lightmap(l2, i3).color(1F, 1, 1, 1).endVertex();
        worldRenderer.pos(0.8125F, height, 0.8125F).tex(icon.getMaxU(), icon.getMinV()).lightmap(l2, i3).color(1F, 1, 1, 1).endVertex();
        worldRenderer.pos(0.8125F, height, 0.1875F).tex(icon.getMaxU(), icon.getMaxV()).lightmap(l2, i3).color(1F, 1, 1, 1).endVertex();

        t.draw();
    }
}
