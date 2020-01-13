package com.icss.mvp.constant;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.icss.mvp.util.StringUtilsLocal;

/**
 * 获取各种文件类型后缀(java/c++/js)
 * @author Administrator
 *
 */
public class FileTypeEnum {

    /**
     * 判断一个对象中所以成员不为空和0
     * 
     * @param obj
     * @return
     */
    public static boolean checkObjFieldIsNull(Object obj) {

        boolean flag = false;

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
                String value = StringUtilsLocal.valueOf(m.invoke(obj));
                if (StringUtils.isNotBlank(value) && !"0".equals(value)) {
                    flag = true;
                    break;
                }
            } catch (Exception ignored) {
            }
        }

        return flag;
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
	 * 判断文件属于哪种开发语言
	 */
	public String  getTypeByDivideType(String divideType) {
		 for (FileTypeDivide type : FileTypeDivide.values()) {
                if (divideType.equals(type.getType())) {
                	return type.getFileType();
                }
          }
		return null;
	}
	
	/**
	 * 获取所有的枚举大类
	 */
	public List<String> getAllTypes() {
		List<String> types = new ArrayList<String>();
		EnumSet<FileType> fileType = EnumSet.allOf(FileType.class);
		for (FileType type : fileType) {
			types.add(type.getFileType());
		}
		return types;
	}
	
	/**
	 * 获取所有的枚举大类+小类
	 */
	public static List<Map<String, Object>> getAllTypesMap() {
		List<Map<String, Object>> types = new ArrayList<Map<String, Object>>();
		EnumSet<FileType> fileType = EnumSet.allOf(FileType.class);
		
		for (FileType type : fileType) {
			Map<String, Object> map = new HashMap<>();
			String key = type.getFileType();
			map.put(key, FileType.getFileTypes(key));
			types.add(map);
		}
		System.out.println(types);
		return types;
	}
	public enum FileTypeDivide{
		//JAVA
		java_JAVA("java", FileType.JAVA_TYPE),
		xml_JAVA("xml", FileType.JAVA_TYPE),
		sql_JAVA("sql", FileType.JAVA_TYPE),
		properties_JAVA("properties", FileType.JAVA_TYPE),
		jsp_JAVA("jsp", FileType.JAVA_TYPE),
		html_JAVA("html", FileType.JAVA_TYPE),
		css_JAVA("css", FileType.JAVA_TYPE),
		js_JAVA("js", FileType.JAVA_TYPE),
		txt_JAVA("txt", FileType.JAVA_TYPE),
		
		//JS
		html_JS("html", FileType.JS_TYPE), 
		htm_JS("htm",FileType.JS_TYPE),
		css_JS("css", FileType.JS_TYPE), 
		js_JS("js", FileType.JS_TYPE),
		txt_JS("txt", FileType.JS_TYPE),
		
		//C++
		h_C("h",FileType.C_TYPE),
		hpp_C("hpp",FileType.C_TYPE),
		hxx_C("hxx",FileType.C_TYPE),
		cpp_C("cpp",FileType.C_TYPE),
		cc_C("cc",FileType.C_TYPE),
		cxx_C("cxx",FileType.C_TYPE),
		c_C("c",FileType.C_TYPE),
		txt_C("txt", FileType.C_TYPE),
		cadd_C("c++",FileType.C_TYPE),
		
		//python (.py .pyc .pyw .pyo .pyd)
		py_p("py",FileType.PYTHON_TYPE),
		pyc_p("pyc",FileType.PYTHON_TYPE),
		pyw_p("pyw",FileType.PYTHON_TYPE),
		pyo_p("pyo",FileType.PYTHON_TYPE),
		pyd_p("pyd",FileType.PYTHON_TYPE),
		sh_p("sh",FileType.PYTHON_TYPE),
		dat_p("dat",FileType.PYTHON_TYPE),
		
		//GO
		go_go("go", FileType.GO_TYPE), 
		json_go("json",FileType.GO_TYPE),
		sh_go("sh", FileType.GO_TYPE);

		private String type;
		private FileType fileType;

		private FileTypeDivide(String type, FileType fileType) {
			this.type = type;
			this.fileType = fileType;
		}

		public String getType() {
			return this.type;
		}

		public String getFileType() {
			return this.fileType.getFileType();
		}
	}
	
	public enum FileType{
		JAVA_TYPE("JAVA"), 
		JS_TYPE("JS"),
		C_TYPE("C"),
		PYTHON_TYPE("P"),
		GO_TYPE("GO");

		FileType(String fileType) {
			this.fileType = fileType;
		}

		private String fileType;

		public String getFileType() {
			return fileType;
		}
		public void setFileType(String fileType) {
			this.fileType = fileType;
		}
		
		/**
		 *获取开发语言包含的文件种类
		 */
		public List<String>  allDivideFiles() {
			List<String> types = new ArrayList<String>();
			 for (FileTypeDivide type : FileTypeDivide.values()) {
	                if (fileType.equals(type.getFileType())) {
	                	types.add(type.getType());
	                }
	          }
			return types;
		}
		
		/**
		 *获取开发语言包含的文件种类
		 */
		public static String  getFileTypes(String fileType) {
			List<String> types = new ArrayList<String>();
			 for (FileTypeDivide type : FileTypeDivide.values()) {
	                if (fileType.equals(type.getFileType())) {
	                	types.add(type.getType());
	                }
	          }
			if(types.size()>0) {
				return "("+ StringUtilsLocal.listToSqlIn(types)+")";
			}
			 
			return "";
		}

        /**
         * 获取开发语言包含的文件种类
         */
        public static Set<String> getFileTypeSet(String fileType) {
            Set<String> types = new HashSet<>();
            for (FileTypeDivide type : FileTypeDivide.values()) {
                if (fileType.equals(type.getFileType())) {
                    types.add(type.getType());
                }
            }
            return types;
        }
		
	}
	
	
	
	public static void main(String[] args) {
		FileTypeEnum fileTypeEnum = new FileTypeEnum();
		
		
		//获取java文件分类
		System.out.println(FileType.JAVA_TYPE.allDivideFiles());
		//获取JS文件分类
		System.out.println(FileType.JS_TYPE.allDivideFiles());
		//获取c++文件分类
		System.out.println(FileType.C_TYPE.allDivideFiles());
		//获取所有的文件大类
		System.out.println(fileTypeEnum.getAllTypes());
		System.out.println(FileType.getFileTypes("JAVA"));
		getAllTypesMap();
    	//根据文件小类获取文件大类
    	System.out.println(fileTypeEnum.getTypeByDivideType("htm"));
	}
}
