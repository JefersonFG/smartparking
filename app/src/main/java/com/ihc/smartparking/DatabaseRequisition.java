package com.ihc.smartparking;

public class DatabaseRequisition {

    public static boolean user_exist(String username) {
        // retornar um booleano se existe usuario com username no banco de dados.
        return false;
    }

    public static boolean is_valid_user(String username, String password) {
        // retornar booleano se existe usuario com username e senha no banco de dados.
        return true;
    }

    public static boolean isencao(String username, String codigo_isencao) {
        // retornar booleano dizendo se deu para fazer a isencao do usuario com o codigo.
        return true;
    }

    public static boolean new_user(String email, String username, String password, String conta) {
        // retornar booleano dizendo se foi possivel registrar o usuario no banco de dados.
        return true;
    }

}
