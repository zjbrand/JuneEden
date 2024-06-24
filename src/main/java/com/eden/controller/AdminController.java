package com.eden.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eden.entity.Admin;
import com.eden.service.AdminService;
import com.eden.utils.VerifyCodeUtils;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("admin")
public class AdminController {
	
	private static final Logger log= LoggerFactory.getLogger(AdminController.class);
	
	@Autowired
	private AdminService adminService;
	
	@RequestMapping("register")
	public String register(Admin admin, String code,HttpSession session) throws UnsupportedEncodingException {
		/*System.out.println(admin.getAdminname());
		System.out.println(admin.getRealname());
		System.out.println(admin.getPassword());
		System.out.println(admin.getGender());*/
		log.info("受け取った管理者名：{},リアル名前：{},パスワード：{},性別：{}",
				admin.getAdminname(),admin.getRealname(),admin.getPassword(),admin.getGender());
		log.info("管理者入力したコード：{}",code);
		
		try {
			String sessionCode= session.getAttribute("code").toString();
			
			if(!sessionCode.equalsIgnoreCase(code)) {
				throw new RuntimeException("入力した検証コードが間違いです、も一度入力してください。");
			} 
			
			adminService.register(admin);
		} catch (Exception e) {
			
			e.printStackTrace();
			return "redirect:/regist?msg="+URLEncoder.encode(e.getMessage(),"UTF-8");
		}
		
		return "redirect:/login";
	}

	@GetMapping("generateImageCode")
	public void generateImageCode(HttpSession session,HttpServletResponse response) throws IOException {
		String code= VerifyCodeUtils.generateVerifyCode(4);
		//System.out.println(code);
		
		session.setAttribute("code", code);
		
		response.setContentType("image/png");
		
		ServletOutputStream os = response.getOutputStream();
		
		VerifyCodeUtils.outputImage(220, 60, os, code);
	}
}
