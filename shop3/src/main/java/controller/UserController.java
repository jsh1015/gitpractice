package controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;
import logic.User;

@Controller //@Component + Controller : 객체를 만들고 컨트롤러의 기능까지 같이 수행
@RequestMapping("user") //user/xxx.shop
public class UserController {
	@Autowired
	private ShopService service;
	
	@GetMapping("*")
	public String form(Model model) {
		model.addAttribute(new User());
		return null;
	}
	@PostMapping("userEntry")
	public ModelAndView userEntry(@Valid User user,BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			bresult.reject("error.input.user");
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		//useraccount 테이블에 내용 등록. 뷰단은 login.jsp로 이동
		try {
			service.userInsert(user);
			mav.setViewName("user/login"); //redirect 를 사용하면 아이디값이 들어가지 않음
			mav.addObject("user",user);//값도 넘어감
		}catch(DataIntegrityViolationException e) {
			//e.printStackTrace();
			bresult.reject("error.duplicate.user");
		}
		return mav;
	}
	@PostMapping("login")
	public ModelAndView login(@Valid User user,BindingResult bresult,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			bresult.reject("error.login.user");
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		try {
			User dbUser = service.getUser(user.getUserid());
			if(!dbUser.getPassword().equals(user.getPassword())) {
				bresult.reject("error.login.password");
				return mav;
			}else {
				session.setAttribute("loginUser",dbUser);
				mav.setViewName("redirect:main.shop");
			}
		}catch(EmptyResultDataAccessException e) {
			e.printStackTrace();
			bresult.reject("error.login.id");
		}
		return mav;
	}
	
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:login.shop";
	}
	
	@RequestMapping("main")
	public String checkmain(HttpSession session) {
		return "user/main";
	}
}
