package com.pietos.bgv.service.cases;

import java.util.List;

import com.pietos.bgv.dto.response.cases.QCComponentLevelResponse;

public interface QCComponentLevelService {

    List<QCComponentLevelResponse> getComponentLevelCases();
}