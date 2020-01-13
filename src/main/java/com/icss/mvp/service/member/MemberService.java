package com.icss.mvp.service.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.dao.member.IMemberDao;
import com.icss.mvp.entity.member.MemberEntity;
import com.icss.mvp.entity.common.response.ListResponse;

/**
 * Created by Ray on 2019/2/15.
 * 
 * @author Ray
 * @date 2019/2/15
 */
@Service("memberService")
@SuppressWarnings("SpringJavaAutowiringInspection")
public class MemberService {

    @Autowired
    private IMemberDao    memberDao;

    private static Logger logger = Logger.getLogger(MemberService.class);

    /**
     * @param projectId 项目编号
     * @param duties 岗位列表
     * @return 项目成员列表
     */
    public ListResponse<MemberEntity> getProjectMembers(String projectId, Set<String> duties) {
        ListResponse<MemberEntity> result = new ListResponse<>();

        List<MemberEntity> members = null;
        try {
            members = memberDao.getProjectLocalMembers(projectId, duties);
        } catch (Exception e) {
            result.setError(CommonResultCode.INTERNAL, e.getMessage());

            logger.error("memberDao.getProjectLocalMembers exception, error: " + e.getMessage());
        }

        result.setData((members == null || members.isEmpty()) ? new ArrayList<>() : members);

        return result;
    }
}
