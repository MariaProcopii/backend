package com.training.license.sharing.controllers;

import com.training.license.sharing.dto.ExpiringLicenseDTO;
import com.training.license.sharing.dto.NewLicenseDTO;
import com.training.license.sharing.dto.LicenseSummaryDTO;
import com.training.license.sharing.dto.UnusedLicenseDTO;
import com.training.license.sharing.services.LicenseService;
import com.training.license.sharing.validator.LicenseValidator;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.training.license.sharing.validator.ModelValidator.validateData;

@RestController
@RequestMapping("/license")
@AllArgsConstructor
public class LicenseController {

    private final LicenseService licenseService;

    private final LicenseValidator licenseValidator;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-expiring-licenses")
    public List<ExpiringLicenseDTO> getExpiringLicenses() {
        return licenseService.getActiveLicenses();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-unused-licenses")
    public List<UnusedLicenseDTO> getUnusedLicenses() {
        return licenseService.getExpiredLicenses();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-new-license")
    public ResponseEntity<Long> addNewLicense(@RequestBody @Valid NewLicenseDTO licenseDTO,
                                                    BindingResult bindingResult) {
        validateData(bindingResult);
        licenseValidator.validateNewLicense(licenseDTO);
        Long savedLicenseId = licenseService.saveNewLicense(licenseDTO);
        return new ResponseEntity<>(savedLicenseId, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Admin')")
    @GetMapping("/get-all-licenses")
    public ResponseEntity<List<LicenseSummaryDTO>> getAllLicenses(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "creatingDate") String sortBy
    ) {
        List<LicenseSummaryDTO> licenses = licenseService.getAllLicenses(name, sortBy);
        return ResponseEntity.ok(licenses);
    }

    @PreAuthorize("hasRole('Admin')")
    @GetMapping("/get-license")
    public ResponseEntity<NewLicenseDTO> getLicense(@RequestParam() String name){
        NewLicenseDTO newLicenseDTO = licenseValidator.getValidatedLicenseEditingDTOByName(name);
        return ResponseEntity.ok().body(newLicenseDTO);
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/edit-license")
    public ResponseEntity<HttpStatus> editLicense(@RequestBody @Valid NewLicenseDTO newLicenseDTO){
        licenseValidator.validateEditingLicense(newLicenseDTO);
        licenseService.editLicense(newLicenseDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
