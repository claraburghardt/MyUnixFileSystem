import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class SistemaArquivosUNIX {
    // Mapa para armazenar usu�rios, diret�rio atual e usu�rio atual
    private Map<String, Usuario> usuarios;
    private Diretorio diretorioAtual;
    private Usuario usuarioAtual;
    
    // Construtor do sistema de arquivos
    public SistemaArquivosUNIX() {
        this.usuarios = new HashMap<>();
        this.diretorioAtual = null;
        this.usuarioAtual = null; 
        inicializarSistema();
    }
    
    public Map<String, Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Map<String, Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Diretorio getDiretorioAtual() {
		return diretorioAtual;
	}

	public void setDiretorioAtual(Diretorio diretorioAtual) {
		this.diretorioAtual = diretorioAtual;
	}

	public Usuario getUsuarioAtual() {
		return usuarioAtual;
	}

	public void setUsuarioAtual(Usuario usuarioAtual) {
		this.usuarioAtual = usuarioAtual;
	}

    // Inicializa o sistema de arquivos
	private void inicializarSistema() {
        formata();
        criarUsuarioAdministrador("admin");
        criarDiretorioHome("admin");
        this.usuarioAtual = usuarios.get("admin"); 
    }

    // Formata o sistema de arquivos
    public void formata() {
        diretorioAtual = new Diretorio("/", new Inode(1, 1, true)); 
        usuarios.clear();
        criarUsuarioAdministrador("admin");
        criarDiretorioHome("admin");
        System.out.println("Sistema de arquivos formatado.");
    }

    // Cria um novo arquivo
    public void touch(String nome) {
        if (usuarioAtual != null) {
            Inode inode = new Inode(1, usuarioAtual.getId(), false);
            Arquivo arquivo = new Arquivo(1, nome, inode);
            diretorioAtual.adicionarArquivo(arquivo);
            System.out.println("Arquivo criado: " + nome);
        } else {
            System.out.println("Usu�rio n�o autorizado");
        }
    }

    // Grava conte�do em um arquivo
    public void gravarConteudo(String nome, int posicao, String buffer) {
        if (usuarioAtual != null) {
            Arquivo arquivo = diretorioAtual.getArquivos().get(nome);
            if (arquivo != null) {
                Inode inode = arquivo.getInode();
                if (usuarioAtual.getId() == inode.getProprietarioID()) {
                    if (inode.getPermissoes().get("user")) {
                    	arquivo.setContent(buffer);
                    	int novotamanho = arquivo.getInode().getTamanhoArquivo() + posicao;
                    	arquivo.getInode().setTamanhoArquivo(novotamanho);
                        System.out.println("Conte�do gravado no arquivo: " + buffer);
                    } else {
                        System.out.println("Permiss�o negada para grava��o no arquivo");
                    }
                } else {
                    System.out.println("Voc� n�o � o propriet�rio do arquivo");
                }
            } else {
                System.out.println("Arquivo n�o encontrado");
            }
        } else {
            System.out.println("Usu�rio n�o autorizado");
        }
    }
    
    // Imprime o conte�do de um arquivo
    public void cat(String nome) {
        if (usuarioAtual != null) {
            Arquivo arquivo = diretorioAtual.getArquivos().get(nome);
            if (arquivo != null) {
                Inode inode = arquivo.getInode();
                if (usuarioAtual.getId() == inode.getProprietarioID()) {
                    if (inode.getPermissoes().get("user")) {
                        String conteudo = arquivo.getContent();
                        System.out.println("Conte�do do arquivo " + nome + ": " + conteudo);
                    } else {
                        System.out.println("Permiss�o negada para leitura do arquivo");
                    }
                } else {
                    System.out.println("Voc� n�o � o propriet�rio do arquivo");
                }
            } else {
                System.out.println("Arquivo n�o encontrado");
            }
        } else {
            System.out.println("Usu�rio n�o autorizado");
        }
    }

    // Remove um arquivo
    public void rm(String nome) {
        if (usuarioAtual != null) {
            Arquivo arquivo = diretorioAtual.getArquivos().get(nome);
            if (arquivo != null) {
                Inode inode = arquivo.getInode();
                if (usuarioAtual.getId() == inode.getProprietarioID()) {
                    if (inode.getPermissoes().get("user")) {
                        diretorioAtual.removerArquivo(nome);
                        System.out.println("Arquivo removido: " + nome);
                    } else {
                        System.out.println("Permiss�o negada para remo��o do arquivo");
                    }
                } else {
                    System.out.println("Voc� n�o � o propriet�rio do arquivo");
                }
            } else {
                System.out.println("Arquivo n�o encontrado");
            }
        } else {
            System.out.println("Usu�rio n�o autorizado");
        }
    }

    // Altera propriet�rio do arquivo
    public void chown(String user1, String user2, String arquivo) {
        if (usuarioAtual != null) {
            Arquivo arquivoObj = diretorioAtual.getArquivos().get(arquivo);
            if (arquivoObj != null) {
                Inode inode = arquivoObj.getInode();
                if (usuarioAtual.getId() == inode.getProprietarioID()) {
                    if (usuarioAtual.possuiPermissao("chown")) {
                        if (usuarios.containsKey(user2)) {
                            inode.setProprietarioID(usuarios.get(user2).getId());
                            System.out.println("Propriet�rio do arquivo alterado para: " + user2);
                        } else {
                            System.out.println("Usu�rio " + user2 + " n�o encontrado");
                        }
                    } else {
                        System.out.println("Permiss�o negada. Voc� n�o possui permiss�o para alterar o propriet�rio do arquivo.");
                    }
                } else {
                    System.out.println("Voc� n�o � o propriet�rio do arquivo");
                }
            } else {
                System.out.println("Arquivo n�o encontrado");
            }
        } else {
            System.out.println("Usu�rio n�o autorizado");
        }
    }

    // Altera permiss�es de arquivo
    public void chmod(String arquivo, String flags) {
        if (usuarioAtual != null) {
            Arquivo arquivoObj = diretorioAtual.getArquivos().get(arquivo);
            if (arquivoObj != null) {
                Inode inode = arquivoObj.getInode();
                if (usuarioAtual.getId() == inode.getProprietarioID()) {
                    if (usuarioAtual.possuiPermissao("chmod")) {
                        if (inode.getPermissoes().get("user")) {
                            atualizarPermissoes(inode, flags);
                            System.out.println("Permiss�es do arquivo alteradas para: " + flags);
                        } else {
                            System.out.println("Permiss�o negada para modifica��o de permiss�es do arquivo");
                        }
                    } else {
                        System.out.println("Permiss�o negada. Voc� n�o possui permiss�o para modificar as permiss�es do arquivo.");
                    }
                } else {
                    System.out.println("Voc� n�o � o propriet�rio do arquivo");
                }
            } else {
                System.out.println("Arquivo n�o encontrado");
            }
        } else {
            System.out.println("Usu�rio n�o autorizado");
        }
    }

    // Atualiza permiss�es
    private void atualizarPermissoes(Inode inode, String flags) {
        for (char flag : flags.toCharArray()) {
            switch (flag) {
                case 'r':
                    inode.getPermissoes().put("read", true);
                    break;
                case 'w':
                    inode.getPermissoes().put("write", true);
                    break;
                case 'x':
                    inode.getPermissoes().put("execute", true);
                    break;
                default:
                    System.out.println("Flag de permiss�o inv�lida: " + flag);
            }
        }
    }

    // Cria novo diret�rio
    public void mkdir(String diretorio) {
        if (usuarioAtual != null) {
            Inode inode = new Inode(1, usuarioAtual.getId(), true); 
            Diretorio novoDiretorio = new Diretorio(diretorio, inode);
            novoDiretorio.setParent(diretorioAtual); 
            diretorioAtual.adicionarSubdiretorio(novoDiretorio);
            System.out.println("Diret�rio criado: " + diretorio);
        } else {
            System.out.println("Usu�rio n�o autorizado");
        }
    }

    // Remove diret�rio
    public void rmdir(String nome) {
        if (usuarioAtual != null) {
            Diretorio diretorio = diretorioAtual.getSubdiretorios().get(nome);
            if (diretorio != null) {
                Inode inode = diretorio.getInode();
                if (usuarioAtual.getId() == inode.getProprietarioID()) {
                    if (inode.getPermissoes().get("user")) {
                        diretorioAtual.removerSubdiretorio(nome);
                        System.out.println("Diret�rio removido: " + nome);
                    } else {
                        System.out.println("Permiss�o negada para remo��o do diret�rio");
                    }
                } else {
                    System.out.println("Voc� n�o � o propriet�rio do diret�rio");
                }
            } else {
                System.out.println("Diret�rio n�o encontrado");
            }
        } else {
            System.out.println("Usu�rio n�o autorizado");
        }
    }

    // Muda de diret�rio
    public void cd(String diretorio) {
        if (usuarioAtual != null) {
            Diretorio novoDiretorio;
            if (diretorio.equals("..")) {
                novoDiretorio = diretorioAtual.getParent();
                if (novoDiretorio == null) {
                    System.out.println("J� est� no diret�rio raiz");
                    return;
                }
            } else {
                Diretorio subdiretorio = diretorioAtual.getSubdiretorios().get(diretorio);
                if (subdiretorio != null) {
                    novoDiretorio = subdiretorio;
                } else {
                    novoDiretorio = diretorioAtual.getSubdiretorios().get("home");
                    if (novoDiretorio == null) {
                        System.out.println("Diret�rio n�o encontrado");
                        return;
                    }
                }
            }
            diretorioAtual = novoDiretorio;
            System.out.println("Diret�rio atual alterado para: " + diretorioAtual.getNome());
        } else {
            System.out.println("Usu�rio n�o autorizado");
        }
    }

 // Lista arquivos de diret�rio com detalhes de inode
    public void ls(String diretorio) {
        if (usuarioAtual != null) {
            Diretorio diretorioAtualListagem;
            if (diretorio == null || diretorio.isEmpty()) {
                diretorioAtualListagem = diretorioAtual;
            } else {
                Diretorio subdiretorio = diretorioAtual.getSubdiretorios().get(diretorio);
                if (subdiretorio != null) {
                    diretorioAtualListagem = subdiretorio;
                } else {
                    System.out.println("Diret�rio n�o encontrado");
                    return;
                }
            }
            System.out.println("Conte�do do diret�rio " + diretorioAtualListagem.getNome() + ":");

            // Listar arquivos com detalhes de inode
            for (Map.Entry<String, Arquivo> entry : diretorioAtualListagem.getArquivos().entrySet()) {
                Arquivo arquivo = entry.getValue();
                Inode inode = arquivo.getInode();
                System.out.println("Arquivo: " + entry.getKey() +
                        ", Propriet�rio: " + inode.getProprietarioID() +
                        ", Tamanho: " + inode.getTamanhoArquivo() + " Bytes" +
                        ", Inode: " + inode.toString() +
                        ", Criado em: " + inode.getDataCriacao());
            }

            // Listar subdiret�rios
            for (Map.Entry<String, Diretorio> entry : diretorioAtualListagem.getSubdiretorios().entrySet()) {
                Diretorio dir = entry.getValue();
                Inode inode = dir.getInode();
            	System.out.println("Diret�rio: " + entry.getKey() +
                ", Propriet�rio: " + inode.getProprietarioID() +
                ", Inode: " + inode.toString() +
                ", Criado em: " + inode.getDataCriacao());;
            }
        } else {
            System.out.println("Usu�rio n�o autorizado");
        }
    }

    // Adiciona novo usu�rio
    public void adduser(String nome, int id) {
        if (usuarioAtual != null) {
            Usuario admin = usuarioAtual;
            if (admin.possuiPermissao("admin")) {
                if (!usuarios.containsKey(nome)) {
                    Usuario novoUsuario = new Usuario(id, nome);
                    usuarios.put(nome, novoUsuario);
                    System.out.println("Usu�rio adicionado: " + nome);
                } else {
                    System.out.println("Nome de usu�rio j� existe");
                }
            } else {
                System.out.println("Permiss�o negada. Apenas administradores podem adicionar usu�rios.");
            }
        } else {
            System.out.println("Usu�rio n�o autorizado");
        }
    }

    // Remove usu�rio
    public void rmuser(String nome) {
        if (usuarioAtual != null) {
            Usuario admin = usuarioAtual;
            if (admin.possuiPermissao("admin")) {
                if (usuarios.containsKey(nome)) {
                    usuarios.remove(nome);
                    System.out.println("Usu�rio removido: " + nome);
                } else {
                    System.out.println("Usu�rio n�o encontrado");
                }
            } else {
                System.out.println("Permiss�o negada. Apenas administradores podem remover usu�rios.");
            }
        } else {
            System.out.println("Usu�rio n�o autorizado");
        }
    }

    // Lista usu�rios existentes
    public void lsuser() {
        if (usuarioAtual != null) {
            Usuario admin = usuarioAtual;
            if (admin.possuiPermissao("admin")) {
                System.out.println("Lista de usu�rios:");

                for (Map.Entry<String, Usuario> entry : usuarios.entrySet()) {
                    System.out.println("Nome: " + entry.getKey() + ", ID: " + entry.getValue().getId());
                }
            } else {
                System.out.println("Permiss�o negada. Apenas administradores podem listar usu�rios.");
            }
        } else {
            System.out.println("Usu�rio n�o autorizado");
        }
    }
    
    // Troca de usu�rio    
    public void su(String nomeUsuario) {
        if (usuarios.containsKey(nomeUsuario)) {
            usuarioAtual = usuarios.get(nomeUsuario);
            System.out.println("Troca de usu�rio para: " + nomeUsuario);
        } else {
            System.out.println("Usu�rio n�o encontrado");
        }
    }
    
    // Exibe as datas referentes ao arquivo
    public void exibirInformacoesArquivo(String nome) {
        if (usuarioAtual != null) {
            Arquivo arquivo = diretorioAtual.getArquivos().get(nome);
            if (arquivo != null) {
                Inode inode = arquivo.getInode();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                System.out.println("Informa��es sobre o arquivo " + nome + ":");
                System.out.println("Criado em: " + sdf.format(inode.getDataCriacao()));
                System.out.println("�ltima modifica��o em: " + sdf.format(inode.getDataUltimaAtualizacao()));
                System.out.println("�ltimo acesso em: " + sdf.format(inode.getDataUltimoAcesso()));
            } else {
                System.out.println("Arquivo n�o encontrado");
            }
        } else {
            System.out.println("Usu�rio n�o autorizado");
        }
    }

    // Cria usu�rio admin
    private void criarUsuarioAdministrador(String nome) {
        Usuario admin = new Usuario(1, nome);
        admin.adicionarPermissao("admin", true);
        admin.adicionarPermissao("read", true);  
        admin.adicionarPermissao("write", true); 
        admin.adicionarPermissao("execute", true);
        admin.adicionarPermissao("chown", true);
        admin.adicionarPermissao("chmod", true);
        usuarios.put(nome, admin);
        usuarioAtual = admin;
    }

    // Cria diret�rio home para o usu�rio
    private void criarDiretorioHome(String usuario) {
        Inode inode = new Inode(1, usuarios.get(usuario).getId(), true);
        Diretorio home = new Diretorio("home", inode);
        home.setParent(diretorioAtual);
        diretorioAtual.adicionarSubdiretorio(home);
        diretorioAtual = home;
    }
}