import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Inode {
    private int inodeID;
    private int proprietarioID;
    private Date dataUltimaAtualizacao;
    private Date dataUltimoAcesso;
    private Date dataCriacao;
    private Map<String, Boolean> permissoes; 
    private boolean isDiretorio;
    private int tamanhoArquivo;

    public Inode(int inodeID, int proprietarioID, boolean isDiretorio) {
        this.inodeID = inodeID;
        this.proprietarioID = proprietarioID;
        this.dataCriacao = new Date();
        this.dataUltimaAtualizacao = dataCriacao;
        this.dataUltimoAcesso = dataCriacao;
        this.isDiretorio = isDiretorio;
        this.tamanhoArquivo = 0;

        // Inicializa as permissões padrão
        this.permissoes = new HashMap<>();
        this.permissoes.put("user", true);
        this.permissoes.put("group", true);
        this.permissoes.put("others", true);
    }

    public int getInodeID() {
		return inodeID;
	}

	public void setInodeID(int inodeID) {
		this.inodeID = inodeID;
	}

	public int getProprietarioID() {
		return proprietarioID;
	}

	public void setProprietarioID(int proprietarioID) {
		this.proprietarioID = proprietarioID;
	}

	public Date getDataUltimaAtualizacao() {
		return dataUltimaAtualizacao;
	}

	public void setDataUltimaAtualizacao(Date dataUltimaAtualizacao) {
		this.dataUltimaAtualizacao = dataUltimaAtualizacao;
	}

	public Date getDataUltimoAcesso() {
		return dataUltimoAcesso;
	}

	public void setDataUltimoAcesso(Date dataUltimoAcesso) {
		this.dataUltimoAcesso = dataUltimoAcesso;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public void setPermissoes(Map<String, Boolean> permissoes) {
		this.permissoes = permissoes;
	}

	public void setDiretorio(boolean isDiretorio) {
		this.isDiretorio = isDiretorio;
	}

	public Map<String, Boolean> getPermissoes() {
        return permissoes;
    }

    public boolean isDiretorio() {
        return isDiretorio;
    }

	public int getTamanhoArquivo() {
		return tamanhoArquivo;
	}

	public void setTamanhoArquivo(int tamanhoArquivo) {
		this.tamanhoArquivo = tamanhoArquivo;
	}
}