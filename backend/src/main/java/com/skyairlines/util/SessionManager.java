package com.skyairlines.util;

import com.skyairlines.model.entity.Usuario;
import com.skyairlines.model.enums.RolUsuario;

public class SessionManager {

    private static volatile SessionManager instance;
    private static final ThreadLocal<SessionData> sessionData = new ThreadLocal<>();

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }

    public void login(Usuario usuario) {
        SessionData data = new SessionData();
        data.usuarioId = usuario.getId();
        data.email = usuario.getEmail();
        data.rol = usuario.getRol();
        data.loggedIn = true;
        sessionData.set(data);
    }

    public void logout() {
        sessionData.remove();
    }

    public boolean isLoggedIn() {
        SessionData data = sessionData.get();
        return data != null && data.loggedIn;
    }

    public Integer getUsuarioId() {
        SessionData data = sessionData.get();
        return data != null ? data.usuarioId : null;
    }

    public String getEmail() {
        SessionData data = sessionData.get();
        return data != null ? data.email : null;
    }

    public String getNombre() {
        SessionData data = sessionData.get();
        return data != null ? data.nombre : null;
    }

    public String getApellido() {
        SessionData data = sessionData.get();
        return data != null ? data.apellido : null;
    }

    public RolUsuario getRol() {
        SessionData data = sessionData.get();
        return data != null ? data.rol : null;
    }

    public String getRolDisplay() {
        SessionData data = sessionData.get();
        if (data == null || data.rol == null) {
            return "Usuario Desconectado";
        }
        return "Usuario Conectado: " + data.rol.getDbValue();
    }

    private static class SessionData {
        Integer usuarioId;
        String email;
        String nombre;
        String apellido;
        RolUsuario rol;
        boolean loggedIn;
    }
}
