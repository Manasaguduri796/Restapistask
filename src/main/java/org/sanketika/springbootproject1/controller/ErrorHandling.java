package org.sanketika.springbootproject1.controller;
import org.hibernate.exception.JDBCConnectionException;
import org.sanketika.springbootproject1.response.DatasetResponse;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import java.sql.SQLNonTransientConnectionException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorHandling {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentTypeMismatchException m)  {
        Map<String, Object> response = new HashMap<>();
        response.put("Status", HttpStatus.BAD_REQUEST.toString());
        response.put("message", "invalid request parameter is provided.");
        response.put("timestamp",LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }



    @ExceptionHandler({JDBCConnectionException.class,SQLNonTransientConnectionException.class, DataAccessResourceFailureException.class,CannotCreateTransactionException.class})
    public ResponseEntity<Map<String, Object>> handleDbException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(DatasetResponse.createResponse("Failure",HttpStatus.INTERNAL_SERVER_ERROR,"database connection is down",null));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String,Object>> handleNoResourceFound(Exception e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(DatasetResponse.createResponse("Failure",HttpStatus.NOT_FOUND,"requested dataset id is not found or route is incorrect",null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleGenericException(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(DatasetResponse.createResponse("Failure",HttpStatus.INTERNAL_SERVER_ERROR,"An unexpected error is occured",null));
    }

}
