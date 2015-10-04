package hu.sapka12.mozaik.maker;

import hu.sapka12.mozaik.maker.ITile;

public interface IInputImage<T>
{
    public int getTilesInARow();

    public int getTilesInColumn();

    public ITile<T> getTile(int row, int column);
}
