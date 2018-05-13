package main.resources.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "t_ghxx", schema = "hospital", catalog = "")
public class TGhxxEntity {
    private String ghbh;
    private String hzbh;
    private String ysbh;
    private String brbh;
    private int ghrc;
    private byte thbz;
    private BigDecimal ghfy;
    private Timestamp rqsj;

    @Id
    @Column(name = "GHBH", nullable = false, length = 6)
    public String getGhbh() {
        return ghbh;
    }

    public void setGhbh(String ghbh) {
        this.ghbh = ghbh;
    }

    @Basic
    @Column(name = "HZBH", nullable = false, length = 6)
    public String getHzbh() {
        return hzbh;
    }

    public void setHzbh(String hzbh) {
        this.hzbh = hzbh;
    }

    @Basic
    @Column(name = "YSBH", nullable = false, length = 6)
    public String getYsbh() {
        return ysbh;
    }

    public void setYsbh(String ysbh) {
        this.ysbh = ysbh;
    }

    @Basic
    @Column(name = "BRBH", nullable = false, length = 6)
    public String getBrbh() {
        return brbh;
    }

    public void setBrbh(String brbh) {
        this.brbh = brbh;
    }

    @Basic
    @Column(name = "GHRC", nullable = false)
    public int getGhrc() {
        return ghrc;
    }

    public void setGhrc(int ghrc) {
        this.ghrc = ghrc;
    }

    @Basic
    @Column(name = "THBZ", nullable = false)
    public byte getThbz() {
        return thbz;
    }

    public void setThbz(byte thbz) {
        this.thbz = thbz;
    }

    @Basic
    @Column(name = "GHFY", nullable = false, precision = 2)
    public BigDecimal getGhfy() {
        return ghfy;
    }

    public void setGhfy(BigDecimal ghfy) {
        this.ghfy = ghfy;
    }

    @Basic
    @Column(name = "RQSJ", nullable = false)
    public Timestamp getRqsj() {
        return rqsj;
    }

    public void setRqsj(Timestamp rqsj) {
        this.rqsj = rqsj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TGhxxEntity that = (TGhxxEntity) o;
        return ghrc == that.ghrc &&
                thbz == that.thbz &&
                Objects.equals(ghbh, that.ghbh) &&
                Objects.equals(hzbh, that.hzbh) &&
                Objects.equals(ysbh, that.ysbh) &&
                Objects.equals(brbh, that.brbh) &&
                Objects.equals(ghfy, that.ghfy) &&
                Objects.equals(rqsj, that.rqsj);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ghbh, hzbh, ysbh, brbh, ghrc, thbz, ghfy, rqsj);
    }
}
