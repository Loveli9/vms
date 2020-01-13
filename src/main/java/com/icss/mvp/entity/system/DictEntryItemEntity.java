package com.icss.mvp.entity.system;

/**
 * Created by Ray on 2019/3/20.
 *
 * @author Ray
 * @date 2019/3/20 15:25
 */
public class DictEntryItemEntity extends EntryItemEntity {

    private static final long serialVersionUID = 1L;

    private String            entryName;

    private String            entryCode;

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = decode(entryName);
    }

    public String getEntryCode() {
        return entryCode;
    }

    public void setEntryCode(String entryCode) {
        this.entryCode = entryCode;
    }
}
