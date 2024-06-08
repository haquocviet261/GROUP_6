package com.petshop.controller;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/admin")
public class AdminController {
    @GetMapping("/get")
    public String get() {
        return "GET: admin controller";
    }
    @PostMapping
    public String post() {
        return "POST: admin controller";
    }
    @PutMapping
    public String put() {
        return "PUT: admin controller";
    }
    @DeleteMapping
    public String delete() {
        return "DELETE: admin controller";
    }
}
