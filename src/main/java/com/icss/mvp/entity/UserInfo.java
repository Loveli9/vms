package com.icss.mvp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

/**
 * 
 * <pre>
 * <b>描述：用户实体</b>
 * <b>任务编号：</b>
 * <b>作者：张鹏飞</b> 
 * <b>创建日期： 2017年5月12日 下午2:57:58</b>
 * </pre>
 */
public class UserInfo
{
    private String USERID;
    
    private String USERCODE;
    
    private String USERNAME;
    
    private String PASSWORD;
    
    private int ISONLINE;
    private String auth;
    private int ISDEL;
    private String permissionids;
    private String permissionNames;
    private String bu;
    private String du;
    private String dept;
    private String hwpdu;
    private String hwzpdu;
    private String pduspdt;
    private String buVal;
    private String duVal;
    private String deptVal;
    private String hwpduVal;
    private String hwzpduVal;
    private String pduspdtVal;
    private String roleId;
    private String roleName;
    private String roleLevel;
    private List<String> menus;
    private String currentroleId;
    private String currentroleName;
    private String currentroleLevel;
    private String currentperId;
    private String currentperName;
    private String currentperLevel;
    private String CREATER;
    
    private String CREATERNAME;

    private Date CREATERTIME;
    
    private String UPDATER;
    
    private String UPDATERNAME;
    
    private Date UPDATERTIME;
    
    private int IDENTITY;// 用户身份 1=root 2=admin 3=user
    private String usertype;// 用户类型 1=中软 2=华为

    private int isDeleted;
    
	public int getIsDeleted() {
		return isDeleted;
	}
	
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
    
    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getBuVal() {
		return buVal;
	}

	public void setBuVal(String buVal) {
		this.buVal = buVal;
	}

	public String getDuVal() {
		return duVal;
	}

	public void setDuVal(String duVal) {
		this.duVal = duVal;
	}

	public String getDeptVal() {
		return deptVal;
	}

	public void setDeptVal(String deptVal) {
		this.deptVal = deptVal;
	}

	public String getHwpduVal() {
		return hwpduVal;
	}

	public void setHwpduVal(String hwpduVal) {
		this.hwpduVal = hwpduVal;
	}

	public String getHwzpduVal() {
		return hwzpduVal;
	}

	public void setHwzpduVal(String hwzpduVal) {
		this.hwzpduVal = hwzpduVal;
	}

	public String getPduspdtVal() {
		return pduspdtVal;
	}

