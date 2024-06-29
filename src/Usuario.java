import java.util.HashMap;
import java.util.Map;

public class Usuario {
    private int id;
    private String nome;
    private Map<String, Boolean> permissoes;

    // Construtor para inicializar um usuário com ID e nome
    public Usuario(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.permissoes = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Map<String, Boolean> getPermissoes() {
        return permissoes;
    }

    // Adiciona uma permissão ao usuário
    public void adicionarPermissao(String permissao, boolean valor) {
        permissoes.put(permissao, valor);
    }

    // Verifica se o usuário possui uma determinada permissão
    public boolean possuiPermissao(String permissao) {
        return permissoes.getOrDefault(permissao, false);
    }
}
