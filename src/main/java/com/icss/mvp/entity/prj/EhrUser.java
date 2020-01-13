package com.icss.mvp.entity.prj;

import com.icss.mvp.entity.common.BaseEntity;

/**
 * Created by Ray on 2018/12/5.
 */
public class EhrUser extends BaseEntity {

    /**
     * default serial version UID
     */
    private static final long serialVersionUID = 1L;

    private String            lastName;
    private String            lob;
    private String            du;
    private String            dd;

    private String            budu;

    private String            email;
    private String            lobNumber;
    private String            nationalIdentifier;
    private String            jobType;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLob() {
        return lob;
    }

    public void setLob(String lob) {
        this.lob = lob;
    }

    public String getDu() {
        return du;
    }

    public void setDu(String du) {
        this.du = du;
    }

    public String getDd() {
        return dd;
    }

    public void setDd(String dd) {
        this.dd = dd;
    }

    public String getBudu() {
        return budu;
    }

    public void setBudu(String budu) {
        this.budu = budu;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLobNumber() {
        return lobNumber;
    }

    public void setLobNumber(String lobNumber) {
        this.lobNumber = lobNumber;
    }

    public String getNationalIdentifier() {
        return nationalIdentifier;
    }

    public void setNationalIdentifier(String nationalIdentifier) {
        this.nationalIdentifier = nationalIdentifier;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }
}
