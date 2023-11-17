package com.training.license.sharing.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.training.license.sharing.entities.enums.RequestStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requests_id_seq")
    @SequenceGenerator(name = "requests_id_seq", sequenceName = "requests_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;

    @NotBlank
    @NotNull
    @Column(name = "app")
    private String app;

    @NotNull
    @DateTimeFormat(pattern = "dd-MMM-yyyy HH:mm")
    @Column(name = "request_date")
    private LocalDateTime requestDate;

    @NotNull
    @DateTimeFormat(pattern = "dd-MMM-yyyy")
    @Column(name = "start_of_use")
    private LocalDate startOfUse;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private User user;

}

