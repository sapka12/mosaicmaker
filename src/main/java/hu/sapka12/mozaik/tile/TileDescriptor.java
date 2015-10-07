package hu.sapka12.mozaik.tile;

import hu.sapka12.mozaik.maker.bufferedimage.Tile;

public class TileDescriptor {

    private final Tile tile;
    private final int xPositionInOriginal;
    private final int yPositionInOriginal;

    public TileDescriptor(Tile tile, int xPositionInOriginal, int yPositionInOriginal) {
        this.tile = tile;
        this.xPositionInOriginal = xPositionInOriginal;
        this.yPositionInOriginal = yPositionInOriginal;
    }

    public Tile getTile() {
        return tile;
    }

    public int getxPositionInOriginal() {
        return xPositionInOriginal;
    }

    public int getyPositionInOriginal() {
        return yPositionInOriginal;
    }

}
