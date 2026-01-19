package com.library.member.mapper;

import com.library.member.dto.MemberDTO;
import com.library.member.entity.Member;
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