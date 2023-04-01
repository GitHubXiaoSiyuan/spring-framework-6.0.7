package cn.xiaosy.springdemo.scanner.asm;

import org.springframework.asm.ClassReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ASMTest {

	public static void main(String[] args) throws IOException {

		String absoluteUserServiceClassPath = "/home/xiaosy/files/projects/kernel_projects/framework/spring-framework-6.0.7/spring-demo/build/classes/java/main/cn/xiaosy/springdemo/scanner/service/UserService.class";
		FileInputStream inputStream = new FileInputStream(absoluteUserServiceClassPath);

		ClassReader classReader = new ClassReader(inputStream);

		System.out.println(classReader.getClassName());
		System.out.println(classReader.getSuperName());

		// cn/xiaosy/springdemo/scanner/service/UserService
		// java/lang/Object
	}

}
