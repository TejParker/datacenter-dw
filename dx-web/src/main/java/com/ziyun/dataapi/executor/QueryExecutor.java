package com.ziyun.dataapi.executor;

import com.ziyun.dataapi.bean.DataResponse;

public interface QueryExecutor {
    DataResponse executeSQL(String sql);
}
