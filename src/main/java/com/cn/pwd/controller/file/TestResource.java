package com.cn.pwd.controller.file;

import com.cn.pwd.service.TaskJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/task")
public class TestResource {

    @Autowired
    TaskJobService taskJobService;

    @GetMapping("/test1")
    public ResponseEntity<?> test1(String name) {
        try {
            taskJobService.startTask(name);
            return new ResponseEntity<>(name,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/test2")
    public ResponseEntity<?> test2(String name) {
        try {
            taskJobService.closeTask(name);
            return new ResponseEntity<>(name,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}
