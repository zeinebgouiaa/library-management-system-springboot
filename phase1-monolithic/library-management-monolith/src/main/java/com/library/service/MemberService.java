package com.library.service;

import com.library.dto.MemberDTO;
import com.library.entity.Member;
import com.library.exception.DuplicateResourceException;
import com.library.exception.ResourceNotFoundException;
import com.library.mapper.MemberMapper;
import com.library.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public List<MemberDTO> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(memberMapper::toDTO)
                .collect(Collectors.toList());
    }

    public MemberDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
        return memberMapper.toDTO(member);
    }

    public MemberDTO createMember(MemberDTO memberDTO) {
        if (memberRepository.existsByEmail(memberDTO.getEmail())) {
            throw new DuplicateResourceException("Member with email " + memberDTO.getEmail() + " already exists");
        }
        Member member = memberMapper.toEntity(memberDTO);
        Member savedMember = memberRepository.save(member);
        return memberMapper.toDTO(savedMember);
    }

    public MemberDTO updateMember(Long id, MemberDTO memberDTO) {
        Member existingMember = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));

        if (!existingMember.getEmail().equals(memberDTO.getEmail()) &&
                memberRepository.existsByEmail(memberDTO.getEmail())) {
            throw new DuplicateResourceException("Member with email " + memberDTO.getEmail() + " already exists");
        }

        memberMapper.updateEntityFromDTO(memberDTO, existingMember);
        Member updatedMember = memberRepository.save(existingMember);
        return memberMapper.toDTO(updatedMember);
    }

    public void deleteMember(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new ResourceNotFoundException("Member not found with id: " + id);
        }
        memberRepository.deleteById(id);
    }
}