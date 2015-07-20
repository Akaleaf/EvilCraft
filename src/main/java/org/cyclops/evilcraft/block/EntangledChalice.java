package org.cyclops.evilcraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.evilcraft.core.block.IBlockRarityProvider;
import org.cyclops.evilcraft.core.block.IBlockTank;
import org.cyclops.evilcraft.core.block.component.BlockTankComponent;
import org.cyclops.evilcraft.tileentity.TileEntangledChalice;

import java.util.List;

/**
 * Chalice that can be bound to other chalices which causes them to always share the same fluid amount.
 * Can be filled or drained in blockState mode, and can be used to auto-supply ALL slots in a player inventory.
 * @author rubensworks
 *
 */
public class EntangledChalice extends ConfigurableBlockContainer implements IInformationProvider, IBlockTank, IBlockRarityProvider {
    
	/**
	 * Meta data for supplying.
	 */
    @BlockProperty
    public static final PropertyBool SUPPLY = PropertyBool.create("supply");
	
    private static EntangledChalice _instance = null;
    
    private BlockTankComponent<EntangledChalice> tankComponent = new BlockTankComponent<EntangledChalice>(this);
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static EntangledChalice getInstance() {
        return _instance;
    }

    public EntangledChalice(ExtendedConfig<BlockConfig> eConfig) {
        super(eConfig, Material.iron, TileEntangledChalice.class);
    }

    @Override
    public void setBlockBoundsForItemRender() {
        setBlockBounds(0.16F, 0F, 0.16F, 0.84F, 0.98F, 0.84F);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos blockPos) {
        setBlockBoundsForItemRender();
    }

    @Override
    public void addCollisionBoxesToList(World world, BlockPos blockPos, IBlockState blockState, AxisAlignedBB area, List list, Entity entity) {
        setBlockBounds(0, 0, 0, 1, 1, 1);
        super.addCollisionBoxesToList(world, blockPos, blockState, area, list, entity);
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumFacing side, float motionX, float motionY, float motionZ) {
    	return tankComponent.onBlockActivatedTank(world, blockPos, player, side, motionX, motionY, motionZ) ||
                super.onBlockActivated(world, blockPos, blockState, player, side, motionX, motionY, motionZ);
    }
    
    @Override
    public String getInfo(ItemStack itemStack) {
        return tankComponent.getInfoTank(itemStack);
    }

	@SuppressWarnings("rawtypes")
	@Override
	public void provideInformation(ItemStack itemStack,
			EntityPlayer entityPlayer, List list, boolean par4) {

	}

	@Override
	public String getTankNBTName() {
		return TileEntangledChalice.NBT_TAG_TANK;
	}

	@Override
	public int getTankCapacity(ItemStack itemStack) {
		return getMaxCapacity();
	}
	
	@Override
	public void setTankCapacity(ItemStack itemStack, int capacity) {
		// Do nothing
	}
	
	@Override
	public void setTankCapacity(NBTTagCompound tag, int capacity) {
		// Do nothing
	}
	
	@Override
	public void writeAdditionalInfo(TileEntity tile, NBTTagCompound tag) {
    	super.writeAdditionalInfo(tile, tag);
    	tankComponent.writeAdditionalInfo(tile, tag);
    }
	
	@Override
	public int getMaxCapacity() {
		return TileEntangledChalice.BASE_CAPACITY;
	}
	
	@Override
	public boolean isActivatable() {
		return true;
	}
	
	@Override
	public ItemStack toggleActivation(ItemStack itemStack, World world, EntityPlayer player) {
		if(player.isSneaking()) {
            if(!world.isRemote) {
            	ItemStack activated = itemStack.copy();
            	activated.setItemDamage(1 - activated.getItemDamage());
            	return activated;
            }
            return itemStack;
		}
		return itemStack;
	}

	@Override
	public boolean isActivated(ItemStack itemStack, World world, Entity entity) {
		return itemStack.getItemDamage() == 1;
	}
	
	@Override
	public int getLightValue(IBlockAccess world, BlockPos blockPos) {
		TileEntity tile = world.getTileEntity(blockPos);
		if(tile != null && tile instanceof TileEntangledChalice) {
			TileEntangledChalice tank = (TileEntangledChalice) tile;
			if(tank.getTank().getFluidType() != null) {
				return (int) Math.min(15, tank.getFillRatio() * tank.getTank().getFluidType().getLuminosity());
			}
		}
		return 0;
	}

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        ItemStack itemStack = new ItemStack(item);
        EntangledChaliceItem chaliceItem = (EntangledChaliceItem) Item.getItemFromBlock(EntangledChalice.getInstance());
        chaliceItem.setTankID(itemStack, "creativeTank0");
        list.add(itemStack);
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return EnumRarity.RARE;
    }
}
