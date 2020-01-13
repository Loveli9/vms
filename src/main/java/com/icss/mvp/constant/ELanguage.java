package com.icss.mvp.constant;

import java.util.HashSet;
import java.util.Set;

import com.icss.mvp.util.CollectionUtilsLocal;

/**
 * File suffix list used by programming language
 * 
 * @author Ray
 * @date 2018/10/8
 */
public enum ELanguage {
	/**
	 * all
	 */
	ALL("ALL", ""),
    /**
     * Java
     */
    JAVA("JAVA", "css,html,java,js,jsp,properties,sql,txt,xml"),
    /**
     * JavaScript
     */
    JS("JS", "txt,js,html,htm,css"),
    /**
     * C/C++
     */
    C("C", "cc,cpp,h,hpp,hxx"),
    /**
     * Python
     */
    PYTHON("PYTHON", "dat,py,pyc,pyd,pyo,pyw,sh"),
    /**
     * Golang
     */
    GO("GO", "go,json,sh");

    ELanguage(String type, String suffix){
        this.type = type;
        this.suffix = suffix;
    }

    private String type;
    private String suffix;

    public String getType() {
        return type;
    }

    public static ELanguage getByType(String input) {
        for (ELanguage language : ELanguage.values()) {
            if (language.type.equalsIgnoreCase(input)) {
                return language;
            }
        }
        return null;
    }

    public static Set<String> getSuffix(String input) {
        return getSuffix(getByType(input));
    }

    public static Set<String> getSuffix(ELanguage input) {
        return input != null ? CollectionUtilsLocal.splitToSet(input.suffix) : new HashSet<>();
    }
}
