package hu.sapka12.mozaik.index.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Identifier
{
    private final String userId;
    private final String pictureId;

    public Identifier(String userId, String pictureId)
    {
        this.userId = userId;
        this.pictureId = pictureId;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getPictureId()
    {
        return pictureId;
    }
 
    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }   
}
