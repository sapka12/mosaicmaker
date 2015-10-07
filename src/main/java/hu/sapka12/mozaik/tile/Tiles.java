package hu.sapka12.mozaik.tile;

import hu.sapka12.mozaik.maker.bufferedimage.Tile;

public class Tiles {

    private final Tile[][] tiles;

    Tiles(Tile[][] tileArray) {
        this.tiles = tileArray;
    }

    public int numberOfRows() {
        return tiles.length;
    }

    public int numberOfColumns() {
        return tiles[0].length;
    }
}
