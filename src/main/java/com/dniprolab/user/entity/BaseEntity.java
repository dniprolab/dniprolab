package com.dniprolab.user.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by Overlord on 02.01.2016.
 */
@MappedSuperclass
public abstract class BaseEntity<ID> {

    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "CREATED")
    DateTime created;

    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "UPDATED")
    DateTime updated;

    @Version
    private Long version;

    public abstract ID getId();

    @PrePersist
    public void prePersist(){
        DateTime now = DateTime.now();
        created = now;
        updated = now;
    }

    @PreUpdate
    public void update(){
        updated = DateTime.now();
    }

    public DateTime getCreated() {
        return created;
    }

    public DateTime getUpdated() {
        return updated;
    }

    public Long getVersion() {
        return version;
    }
}
