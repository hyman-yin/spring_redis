package hyman.sr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegisterController {
	@RequestMapping("/enterRegister.do")
	public String enterRegister(){
		System.out.println(1/0);
		return "register";
	}
}
