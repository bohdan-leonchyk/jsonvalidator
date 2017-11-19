package com.leonchyk.jsonvalidator.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonchyk.jsonvalidator.entity.MyEntity;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;

@EnableSwagger2
@RestController
public class MyController {

    @RequestMapping(value = "/postObj", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> postObj(@RequestBody String obj) {
        try {
            MyEntity myEntity = new ObjectMapper().readValue(obj, MyEntity.class);
            validate(myEntity);
            return ResponseEntity.ok(myEntity);
        } catch (Exception re) {
            return ResponseEntity.badRequest().body(re.getMessage());
        }
    }

    private void validate(MyEntity myEntity) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Map<String, String> map = new HashMap<>();

        validator
                .validate(myEntity)
                .forEach(d -> map.put(d.getPropertyPath().toString(), d.getMessage()));

        if (!map.isEmpty()) {
            throw new RuntimeException(new JSONObject(map).toString());
        }
    }

}
