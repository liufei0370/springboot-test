package generator;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 * @author liufei
 * @date 2019/5/30 10:09
 */
public class ServiceRunnerTest {
    private static Logger logger = LoggerFactory.getLogger(ServiceRunnerTest.class);

    private static String projectName = "springboot-iservice-impl";
    private static String packageName = "com.springboot.test.iservice.impl";

    private static List<String> commonMethodNames = new ArrayList<String>(){
        {
            add("wait");
            add("equals");
            add("toString");
            add("hashCode");
            add("getClass");
            add("notify");
            add("notifyAll");
        }
    };

    private static List<String> typeNames = new ArrayList<String>(){
        {
            add("java.lang.String");
            add("java.lang.Integer");
            add("java.lang.Long");
            add("java.lang.Double");
            add("java.lang.Short");
            add("java.lang.Boolean");
            add("java.lang.Float");
            add("int");
            add("long");
            add("double");
            add("short");
            add("boolean");
            add("float");
        }
    };

    public static void main(String[] args) throws Exception {
        List<Class<?>> classList = getClassesFromPackage(packageName);
        for (Class<?> aClass : classList) {
            if(aClass.getName().endsWith("ServiceImpl")){
                TypeSpec.Builder builder = TypeSpec.classBuilder(aClass.getSimpleName()+"Test")//构造一个类,类名
                        //.addSuperinterface(ParameterizedTypeName.get(InterfaceName, superinterface))添加接口，ParameterizedTypeName的参数1是接口，参数2是接口的泛型
                        .superclass(ClassName.bestGuess("generator.config.BaseServiceTest"))//继承父类
                        .addModifiers(Modifier.PUBLIC);//定义类的修饰符;
                Service declaredService = aClass.getDeclaredAnnotation(Service.class);
                if(declaredService!=null){
                    Class<?>[] classes = aClass.getInterfaces();
                    String interfaceName=null;
                    for(Class<?> cls : classes){
                        interfaceName = toLowerCaseFirstOne(cls.getSimpleName());
                        FieldSpec.Builder fieldSpecBuilder = FieldSpec.builder(ClassName.get(cls.getPackage().getName(), cls.getSimpleName()),interfaceName, Modifier.PRIVATE);
                        fieldSpecBuilder.addAnnotation(Autowired.class);
                        FieldSpec fieldSpec = fieldSpecBuilder.build();
                        builder.addField(fieldSpec);
                    }
                    Method[] methods = aClass.getMethods();
                    List<String> methodNames = new ArrayList<>();
                    for(Method method : methods){
                        if(!commonMethodNames.contains(method.getName())){
                            String methodName = getMethodName(methodNames,method.getName(),0);
                            methodNames.add(methodName);
                            MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder(methodName);
                            methodSpecBuilder.addModifiers(Modifier.PUBLIC)
                                    .addException(Exception.class)
                                    .returns(void.class)
                                    .addAnnotation(Test.class);
                            if(method.getName().startsWith("update")||
                                    method.getName().startsWith("save")||
                                    method.getName().startsWith("insert")){
                                methodSpecBuilder.addAnnotation(Transactional.class);
                            }
                            Parameter[] parameters = method.getParameters();
                            StringBuffer statement = new StringBuffer();
                            List<Object> list = new ArrayList<>();
                            StringBuffer parameterNames = new StringBuffer();
                            for(Parameter parameter : parameters){
                                parameterNames.append(parameter.getName()).append(",");
                                statement.append("$T ").append(parameter.getName()).append(" = ");
                                list.add(parameter.getType());
                                if(typeNames.contains(parameter.getType().getName())){
                                    statement.append(getParameter(parameter)).append(";\n");
                                }else {
                                    statement.append(" new $T();\n");
                                    list.add(parameter.getType());
                                }
                            }
                            statement.append(interfaceName).append(".").append(method.getName()).append("(");
                            if(parameterNames.length()>0){
                                statement.append(parameterNames.substring(0,parameterNames.length()-1));
                            }else {
                                statement.append(parameterNames);
                            }
                            statement.append(")");
                            methodSpecBuilder.addStatement(statement.toString(),list.toArray());

                            MethodSpec methodSpec = methodSpecBuilder.build();
                            builder.addMethod(methodSpec);
                        }
                    }
                }
                TypeSpec classTest = builder.build();
                JavaFile javaFile = JavaFile.builder(packageName, classTest)//定义生成的包名,和类
                        .build();
                javaFile.writeTo(new File(projectName+"/src/test/java"));
            }
        }
        System.out.println("OK！");
    }

    public static String getParameter(Parameter parameter) throws Exception {
        switch (parameter.getType().getTypeName()){
            case "java.lang.String":return "java.lang.String";
            case "java.lang.Long":return "0";
            case "long":return "0";
            case "java.lang.Integer":return "0";
            case "int":return "0";
            case "java.lang.Double":return "0";
            case "double":return "0";
            case "java.lang.Short":return "0";
            case "short":return "0";
            case "java.lang.Float":return "0";
            case "float":return "0";
            case "java.lang.Boolean":return "true";
            case "boolean":return "true";
            default: return parameter.getType().getName();
        }
    }

    private static String getMethodName(List<String> methodNames,String methodName,int endWith){
        if(!methodNames.contains(methodName)) {
            return methodName;
        }else {
            if(endWith!=0&&!methodNames.contains(methodName+endWith)){
                return methodName+endWith;
            }else {
                return getMethodName(methodNames,methodName,++endWith);
            }
        }
    }

    //首字母转小写
    public static String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    /**
     * 获得包下面的所有的class
     *
     * @param
     *
     * @return List包含所有class的实例
     */

    public static List<Class<?>> getClassesFromPackage(String packageName) {
        List<Class<?>> clazzs = new ArrayList<>();
        // 是否循环搜索子包
        boolean recursive = true;
        // 包名对应的路径名称
        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> dirs;

        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {

                URL url = dirs.nextElement();
                String protocol = url.getProtocol();

                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findClassInPackageByFile(packageName, filePath, recursive, clazzs);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return clazzs;
    }

    /**
     * 在package对应的路径下找到所有的class
     */
    public static void findClassInPackageByFile(String packageName, String filePath, final boolean recursive,
                                                List<Class<?>> clazzs) {
        File dir = new File(filePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 在给定的目录下找到所有的文件，并且进行条件过滤
        File[] dirFiles = dir.listFiles(file -> {
            boolean acceptDir = recursive && file.isDirectory();// 接受dir目录
            boolean acceptClass = file.getName().endsWith("class");// 接受class文件
            return acceptDir || acceptClass;
        });

        for (File file : dirFiles) {
            if (file.isDirectory()) {
                findClassInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, clazzs);
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    clazzs.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
