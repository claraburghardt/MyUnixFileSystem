import java.util.HashMap;
import java.util.Map;

public class Diretorio {
    private static int proximoId = 1;
    private int id;
    private String nome;
    private Inode inode;
    private Map<String, Arquivo> arquivos;
    private Map<String, Diretorio> subdiretorios;
    private Diretorio parent;

    // Construtor principal para criar um diretório com um inode
    public Diretorio(String nome, Inode inode) {
        this.id = proximoId++;
        this.nome = nome;
        this.inode = inode;
        this.arquivos = new HashMap<>();
        this.subdiretorios = new HashMap<>();
    }

    public Diretorio(String nome, Inode inode, Diretorio parent) {
        this(nome, inode);
        this.parent = parent;
    }

    public static int getProximoId() {
		return proximoId;
	}

	public static void setProximoId(int proximoId) {
		Diretorio.proximoId = proximoId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Inode getInode() {
		return inode;
	}

	public void setInode(Inode inode) {
		this.inode = inode;
	}

	public Map<String, Arquivo> getArquivos() {
		return arquivos;
	}

	public void setArquivos(Map<String, Arquivo> arquivos) {
		this.arquivos = arquivos;
	}

	public Map<String, Diretorio> getSubdiretorios() {
		return subdiretorios;
	}

	public void setSubdiretorios(Map<String, Diretorio> subdiretorios) {
		this.subdiretorios = subdiretorios;
	}

	public Diretorio getParent() {
		return parent;
	}

	public void setParent(Diretorio parent) {
		this.parent = parent;
	}

	// Adiciona um arquivo ao diretório
    public boolean adicionarArquivo(Arquivo arquivo) {
        if (!arquivos.containsKey(arquivo.getNome())) {
            arquivos.put(arquivo.getNome(), arquivo);
            return true;
        }
        return false;
    }

    // Adiciona um subdiretório ao diretório
    public boolean adicionarSubdiretorio(Diretorio subdiretorio) {
        if (!subdiretorios.containsKey(subdiretorio.getNome())) {
            subdiretorios.put(subdiretorio.getNome(), subdiretorio);
            return true;
        }
        return false;
    }

    // Remove um arquivo do diretório
    public boolean removerArquivo(String nomeArquivo) {
        return arquivos.remove(nomeArquivo) != null;
    }

    // Remove um subdiretório do diretório
    public boolean removerSubdiretorio(String nomeSubdiretorio) {
        return subdiretorios.remove(nomeSubdiretorio) != null;
    }
}
