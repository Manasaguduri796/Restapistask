package org.sanketika.springbootproject1.response;

import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class DatasetResponse {
    public static Map<String, Object> createResponse(String status, HttpStatus responseCode, String errorMessage, Object result) {
       Map<String,Object> response=new LinkedHashMap<>();
       response.put("id","api");
       response.put("ver","1.0");
       response.put("ts", Instant.now().toString());


       Map<String,Object> param=new LinkedHashMap<>();
       param.put("resmsgid", UUID.randomUUID().toString());
       param.put("status",status);
       param.put("error_msg",errorMessage);


       response.put("params",param);
       response.put("responseCode",responseCode.value());
       response.put("result",result);

       return response;
    }
}
