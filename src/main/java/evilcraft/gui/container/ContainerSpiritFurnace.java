package evilcraft.gui.container;

import net.minecraft.entity.player.InventoryPlayer;
import evilcraft.api.gui.container.ContainerWorking;
import evilcraft.api.gui.slot.SlotFluidContainer;
import evilcraft.api.gui.slot.SlotRemoveOnly;
import evilcraft.api.gui.slot.SlotWorking;
import evilcraft.blocks.SpiritFurnace;
import evilcraft.entities.tileentities.TileSpiritFurnace;

/**
 * Container for the {@link SpiritFurnace}.
 * @author rubensworks
 *
 */
public class ContainerSpiritFurnace extends ContainerWorking<TileSpiritFurnace> {
    
    private static final int INVENTORY_OFFSET_X = 8;
    private static final int INVENTORY_OFFSET_Y = 84;
    
    /**
     * Container slot X coordinate.
     */
    public static final int SLOT_CONTAINER_X = 8;
    /**
     * Container slot Y coordinate.
     */
    public static final int SLOT_CONTAINER_Y = 36;
    
    /**
     * Box slot X coordinate.
     */
    public static final int SLOT_BOX_X = 79;
    /**
     * Box slot Y coordinate.
     */
    public static final int SLOT_BOX_Y = 36;
    
    private static final int SLOTS_X = 2;
    private static final int SLOTS_Y = 2;
    /**
     * The amount of drop slots.
     */
    public static final int SLOTS_DROP = SLOTS_X * SLOTS_Y;
    /**
     * Drop slot X coordinate.
     */
    public static final int SLOT_DROP_X = 134;
    /**
     * Drop slot Y coordinate.
     */
    public static final int SLOT_DROP_Y = 28;

    /**
     * Make a new instance.
     * @param inventory The inventory of the player.
     * @param tile The tile entity that calls the GUI.
     */
    public ContainerSpiritFurnace(InventoryPlayer inventory, TileSpiritFurnace tile) {
        super(inventory, tile);

        // Adding inventory
        addSlotToContainer(new SlotFluidContainer(tile, TileSpiritFurnace.SLOT_CONTAINER,
        		SLOT_CONTAINER_X, SLOT_CONTAINER_Y,
        		TileSpiritFurnace.ACCEPTED_FLUID)); // Container emptier
        addSlotToContainer(new SlotWorking<TileSpiritFurnace>(TileSpiritFurnace.SLOT_BOX, SLOT_BOX_X, SLOT_BOX_Y, tile)); // Box slot
        
        int i = 0;
        for (int y = 0; y < SLOTS_X; y++) {
            for (int x = 0; x < SLOTS_Y; x++) {
            	addSlotToContainer(new SlotRemoveOnly(tile, TileSpiritFurnace.SLOTS_DROP[i],
            			SLOT_DROP_X + x * ITEMBOX, SLOT_DROP_Y + y * ITEMBOX)); // Drop slot
            	i++;
            }
        }

        this.addPlayerInventory(inventory, INVENTORY_OFFSET_X, INVENTORY_OFFSET_Y);
    }
    
}