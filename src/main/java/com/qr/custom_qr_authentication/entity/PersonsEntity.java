package com.qr.custom_qr_authentication.entity;


import lombok.*;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/*
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
*/

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("java:s125")
//@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
/*
@SQLDelete(sql = "UPDATE humanresources_schema.persons SET active = false WHERE id = ? AND record_version = ?")
@Entity
@Table(name = "persons",schema = "HUMANRESOURCES_SCHEMA")
 */
public class PersonsEntity {

    /*
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
     */
    private long id;

    /*
    @Column(name = "first_name", length = 50)
     */
    private String firstname;

    /*
    @Column(name = "last_name", length = 50)
     */
    private String lastname;

    /*
    @Column(name = "active")
     */
    private boolean active;

    /*
    @Column(
            name = "email",
            length = 255
    )
    @Pattern(regexp = "^$|^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "Invalid email")
     */
    private String email;

    /*
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
     */
    private OffsetDateTime createdAt;

    /*
    @CreatedBy
    @Column(name = "created_by", length = 50, nullable = true, updatable = false)
     */
    private String createdBy;

    /*
    @LastModifiedDate
    @Column(name = "updated_at")
     */
    private OffsetDateTime updatedAt;

    /*
    @LastModifiedBy
    @Column(name = "last_modified_by", length = 50)
     */
    private String lastModifiedBy; // Tracks who soft-deleted or edited the record

    //region mapping

    /*
    @OrderBy("updated_at ASC")
    @OneToMany(mappedBy = "person",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
     */
    @Builder.Default
    private List<PhotoEntity> photos = new ArrayList<>();

    /**
     * Links a photo to this person (sets Photo.Person_ID) and adds it to the set.
     * This is the preferred way to establish the link.
     * @param photo The PhotoEntity to associate.
     */
    public void addPhoto(PhotoEntity photo) {
        photos.add(photo);
        photo.setPerson(this);
    }

    public boolean areTherePhotos(){
        return Optional.ofNullable(this.photos).map(e->!e.isEmpty()).orElse(false);
    }

    /**
     * Removes the link between the person and the photo (sets Photo.Person_ID to NULL).
     * Since orphanRemoval=true, removing the photo from the set will DELETE it
     * from the database if this person is the only reference (the only one linked).
     * @param photo The PhotoEntity to disassociate/delete.
     */
    public void removePhoto(PhotoEntity photo) {
        // 1. Remove from the collection (triggers DELETE due to orphanRemoval=true)
        this.photos.remove(photo);

        // Step 2: Unlink the inverse side (THE CRITICAL STEP)
        // This sets the PhotoEntity's reference to the PersonEntity to null,
        // preventing Hibernate from trying to process a stale reference during flush.
        photo.setPerson(null);
    }

    //endregion


    @Builder.Default
    /*
    @Version
    @Column(name = "record_version")
     */
    private long version = 0L;


    //region getters
    public Optional<String> getFirstname(){
        return Optional.ofNullable(this.firstname);
    }

    public Optional<String> getLastname(){
        return Optional.ofNullable(this.lastname);
    }

    public Optional<String> getMail(){return Optional.ofNullable(this.email);}

    public Optional<OffsetDateTime> getCreatedAt() {
        // Since 'creates_at' is 'nullable = false' and managed by @CreatedDate,
        // it should always have a value after persistence.
        return Optional.ofNullable(this.createdAt);
    }

    public Optional<String> getCreatedBy() {
        // Since 'created_by' is 'nullable = false' and managed by @CreatedBy,
        // it should always have a value after persistence.
        return Optional.ofNullable(this.createdBy);
    }

    public Optional<OffsetDateTime> getUpdatedAt() {
        // 'updated_at' is 'nullable = false', so it should always have a value.
        return Optional.ofNullable(this.updatedAt);
    }

    public Optional<String> getLastModifiedBy() {
        return Optional.ofNullable(this.lastModifiedBy);
    }

    //endregion

    /**
     * this will print the class fields with newline for each field
     *
     * @return
     */
    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .setExcludeFieldNames("photos")
                .toString();
    }

    //region Hash and equals code
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonsEntity personEntity)) return false;
        return Objects.equals(this.id, personEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
    //endregion

}
