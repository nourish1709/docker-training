package com.nourish1709.learning.shoppingservice.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@Data
@Service
@RefreshScope
public class DiscountServiceImpl implements DiscountService {

    @Value("${features.discount.available:false}")
    private boolean discountAvailable;

    @Override
    public boolean isDiscountAvailable() {
        return discountAvailable;
    }
}
