package zeropoint.minecraft.core.util;

public enum EnumBlockSide {
	BOTTOM(0, "bottom"), TOP(1, "top"), NORTH(2, "north"), SOUTH(3, "south"), WEST(4, "west"), EAST(5, "east");
	public final int side;
	public final String name;
	private EnumBlockSide(int iside, String sname) {
		side = iside;
		name = sname;
	}
	public static final EnumBlockSide getByInt(int l) {
		switch (l) {
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
