package com.icss.mvp.entity.common;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.Labels;

/**
 * @author Ray
 * @date 2018/9/2
 */
public class BaseEntity implements Serializable {

    /**
     * default serial version UID
     */
    private static final long     serialVersionUID = 1L;

    protected static final String ESSENTIAL_LABEL  = "essential";

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    /**
     * @param labels varargs 变长参数
     * @return
     */
    public String toLabelString(String... labels) {
        if (labels.length == 0) {
            return JSON.toJSONString(this, Labels.includes(ESSENTIAL_LABEL));
        }

        List<String> varargs = new ArrayList<>(Arrays.asList(labels));
        if (varargs.contains(ESSENTIAL_LABEL)) {
            return JSON.toJSONString(this, Labels.includes(labels));
        }

        varargs.add(ESSENTIAL_LABEL);
        return JSON.toJSONString(this, Labels.includes(varargs.toArray(new String[varargs.size()])));
    }

    public String decode(String... args) {

        if (args.length == 0) {
            return null;
        }

        String input = args[0];
        if (StringUtils.isBlank(input)) {
            return input;
        }

        String encoding = args.length > 1 ? args[1] : null;
        encoding = StringUtils.isBlank(encoding) ? "UTF-8" : encoding;

        String decode;
        try {
            decode = URLDecoder.decode(input, encoding);
        } catch (UnsupportedEncodingException e) {
            decode = null;
        }

        return StringUtils.isNotBlank(decode) ? decode : input;
    }
}
