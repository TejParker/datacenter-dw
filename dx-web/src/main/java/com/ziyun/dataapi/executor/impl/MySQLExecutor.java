package com.ziyun.dataapi.executor.impl;

import com.ziyun.dataapi.bean.DataResponse;
import com.ziyun.dataapi.executor.QueryExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MySQLExecutor implements QueryExecutor {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public DataResponse executeSQL(String sql) {
        try {
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
            return new DataResponse(true, "Query executed successfully.", results);
        } catch (Exception e) {
            return new DataResponse(false, "Error executing query: " + e.getMessage(), null);
        }

        /* List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        return new DataResponse(results); */
    }
}
