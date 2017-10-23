package developer.allef.smartmobi.smartmobii.Model;

import android.net.Uri;

/**
 * Created by Allef on 07/05/2017.
 */

public class Usuario {
    private String idUsuario;
    private String nome;
    private String email;
    private String senha;
    private String urlPhto;

    public Usuario() {

    }

    public Usuario(String idUsuario) {
    }

    public String getUrlPhto() {
        return urlPhto;
    }

    public void setUrlPhto(String urlPhto) {
        this.urlPhto = urlPhto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

}
