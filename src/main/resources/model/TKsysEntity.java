package main.resources.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "t_ksys", schema = "hospital", catalog = "")
public class TKsysEntity {
    private String ysbh;
    private String ksbh;
    private String ysmc;
    private String ptzs;
    private String dlkl;
    private byte sfzj;
    private Timestamp dlrq;

    @Id
    @Column(name = "YSBH", nullable = false, length = 6)
    public String getYsbh() {
        return ysbh;
    }

    public void setYsbh(String ysbh) {
        this.ysbh = ysbh;
    }

    @Basic
    @Column(name = "KSBH", nullable = false, length = 6)
    public String getKsbh() {
        return ksbh;
    }

    public void setKsbh(String ksbh) {
        this.ksbh = ksbh;
    }

    @Basic
    @Column(name = "YSMC", nullable = false, length = 10)
    public String getYsmc() {
        return ysmc;
    }

    public void setYsmc(String ysmc) {
        this.ysmc = ysmc;
    }

    @Basic
    @Column(name = "PTZS", nullable = false, length = 4)
    public String getPtzs() {
        return ptzs;
    }

    public void setPtzs(String ptzs) {
        this.ptzs = ptzs;
    }

    @Basic
    @Column(name = "DLKL", nullable = false, length = 8)
    public String getDlkl() {
        return dlkl;
    }

    public void setDlkl(String dlkl) {
        this.dlkl = dlkl;
    }

    @Basic
    @Column(name = "SFZJ", nullable = false)
    public byte getSfzj() {
        return sfzj;
    }

    public void setSfzj(byte sfzj) {
        this.sfzj = sfzj;
    }

    @Basic
    @Column(name = "DLRQ", nullable = true)
    public Timestamp getDlrq() {
        return dlrq;
    }

    public void setDlrq(Timestamp dlrq) {
        this.dlrq = dlrq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TKsysEntity that = (TKsysEntity) o;
        return sfzj == that.sfzj &&
                Objects.equals(ysbh, that.ysbh) &&
                Objects.equals(ksbh, that.ksbh) &&
                Objects.equals(ysmc, that.ysmc) &&
                Objects.equals(ptzs, that.ptzs) &&
                Objects.equals(dlkl, that.dlkl) &&
                Objects.equals(dlrq, that.dlrq);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ysbh, ksbh, ysmc, ptzs, dlkl, sfzj, dlrq);
    }
}
