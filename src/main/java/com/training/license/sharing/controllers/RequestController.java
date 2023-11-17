package com.training.license.sharing.controllers;

import com.training.license.sharing.dto.RequestDTO;
import com.training.license.sharing.dto.UserRequestDTO;
import com.training.license.sharing.services.RequestService;
import com.training.license.sharing.validator.RequestValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
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

import static org.springframework.http.HttpStatus.BAD_REQUEST;
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
        return requestService.findAll(asc, field);
    }

    @PostMapping("/request-access")
    public ResponseEntity responseAccess(@RequestBody @Valid RequestDTO dto, BindingResult bindingResult) {
        requestValidator.validateRequestAccess(dto, bindingResult);
        if (!bindingResult.hasErrors()) {
            requestService.requestAccess(dto);
            return ResponseEntity.ok(OK);
        }
        final List<String> errors = getErrorMessageList(bindingResult);
        return new ResponseEntity<>(errors, BAD_REQUEST);
    }

    private static List<String> getErrorMessageList(BindingResult bindingResult) {
        return bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getCode)
                .toList();
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'REVIEWER')")
    @PutMapping("/approve-access")
    public ResponseEntity approveAccess(@RequestBody List<Long> ids, BindingResult bindingResult) {
        requestValidator.validateRequestAccessApproval(ids, bindingResult);
        if (!bindingResult.hasErrors()) {
            requestService.approveRequest(ids);
            return ResponseEntity.ok(OK);
        }
        final List<String> errors = getErrorMessageList(bindingResult);
        return new ResponseEntity<>(errors, BAD_REQUEST);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'REVIEWER')")
    @PutMapping("/reject-access")
    public ResponseEntity<HttpStatus> rejectAccess(@RequestBody List<Long> ids) {
        requestService.rejectRequest(ids);
        return ResponseEntity.ok(OK);
    }
}
