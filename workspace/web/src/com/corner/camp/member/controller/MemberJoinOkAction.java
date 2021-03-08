package com.corner.camp.member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.corner.action.Action;
import com.corner.action.ActionForward;
import com.corner.camp.member.dao.MemberDAO;
import com.corner.camp.member.vo.MemberVO;
import com.corner.util.SHA256;

public class MemberJoinOkAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        req.setCharacterEncoding("UTF-8");
        ActionForward forward = null;

        MemberVO vo = new MemberVO();
        MemberDAO dao = new MemberDAO();
        
        vo.setMemberId(req.getParameter("memberId"));
        vo.setMemberPw(req.getParameter("memberPw"));
//        vo.setMemberName(req.getParameter("memberName"));
//        vo.setMemberAge(Integer.parseInt(req.getParameter("memberAge")));
//        vo.setMemberGender(req.getParameter("memberGender"));
        vo.setMemberEmail(req.getParameter("memberEmail"));
        vo.setMemberEmailHash(req.getParameter("memberEmail"));
        vo.setMemberEmailChecked(0);
//        vo.setMemberZipcode(req.getParameter("memberZipcode"));
//        vo.setMemberAddress(req.getParameter("memberAddress"));
//        vo.setMemberAddressDetail(req.getParameter("memberAddressDetail"));
//        vo.setMemberAddressEtc(req.getParameter("memberAddressEtc"));
        
        
        System.out.println("회원가입 체크--");

        if (!dao.join(vo)) {
        	System.out.println("회원가입 실패");
//            PrintWriter out = resp.getWriter();
//            resp.setContentType("text/html;charset=utf-8");
//            out.println("<script>alert('서버가 불안정합니다. 잠시 후 다시 시도해주세요.')</script>");
//            out.close();
        	forward = new ActionForward();
        	forward.setRedirect(false);
        	forward.setPath("/user/MemberJoin.me?join=false");
        } else {
            	System.out.println("회원가입 성공");
				forward = new ActionForward();
				forward.setRedirect(false);
				forward.setPath("/user/MemberLogin.me");
        }

        return forward;
    }
}
