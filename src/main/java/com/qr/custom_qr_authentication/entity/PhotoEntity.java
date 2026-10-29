package com.qr.custom_qr_authentication.entity;

import lombok.*;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/*
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
*/

import java.sql.Types;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;

//@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
/*
@SQLDelete(sql = "UPDATE humanresources_schema.photos SET active = false WHERE id = ? AND record_version = ?")
@Entity
@Table(name = "photos", schema = "humanresources_schema")
 */
public class PhotoEntity {

    /*
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     */
    private Long id;

    /*
    @Column(name = "file_name", nullable = false)
     */
    private String fileName;

    /*
    @Column(name = "content_type")
     */
    private String contentType; // bijv. "image/jpeg"


    /**
     * this annotation are for postgresql
     */
    /*
    @Lob
    @JdbcTypeCode(Types.BINARY)
    @Column(name = "image_data")
     */
    private byte[] imageData;

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
    @CreationTimestamp
    @Column(name = "updated_at", updatable = false)
     */
    private OffsetDateTime updatedAt;

    /*
    @LastModifiedBy
    @Column(name = "last_modified_by", length = 50)
     */
    private String lastModifiedBy; // Tracks who soft-deleted or edited the record

    /*
    @Column(name = "active")
     */
    private boolean active;

    //region optional getters
    public Optional<Long> getId(){
        return Optional.ofNullable(this.id);
    }

    public Optional<String> getFilename(){
        return Optional.ofNullable(this.fileName);
    }

    public Optional<String> getContentTyp(){
        return Optional.ofNullable(this.contentType);
    }

    public Optional<byte[]> getImageData(){
        return Optional.ofNullable(this.imageData);
    }

    public Optional<OffsetDateTime> getUploadedAt(){
        return Optional.ofNullable(this.updatedAt);
    }

    public Optional<Boolean> getActive(){
        return Optional.ofNullable(this.active);
    }

    public Optional<PersonsEntity> getPerson(){
        return Optional.ofNullable(this.person);
    }
    //endregion



    //region mapping

    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
     */
    private PersonsEntity person;

    //endregion

    @Builder.Default
    /*
    @Version
    @Column(name = "record_version")
     */
    private long version = 0L;


    /**
     * this will print the class fields with newline for each field
     *
     * @return
     */
    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .setExcludeFieldNames("person")
                .toString();
    }

    //region Hash and equals code
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhotoEntity)) return false;
        PhotoEntity photoEntity = (PhotoEntity) o;
        return Objects.equals(this.id, photoEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
    //endregion

}
