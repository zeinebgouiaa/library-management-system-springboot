package com.library.loan.client;

import com.library.loan.dto.MemberDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "member-service")
public interface MemberClient {

    @GetMapping("/api/members/{id}")
    MemberDTO getMemberById(@PathVariable("id") Long id);
}