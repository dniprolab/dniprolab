package com.fcdnipro.dniprolab.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Advertisement.
 */
@Entity
@Table(name = "advertisement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "advertisement")
public class Advertisement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date")
    private ZonedDateTime date;
    
    @NotNull
    @Size(max = 128)
    @Column(name = "text", length = 128, nullable = false)
    private String text;
    
    @Column(name = "author")
    private String author;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }
    
    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Advertisement advertisement = (Advertisement) o;
        if(advertisement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, advertisement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Advertisement{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", text='" + text + "'" +
            ", author='" + author + "'" +
            '}';
    }
}
