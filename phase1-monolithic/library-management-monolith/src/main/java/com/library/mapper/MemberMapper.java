package com.library.mapper;

import com.library.dto.MemberDTO;
import com.library.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MemberMapper {
    MemberDTO toDTO(Member member);
    Member toEntity(MemberDTO memberDTO);
    void updateEntityFromDTO(MemberDTO memberDTO, @MappingTarget Member member);
}