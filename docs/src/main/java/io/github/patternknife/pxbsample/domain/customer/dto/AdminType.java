package io.github.patternknife.pxbsample.domain.customer.dto;

import io.github.patternknife.pxbsample.domain.customer.entity.Customer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminType {

    private Boolean isSuperAdmin;
    private Boolean isAdmin;
    private Customer customer;

}
