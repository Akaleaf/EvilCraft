package org.cyclops.evilcraft.core.client.model;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.core.broom.BroomParts;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

/**
 * Model for a variant of a broom part item.
 * @author rubensworks
 */
public class BroomPartModel implements IModel {

    @Override
    public Collection<ResourceLocation> getDependencies() {
        ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();
        builder.addAll(BroomParts.REGISTRY.getPartModels());
        return builder.build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<ResourceLocation> getTextures() {
        return Collections.emptyList();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format,
                                    Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        BroomPartModelBaked bakedModel = new BroomPartModelBaked();

        // Add aspects to baked model.
        for(IBroomPart part : BroomParts.REGISTRY.getParts()) {
            try {
                IModel model = ModelLoaderRegistry.getModel(BroomParts.REGISTRY.getPartModel(part));
                IBakedModel bakedAspectModel = model.bake(state, format, bakedTextureGetter);
                bakedModel.addBroomPartModel(part, bakedAspectModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return bakedModel;
    }

    @Override
    public IModelState getDefaultState() {
        return ModelRotation.X0_Y0;
    }

}
