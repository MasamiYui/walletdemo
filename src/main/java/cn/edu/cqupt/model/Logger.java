package cn.edu.cqupt.model;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class Logger {

	@Pointcut("execution(* cn.edu.cqupt.model.Camera.snap(..))")
	public void cameraSnap(){
		
	}
	
	@Before("cameraSnap()")
	public void aboutToTakePhoto(){
		System.out.println("About To Take Photo .....");
	}
}
