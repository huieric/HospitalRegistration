package main.resources.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "t_ksxx", schema = "hospital", catalog = "")
public class TKsxxEntity {
    private String ksbh;
    private String ksmc;
    private String pyzs;

    @Id
    @Column(name = "KSBH", nullable = false, length = 6)
    public String getKsbh() {
        return ksbh;
    }

    public void setKsbh(String ksbh) {
        this.ksbh = ksbh;
    }

    @Basic
    @Column(name = "KSMC", nullable = false, length = 10)
    public String getKsmc() {
        return ksmc;
    }

    public void setKsmc(String ksmc) {
        this.ksmc = ksmc;
    }

    @Basic
    @Column(name = "PYZS", nullable = false, length = 8)
    public String getPyzs() {
        return pyzs;
    }

    public void setPyzs(String pyzs) {
        this.pyzs = pyzs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TKsxxEntity that = (TKsxxEntity) o;
        return Objects.equals(ksbh, that.ksbh) &&
                Objects.equals(ksmc, that.ksmc) &&
                Objects.equals(pyzs, that.pyzs);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ksbh, ksmc, pyzs);
    }
}
