package com.ziyun.dataapi.service;

import com.ziyun.dataapi.bean.DataRequest;
import com.ziyun.dataapi.bean.DataResponse;
import org.springframework.context.annotation.Bean;


public interface DataService {
    DataResponse processRequest(DataRequest request);
}
