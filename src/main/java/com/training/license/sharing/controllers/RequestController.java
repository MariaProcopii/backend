package com.training.license.sharing.controllers;

import com.training.license.sharing.dto.RequestDTO;
import com.training.license.sharing.dto.UserRequestDTO;
import com.training.license.sharing.services.RequestService;
import com.training.license.sharing.validator.RequestValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Optional;

import static com.training.license.sharing.validator.ModelValidator.validateData;
import static com.training.license.sharing.validator.ParameterValidator.isObjectFieldValid;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/requests")
public class RequestController {

    private final RequestService requestService;
    private final RequestValidator requestValidator;

    @Autowired
    public RequestController(RequestService requestService, RequestValidator requestValidator) {
        this.requestService = requestService;
        this.requestValidator = requestValidator;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'REVIEWER')")
    @GetMapping("/get-requests")
    public List<UserRequestDTO> findAll(@RequestParam(name = "asc", required = false) Boolean asc,
                                        @RequestParam(name = "field", required = false) String field) {

        Optional.ofNullable(field).ifPresent(f -> isObjectFieldValid(f, UserRequestDTO.class));
        return requestService.findAll(asc, field);
    }

    @PostMapping("/request-access")
    public ResponseEntity responseAccess(@RequestBody @Valid RequestDTO dto, BindingResult bindingResult) {
        requestValidator.validateRequestAccess(dto, bindingResult);
        validateData(bindingResult);
        requestService.requestAccess(dto);
        return ResponseEntity.ok(OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'REVIEWER')")
    @PutMapping("/approve-access")
    public ResponseEntity approveAccess(@RequestBody List<Long> ids, BindingResult bindingResult) {
        requestValidator.validateRequestAccessApproval(ids, bindingResult);
        validateData(bindingResult);
        requestService.approveRequest(ids);
        return ResponseEntity.ok(OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'REVIEWER')")
    @PutMapping("/reject-access")
    public ResponseEntity rejectAccess(@RequestBody List<Long> ids, BindingResult bindingResult) {
        requestValidator.validateRequestAccessRejection(ids, bindingResult);
        validateData(bindingResult);
        requestService.rejectRequest(ids);
        return ResponseEntity.ok(OK);
    }
}