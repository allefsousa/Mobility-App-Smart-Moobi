package developer.allef.smartmobi.smartmobii.Model;

import java.io.Serializable;

/**
 * Created by Allef on 05/04/2017.
 */

public class LocalVaga implements Serializable {

    public static final long  serialVersionUID = 100L;
    private Double latitude;
    private String nomeRua;
    private Double longitude;
    private String id;
    private int tipoVaga;
    private String statusVaga;



    public LocalVaga() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTipoVaga() {
        return tipoVaga;
    }

    public void setTipoVaga(int tipoVaga) {
        this.tipoVaga = tipoVaga;
    }

    public String getNomeRua() {
        return nomeRua;
    }

    public void setNomeRua(String nomeRua) {
        this.nomeRua = nomeRua;
    }
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStatusVaga() {
        return statusVaga;
    }

    public void setStatusVaga(String statusVaga) {
        this.statusVaga = statusVaga;
    }


    @Override
    public String toString() {
        return "LocalVaga{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
