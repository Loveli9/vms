package com.icss.mvp.tree;

import com.icss.mvp.entity.OpDepartment;
import com.icss.mvp.entity.PermissionDetail;
import com.icss.mvp.entity.SysHwdept;
import com.icss.mvp.entity.TblArea;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionTreeUtil {
    //中软用户path开始
    public static final String CS_DEPT = "/CS/";
    //华为部门path开始
    public static final String HW_DEPT = "/HW/";
    //地域部门path开始
    public static final String AR_DEPT = "/AR/";
    public static NodeTuple builperTree(List<? extends Object> list) throws Exception {
        if (CollectionUtils.isEmpty(list) ){
            throw new Exception("权限信息有误");
        }
        Object dept = list.get(0);
        if (dept instanceof PermissionDetail){
        	PermissionDetail perDetail = (PermissionDetail) dept;


            Node rootNode = new Node(perDetail.getPermissionid(),
                    "权限",
                    perDetail.getParentperid(),
                    perDetail.getPerLevel().toString());
            rootNode.setPath(CS_DEPT + perDetail.getPermissionid());
            //用一个map来存储每个node的id和node，方便查找
            Map<String, Node> nodeMap = new HashMap<>();
            nodeMap.put(perDetail.getPermissionid(), rootNode);

            for (int i = 1; i < list.size(); i++) {
            	PermissionDetail perDet = (PermissionDetail) list.get(i);
                Node parentNode = nodeMap.get(perDet.getParentperid());
                if (null != parentNode){
                    Node childNode = new Node(perDet.getPermissionid(),
                    		perDet.getPermissionName(),
                    		perDet.getParentperid(),
                    		perDet.getPerLevel().toString());
                    childNode.setPath(parentNode.getPath()+"/"+ perDet.getPermissionid());
                    List<Node> childNodes = parentNode.getChildren();
                    if (null == childNodes){
                        childNodes = new ArrayList<>();
                        parentNode.setChildren(childNodes);
                    }
                    childNodes.add(childNode);
                    nodeMap.put(perDet.getPermissionid(), childNode);
                }else {
                    throw new Exception("部门信息有误");
                }
            }
            return new NodeTuple(rootNode, nodeMap);
        }else if (dept instanceof SysHwdept){
            String rootId = "HW0";
            Node rootNode = new Node(rootId, "华为", "0", "1");
            rootNode.setPath(HW_DEPT + rootId);
            Map<String, Node> nodeMap = new HashMap<>();
            nodeMap.put(rootId, rootNode);
            for (int i = 0; i < list.size(); i++) {
                SysHwdept sysHwdept = (SysHwdept) list.get(i);
                //中软组织架构的root节点的id为0，华为的也为0的话在构建树的时候存在结构异常，因此在这里稍作变通
                String parentId = sysHwdept.getParentId() == 0 ? rootId : sysHwdept.getParentId().toString();
                Node parentNode = nodeMap.get(parentId);
                if (null != parentNode){
                    Node childNode = new Node(sysHwdept.getDeptId().toString(),
                            sysHwdept.getDeptName(),
                            parentId,
                            (sysHwdept.getDeptLevel() + 1) + "");
                    childNode.setPath(parentNode.getPath() + "/" + sysHwdept.getDeptId());
                    List<Node> childNodes = parentNode.getChildren();
                    if (null == childNodes){
                        childNodes = new ArrayList<>();
                        parentNode.setChildren(childNodes);
                    }
                    childNodes.add(childNode);
                    nodeMap.put(sysHwdept.getDeptId().toString(), childNode);
                }else {
                    throw new Exception("部门信息有误");
                }
            }
            return new NodeTuple(rootNode, nodeMap);
        }else if (dept instanceof TblArea){
            String rootId = "AR0";
            Node rootNode = new Node(rootId, "地域", "0", "1");
            rootNode.setPath(AR_DEPT + rootId);
            List<Node> childrenNodes = new ArrayList<>();
            rootNode.setChildren(childrenNodes);
            Map<String, Node> nodeMap = new HashMap<>();
            nodeMap.put(rootId, rootNode);
            for (Object obj : list) {
                TblArea tblArea = (TblArea) obj;
                Node node = new Node(tblArea.getAreaCode(), tblArea.getAreaName(), rootId, "2");
                node.setPath(rootNode.getPath() + "/" + tblArea.getAreaCode());
                childrenNodes.add(node);
                nodeMap.put(tblArea.getAreaCode(), node);
            }
            return new NodeTuple(rootNode, nodeMap);
        }

        return null;
    }

    public static Node createRootNode(){
        Node rootNode = new Node("0", "", "-1", "0");
        List<Node> nodes = new ArrayList<>();
        rootNode.setChildren(nodes);
        return rootNode;
    }
}
