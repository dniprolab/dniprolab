package com.fcdnipro.dniprolab.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Message.
 */
@Entity
@Table(name = "message")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "message")
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 64)
    @Column(name = "title", length = 64)
    private String title;

    @Lob
    @Column(name = "text")
    private String text;
    
    @Lob
    @Column(name = "image")
    private byte[] image;
    
    @Column(name = "image_content_type")
    private String imageContentType;

    @Lob
    @Column(name = "document")
    private byte[] document;
    
    @Column(name = "document_content_type")
    private String documentContentType;

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

    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }

    public byte[] getImage() {
        return image;
    }
    
    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public byte[] getDocument() {
        return document;
    }
    
    public void setDocument(byte[] document) {
        this.document = document;
    }

    public String getDocumentContentType() {
        return documentContentType;
    }

    public void setDocumentContentType(String documentContentType) {
        this.documentContentType = documentContentType;
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

    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
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
        Message message = (Message) o;
        if(message.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Message{" +
            "id=" + id +
            ", text='" + text + "'" +
            ", image='" + image + "'" +
            ", imageContentType='" + imageContentType + "'" +
            ", document='" + document + "'" +
            ", documentContentType='" + documentContentType + "'" +
            ", author='" + author + "'" +
            ", created='" + created + "'" +
            ", title='" + title + "'" +
            '}';
    }
}
