package cn.edu.cqupt.controller;


import cn.edu.cqupt.model.Camera;
import cn.edu.cqupt.service.ERC20Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
public class HelloController {
	
	@Autowired
	private Camera camera;

	@Autowired
	private ERC20Service erc20Service;
	
	@RequestMapping(value = "/hello")
	public String hello(){
		return "Hello";
	}
	
	@RequestMapping(value = "/camera")
	public String cam(){
		return camera.snap();
	}
	
	@RequestMapping(value = "/camera/{value}")
	public String exposure(@PathVariable("value") int expo){
		return camera.snap(expo);
	}
	
	@RequestMapping(value = "/camera/name/{value}")
	public String name(@PathVariable("value") String name){
		return camera.snap(name);
	}

	@RequestMapping(value = "/test")
	public String Test() throws IOException, ExecutionException, InterruptedException {
		return erc20Service.sendRawTransaction("0x095ea7b300000000000000000000000002e8f1876713f78edda8704ffcf288c7334c04e0000000000000000000000000000000000000000000000000000000000000007b");
	}

}
