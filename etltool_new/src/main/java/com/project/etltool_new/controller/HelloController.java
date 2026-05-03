package com.project.etltool_new.controller;

import com.project.etltool_new.entity.Employee;
import com.project.etltool_new.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;

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
    @PutMapping("/update/{id}")
    public String updateEmployee(@PathVariable int id, @RequestBody Employee emp) {

        Employee existing = repo.findById(id).orElse(null);

        if (existing != null) {
            existing.setName(emp.getName());
            repo.save(existing);
            return "Updated Successfully";
        } else {
            return "Employee Not Found";
        }
    }
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(file.getInputStream()));

            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {

                // header skip
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] data = line.split(",");

                Employee emp = new Employee();
                emp.setName(data[0]);
                emp.setEmail(data[1]);
                emp.setDepartment(data[2]);
                emp.setSalary(Double.parseDouble(data[3]));

                repo.save(emp);
            }

            return "CSV Uploaded Successfully";

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}