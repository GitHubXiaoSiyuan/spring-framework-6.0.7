package cn.xiaosy.springdemo.scanner.service;

import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

public class CustomerExcludeMather implements TypeFilter {
	@Override
	public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
		// 有 @CustomerExclude 注解的排除
		// 返回 true 表示排除，默认为 false （即：不排除）
		if (metadataReader.getAnnotationMetadata().hasAnnotation(CustomerExclude.class.getName())) {
			return true;
		}
		// 全限定类名
		String className = metadataReader.getClassMetadata().getClassName();
		// 匹配逻辑...

		return false;
	}
}
