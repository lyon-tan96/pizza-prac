package vttp2022.assessment.csf.orderbackend.repositories;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class Response {

    private Integer code;
    private String message;
    private String data = "{}";

    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("code", code)
                .add("message", message)
                .add("data", data)
                .build();
    }
    
}
