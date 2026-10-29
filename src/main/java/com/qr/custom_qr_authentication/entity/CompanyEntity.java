package com.qr.custom_qr_authentication.entity;


import lombok.*;

/*
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
*/

import java.time.OffsetDateTime;
import java.util.Objects;

/*
@EntityListeners(AuditingEntityListener.class)

 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
/*
@SQLDelete(sql = "UPDATE humanresources_schema.companies SET active = false WHERE id = ? AND record_version = ?")
@Entity
@Table(name = "companies",schema = "HUMANRESOURCES_SCHEMA")
 */
public class CompanyEntity {

    /*
    @Id                  // 3. Marks this field as the Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 4. Auto-increments the ID (like SERIAL in PostgreSQL)
     */
    private Long id;

    /*
    @Column(
            name = "company_name",
            nullable = false, unique = true,
            length = 100
    ) // 5. Customizes column rules
     */
    private String nameCompany;

    /*
    @Column(
            name = "legal_entity_type",
            nullable = false,
            length = 50 // Restricts the VARCHAR column size in the database schema
    )
     */
    private String legalEntityType; // e.g., "LLC", "GmbH", "Inc"

    /*
    @Column(
            name = "tenant_id",
            nullable = false,
            unique = true,
            length = 36
    )
     */
    private String tenantId; // The ID you would put in your JWT!

    /*
    @CreatedDate
    @Column(name = "created_at", updatable = false)
     */
    private OffsetDateTime createdAt;

    /*
    @CreatedBy
    @Column(name = "created_by", length = 50, nullable = true, updatable = false)
     */
    private String createdBy;

    /*
    @LastModifiedDate
    @Column(name = "updated_at") // New column to store the last modification time
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
        return "CompanyEntity{" +
                "id=" + id +
                ", nameCompany='" + nameCompany + '\'' +
                ", tenantId='" + tenantId + '\'' +
                '}';
    }

    //region Hash and equals code
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompanyEntity companyEntity)) return false;
        return Objects.equals(this.id, companyEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
    //endregion

}
