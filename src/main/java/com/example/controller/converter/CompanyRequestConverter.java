package com.example.controller.converter;

import com.example.domain.constant.Company;
import org.springframework.core.convert.converter.Converter;

public class CompanyRequestConverter implements Converter<String, Company> {

    @Override
    public Company convert(String source) {
        return Company.from(source);
    }

}
