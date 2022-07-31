package com.ggshin.jwtdemo2.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ggshin.jwtdemo2.model.Member;
import com.ggshin.jwtdemo2.oauth.PrincipalDetails;
import com.ggshin.jwtdemo2.repository.MemberRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/* 권한 및 인증이 필요한 주소 요청 시 BasicAuthenticationFilter를 반드시 진행하게 됨 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private MemberRepository memberRepository;


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
    }

    /* 인증이나 권한이 필요한 주소 요청이 있을 때마다 해당 필터를 통하게 됨 */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("인증이나 권한이 필요한 주소가 요청됨");

        //Authorization Header의 value가 Bearer로 시작하는지 확인
        String jwtHeader = request.getHeader("Authorization");

        if(jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
            chain.doFilter(request, response);
            return;
        }

        //JwtToken값 확인
        String jwtToken = jwtHeader.replace("Bearer ", ""); //Token값 확인 전, 토큰의 앞부분인 "Bearer " 부분 없애기

        //username을 가져와서 등록된 user인지 확인
        String username = JWT.require(Algorithm.HMAC512("cos_jwt_token")).build().verify(jwtToken).getClaim("username").asString();

        if(username != null) {
            Member memberEntity = memberRepository.findByUsername(username);

            //username이 정상적으로 확인된다면 authentication 설정하기
            PrincipalDetails principalDetails = new PrincipalDetails(memberEntity);
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        }

        super.doFilterInternal(request, response, chain);
    }
}
