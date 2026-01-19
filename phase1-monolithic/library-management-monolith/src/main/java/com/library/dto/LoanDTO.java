package com.library.dto;

import com.library.entity.Loan;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO {
    private Long id;

    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "Member ID is required")
    private Long memberId;

    @NotNull(message = "Loan date is required")
    @PastOrPresent(message = "Loan date cannot be in the future")
    private LocalDate loanDate;

    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    private LocalDate dueDate;

    private LocalDate returnDate;

    @NotNull(message = "Status is required")
    private Loan.LoanStatus status;
}