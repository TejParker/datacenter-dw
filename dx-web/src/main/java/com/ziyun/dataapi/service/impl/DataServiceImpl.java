package com.ziyun.dataapi.service.impl;

import com.ziyun.dataapi.bean.DataRequest;
import com.ziyun.dataapi.bean.DataResponse;
import com.ziyun.dataapi.executor.QueryExecutor;
import com.ziyun.dataapi.parser.QueryParser;
import com.ziyun.dataapi.service.DataService;
import org.springframework.stereotype.Service;

@Service
public class DataServiceImpl implements DataService {
    private final QueryParser parser;
    private final QueryExecutor executor;

    public DataServiceImpl(QueryParser parser, QueryExecutor executor) {
        this.parser = parser;
        this.executor = executor;
    }

    @Override
    public DataResponse processRequest(DataRequest request) {
        String dsl = parser.parseRequestToDSL(request);
        String sql = parser.generateSQL(dsl);
        return executor.executeSQL(sql);
    }
}
