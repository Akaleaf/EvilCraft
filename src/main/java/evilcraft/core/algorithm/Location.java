package evilcraft.core.algorithm;

import evilcraft.api.ILocation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Location class.
 * @author rubensworks
 *
 */
public class Location implements ILocation {

    private int[] coordinates;
    
    /**
     * Make a new instance.
     * @param coordinates The coordinates.
     */
    public Location(int... coordinates) {
        this.coordinates = coordinates;
    }
    
    @Override
    public int getDimensions() {
        return this.coordinates.length;
    }

    @Override
    public int[] getCoordinates() {
        return coordinates;
    }

    @Override
    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }
    
    @Override
	public Size getDifference(ILocation location) {
    	if(location.getDimensions() != this.getDimensions()) {
    		throw new IllegalArgumentException(
    				"The dimensions of this and the given dimension are not the same.");
    	}
    	int[] diffs = new int[getDimensions()];
    	for(int i = 0; i < diffs.length; i++) {
    		diffs[i] = Math.abs(location.getCoordinates()[i] - this.getCoordinates()[i]);
    	}
		return new Size(diffs);
	}
    
    @Override
    public int getDistance(ILocation location) {
    	Size difference = getDifference(location);
    	int distance = 0;
    	for(int i = 0; i < difference.getDimensions(); i++) {
    		distance += difference.getSizes()[i];
    	}
    	return (int) Math.pow(distance, 1 / difference.getDimensions());
    }

    @Override
    public ILocation copy() {
        return new Location(coordinates.clone());
    }
    
    @Override
    public String toString() {
    	StringBuilder builder = new StringBuilder();
    	builder.append("[");
    	for(int coord : getCoordinates()) {
    		builder.append(coord);
    		builder.append(';');
    	}
    	builder.append("]");
    	return builder.toString();
    }

    @Override
    public boolean equals(Object object) {
    	return object instanceof ILocation && ((ILocation) object).getCoordinates().equals(getCoordinates());
    }

	@Override
	public int compareTo(ILocation o) {
		if(getDimensions() != o.getDimensions()) {
			return getDimensions() - o.getDimensions();
		}
		int i = 0;
		boolean validBuffer = false;
		int buffer = Integer.MAX_VALUE; // This is used to store the minimum compared value > 0
		while(i < getDimensions()) {
			if(getCoordinates()[i] != o.getCoordinates()[i]) {
				int comp =  getCoordinates()[i] - o.getCoordinates()[i];
				if(comp < 0) {
					return comp;
				} else {
					validBuffer = true;
					buffer = Math.min(buffer, comp);
				}
			}
			i++;
		}
		return validBuffer ? buffer : 0;
	}

	@Override
	public NBTTagCompound toNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setIntArray("coordinates", getCoordinates());
		return tag;
	}

	@Override
	public void fromNBT(NBTTagCompound tag) {
		setCoordinates(tag.getIntArray("coordinates"));
	}

	@Override
	public ILocation subtract(ILocation location) {
		if(getDimensions() != location.getDimensions()) {
			throw new IllegalArgumentException("The dimensions differ: this: ["
		+ getDimensions() + "] subtractor [" + location.getDimensions() + "]");
		}
		
		int[] result = new int[getDimensions()];
		for(int i = 0; i < getDimensions(); i++) {
			result[i] = getCoordinates()[i] - location.getCoordinates()[i];
		}
		
		ILocation ret = copy();
		ret.setCoordinates(result);
		return ret;
	}

	@Override
	public ILocation add(ILocation location) {
		int[] c = location.getCoordinates();
		return subtract(new Location(-c[0], -c[1], -c[2]));
	}

	@Override
	public ILocation offset(ForgeDirection direction) {
		if(getDimensions() != 3) {
			throw new IllegalArgumentException("Can only add a direction to a location of three dimensions.");
		}

		int[] result = new int[getDimensions()];
		result[0] = getCoordinates()[0] + direction.offsetX;
		result[1] = getCoordinates()[1] + direction.offsetY;
		result[2] = getCoordinates()[2] + direction.offsetZ;

		ILocation ret = copy();
		ret.setCoordinates(result);
		return ret;
	}

}
