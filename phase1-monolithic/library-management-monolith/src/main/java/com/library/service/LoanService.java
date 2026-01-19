package com.library.service;

import com.library.dto.LoanDTO;
import com.library.dto.LoanResponseDTO;
import com.library.entity.Book;
import com.library.entity.Loan;
import com.library.entity.Member;
import com.library.exception.BusinessException;
import com.library.exception.ResourceNotFoundException;
import com.library.mapper.LoanMapper;
import com.library.repository.BookRepository;
import com.library.repository.LoanRepository;
import com.library.repository.MemberRepository;
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
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final LoanMapper loanMapper;

    public List<LoanResponseDTO> getAllLoans() {
        return loanRepository.findAll().stream()
                .map(loanMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public LoanResponseDTO getLoanById(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + id));
        return loanMapper.toResponseDTO(loan);
    }

    public List<LoanResponseDTO> getLoansByMemberId(Long memberId) {
        return loanRepository.findByMemberId(memberId).stream()
                .map(loanMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<LoanResponseDTO> getLoansByBookId(Long bookId) {
        return loanRepository.findByBookId(bookId).stream()
                .map(loanMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public LoanResponseDTO createLoan(LoanDTO loanDTO) {
        Book book = bookRepository.findById(loanDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + loanDTO.getBookId()));

        Member member = memberRepository.findById(loanDTO.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + loanDTO.getMemberId()));

        if (book.getAvailableCopies() <= 0) {
            throw new BusinessException("No copies available for book: " + book.getTitle());
        }

        if (member.getStatus() != Member.MemberStatus.ACTIVE) {
            throw new BusinessException("Member is not active");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        Loan loan = loanMapper.toEntity(loanDTO);
        loan.setBook(book);
        loan.setMember(member);
        loan.setStatus(Loan.LoanStatus.ACTIVE);

        Loan savedLoan = loanRepository.save(loan);
        return loanMapper.toResponseDTO(savedLoan);
    }

    public LoanResponseDTO returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + loanId));

        if (loan.getStatus() == Loan.LoanStatus.RETURNED) {
            throw new BusinessException("Book has already been returned");
        }

        loan.setReturnDate(LocalDate.now());
        loan.setStatus(Loan.LoanStatus.RETURNED);

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        Loan updatedLoan = loanRepository.save(loan);
        return loanMapper.toResponseDTO(updatedLoan);
    }

    public void deleteLoan(Long id) {
        if (!loanRepository.existsById(id)) {
            throw new ResourceNotFoundException("Loan not found with id: " + id);
        }
        loanRepository.deleteById(id);
    }
}