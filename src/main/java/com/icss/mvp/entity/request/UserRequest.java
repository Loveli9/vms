package com.icss.mvp.entity.request;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.icss.mvp.entity.system.UserEntity;

/**
 * Created by Ray on 2018/12/2.
 */
public class UserRequest extends UserEntity {

    /**
     * default serial version UID
     */
    private static final long serialVersionUID = 1L;

    private Set<String>       ids;

    private Set<String>       names;

    private Set<String>       aliases;

    private Set<Integer>      types;

    public Set<String> getIds() {
        if (ids == null || ids.isEmpty()) {
            Set<String> nameSet = new HashSet<>();
            if (StringUtils.isNotBlank(super.getId())) {
                nameSet.add(super.getId().trim());
            }
            names = nameSet;
        }
        return names;
    }

    public void setIds(Set<String> ids) {
        this.ids = ids;
    }

    public Set<String> getNames() {
        if (names == null || names.isEmpty()) {
            Set<String> nameSet = new HashSet<>();
            if (StringUtils.isNotBlank(super.getName())) {
                nameSet.add(super.getName().trim());
            }
            names = nameSet;
        }
        return names;
    }

    public void setNames(Set<String> names) {
        this.names = names;
    }

    public Set<String> getAliases() {
        if (aliases == null || aliases.isEmpty()) {
            Set<String> nameSet = new HashSet<>();
            if (StringUtils.isNotBlank(super.getNickname())) {
                nameSet.add(super.getNickname().trim());
            }
            aliases = nameSet;
        }
        return aliases;
    }

    public void setAliases(Set<String> aliases) {
        this.aliases = aliases;
    }

    public Set<Integer> getTypes() {
        if (types == null || types.isEmpty()) {
            Set<Integer> typeSet = new HashSet<>();
            if (super.getType() >= 0) {
                typeSet.add(super.getType());
            }
            types = typeSet;
        }
        return types;
    }

    public void setTypes(Set<Integer> types) {
        this.types = types;
    }
}