	public void setPduspdtVal(String pduspdtVal) {
		this.pduspdtVal = pduspdtVal;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getCurrentperId() {
		return currentperId;
	}

	public void setCurrentperId(String currentperId) {
		this.currentperId = currentperId;
	}

	public String getCurrentperName() {
		return currentperName;
	}

	public void setCurrentperName(String currentperName) {
		this.currentperName = currentperName;
	}

	public String getCurrentperLevel() {
		return currentperLevel;
	}

	public void setCurrentperLevel(String currentperLevel) {
		this.currentperLevel = currentperLevel;
	}

	public String getPermissionNames() {
		return permissionNames;
	}

	public void setPermissionNames(String permissionNames) {
		this.permissionNames = permissionNames;
	}

	public String getPermissionids() {
		return permissionids;
	}

	public void setPermissionids(String permissionids) {
		this.permissionids = permissionids;
	}

	public String getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(String roleLevel) {
		this.roleLevel = roleLevel;
	}

	public String getCurrentroleLevel() {
		return currentroleLevel;
	}

	public void setCurrentroleLevel(String currentroleLevel) {
		this.currentroleLevel = currentroleLevel;
	}

	public List<String> getMenus() {
		return menus;
	}

	public void setMenus(List<String> menus) {
		this.menus = menus;
	}

	public String getCurrentroleId() {
		return currentroleId;
	}

	public void setCurrentroleId(String currentroleId) {
		this.currentroleId = currentroleId;
	}

	public String getCurrentroleName() {
		return currentroleName;
	}

	public void setCurrentroleName(String currentroleName) {
		this.currentroleName = currentroleName;
	}

	public String getUSERID()
    {
        return USERID;
    }
    
    public void setUSERID(String uSERID)
    {
        USERID = uSERID;
    }
    
    public String getUSERCODE()
    {
        return USERCODE;
    }
    
    public void setUSERCODE(String uSERCODE)
    {
        USERCODE = uSERCODE;
    }
    
    public String getUSERNAME()
    {
        return USERNAME;
    }
    
    public void setUSERNAME(String uSERNAME)
    {
        USERNAME = uSERNAME;
    }
    
    public String getPASSWORD()
    {
        return PASSWORD;
    }
    
    public void setPASSWORD(String pASSWORD)
    {
        PASSWORD = pASSWORD;
    }
    
    public int getISONLINE()
    {
        return ISONLINE;
    }
    
    public void setISONLINE(int iSONLINE)
    {
        ISONLINE = iSONLINE;
    }
    
    public int getISDEL()
    {
        return ISDEL;
    }
    
    public void setISDEL(int iSDEL)
    {
        ISDEL = iSDEL;
    }
    
    public String getCREATER()
    {
        return CREATER;
    }
    
    public void setCREATER(String cREATER)
    {
        CREATER = cREATER;
    }
    
    public String getCREATERNAME()
    {
        return CREATERNAME;
    }
    
    public void setCREATERNAME(String cREATERNAME)
    {
        CREATERNAME = cREATERNAME;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getCREATERTIME()
    {
    	CREATERTIME = CREATERTIME==null?new Date():CREATERTIME;
        return CREATERTIME;
    }
    
    public void setCREATERTIME(Date cREATERTIME)
    {
        CREATERTIME = cREATERTIME;
    }
    
    public String getUPDATER()
    {
        return UPDATER;
    }
    
    public void setUPDATER(String uPDATER)
    {
        UPDATER = uPDATER;
    }
    
    public String getUPDATERNAME()
    {
        return UPDATERNAME;
    }
    
    public void setUPDATERNAME(String uPDATERNAME)
    {
        UPDATERNAME = uPDATERNAME;
    }
    
    public Date getUPDATERTIME()
    {
    	UPDATERTIME = UPDATERTIME==null?new Date():UPDATERTIME;
        return UPDATERTIME;
    }
    
    public void setUPDATERTIME(Date uPDATERTIME)
    {
        UPDATERTIME = uPDATERTIME;
    }
    
    public int getIDENTITY()
    {
        return IDENTITY;
    }
    
    public void setIDENTITY(int iDENTITY)
    {
        IDENTITY = iDENTITY;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getBu() {
        return bu;
    }

    public void setBu(String bu) {
        this.bu = bu;
    }

    public String getDu() {
        return du;
    }

    public void setDu(String du) {
        this.du = du;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getHwpdu() {
        return hwpdu;
    }

    public void setHwpdu(String hwpdu) {
        this.hwpdu = hwpdu;
    }

    @Override
	public String toString() {
		return "UserInfo [USERID=" + USERID + ", USERCODE=" + USERCODE + ", USERNAME=" + USERNAME + ", PASSWORD="
				+ PASSWORD + ", ISONLINE=" + ISONLINE + ", auth=" + auth + ", ISDEL=" + ISDEL + ", permissionids="
				+ permissionids + ", permissionNames=" + permissionNames + ", bu=" + bu + ", du=" + du + ", dept="
				+ dept + ", hwpdu=" + hwpdu + ", hwzpdu=" + hwzpdu + ", pduspdt=" + pduspdt + ", buVal=" + buVal
				+ ", duVal=" + duVal + ", deptVal=" + deptVal + ", hwpduVal=" + hwpduVal + ", hwzpduVal=" + hwzpduVal
				+ ", pduspdtVal=" + pduspdtVal + ", roleId=" + roleId + ", roleName=" + roleName + ", roleLevel="
				+ roleLevel + ", menus=" + menus + ", currentroleId=" + currentroleId + ", currentroleName="
				+ currentroleName + ", currentroleLevel=" + currentroleLevel + ", currentperId=" + currentperId
				+ ", currentperName=" + currentperName + ", currentperLevel=" + currentperLevel + ", CREATER=" + CREATER
				+ ", CREATERNAME=" + CREATERNAME + ", CREATERTIME=" + CREATERTIME + ", UPDATER=" + UPDATER
				+ ", UPDATERNAME=" + UPDATERNAME + ", UPDATERTIME=" + UPDATERTIME + ", IDENTITY=" + IDENTITY +",usertype="+ usertype + "]";
	}

	public String getHwzpdu() {
        return hwzpdu;
    }

    public void setHwzpdu(String hwzpdu) {
        this.hwzpdu = hwzpdu;
    }

    public String getPduspdt() {
        return pduspdt;
    }

    public void setPduspdt(String pduspdt) {
        this.pduspdt = pduspdt;
    }
}
