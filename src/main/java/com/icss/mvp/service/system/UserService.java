package com.icss.mvp.service.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.dao.system.IUserDao;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.request.UserRequest;
import com.icss.mvp.entity.system.UserEntity;
import com.icss.mvp.util.CollectionUtilsLocal;

/**
 * @author  Ray on 2018/12/2.
 *
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service("userService")
public class UserService {

    private static Logger logger = Logger.getLogger(UserService.class);

    static final int HUA_USER = 2;

    static final int ZR_USER = 1;

    static final int SYS_USER = 0;

    @Autowired
    IUserDao              userDao;

    public ListResponse<UserEntity> getUsers(UserRequest userEntity) {
        Map<String, Object> params = new HashMap<>(0);
        params.put("ids", userEntity.getIds());
        if (userEntity.getIds().isEmpty()) {
            params.put("names", userEntity.getNames());
        }
        params.put("password", userEntity.getPassword());
        params.put("types", userEntity.getTypes());

        ListResponse<UserEntity> result = new ListResponse<>();
        try {
            result.setData(userDao.getUserInfo(params));
        } catch (Exception e) {
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
            logger.error("userDao.getUserInfo exception, error: " + e.getMessage());
        }

        if (result.getData() == null || result.getData().isEmpty()) {
            result.setData(new ArrayList<>());
        }

        return result;
    }

    public UserEntity verifyUser(String id, String password, Integer... types) {
        UserRequest params = new UserRequest();
        params.setId(id);
        params.setPassword(password);
        if (types.length > 0 && types[0] >= 0) {
            params.setTypes(new HashSet<>(types[0]));
        }

        UserEntity result = null;
        ListResponse<UserEntity> response = getUsers(params);
        if (!response.getSucceed()) {
            logger.error("getUsers failed, error: " + response.getMessage());
        } else if (response.getData().size() != 1) {
            logger.error("verifyUser failed, error: not found or matched more than one records. matched "
                         + response.getData().size());
        } else {
            result = response.getData().get(0);
        }

        return result;
    }

    public BaseResponse updateUser(UserEntity user) {
        BaseResponse result = new BaseResponse();
        try {
            userDao.replaceUserInfo(user);
        } catch (Exception e) {
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
            logger.error("userDao.replaceUserInfo exception, error: " + e.getMessage());
        }

        return result;
    }

    public PlainResponse<UserEntity> alterUser(UserEntity user) {
        PlainResponse<UserEntity> result = new PlainResponse<>();
        if (user == null || StringUtils.isBlank(user.getId())) {
            result.setError(CommonResultCode.INVALID_PARAMETER, "userId");
            return result;
        }

        UserEntity userInfo = verifyUser(user.getId(), null, user.getType());
        if (userInfo == null) {
            userInfo = user;
            // 用户首次登录，设定默认权限为 "普通用户"
            userInfo.setGrantedPermissionIds("7");
            // 用户首次登录，设定默认部门为 "默认产品线"
            if (user.getType() == HUA_USER) {
                user.setTrunk("32");
            }
            userInfo.setCreateTime(new Date());
            userInfo.setModifyTime(userInfo.getCreateTime());
        } else {
            userInfo.setTrunk(alertCollection(userInfo.getTrunk(), user.getTrunk()));
            userInfo.setBranch(alertCollection(userInfo.getBranch(), user.getBranch()));
            userInfo.setDept(alertCollection(userInfo.getDept(), user.getDept()));

            if (userInfo.getType() == ZR_USER) {
                userInfo.setName(user.getName());
            }

            userInfo.setPassword(user.getPassword());
            userInfo.setNickname(user.getNickname());

            userInfo.setModifyTime(new Date());
        }

        updateUser(userInfo);

        result.setData(userInfo);
        return result;
    }
    
    private String alertCollection(String origin, String newest) {
        if (StringUtils.isBlank(newest)) {
            return origin;
        }

        Set<String> result = CollectionUtilsLocal.splitToSet(origin);
        result.add(newest);

        return StringUtils.join(result, CollectionUtilsLocal.SPLIT_REGEX);
    }

    public BaseResponse alterUser(Map<String, Object> user) {
        BaseResponse result = new BaseResponse();
        String userId = (user == null || !user.containsKey("id")) ? "" : String.valueOf(user.get("id"));
        if (StringUtils.isBlank(userId)) {
            result.setError(CommonResultCode.INVALID_PARAMETER, "userId");
            return result;
        }

        try {
            userDao.alertUserInfo(user);
        } catch (Exception e) {
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }
}
