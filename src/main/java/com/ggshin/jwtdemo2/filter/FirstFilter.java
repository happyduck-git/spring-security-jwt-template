package com.ggshin.jwtdemo2.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class FirstFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


        /* Servlet Request를 상속:
            Http protocol의 request 정보를 Servlet에 전달하기 위한 목적으로 사용
            Header 정보, parameter, cookie, uri, url 등의 정보를 읽어들이는 메서드를 가진 클래스
            Body의 Stream을 읽어들이는 method를 가지고 있음
        */
        HttpServletRequest req = (HttpServletRequest) request;

        /* Servlet Response를 상속:
            Servlet이 HttpServletResponse 객체에 Content Type, response code, response message 등을 담아서 전송한다.
         */
        HttpServletResponse res = (HttpServletResponse) response;

        res.setCharacterEncoding("UTF-8");
        if(req.getMethod().equals("POST")) { //요청이 POST일 때
            String headerAuth = req.getHeader("Authorization"); //Authorization 헤더의 value 확인
            if(headerAuth.equals("codestates")) { //value가 codestates인 경우에 chain.doFilter() 수행
                chain.doFilter(req, res);

            } else { //value가 다른 값이면 "인증 실패" 메세지 보내기
                PrintWriter writer = res.getWriter();
                writer.println("인증 실패");
            }
        }





    }
}
