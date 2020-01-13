package com.icss.mvp.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author chengchenhui 133918
 */
public class CollectionUtilsLocal {

    private static Logger logger = Logger.getLogger(CollectionUtilsLocal.class);

    /**
     * 分割符，默认为英文逗号
     */
    public static final String SPLIT_REGEX = ",";

    /**
     * List<Map<String, Object>>排序
     *
     * @param resultList 需要排序的List<Map<String, Object>>
     * @param sort 排序字段，即按照Map里的哪个字段排序 仅限字段值为数字的字段
     * @param sortOrder 排序方式 升序asc 降序desc
     */
    public static void listSort(List<Map<String, Object>> resultList, final String sort, final String sortOrder) {
        // resultList是需要排序的list，其内放的是Map
        // 返回的结果集
        Collections.sort(resultList, (o1, o2) -> {
            // o1，o2是list中的Map，可以在其内取得值，按其排序，s1和s2是排序字段值
            Double s1 = Double.valueOf(String.valueOf(o1.get(sort)));
                         Double s2 = Double.valueOf(String.valueOf(o2.get(sort)));
                         if ("asc".equalsIgnoreCase(sortOrder)) {
                             return s1 > s2 ? 1 : -1;
                         } else if ("desc".equalsIgnoreCase(sortOrder)) {
                             return s1 > s2 ? -1 : 1;
                         } else {
                             return 1;
                         }
                     });
    }

    public static Set<String> splitToSet(String whole) {
        Set<String> result = new HashSet<>();

        if (StringUtils.isNotBlank(whole)) {
            for (String part : whole.split(SPLIT_REGEX)) {
                if (StringUtils.isNotBlank(part)) {
                    result.add(part);
                }
            }
        }

        return result;
    }

    public static List<String> splitToList(String whole) {
        return new ArrayList<>(splitToSet(whole));
    }

    public static <T> List<T> splitToList(String whole, Class<T> clazz) {
        return new ArrayList<>(splitToSet(whole, clazz));
    }

    public static <T> Set<T> splitToSet(String whole, Class<T> clazz) {
        Set<T> result = new HashSet<>();

        for (String element : splitToSet(whole)) {
            T casted = classCast(element, clazz);
            if (casted != null) {
                result.add(casted);
            }
        }

        return result;
    }

    public static <T> T classCast(String input, Class<T> clazz) {
        if (input == null) {
            return null;
        }

        T result = null;

        if (String.class.isAssignableFrom(clazz)) {
            result = (clazz.cast(input));
        } else if (Modifier.isAbstract(clazz.getModifiers())) {

        } else if (Boolean.class.isAssignableFrom(clazz)) {
            result = (clazz.cast(Boolean.parseBoolean(input)));
        } else if (StringUtils.isNotBlank(input)) {
            try {
                if (Integer.class.isAssignableFrom(clazz)) {
                    result = (clazz.cast(Integer.parseInt(input)));
                } else if (Double.class.isAssignableFrom(clazz)) {
                    result = (clazz.cast(Double.parseDouble(input)));
                } else if (Float.class.isAssignableFrom(clazz)) {
                    result = (clazz.cast(Float.parseFloat(input)));
                } else if (Long.class.isAssignableFrom(clazz)) {
                    result = (clazz.cast(Long.parseLong(input)));
                } else if (Short.class.isAssignableFrom(clazz)) {
                    result = (clazz.cast(Short.parseShort(input)));
                } else if (Byte.class.isAssignableFrom(clazz)) {
                    result = (clazz.cast(Byte.parseByte(input)));
                }
            } catch (NumberFormatException e) {
                logger.error("parse number exception, error: " + e.getMessage() + " , input: " + input);
            }
        }

        return result;
    }

    public static Set<String> getMatched(String whole, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return getMatched(whole, pattern);
    }

    public static Set<String> getMatched(String whole, Pattern pattern) {
        /**
         * <pre>
         * /CS/2556/30028/30029/30047,/HW/HW0/32/2831/2804401,/AR/AR0/2604,/AR/AR0/2605
         * </pre>
         **/
        // (?<=.*(/AR/AR0/))\d+
        // (?<=.*/HW/HW0/\\d+/\\d+/)\\d+;
        // (?<=.*/CS/\\d+/\\d+/)\\d+
        // 截取 /AR/AR0/ 之后遇到的 数字

        Matcher matcher = pattern.matcher(whole);
        Set<String> result = new HashSet<>();
        // if (matcher.find()) {
        // for (int i = 0; i <= matcher.groupCount(); i++) {
        // result.add(matcher.group(i));
        // }
        // }
        while (matcher.find()) {
            result.add(matcher.group());
        }

        return result;
    }

    public static List<String> filterEmpty(List<String> collection) {
        return collection.stream().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
    }

    public static List<String> filterBlank(List<String> collection) {
        return collection.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }

    public static String joinIgnoreEmpty(List<String> collection, String... args) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }

        return StringUtils.join(filterEmpty(collection), args.length > 0 ? args[0] : SPLIT_REGEX);
    }

    public static String joinIgnoreBlank(List<String> collection, String... args) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }

        return StringUtils.join(filterBlank(collection), args.length > 0 ? args[0] : SPLIT_REGEX);
    }

    public static Set<String> filterEmptyUnique(List<String> collection) {
        return collection.stream().filter(StringUtils::isNotEmpty).collect(Collectors.toSet());
    }

    public static Set<String> filterBlankUnique(List<String> collection) {
        return collection.stream().filter(StringUtils::isNotBlank).collect(Collectors.toSet());
    }

    public static Set<String> filter(List<String> collection) {
        return collection.stream().filter(StringUtils::isNotBlank).map(o -> o.trim().toLowerCase()).collect(Collectors.toSet());
    }

    public static String joinUniqueIgnoreEmpty(List<String> collection, String... args) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }

        return StringUtils.join(filterEmptyUnique(collection), args.length > 0 ? args[0] : SPLIT_REGEX);
    }

    public static String joinUniqueIgnoreBlank(List<String> collection, String... args) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }

        return StringUtils.join(filterBlankUnique(collection), args.length > 0 ? args[0] : SPLIT_REGEX);
    }

    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<>(0);

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            /**
             * <pre>
             *     这里需要说明一下：他是根据拼凑的字符来找你写的getter方法的 
             *     在Boolean值的时候是isXXX（默认使用ide生成getter的都是isXXX）
             *     如果出现NoSuchMethod异常 就说明它找不到那个getter方法
             * </pre>
             */
            try {
                Method m = obj.getClass().getMethod("get" + getMethodName(field.getName()));
                map.put(field.getName(), m.invoke(obj));
            } catch (Exception ignored) {
            }
        }

        return map;
    }

    /**
     * 把一个字符串的第一个字母大写
     *
     * @param fieldName
     * @return
     * @throws Exception
     */
    private static String getMethodName(String fieldName) throws Exception {
        byte[] items = fieldName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

    /**
     * 判断集合对象是否为空
     * 
     * @param collections 集合对象
     * @return 结果（为空：true，不为空：false）
     */
    @Deprecated
    public static boolean isEmpty(Collection collections) {
        return CollectionUtils.isEmpty(collections);
    }
}
