package com.midas.uitls.tools;

import java.util.List;

import com.midas.enums.DataType;

/**
 * 枚举工具类
 * 
 * @author arron
 *
 */
public class EnumUtils {

    /**
     * 字符转换为枚举对象 注：使用该方法， 枚举对象需要有重写toString
     * 
     * @param obj
     *            Object
     * @return 
     * @return {@link Enum<?> }
     */
    @SuppressWarnings("all")
    public static Enum<?> convertEnum(Class<? extends Enum> clazz, Object obj) {
        Enum<?> em = null;
        List<Enum> list = org.apache.commons.lang3.EnumUtils.getEnumList(clazz);
        for (Enum ds : list) {
            if (ds.toString().equals(obj)) {
                em = ds;
            }
        }
        return em;
    }
    
    public static void main(String[] args) {
        DataType obj = (DataType) convertEnum(DataType.class, "R");
        System.out.println(obj.getType());
    }
    

}
