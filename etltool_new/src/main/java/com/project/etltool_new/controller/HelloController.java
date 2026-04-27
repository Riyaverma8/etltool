package com.project.etltool_new.controller;

import com.project.etltool_new.entity.Employee;
import com.project.etltool_new.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @Autowired
    private EmployeeRepository repo;

    @PostMapping("/add")
    public String addEmployee(@RequestBody Employee emp) {
        repo.save(emp);
        return "Data Saved Successfully";
    }

    @GetMapping("/get")
    public java.util.List<Employee> getAll() {
        return repo.findAll();
    }
}