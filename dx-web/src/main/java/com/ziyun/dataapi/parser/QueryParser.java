package com.ziyun.dataapi.parser;

import com.ziyun.dataapi.bean.DataRequest;

public interface QueryParser {
    String parseRequestToDSL(DataRequest request);
    String generateSQL(String dsl);
}
