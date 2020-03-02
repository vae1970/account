package com.vae.account.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * BaseEntity
 */
@Getter
@Setter
public class BaseEntity {

    /**
     * id
     */
    protected Long id;
    /**
     * create time
     */
    private Date createdAt;
    /**
     * update time
     */
    private Date updatedAt;
    /**
     * version
     */
    private Integer version;

}
