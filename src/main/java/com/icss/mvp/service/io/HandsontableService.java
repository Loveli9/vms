package com.icss.mvp.service.io;

import com.alibaba.fastjson.JSON;
import com.icss.mvp.dao.io.HandsontableDao;
import com.icss.mvp.entity.HandsontableInfo;
import com.icss.mvp.entity.request.HandsontableRequest;
import com.icss.mvp.service.CommonService;
import com.icss.mvp.util.IOUtils;
import com.icss.mvp.util.UUIDUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by up on 2019/2/22.
 */
@Service("HandsontableService")
@PropertySource("classpath:task.properties")
public class HandsontableService extends CommonService {

    private static Logger logger = Logger.getLogger(HandsontableService.class);
    @Value("${handsontable-path}")
    public String handsontablePath;
    @Autowired
    private HandsontableDao handsontableDao;


    public int saveHandsontable(HandsontableRequest handsontableRequest) throws Exception {
        logger.debug(handsontableRequest.toString());
        if(handsontableRequest.getData()==null || handsontableRequest.getData().size()<=0){
            throw new Exception("保存异常，入参遍历无表格数据");
        }
        String path = handsontablePath + handsontableRequest.getNo() + "/handsontable_" + handsontableRequest.getModule() + ".json";
        IOUtils.saveDataToFile(path,handsontableRequest.toString());
        HandsontableInfo handsontableInfo = handsontableDao.getHandsontable(handsontableRequest.getNo(),handsontableRequest.getModule());
        if(handsontableInfo==null){
            handsontableInfo = new HandsontableInfo();
            handsontableInfo.setNo(handsontableRequest.getNo());
            handsontableInfo.setModule(handsontableRequest.getModule());
            handsontableInfo.setIsDeleted(0);
            Date today = new Date();
            handsontableInfo.setCreateTime(today);
            handsontableInfo.setModifyTime(today);
            handsontableDao.insertHandsontable(handsontableInfo);
        }
        return 0;
    }

    public HandsontableRequest queryHandsontable(HandsontableRequest handsontableRequest) {
        logger.debug(handsontableRequest.toString());
        HandsontableInfo handsontableInfo = handsontableDao.getHandsontable(handsontableRequest.getNo(),handsontableRequest.getModule());
        if(handsontableInfo==null){
            logger.error("文件记录不存在");
            return handsontableRequest;
        }
        String path = handsontablePath + handsontableRequest.getNo() + "/handsontable_" + handsontableRequest.getModule() + ".json";
        String data = IOUtils.getDatafromFile(path);
        handsontableRequest = JSON.parseObject(data,HandsontableRequest.class);
        logger.debug(handsontableRequest.toString());
        return handsontableRequest;
    }

}
