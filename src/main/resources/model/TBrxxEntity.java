package main.resources.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "t_brxx", schema = "hospital", catalog = "")
public class TBrxxEntity {
    private String brbh;
    private String brmc;
    private String dlkl;
    private BigDecimal ycje;
    private Timestamp dlrq;

    @Id
    @Column(name = "BRBH", nullable = false, length = 6)
    public String getBrbh() {
        return brbh;
    }

    public void setBrbh(String brbh) {
        this.brbh = brbh;
    }

    @Basic
    @Column(name = "BRMC", nullable = false, length = 10)
    public String getBrmc() {
        return brmc;
    }

    public void setBrmc(String brmc) {
        this.brmc = brmc;
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
    @Column(name = "YCJE", nullable = false, precision = 2)
    public BigDecimal getYcje() {
        return ycje;
    }

    public void setYcje(BigDecimal ycje) {
        this.ycje = ycje;
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
        TBrxxEntity that = (TBrxxEntity) o;
        return Objects.equals(brbh, that.brbh) &&
                Objects.equals(brmc, that.brmc) &&
                Objects.equals(dlkl, that.dlkl) &&
                Objects.equals(ycje, that.ycje) &&
                Objects.equals(dlrq, that.dlrq);
    }

    @Override
    public int hashCode() {

        return Objects.hash(brbh, brmc, dlkl, ycje, dlrq);
    }
}
