package com.EmployeSystem.Management.controller;

import com.EmployeSystem.Management.model.AuditTrail;
import com.EmployeSystem.Management.repository.AuditTrailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-trails")
public class AuditTrailController {
    @Autowired
    private AuditTrailRepository repository;

    @GetMapping
    public List<AuditTrail> getAllAuditLogs() {
        return repository.findAll();
    }

    @GetMapping("/employee/{employeeId}")
    public List<AuditTrail> getLogsByEmployee(@PathVariable Long employeeId) {
        return repository.findByEmployeeId(employeeId);
    }
}

