package com.icss.mvp.service.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.icss.mvp.util.CollectionUtilsLocal;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.dao.system.IDepartmentDao;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.system.DepartmentEntity;
import com.icss.mvp.entity.system.Pedigree;
import com.icss.mvp.util.MybatisUtils;

/**
 * @author Ray on 2018/10/1.
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service("departmentService")
public class DepartmentService {

    private static Logger      logger               = Logger.getLogger(DepartmentService.class);

    public final static String SUPPLIER_TYPE_WAHWAY = "hw";
    public final static String CONTRACTOR_TYPE      = "op";

    @Autowired
    private IDepartmentDao     departmentDao;

    static final int           CTR_TIC              = 5;

    /**
     * 获取城市列表
     *
     * @return
     */
    public ListResponse<DepartmentEntity> describeDepartments(DepartmentEntity dept, List<Integer> ids) {
        ListResponse<DepartmentEntity> result = new ListResponse<>();

        List<DepartmentEntity> data = null;
        try {
            data = departmentDao.queryDepartments(generateConditions(dept, ids));
        } catch (Exception e) {
            logger.error("departmentDao.queryDepartments exception: " + e.getMessage());

            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        result.setData((data == null || data.isEmpty()) ? new ArrayList<>() : data);

        return result;
    }

    /**
     * 部门列表查询
     *
     * @return
     */
    public ListResponse<DepartmentEntity> describeDepartments(DepartmentEntity dept) {
        ListResponse<DepartmentEntity> result = new ListResponse<>();

        Map<String, Object> parameter = new HashMap<>(0);
        parameter.put("order", "d.`dept_id`");
        parameter.put("sort", "ASC");

        getConditions(parameter, dept);

        List<DepartmentEntity> data = null;
        try {
            if (CONTRACTOR_TYPE.equalsIgnoreCase(dept.getType())) {
                data = departmentDao.queryOpDepts(parameter);
            } else {
                data = departmentDao.queryHwDepts(parameter);
            }
        } catch (Exception e) {
            logger.error("departmentDao.queryDepartments exception: " + e.getMessage());

            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        result.setData((data == null || data.isEmpty()) ? new ArrayList<>() : data);

        return result;
    }

    private void getConditions(Map<String, Object> parameter, DepartmentEntity entity) {
        if (entity != null) {
            MybatisUtils.buildParam(parameter, entity.getId(), "id");
            MybatisUtils.buildParam(parameter, entity.getName(), "name");
            MybatisUtils.buildScopeParam(parameter, entity.getCode(), "code");
            MybatisUtils.buildParam(parameter, entity.getLevel(), "level");
            MybatisUtils.buildParam(parameter, entity.getParentCode(), "parentCode");
        }
    }

    private Map<String, Object> generateConditions(DepartmentEntity dept, List<Integer> ids) {
        Map<String, Object> param = new HashMap<>(0);
        // 部分特殊参数的处理和封装，也可直接通过反射
        if (ids == null || ids.isEmpty()) {
            param.put("id", dept.getId());
        } else {
            param.put("ids", ids);
        }

        if (StringUtils.isNotBlank(dept.getName())) {
            param.put("name", dept.getName());
        }

        List<String> codes = CollectionUtilsLocal.splitToList(dept.getCode());
        if (!codes.isEmpty()) {
            param.put((codes.size() == 1 ? "code" : "codes"), codes);
        }

        List<String> parentCodes = CollectionUtilsLocal.splitToList(dept.getParentCode());
        if (!parentCodes.isEmpty()) {
            param.put((parentCodes.size() == 1 ? "parentCode" : "parentCodes"), parentCodes);
        }

        if (dept.getLevel() > 0) {
            param.put("level", dept.getLevel());
        }
        if (dept.getRank() > 0) {
            param.put("rank", dept.getRank());
        }
        if (dept.getIsDeleted() >= 0) {
            param.put("isDelete", dept.getIsDeleted());
        }

        // 一次仅能获取一种机构类型的数据
        param.put("type", StringUtils.isBlank(dept.getType()) ? SUPPLIER_TYPE_WAHWAY : dept.getType());

        return param;
    }

    public Map<String, String[]> getChainMap(String type, int level, String trunkCode, boolean isUltimate) {

        List<DepartmentEntity> existList;
        if (CONTRACTOR_TYPE.equalsIgnoreCase(type)) {
            existList = queryOpByLevel(0, isUltimate);
        } else {
            existList = queryHwByLevel(0, isUltimate);
        }

        // 部门编码映射集合，可根据部门编码获取已存储的部门数据
        Map<String, DepartmentEntity> existIndexMap = getCodeIndexMap(existList);
        // 部门名称映射集合，拼接部门名称以及所属上级各部门名称为完整部门名称，以完整部门名称获取对应各层级部门编码
        return getFullNameMap(existIndexMap, level, trunkCode);
    }

    public Map<String, Pedigree> getPedigreeMap(String type, int level, String trunkCode, boolean isUltimate) {

        List<DepartmentEntity> existList;
        if (CONTRACTOR_TYPE.equalsIgnoreCase(type)) {
            existList = queryOpByLevel(0, isUltimate);
        } else {
            existList = queryHwByLevel(0, isUltimate);
        }

        // 部门编码映射集合，可根据部门编码获取已存储的部门数据
        Map<String, DepartmentEntity> existIndexMap = getCodeIndexMap(existList);
        // 部门名称映射集合，拼接部门名称以及所属上级各部门名称为完整部门名称，以完整部门名称获取对应各层级部门编码
        return getPedigreeMap(existIndexMap, level, trunkCode);
    }

    public PlainResponse renewOrganization(String type, boolean isUltimate) {
        PlainResponse result = new PlainResponse();

        if (CONTRACTOR_TYPE.equalsIgnoreCase(type)) {
            renewOpDepartment(isUltimate);
        } else if (SUPPLIER_TYPE_WAHWAY.equalsIgnoreCase(type)) {
            renewHwDepartment(isUltimate);
        } else {
            renewHwDepartment(isUltimate);
            renewOpDepartment(isUltimate);
        }

        return result;
    }

    public PlainResponse renewOpDepartment(boolean isUltimate) {
        PlainResponse result = new PlainResponse();

        List<DepartmentEntity> copyList = readOpCopy();
        Map<String, DepartmentEntity> newCodeMap = getCodeIndexMap(copyList);

        for (int i = 1; i < CTR_TIC; i++) {
            alertOp(newCodeMap, i, isUltimate);
        }

        return result;
    }

    public PlainResponse renewHwDepartment(boolean isUltimate) {
        PlainResponse result = new PlainResponse();

        List<DepartmentEntity> copyList = readHwCopy();
        Map<String, DepartmentEntity> newCodeMap = getCodeIndexMap(copyList);

        for (int i = 1; i < CTR_TIC; i++) {
            alertHw(newCodeMap, i, isUltimate);
        }

        return result;
    }

    private List<DepartmentEntity> readOpCopy() {
        List<DepartmentEntity> result = null;
        try {
            result = departmentDao.readOpCopy();
        } catch (Exception e) {
            logger.error("departmentDao.readOpCopy exception, error: "+e.getMessage());
        }

        return (result == null || result.isEmpty()) ? new ArrayList<>() : result;
    }

    private List<DepartmentEntity> queryOpByLevel(int level, boolean isUltimate) {
        DepartmentEntity param = new DepartmentEntity();
        param.setType(CONTRACTOR_TYPE);
        param.setLevel(level);
        param.setIsDeleted(-1);

        List<DepartmentEntity> result = null;
        try {
            if (isUltimate) {
                result = departmentDao.queryDepartments(generateConditions(param, null));
            } else {
                Map<String, Object> parameters = new HashMap<>(0);
                getConditions(parameters, param);

                result = departmentDao.queryOpDepts(parameters);
            }
        } catch (Exception e) {
            logger.error("departmentDao.queryOpDepts exception, error: "+e.getMessage());
        }

        return (result == null || result.isEmpty()) ? new ArrayList<>() : result;
    }

    private List<DepartmentEntity> readHwCopy() {
        List<DepartmentEntity> result = null;
        try {
            result = departmentDao.readHwCopy();
        } catch (Exception e) {
            logger.error("departmentDao.readHwCopy exception, error: "+e.getMessage());
        }

        return (result == null || result.isEmpty()) ? new ArrayList<>() : result;
    }

    private List<DepartmentEntity> queryHwByLevel(int level, boolean isUltimate) {
        DepartmentEntity param = new DepartmentEntity();
        param.setType(SUPPLIER_TYPE_WAHWAY);
        param.setLevel(level);
        // 不增加新数据记录，对igneo不做限定
        param.setIsDeleted(-1);

        List<DepartmentEntity> result = null;
        try {
            if (isUltimate) {
                result = departmentDao.queryDepartments(generateConditions(param, null));
            } else {
                Map<String, Object> parameters = new HashMap<>(0);
                getConditions(parameters, param);

                result = departmentDao.queryHwDepts(parameters);
            }
        } catch (Exception e) {
            logger.error("departmentDao.queryHwDepts exception, error: "+e.getMessage());
        }

        return (result == null || result.isEmpty()) ? new ArrayList<>() : result;
    }

    public void alertOp(Map<String, DepartmentEntity> newIndexMap, int level, boolean isUltimate) {
        // 由1级部门开始更新组织机构，需要每次重新读取一次数据库记录
        List<DepartmentEntity> existList = queryOpByLevel(0, isUltimate);

        List<DepartmentEntity> alertList = new ArrayList<>();
        List<String> validList = new ArrayList<>();

        validDepartment(newIndexMap, existList, level, alertList, validList, CONTRACTOR_TYPE);

        if (!alertList.isEmpty()) {
            try {
                if (isUltimate) {
                    // TODO:insert ultimate
                } else {
                    departmentDao.increaseOpDepartments(alertList);
            }
            } catch (Exception e) {
                logger.error("departmentDao.increaseOpDepartments exception, error: "+e.getMessage());
            }
        }

        if (!validList.isEmpty()) {
            try {
                if (isUltimate) {
                    // TODO:insert ultimate
                } else {
                    departmentDao.switchOpDepartments(level, validList, new Date());
                }
            } catch (Exception e) {
                logger.error("departmentDao.switchOpDepartments exception, error: "+e.getMessage());
            }
        }
    }

    private void validDepartment(Map<String, DepartmentEntity> newIndexMap, List<DepartmentEntity> existList,
                                 int level, List<DepartmentEntity> alertList, List<String> validList, String type) {
        // 部门编码映射集合，可根据部门编码获取已存储的部门数据
        Map<String, DepartmentEntity> existIndexMap = getCodeIndexMap(existList);
        // 部门名称映射集合，拼接部门名称以及所属上级各部门名称为完整部门名称，以完整部门名称获取对应各层级部门编码
        Map<String, String[]> existNameMap = getFullNameMap(existIndexMap, 0, "");

        // 根据新数据生成指定层级的有效的完整部门名称列表
        Map<String, String[]> newNameMap = getFullNameMap(newIndexMap, level, "", true);

        for (String entry : newNameMap.keySet()) {
            List<String> chain = new ArrayList<>(Arrays.asList(StringUtils.split(entry, "/")));
            String name;
            if (chain.size() != level || StringUtils.isBlank(name = chain.get(level - 1))) {
                continue;
            }

            String code;
            String[] chainCode = existNameMap.get(entry);
            code = (chainCode != null && chainCode.length == level) ? chainCode[level - 1] : "";

            String parentCode = "";
            if (level > 1) {
                chain.remove(level - 1);

                String[] parentChain = existNameMap.get(StringUtils.join(chain, "/"));
                if (parentChain != null && parentChain.length == level - 1) {
                    DepartmentEntity parent = existIndexMap.get(parentChain[level - 2]);
                    // 判断上级部门数据是否有效
                    parentCode = visible(parent) ? parent.getCode() : "";
                }
            } else {
                parentCode = "0";
            }

            if (StringUtils.isBlank(parentCode)) {
                // 从1级部门开始处理，上级部门不存在属于错误数据
                continue;
            }

            DepartmentEntity entity;
            if (StringUtils.isNotBlank(code)) {
                // 已存在数据库记录，根据部门编码获取相关数据
                entity = existIndexMap.get(code);
                entity.setIsDeleted(0);
            } else {
                entity = new DepartmentEntity();
                entity.setName(name);
                entity.setCode(String.valueOf(entity.getCreateTime().getTime()));
                entity.setLevel(level);
                entity.setParentCode(parentCode);
                entity.setType(type);

                alertList.add(entity);
            }

            validList.add(entity.getCode());

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                logger.error("Thread.sleep exception, error: "+e.getMessage());
                // clean up state...
                Thread.currentThread().interrupt();
            }
        }
    }

    public void alertHw(Map<String, DepartmentEntity> newIndexMap, int level, boolean isUltimate) {
        // 由1级部门开始更新组织机构，需要每次重新读取一次数据库记录
        List<DepartmentEntity> existList = queryHwByLevel(0, isUltimate);

        List<DepartmentEntity> alertList = new ArrayList<>();
        List<String> validList = new ArrayList<>();

        validDepartment(newIndexMap, existList, level, alertList, validList, SUPPLIER_TYPE_WAHWAY);

        if (!alertList.isEmpty()) {
            try {
                if (isUltimate) {
                    // TODO:insert ultimate
                } else {
                    departmentDao.increaseHwDepartments(alertList);
                }
            } catch (Exception e) {
                logger.error("departmentDao.increaseHwDepartments exception, error: "+e.getMessage());
            }
        }

        if (!validList.isEmpty()) {
            try {
                if (isUltimate) {
                    // TODO:insert ultimate
                } else {
                    departmentDao.switchHwDepartments(level, validList, new Date());
                }
            } catch (Exception e) {
                logger.error("departmentDao.switchHwDepartments exception, error: "+e.getMessage());
            }
        }
    }

    private boolean valid(DepartmentEntity entity) {
        // level 1 parent_id: 0
        return entity != null && StringUtils.isNotBlank(entity.getName()) && StringUtils.isNotBlank(entity.getCode())
               && StringUtils.isNotBlank(entity.getParentCode());
    }

    private boolean visible(DepartmentEntity entity, boolean... visible) {
        boolean result = valid(entity);
        if (result && visible.length > 0) {
            result = entity.getIsDeleted() == (visible[0] ? 0 : 1);
        }

        return result;
    }

    private Map<String, String[]> getFullNameMap(Map<String, DepartmentEntity> indexMap, int level, String trunkCode,
                                                 boolean... display) {
        Map<String, String[]> result = new HashMap<>(0);

        // filter blank code name parentCode, isDelete, level > 0, by level
        List<DepartmentEntity> list = getLevelList(indexMap, level, display);

        for (DepartmentEntity entity : list) {
            int deep = entity.getLevel();
            String[] chain = new String[deep];
            String[] codeChain = chain.clone();

            chain[deep - 1] = entity.getName();
            codeChain[deep - 1] = entity.getCode();

            boolean valid = true;
            String parentCode = entity.getParentCode();
            for (int i = deep - 1; i > 0; i--) {
                DepartmentEntity parent = indexMap.get(parentCode);
                if (!visible(parent, display) || parent.getLevel() != i) {
                    valid = false;
                    break;
                }

                chain[i - 1] = parent.getName();
                codeChain[i - 1] = parent.getCode();

                parentCode = parent.getParentCode();
            }

            if (valid) {
                if (StringUtils.isBlank(trunkCode) || trunkCode.equalsIgnoreCase(codeChain[0])) {
                    result.put(StringUtils.join(chain, "/"), codeChain);
                }
            }
        }

        return result;
    }

    private Map<String, Pedigree> getPedigreeMap(Map<String, DepartmentEntity> indexMap, int level, String trunkCode,
                                                 boolean... display) {
        Map<String, Pedigree> result = new HashMap<>(0);

        // filter blank code name parentCode, isDelete, level > 0, by level
        List<DepartmentEntity> list = getLevelList(indexMap, level, display);

        for (DepartmentEntity entity : list) {
            int deep = entity.getLevel();
            String[] chain = new String[deep];
            String[] codeChain = chain.clone();

            chain[deep - 1] = entity.getName();
            codeChain[deep - 1] = entity.getCode();

            boolean valid = true;
            String parentCode = entity.getParentCode();
            for (int i = deep - 1; i > 0; i--) {
                DepartmentEntity parent = indexMap.get(parentCode);
                if (!visible(parent, display) || parent.getLevel() != i) {
                    valid = false;
                    break;
                }

                chain[i - 1] = parent.getName();
                codeChain[i - 1] = parent.getCode();

                parentCode = parent.getParentCode();
            }

            if (valid) {
                if (StringUtils.isBlank(trunkCode) || trunkCode.equalsIgnoreCase(codeChain[0])) {
                    result.put(StringUtils.join(chain, "/"), new Pedigree(chain, codeChain, level));
                }
            }
        }

        return result;
    }

    /**
     * @param indexMap key: deptCode, value: DepartmentEntity
     * @param level int: deptLevel = level, 0: all
     * @param display true: isDeleted = 0, false: isDelete = 1, null: all
     * @return
     */
    private List<DepartmentEntity> getLevelList(Map<String, DepartmentEntity> indexMap, int level, boolean... display) {
        Stream<DepartmentEntity> list;

        if (level > 0) {
            list = indexMap.values().stream().filter(o -> visible(o, display) && level == o.getLevel());
        } else {
            list = indexMap.values().stream().filter(o -> visible(o, display) && o.getLevel() > 0);
        }

        return list.collect(Collectors.toList());
    }

    private Map<String, DepartmentEntity> getCodeIndexMap(List<DepartmentEntity> list) {
        Map<String, DepartmentEntity> result = null;

        if (list != null && !list.isEmpty()) {
            result = list.stream().collect(Collectors.toMap(DepartmentEntity::getCode,
                                                            Function.<DepartmentEntity> identity(),
                                                            (o1, o2) -> 0 == o1.getIsDeleted() ? o1 : o2));
        }

        return (result == null || result.isEmpty()) ? new HashMap<>(0) : result;
    }

}
