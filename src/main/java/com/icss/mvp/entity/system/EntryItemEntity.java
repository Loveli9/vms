package com.icss.mvp.entity.system;

import com.icss.mvp.entity.common.CommonEntity;
import com.icss.mvp.util.MathUtils;

/**
 * Created by Ray on 2019/3/19.
 *
 * @author Ray
 * @date 2019/3/19 15:01
 */
public class EntryItemEntity extends CommonEntity {

    private static final long serialVersionUID = 1L;

    private String            key;

    private String            value;

    private int               order;

    private String            entryId;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        int id = MathUtils.parseIntSmooth(this.getId(), 0);
        this.order = id == order ? -1 : order;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }
}
