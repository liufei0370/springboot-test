package generator;

import com.alibaba.fastjson.JSONObject;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.element.Modifier;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

/**
 * @author liufei
 * @date 2019/5/17 10:09
 */
public class ControllerRunnerTest {
    private static Logger logger = LoggerFactory.getLogger(ControllerRunnerTest.class);

    private static String projectName = "springboot-web";
    private static String packageName = "com.springboot.test.web.controller.generator";
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
            if(aClass.getName().endsWith("Controller")){
                TypeSpec.Builder builder = TypeSpec.classBuilder(aClass.getSimpleName()+"Test1")//构造一个类,类名
                        //.addSuperinterface(ParameterizedTypeName.get(InterfaceName, superinterface))添加接口，ParameterizedTypeName的参数1是接口，参数2是接口的泛型
                        .superclass(ClassName.bestGuess("generator.config.BaseControllerTest"))//继承父类
                        .addModifiers(Modifier.PUBLIC);//定义类的修饰符;
                Method[] methods = aClass.getMethods();
                RequestMapping declaredRequestMapping = aClass.getDeclaredAnnotation(RequestMapping.class);
                String declaredUrl = getRequestMappingUrl(declaredRequestMapping.value());
                for(Method method : methods){
                    if(method.getDeclaringClass().getName().equals(aClass.getName())){
                        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder(method.getName())//定义方面名
                                .addModifiers(Modifier.PUBLIC)//定义修饰符
                                .addException(Exception.class)
                                .returns(void.class)//定义返回结果
//                                .addParameter(String[].class, "args")//添加方法参数
                                .addAnnotation(ClassName.bestGuess("org.junit.Test1"))
//                                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")//添加方法内容
                                ;
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        String requestMappingMethod = null;
                        String requestMappingUrl = null;
                        if(requestMapping!=null){
                            requestMappingMethod = getRequestMappingMethod(requestMapping.method());
                            requestMappingUrl = getRequestMappingUrl(requestMapping.value());
                        }else {
                            GetMapping getMapping =  method.getAnnotation(GetMapping.class);
                            if(getMapping!=null){
                                requestMappingMethod = "get";
                                requestMappingUrl = getRequestMappingUrl(getMapping.value());
                            }else {
                                PostMapping postMapping = method.getAnnotation(PostMapping.class);
                                if(postMapping!=null){
                                    requestMappingMethod = "post";
                                    requestMappingUrl = getRequestMappingUrl(postMapping.value());
                                }
                            }
                        }
                        Parameter[] parameters = method.getParameters();
                        StringBuffer statement = new StringBuffer();
                        List<Object> list = new ArrayList<>();
                        List<Object> parameterTypeList = new ArrayList<>();
                        StringBuffer content = new StringBuffer();
                        StringBuffer params = new StringBuffer();
                        String requestBodyName = null;
                        for(Parameter parameter : parameters){
                            if(typeNames.contains(parameter.getType().getName())){
                                if(parameter.getAnnotations().length==0||parameter.getAnnotation(RequestParam.class)!=null){
                                    params.append(".param(\"").append(parameter.getName()).append("\",\"").append(getParameter(parameter)).append("\")\n");
                                }else if(parameter.getAnnotation(PathVariable.class)!=null){
                                    requestMappingUrl = requestMappingUrl.replace("{"+parameter.getName()+"}",getParameter(parameter));
                                }else{
                                    statement.append("map.put(\"").append(parameter.getName()).append("\",").append("\"")
                                            .append(JSONObject.toJSON(parameter.getType())).append("\");\n");
                                }
                            }else {
                                if(parameter.getAnnotations().length==0||parameter.getAnnotation(RequestParam.class)!=null){
                                    for(Field field : parameter.getType().getDeclaredFields()){
                                        if(!field.getName().equals("serialVersionUID")){
                                            params.append(".param(\"").append(field.getName()).append("\",");
                                            if(field.getType().getName().equals("java.util.Date")){
                                                params.append("new $T().getTime()");
                                                parameterTypeList.add(Date.class);
                                            }else {
                                                params.append(getFieldValue(field));
                                            }
                                            if(field.getType().getName().equals("java.lang.String")){
                                                params.append(")\n");
                                            }else {
                                                params.append("+\"\")\n");
                                            }
                                        }
                                    }
                                }else if(parameter.getAnnotation(RequestBody.class)!=null){
                                    if(StringUtils.isNotBlank(requestBodyName)){
                                        logger.error("{}类的{}方法存在多个RequestBody，默认使用第一个RequestBody作为参数",aClass.getName(),method.getName());
                                        continue;
                                    }
                                    requestBodyName = parameter.getName();
                                    statement.append("$T ").append(parameter.getName()).append(" =").append(" new $T();\n");
                                    list.add(parameter.getType());
                                    list.add(parameter.getType());
                                    for(Field field : parameter.getType().getDeclaredFields()){
                                        if(!field.getName().equals("serialVersionUID")){
                                            statement.append(parameter.getName()).append(".").append("set").append(toUpperCaseFirstOne(field.getName())).append("(");
                                            if(field.getType().getName().equals("java.util.Date")){
                                                statement.append("new $T()").append(");\n");
                                                list.add(Date.class);
                                            }else {
                                                statement.append(getFieldValue(field)).append(");\n");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if(requestMappingMethod!=null){
                            statement.append("mvc.perform($T.")
                                    .append(requestMappingMethod)//调用方法类型
                                    .append("(\"")
                                    .append(declaredUrl).append(requestMappingUrl)//调用地址
                                    .append("\")\n")
                                    .append(".accept($T.APPLICATION_JSON_UTF8)\n")//参数传递方式
                                    .append(".contentType(MediaType.APPLICATION_JSON_UTF8)\n")//参数传递方式
                                    .append(params);
                            list.add(MockMvcRequestBuilders.class);
                            list.add(MediaType.class);
                            for(Object o : parameterTypeList){
                                list.add(o);
                            }
                            if(StringUtils.isNotBlank(requestBodyName)){
                                statement.append(".content($T.toJSONString(").append(requestBodyName).append("))\n"); //传json参数内容
                                list.add(JSONObject.class);
                            }else {
                                statement.append(".content(\"\")");
                            }
                            statement.append(".session(session))\n")//session消息
                                    .append(".andExpect($T.status().isOk())\n")
                                    .append(".andDo($T.print())");//将运行结果打印出来
                            list.add(MockMvcResultMatchers.class);
                            list.add(MockMvcResultHandlers.class);
                            methodSpecBuilder.addStatement(statement.toString(),list.toArray());
                            MethodSpec methodSpec = methodSpecBuilder.build();
                            builder.addMethod(methodSpec);
                        }else {
                            continue;
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

    //首字母转大写
    public static String toUpperCaseFirstOne(String s){
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    public static Object getFieldValue(Field field) throws IllegalAccessException, InstantiationException {
        switch (field.getType().getTypeName()){
            case "java.lang.String":return "\"java.lang.String\"";
            case "java.lang.Long":return 0l;
            case "long":return 0l;
            case "java.lang.Integer":return 0;
            case "int":return 0;
            case "java.lang.Double":return 0;
            case "double":return 0;
            case "java.lang.Short":return 0;
            case "short":return 0;
            case "java.lang.Float":return 0;
            case "float":return 0;
            case "java.lang.Boolean":return true;
            case "boolean":return true;
            default: return field.getType().getTypeName();
        }
    }


    public static String getRequestMappingMethod(RequestMethod[] methods){
        if(methods.length>0){
            return methods[0].name().toLowerCase();
        }
        return "post";
    }

    public static String getRequestMappingUrl(String[] paths){
        String url = paths[0];
        if(StringUtils.isNotBlank(url)){
            if(!url.startsWith("/")){
                url= "/"+url;
            }
            if(url.endsWith("/")){
                url=url.substring(0,url.length()-1);
            }
            return url;
        }
        return "/";
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
