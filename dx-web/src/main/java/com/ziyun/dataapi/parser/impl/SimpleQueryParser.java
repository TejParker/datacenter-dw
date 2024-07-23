package com.ziyun.dataapi.parser.impl;

import com.ziyun.dataapi.bean.DataRequest;
import com.ziyun.dataapi.parser.QueryParser;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SimpleQueryParser implements QueryParser {
    @Override
    public String parseRequestToDSL(DataRequest request) {
        Map<String, Object> requestParameters = request.getParameters();

        // Implement the logic here
        return "DSL representation";
    }

    @Override
    public String generateSQL(String dsl) {
        // Logic to convert DSL to SQL
        return "SELECT * FROM loc_ip limit 1";
    }
}
