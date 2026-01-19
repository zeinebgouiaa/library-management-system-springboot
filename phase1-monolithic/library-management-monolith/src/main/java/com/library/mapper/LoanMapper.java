package com.library.mapper;

import com.library.dto.LoanDTO;
import com.library.dto.LoanResponseDTO;
import com.library.entity.Loan;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {BookMapper.class, MemberMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LoanMapper {

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "member.id", target = "memberId")
    LoanDTO toDTO(Loan loan);

    @Mapping(source = "bookId", target = "book.id")
    @Mapping(source = "memberId", target = "member.id")
    Loan toEntity(LoanDTO loanDTO);

    LoanResponseDTO toResponseDTO(Loan loan);

    void updateEntityFromDTO(LoanDTO loanDTO, @MappingTarget Loan loan);
}