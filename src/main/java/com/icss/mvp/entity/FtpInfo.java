package com.icss.mvp.entity;

public class FtpInfo {

    private String ipAddr;//ip地址
    
    private Integer port;//端口号
    
    private String userName;//用户名
    
    private String pwd;//密码
    
    private String path;
    
    private String bbName;//版本构建名称
    
    private String gcName;//版工程能力名称
    
    private String no;
    
    private String token;
    
    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getBbName() {
		return bbName;
	}

	public void setBbName(String bbName) {
		this.bbName = bbName;
	}

	public String getGcName() {
		return gcName;
	}

	public void setGcName(String gcName) {
		this.gcName = gcName;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
    

}