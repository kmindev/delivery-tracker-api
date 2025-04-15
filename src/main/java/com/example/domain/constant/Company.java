package com.example.domain.constant;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum Company {

    KR_EPOST("kr.epost", "우체국택배"),
    CJ("kr.cjlogistics", "CJ대한통운"),
    COUPANG("kr.coupangls", "쿠팡 로지스틱스 서비스"),
    CU("kr.cupost", "CU 편의점택배"),
    CHUNIL("kr.chunilps", "천일택배");

    private final String id;
    private final String name;

    Company(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Company from(String name) {
        return Arrays.stream(Company.values())
                .filter(company -> company.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 이름의 회사를 찾을 수 없습니다: " + name));
    }

}
