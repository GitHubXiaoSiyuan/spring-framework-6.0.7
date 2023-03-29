package cn.xiaosy.xspring.spring;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class XiaosySpringContext {

    private Class<?> configClass;



    // 存储 bean 的定义
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    // 单例池
    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();


    private ArrayList<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public XiaosySpringContext(Class<?> configClass) {
        this.configClass = configClass;

        // 扫描 -》 生成 BeanDefinition -》 BeanDefinition 存入 beanDefinitionMap
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScanAnnotation = configClass.getAnnotation(ComponentScan.class);
            // 类的全限定名 -》 扫描路径
            String path = componentScanAnnotation.value();  // cn.xiaosy.service
            path = path.replace(".", "/");  // cn/xiaosy/service

            // 用相对路径找类加载器生成绝对路径
            ClassLoader classLoader = XiaosySpringContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);
            String absolutePath = resource.getFile();   // 文件夹的绝对路径

            File dir = new File(absolutePath);
            if (dir.isDirectory()) {

                File[] files = dir.listFiles();
                for (File f : files) {
                    String name = f.getName(); // 类名 Userservice.class

                    if (name.endsWith(".class")) {

                        // todo 待确认
                        // 优化 斜线和反斜线
                        // 这里是根据当前的运行环境截取的全限定类名，其他环境可能不一样
                        // 9 是 /classes/ 的长度加1
                        String className = f.getAbsolutePath().substring(f.getAbsolutePath().indexOf("/classes/") + 9, f.getAbsolutePath().indexOf(".class")).replaceAll("/", ".");


						// 放到了新的运行环境下了
						if (className.contains("java.main.")) {
							className = className.replaceAll("java.main.","");
						}

                        // 传入全限定类名 cn.xiaosy.service.UserService
                        Class<?> clazz = null;
                        try {
                            clazz = classLoader.loadClass(className);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }


                        // 是否是bean
                        if (clazz.isAnnotationPresent(Component.class)) {

                            // 判断当前类是否由 BeanPostProcessor 派生，或者说，是否实现了 BeanPostProcessor 接口
                            if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                                BeanPostProcessor instance = null;
                                try {
                                    instance = (BeanPostProcessor) clazz.newInstance();
                                    beanPostProcessorList.add(instance);
                                } catch (InstantiationException e) {
                                    throw new RuntimeException(e);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            // 这里有可能是多例 bean 也有可能是单例 bean 所以生成 BeanDefinition
                            // 或者说，这里只知道定义了一个 bean，所以就生成一个 BeanDefinition 类





                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setType(clazz);

                            // 这里简化处理了
                            if (clazz.isAnnotationPresent(Scope.class)) {
                                Scope scopeAnnotation = clazz.getAnnotation(Scope.class);
                                String prototype = scopeAnnotation.value();
                                beanDefinition.setScope(prototype);
                            } else {
                                // 默认单例
                                beanDefinition.setScope("singleton");
                            }


                            // 获取 beanName
                            Component componentAnnotation = clazz.getAnnotation(Component.class);
                            String beanName = componentAnnotation.value();

                            // 默认为类名首字母小写
                            if ("".equals(beanName)) {
                                // 首字母小写
                                beanName = Introspector.decapitalize(clazz.getSimpleName());
                            }

                            // 存储 beanDefinition
                            beanDefinitionMap.put(beanName,beanDefinition);

                        }
                    }

                }

            }
        }


        // 实例化单例 bean ->存入单例池
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            String scope = beanDefinition.getScope();
            if (scope != null) {
                if ("singleton".equals(scope)) {
                    Object bean = createBean(beanName, beanDefinition);
                    singletonObjects.put(beanName,bean);
                }
            }
        }
    }


    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class<?> clazz = beanDefinition.getType();
        try {
            // 获取无参构造函数 并 实例化
            Object instance = clazz.getConstructor().newInstance();


            // 依赖注入
            // Autowired 注解
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    field.set(instance, getBean(field.getName()));
                }
            }

            // aware回调 BeanNameAware
            if (instance instanceof BeanNameAware) {
                BeanNameAware beanNameAware = (BeanNameAware) instance;
                beanNameAware.setBeanName(beanName);
            }

            // 初始化前
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessorBeforeInitialization(beanName,instance);
            }

            // 初始化 InitializingBean
            if (instance instanceof InitializingBean) {
                InitializingBean initializingBean = (InitializingBean) instance;
                initializingBean.afterPropertiesSet();
            }

            // 初始化后
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessorAfterInitialization(beanName,instance);
            }




            return instance;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    public Object getBean(String beanName) {

        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

        if (beanDefinition == null) {
            throw new NullPointerException();
        } else {
            String scope = beanDefinition.getScope();
            if ("singleton".equals(scope)) {
                // 单例 bean
                Object bean = singletonObjects.get(beanName);
                if (bean == null) {
                    Object newBean = createBean(beanName, beanDefinition);
                    singletonObjects.put(beanName,newBean);
                }
                return bean;
            } else if ("prototype".equals(scope)) {
                // 原型多例 bean
                return createBean(beanName, beanDefinition);
            }

        }

        return null;
    }


}
