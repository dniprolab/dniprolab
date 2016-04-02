package com.fcdnipro.dniprolab.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A VideoAnalytics.
 */
@Entity
@Table(name = "video_analytics")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "videoanalytics")
public class VideoAnalytics implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 128)
    @Column(name = "label", length = 128, nullable = false)
    private String label;
    
    @NotNull
    @Size(max = 128)
    @Column(name = "reference", length = 128, nullable = false)
    private String reference;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "author")
    private String author;
    
    @Column(name = "date")
    private LocalDate date;
    
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

    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VideoAnalytics videoAnalytics = (VideoAnalytics) o;
        if(videoAnalytics.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, videoAnalytics.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "VideoAnalytics{" +
            "id=" + id +
            ", label='" + label + "'" +
            ", reference='" + reference + "'" +
            ", description='" + description + "'" +
            ", author='" + author + "'" +
            ", date='" + date + "'" +
            '}';
    }
}
