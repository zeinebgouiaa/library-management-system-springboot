package com.library.loan.service;

import com.library.loan.client.BookClient;
import com.library.loan.client.MemberClient;
import com.library.loan.dto.BookDTO;
import com.library.loan.dto.LoanDTO;
import com.library.loan.dto.LoanResponseDTO;
import com.library.loan.dto.MemberDTO;
import com.library.loan.entity.Loan;
import com.library.loan.exception.BusinessException;
import com.library.loan.exception.ResourceNotFoundException;
import com.library.loan.mapper.LoanMapper;
import com.library.loan.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookClient bookClient;
    private final MemberClient memberClient;
    private final LoanMapper loanMapper;  // ← Added mapper

    public List<LoanResponseDTO> getAllLoans() {
        return loanRepository.findAll().stream()
                .map(this::toLoanResponseDTO)
                .collect(Collectors.toList());
    }

    public LoanResponseDTO getLoanById(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + id));
        return toLoanResponseDTO(loan);
    }

    public List<LoanResponseDTO> getLoansByMemberId(Long memberId) {
        return loanRepository.findByMemberId(memberId).stream()
                .map(this::toLoanResponseDTO)
                .collect(Collectors.toList());
    }

    public List<LoanResponseDTO> getLoansByBookId(Long bookId) {
        return loanRepository.findByBookId(bookId).stream()
                .map(this::toLoanResponseDTO)
                .collect(Collectors.toList());
    }

    public LoanResponseDTO createLoan(LoanDTO loanDTO) {
        // Call Book Service via Feign Client
        BookDTO book = bookClient.getBookById(loanDTO.getBookId());
        if (book == null) {
            throw new ResourceNotFoundException("Book not found with id: " + loanDTO.getBookId());
        }

        // Call Member Service via Feign Client
        MemberDTO member = memberClient.getMemberById(loanDTO.getMemberId());
        if (member == null) {
            throw new ResourceNotFoundException("Member not found with id: " + loanDTO.getMemberId());
        }

        // Business validations
        if (book.getAvailableCopies() <= 0) {
            throw new BusinessException("No copies available for book: " + book.getTitle());
        }

        if (!"ACTIVE".equals(member.getStatus())) {
            throw new BusinessException("Member is not active");
        }

        // Update book available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookClient.updateBook(book.getId(), book);

        // Create loan using mapper
        Loan loan = loanMapper.toEntity(loanDTO);
        loan.setStatus(Loan.LoanStatus.ACTIVE);

        Loan savedLoan = loanRepository.save(loan);
        return toLoanResponseDTO(savedLoan);
    }

    public LoanResponseDTO returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + loanId));

        if (loan.getStatus() == Loan.LoanStatus.RETURNED) {
            throw new BusinessException("Book has already been returned");
        }

        loan.setReturnDate(LocalDate.now());
        loan.setStatus(Loan.LoanStatus.RETURNED);

        // Update book available copies
        BookDTO book = bookClient.getBookById(loan.getBookId());
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookClient.updateBook(book.getId(), book);

        Loan updatedLoan = loanRepository.save(loan);
        return toLoanResponseDTO(updatedLoan);
    }

    public LoanResponseDTO updateLoan(Long id, LoanDTO loanDTO) {
        Loan existingLoan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + id));

        loanMapper.updateEntityFromDTO(loanDTO, existingLoan);
        Loan updatedLoan = loanRepository.save(existingLoan);
        return toLoanResponseDTO(updatedLoan);
    }

    public void deleteLoan(Long id) {
        if (!loanRepository.existsById(id)) {
            throw new ResourceNotFoundException("Loan not found with id: " + id);
        }
        loanRepository.deleteById(id);
    }

    // Helper method to convert Loan to LoanResponseDTO
    private LoanResponseDTO toLoanResponseDTO(Loan loan) {
        BookDTO book = bookClient.getBookById(loan.getBookId());
        MemberDTO member = memberClient.getMemberById(loan.getMemberId());

        LoanResponseDTO response = new LoanResponseDTO();
        response.setId(loan.getId());
        response.setBook(book);
        response.setMember(member);
        response.setLoanDate(loan.getLoanDate());
        response.setDueDate(loan.getDueDate());
        response.setReturnDate(loan.getReturnDate());
        response.setStatus(loan.getStatus().name());

        return response;
    }
}
