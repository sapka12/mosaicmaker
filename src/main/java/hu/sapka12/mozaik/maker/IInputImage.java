package hu.sapka12.mozaik.maker;

public interface IInputImage<T>
{
    public int getTilesInARow();

    public int getTilesInColumn();

    public ITile<T> getTile(int row, int column);
}
