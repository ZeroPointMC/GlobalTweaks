package zeropoint.minecraft.core.util;

/**
 * Values for all six sides of a Block, containing the numeric ID and a human readable name
 * 
 * @author Zero Point
 */
public enum EnumBlockSide {
	@SuppressWarnings("javadoc")
	BOTTOM(0, "bottom"), @SuppressWarnings("javadoc")
	TOP(1, "top"), @SuppressWarnings("javadoc")
	NORTH(2, "north"), @SuppressWarnings("javadoc")
	SOUTH(3, "south"), @SuppressWarnings("javadoc")
	WEST(4, "west"), @SuppressWarnings("javadoc")
	EAST(5, "east");
	/**
	 * The integer ID of the represented side
	 */
	public final int side;
	/**
	 * The human readable name of the represented side
	 */
	public final String name;
	private EnumBlockSide(int iside, String sname) {
		this.side = iside;
		this.name = sname;
	}
	/**
	 * Get the EnumBlockSide representing the side with the given ID
	 * 
	 * @param sideID
	 *            - the numeric side ID
	 * @return EnumBlockSide representing the side, or <code>null</code> if an invalid ID is given
	 */
	public static final EnumBlockSide getByInt(int sideID) {
		switch (sideID) {
			case 0:
				return BOTTOM;
			case 1:
				return TOP;
			case 2:
				return NORTH;
			case 3:
				return SOUTH;
			case 4:
				return WEST;
			case 5:
				return EAST;
			default:
				return null;
		}
	}
}
