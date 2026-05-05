package com.project.etltool_new.controller;

import com.project.etltool_new.entity.Employee;
import com.project.etltool_new.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@RestController
public class HelloController {

    @Autowired
    private EmployeeRepository repo;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {

        int inserted = 0;
        int skipped = 0;

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(file.getInputStream()));

            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {

                // skip header
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] data = line.split(",");

                // validation: check column count
                if (data.length < 4) {
                    skipped++;
                    continue;
                }

                try {
                    String name = data[0];
                    String email = data[1];
                    String department = data[2];
                    double salary = Double.parseDouble(data[3]);

                    // duplicate check
                    if (repo.existsByEmail(email)) {
                        skipped++;
                        continue;
                    }

                    Employee emp = new Employee();
                    emp.setName(name);
                    emp.setEmail(email);
                    emp.setDepartment(department);
                    emp.setSalary(salary);

                    repo.save(emp);
                    inserted++;

                } catch (Exception e) {
                    // invalid row (salary error etc.)
                    skipped++;
                }
            }

        } catch (Exception e) {
            return "File processing failed!";
        }

        return "Upload Completed | Inserted: " + inserted + " | Skipped: " + skipped;
    }
}