package com.example.dto.delivery_tracker.request.converter;

import com.example.Company;
import org.springframework.core.convert.converter.Converter;

public class CompanyRequestConverter implements Converter<String, Company> {

    @Override
    public Company convert(String source) {
        return Company.from(source);
    }

}
