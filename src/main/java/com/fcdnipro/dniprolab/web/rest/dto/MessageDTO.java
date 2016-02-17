package com.fcdnipro.dniprolab.web.rest.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;


/**
 * A DTO for the Message entity.
 */
public class MessageDTO implements Serializable {

    private Long id;

    @Lob
    private String text;


    @Lob
    private byte[] image;

    private String imageContentType;

    @Lob
    private byte[] document;

    private String documentContentType;

    private String author;


    private ZonedDateTime created;


    @Size(max = 64)
    private String title;


    private Long userId;

    private String userLogin;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MessageDTO messageDTO = (MessageDTO) o;

        if ( ! Objects.equals(id, messageDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
            "id=" + id +
            ", text='" + text + "'" +
            ", image='" + image + "'" +
            ", document='" + document + "'" +
            ", author='" + author + "'" +
            ", created='" + created + "'" +
            ", title='" + title + "'" +
            '}';
    }
}
