package com.aireno.vapas.service.base;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

public abstract class EntityBase implements Serializable
{
    public void setId(Long id) {
        this.id = id;
    }

    private Long id;

    public EntityBase()
    {
    }

    public Long getId()
    {
        if (id == null)
            id = new Long(0);
        return id;
    }
}
