package com.springboot.test.web.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * jvm虚拟缓存工具类
 * @author liufei
 * @date 2019/1/2 10:55
 */
public class JvmCacheDataUtil {
    private static Logger logger = LoggerFactory.getLogger(JvmCacheDataUtil.class);

    public static final Map map = new ConcurrentHashMap();

    public static String getStringCache(String key){
        return (String)map.get(key);
    }

    public static Map getMapCache(String key){
        return (Map)map.get(key);
    }

    public static List getListCache(String key){
        return (List)map.get(key);
    }

    public static Object getObjectCache(String key){
        return map.get(key);
    }

    public static boolean delCache(String key){
        try{
            map.remove(key);
            return true;
        }catch (Exception e){
            logger.error("删除缓存异常",e);
        }
        return false;
    }

    public static boolean putObjectCache(Map<String,Object> dataMap){
        for(String key : dataMap.keySet()){
            try{
                map.put(key,dataMap.get(key));
            }catch (Exception e){
                logger.error("加载缓存异常",e);
                return false;
            }
        }
        return true;
    }
}
