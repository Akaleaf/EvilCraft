package evilcraft.api.broom;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.init.IRegistry;

import java.util.Collection;

/**
 * Registry for broom parts.
 * @author rubensworks
 */
public interface IBroomPartRegistry extends IRegistry {

    /**
     * Register a new broom part.
     * @param part The part.
     */
    public <P extends IBroomPart> P registerPart(P part);

    /**
     * @return All broom parts.
     */
    public Collection<IBroomPart> getParts();

    /**
     * @param type The type of parts to retrieve.
     * @return All broom parts of the given type..
     */
    public Collection<IBroomPart> getParts(IBroomPart.BroomPartType type);

    /**
     * Register a model resource location for the given part.
     * @param part The part
     * @param modelLocation The model resource location.
     */
    @SideOnly(Side.CLIENT)
    public void registerPartModel(IBroomPart part, ModelResourceLocation modelLocation);

    /**
     * Get the model resource location of the given part.
     * @param part The part.
     * @return The model resource location.
     */
    @SideOnly(Side.CLIENT)
    public  ModelResourceLocation getPartModel(IBroomPart part);

    /**
     * Get all registered model resource locations for the parts.
     * @return All model resource locations.
     */
    @SideOnly(Side.CLIENT)
    public Collection<ModelResourceLocation> getPartModels();

}
