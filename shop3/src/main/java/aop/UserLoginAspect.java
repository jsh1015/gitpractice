package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import exception.LoginExceptioncheck;
import logic.User;

@Component
@Aspect
public class UserLoginAspect {
	@Around //advice : 핵심로직 전,후
		("execution(* controller.User*.check*(..)) && args(.., session)")
		//pointcut : 핵심로직 설정(접근제어자 상관X controller패키지.User로 시작하는 모든클래스.check로시작하는 모든메서드
		//&& 매개변수가 앞에는 상관없고 뒤에있는 매개변수는 session으로 끝나야함)
	public Object userLoginCheck(ProceedingJoinPoint joinPoint, HttpSession session)throws Throwable{
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			throw new LoginExceptioncheck("로그인 후 거래하세요","login.shop");
		}
		Object ret = joinPoint.proceed();
		return ret;
	}
}
