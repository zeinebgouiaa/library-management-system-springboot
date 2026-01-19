package com.library.dto;

import com.library.entity.Loan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanResponseDTO {
    private Long id;
    private BookDTO book;
    private MemberDTO member;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Loan.LoanStatus status;
}