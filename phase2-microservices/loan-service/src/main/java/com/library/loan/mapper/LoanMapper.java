package com.library.loan.mapper;

import com.library.loan.dto.LoanDTO;
import com.library.loan.entity.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LoanMapper {

    @Mapping(target = "status", expression = "java(loan.getStatus() != null ? loan.getStatus().name() : null)")
    LoanDTO toDTO(Loan loan);

    @Mapping(target = "status", expression = "java(loanDTO.getStatus() != null ? com.library.loan.entity.Loan.LoanStatus.valueOf(loanDTO.getStatus()) : null)")
    Loan toEntity(LoanDTO loanDTO);

    @Mapping(target = "status", expression = "java(loanDTO.getStatus() != null ? com.library.loan.entity.Loan.LoanStatus.valueOf(loanDTO.getStatus()) : null)")
    void updateEntityFromDTO(LoanDTO loanDTO, @MappingTarget Loan loan);
}