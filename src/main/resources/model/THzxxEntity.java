package main.resources.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "t_hzxx", schema = "hospital", catalog = "")
public class THzxxEntity {
    private String hzbh;
    private String hzmc;
    private String pyzs;
    private String ksbh;
    private byte sfzj;
    private int ghrs;
    private BigDecimal ghfy;

    @Id
    @Column(name = "HZBH", nullable = false, length = 6)
    public String getHzbh() {
        return hzbh;
    }

    public void setHzbh(String hzbh) {
        this.hzbh = hzbh;
    }

    @Basic
    @Column(name = "HZMC", nullable = false, length = 12)
    public String getHzmc() {
        return hzmc;
    }

    public void setHzmc(String hzmc) {
        this.hzmc = hzmc;
    }

    @Basic
    @Column(name = "PYZS", nullable = false, length = 4)
    public String getPyzs() {
        return pyzs;
    }

    public void setPyzs(String pyzs) {
        this.pyzs = pyzs;
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
    @Column(name = "SFZJ", nullable = false)
    public byte getSfzj() {
        return sfzj;
    }

    public void setSfzj(byte sfzj) {
        this.sfzj = sfzj;
    }

    @Basic
    @Column(name = "GHRS", nullable = false)
    public int getGhrs() {
        return ghrs;
    }

    public void setGhrs(int ghrs) {
        this.ghrs = ghrs;
    }

    @Basic
    @Column(name = "GHFY", nullable = false, precision = 2)
    public BigDecimal getGhfy() {
        return ghfy;
    }

    public void setGhfy(BigDecimal ghfy) {
        this.ghfy = ghfy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        THzxxEntity that = (THzxxEntity) o;
        return sfzj == that.sfzj &&
                ghrs == that.ghrs &&
                Objects.equals(hzbh, that.hzbh) &&
                Objects.equals(hzmc, that.hzmc) &&
                Objects.equals(pyzs, that.pyzs) &&
                Objects.equals(ksbh, that.ksbh) &&
                Objects.equals(ghfy, that.ghfy);
    }

    @Override
    public int hashCode() {

        return Objects.hash(hzbh, hzmc, pyzs, ksbh, sfzj, ghrs, ghfy);
    }
}
