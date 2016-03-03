package com.fcdnipro.dniprolab.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

/**
 * Entity for videotape document
 */
@Entity
@Table(name = "video")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "video")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 2, max = 64)
    @Column(name = "label", length = 64, nullable = false)
    private String label;

    @NotNull
    @Size(min = 2, max = 128)
    @Column(name = "opponent", length = 128, nullable = false)
    private String reference;

    @Size(max = 256)
    @Column(name = "description", length = 256, nullable = false)
    private String description;

    @Column(name = "author")
    private String author;

    @Column(name = "created")
    private ZonedDateTime created;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Video)) return false;

        Video video = (Video) o;

        if (!getId().equals(video.getId())) return false;
        if (!getLabel().equals(video.getLabel())) return false;
        if (!getReference().equals(video.getReference())) return false;
        if (!getDescription().equals(video.getDescription())) return false;
        if (!getAuthor().equals(video.getAuthor())) return false;
        if (!getCreated().equals(video.getCreated())) return false;
        return getUser().equals(video.getUser());

    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getLabel().hashCode();
        result = 31 * result + getReference().hashCode();
        result = 31 * result + getDescription().hashCode();
        result = 31 * result + getAuthor().hashCode();
        result = 31 * result + getCreated().hashCode();
        result = 31 * result + getUser().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Video{" +
            "id=" + id +
            ", label='" + label + '\'' +
            ", reference='" + reference + '\'' +
            ", description='" + description + '\'' +
            ", author='" + author + '\'' +
            ", created=" + created +
            ", user=" + user +
            '}';
    }
}
