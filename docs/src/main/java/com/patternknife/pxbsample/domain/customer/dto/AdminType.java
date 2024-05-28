package com.patternknife.pxbsample.domain.customer.dto;

import com.patternknife.pxbsample.domain.customer.entity.Customer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminType {

    private Boolean isSuperAdmin;
    private Boolean isAdmin;
    private Customer customer;

}
