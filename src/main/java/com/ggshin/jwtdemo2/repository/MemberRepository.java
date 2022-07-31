package com.ggshin.jwtdemo2.repository;

import com.ggshin.jwtdemo2.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    public Member findByUsername(String username);
}
