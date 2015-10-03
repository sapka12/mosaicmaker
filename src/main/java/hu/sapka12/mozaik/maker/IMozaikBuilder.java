package hu.sapka12.mozaik.maker;

public interface IMozaikBuilder<T>
{

    public T build();

    public void append(ITile<T> substituteTile, int row, int column);
    
}
