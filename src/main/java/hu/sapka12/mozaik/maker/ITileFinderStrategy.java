package hu.sapka12.mozaik.maker;

import hu.sapka12.mozaik.maker.ITile;

public interface ITileFinderStrategy<T>
{

    public ITile<T> findSubstitute(ITile<T> tile);    
}
