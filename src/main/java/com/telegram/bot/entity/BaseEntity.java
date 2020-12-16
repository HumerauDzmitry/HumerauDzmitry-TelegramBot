package com.telegram.bot.entity;

import com.telegram.bot.enums.Status;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @CreatedDate
    @Column( name = "created_at", nullable = false )
    private Date created;

    @Column( name = "created_by", nullable = false )
    private String createdBy;

    @LastModifiedDate
    @Column( name = "updated_at", nullable = false )
    private Date updated;

    @Column( name = "updated_by", nullable = false )
    private String updatedBy;

    @Enumerated( EnumType.STRING )
    @Column( name = "status", nullable = false)
    private Status status;
}
