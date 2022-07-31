package com.ggshin.jwtdemo2.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggshin.jwtdemo2.model.Member;
import com.ggshin.jwtdemo2.oauth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        System.out.println("로그인 시도");

        try {
            ObjectMapper om = new ObjectMapper();
            Member member = om.readValue(request.getInputStream(), Member.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member.getUsername(), member.getPassword()); //Token 생성
            Authentication authentication = authenticationManager.authenticate(authenticationToken); //Token 부여

            return authentication; //요청한 유저에게 token을 응답으로 돌려줌

        }catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //생성한 Token 전달(Response header에서 확인 가능)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("Succesful Authentication");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject("cos jwt token")
                .withExpiresAt(new Date(System.currentTimeMillis() + (60 * 1000 * 10)))
                .withClaim("id", principalDetails.getMember().getId())
                .withClaim("username", principalDetails.getMember().getUsername())
                .sign(Algorithm.HMAC512("cos_jwt_token"));
        response.addHeader("Authorization", "Bearer " + jwtToken);
    }
}
