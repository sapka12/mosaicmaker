package hu.sapka12.mozaik.index.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mozaik_indexdata")
public class IndexData
{

    @Id
    private final Identifier id;
    private final Color color;

    public IndexData(Identifier id, Color color)
    {
        this.id = id;
        this.color = color;
    }

    public Identifier getId()
    {
        return id;
    }

    public Color getColor()
    {
        return color;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
