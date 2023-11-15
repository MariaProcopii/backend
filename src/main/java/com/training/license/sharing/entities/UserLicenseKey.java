package com.training.license.sharing.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class UserLicenseKey implements Serializable {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "license_id")
    private Integer licenseId;
}
