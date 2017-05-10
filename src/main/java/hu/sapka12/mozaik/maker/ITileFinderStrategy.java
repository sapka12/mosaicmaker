package hu.sapka12.mozaik.maker;

public interface ITileFinderStrategy<T>
{
    public ITile<T> findSubstitute(ITile<T> tile);    
}
