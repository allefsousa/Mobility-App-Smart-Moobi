package developer.allef.smartmobi.smartmobii.Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

/**
 * Created by allef on 07/10/2017.
 */

public class Feed {

    private String userId;
    private String photoperfil;
    private String dataPost;
    private String horaPost;
    private String nomeUserPost;
    private String legenda;
    private String emailUserPost;
    private String imagem;
    private int contadorLikes;
    private int contadorComentarios;
    private Long createdAt;



    public Feed() {
    }

    public String getDataPost() {
        return dataPost;
    }

    public void setDataPost(String dataPost) {
        this.dataPost = dataPost;
    }

    public String getHoraPost() {
        return horaPost;
    }

    public void setHoraPost(String horaPost) {
        this.horaPost = horaPost;
    }

    public String getLegenda() {
        return legenda;
    }

    public void setLegenda(String legenda) {
        this.legenda = legenda;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhotoperfil() {
        return photoperfil;
    }

    public void setPhotoperfil(String photoperfil) {
        this.photoperfil = photoperfil;
    }

    public String getNomeUserPost() {
        return nomeUserPost;
    }

    public void setNomeUserPost(String nomeUserPost) {
        this.nomeUserPost = nomeUserPost;
    }

    public String getEmailUserPost() {
        return emailUserPost;
    }

    public void setEmailUserPost(String emailUserPost) {
        this.emailUserPost = emailUserPost;
    }


    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public int getContadorLikes() {
        return contadorLikes;
    }

    public void setContadorLikes(int contadorLikes) {
        this.contadorLikes = contadorLikes;
    }

    public int getContadorComentarios() {
        return contadorComentarios;
    }

    public void setContadorComentarios(int contadorComentarios) {
        this.contadorComentarios = contadorComentarios;
    }


    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}

