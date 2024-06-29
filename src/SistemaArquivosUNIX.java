import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class SistemaArquivosUNIX {
    // Mapa para armazenar usuários, diretório atual e usuário atual
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
            System.out.println("Usuário não autorizado");
        }
    }

    // Grava conteúdo em um arquivo
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
                        System.out.println("Conteúdo gravado no arquivo: " + buffer);
                    } else {
                        System.out.println("Permissão negada para gravação no arquivo");
                    }
                } else {
                    System.out.println("Você não é o proprietário do arquivo");
                }
            } else {
                System.out.println("Arquivo não encontrado");
            }
        } else {
            System.out.println("Usuário não autorizado");
        }
    }
    
    // Imprime o conteúdo de um arquivo
    public void cat(String nome) {
        if (usuarioAtual != null) {
            Arquivo arquivo = diretorioAtual.getArquivos().get(nome);
            if (arquivo != null) {
                Inode inode = arquivo.getInode();
                if (usuarioAtual.getId() == inode.getProprietarioID()) {
                    if (inode.getPermissoes().get("user")) {
                        String conteudo = arquivo.getContent();
                        System.out.println("Conteúdo do arquivo " + nome + ": " + conteudo);
                    } else {
                        System.out.println("Permissão negada para leitura do arquivo");
                    }
                } else {
                    System.out.println("Você não é o proprietário do arquivo");
                }
            } else {
                System.out.println("Arquivo não encontrado");
            }
        } else {
            System.out.println("Usuário não autorizado");
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
                        System.out.println("Permissão negada para remoção do arquivo");
                    }
                } else {
                    System.out.println("Você não é o proprietário do arquivo");
                }
            } else {
                System.out.println("Arquivo não encontrado");
            }
        } else {
            System.out.println("Usuário não autorizado");
        }
    }

    // Altera proprietário do arquivo
    public void chown(String user1, String user2, String arquivo) {
        if (usuarioAtual != null) {
            Arquivo arquivoObj = diretorioAtual.getArquivos().get(arquivo);
            if (arquivoObj != null) {
                Inode inode = arquivoObj.getInode();
                if (usuarioAtual.getId() == inode.getProprietarioID()) {
                    if (usuarioAtual.possuiPermissao("chown")) {
                        if (usuarios.containsKey(user2)) {
                            inode.setProprietarioID(usuarios.get(user2).getId());
                            System.out.println("Proprietário do arquivo alterado para: " + user2);
                        } else {
                            System.out.println("Usuário " + user2 + " não encontrado");
                        }
                    } else {
                        System.out.println("Permissão negada. Você não possui permissão para alterar o proprietário do arquivo.");
                    }
                } else {
                    System.out.println("Você não é o proprietário do arquivo");
                }
            } else {
                System.out.println("Arquivo não encontrado");
            }
        } else {
            System.out.println("Usuário não autorizado");
        }
    }

    // Altera permissões de arquivo
    public void chmod(String arquivo, String flags) {
        if (usuarioAtual != null) {
            Arquivo arquivoObj = diretorioAtual.getArquivos().get(arquivo);
            if (arquivoObj != null) {
                Inode inode = arquivoObj.getInode();
                if (usuarioAtual.getId() == inode.getProprietarioID()) {
                    if (usuarioAtual.possuiPermissao("chmod")) {
                        if (inode.getPermissoes().get("user")) {
                            atualizarPermissoes(inode, flags);
                            System.out.println("Permissões do arquivo alteradas para: " + flags);
                        } else {
                            System.out.println("Permissão negada para modificação de permissões do arquivo");
                        }
                    } else {
                        System.out.println("Permissão negada. Você não possui permissão para modificar as permissões do arquivo.");
                    }
                } else {
                    System.out.println("Você não é o proprietário do arquivo");
                }
            } else {
                System.out.println("Arquivo não encontrado");
            }
        } else {
            System.out.println("Usuário não autorizado");
        }
    }

    // Atualiza permissões
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
                    System.out.println("Flag de permissão inválida: " + flag);
            }
        }
    }

    // Cria novo diretório
    public void mkdir(String diretorio) {
        if (usuarioAtual != null) {
            Inode inode = new Inode(1, usuarioAtual.getId(), true); 
            Diretorio novoDiretorio = new Diretorio(diretorio, inode);
            novoDiretorio.setParent(diretorioAtual); 
            diretorioAtual.adicionarSubdiretorio(novoDiretorio);
            System.out.println("Diretório criado: " + diretorio);
        } else {
            System.out.println("Usuário não autorizado");
        }
    }

    // Remove diretório
    public void rmdir(String nome) {
        if (usuarioAtual != null) {
            Diretorio diretorio = diretorioAtual.getSubdiretorios().get(nome);
            if (diretorio != null) {
                Inode inode = diretorio.getInode();
                if (usuarioAtual.getId() == inode.getProprietarioID()) {
                    if (inode.getPermissoes().get("user")) {
                        diretorioAtual.removerSubdiretorio(nome);
                        System.out.println("Diretório removido: " + nome);
                    } else {
                        System.out.println("Permissão negada para remoção do diretório");
                    }
                } else {
                    System.out.println("Você não é o proprietário do diretório");
                }
            } else {
                System.out.println("Diretório não encontrado");
            }
        } else {
            System.out.println("Usuário não autorizado");
        }
    }

    // Muda de diretório
    public void cd(String diretorio) {
        if (usuarioAtual != null) {
            Diretorio novoDiretorio;
            if (diretorio.equals("..")) {
                novoDiretorio = diretorioAtual.getParent();
                if (novoDiretorio == null) {
                    System.out.println("Já está no diretório raiz");
                    return;
                }
            } else {
                Diretorio subdiretorio = diretorioAtual.getSubdiretorios().get(diretorio);
                if (subdiretorio != null) {
                    novoDiretorio = subdiretorio;
                } else {
                    novoDiretorio = diretorioAtual.getSubdiretorios().get("home");
                    if (novoDiretorio == null) {
                        System.out.println("Diretório não encontrado");
                        return;
                    }
                }
            }
            diretorioAtual = novoDiretorio;
            System.out.println("Diretório atual alterado para: " + diretorioAtual.getNome());
        } else {
            System.out.println("Usuário não autorizado");
        }
    }

 // Lista arquivos de diretório com detalhes de inode
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
                    System.out.println("Diretório não encontrado");
                    return;
                }
            }
            System.out.println("Conteúdo do diretório " + diretorioAtualListagem.getNome() + ":");

            // Listar arquivos com detalhes de inode
            for (Map.Entry<String, Arquivo> entry : diretorioAtualListagem.getArquivos().entrySet()) {
                Arquivo arquivo = entry.getValue();
                Inode inode = arquivo.getInode();
                System.out.println("Arquivo: " + entry.getKey() +
                        ", Proprietário: " + inode.getProprietarioID() +
                        ", Tamanho: " + inode.getTamanhoArquivo() + " Bytes" +
                        ", Inode: " + inode.toString() +
                        ", Criado em: " + inode.getDataCriacao());
            }

            // Listar subdiretórios
            for (Map.Entry<String, Diretorio> entry : diretorioAtualListagem.getSubdiretorios().entrySet()) {
                Diretorio dir = entry.getValue();
                Inode inode = dir.getInode();
            	System.out.println("Diretório: " + entry.getKey() +
                ", Proprietário: " + inode.getProprietarioID() +
                ", Inode: " + inode.toString() +
                ", Criado em: " + inode.getDataCriacao());;
            }
        } else {
            System.out.println("Usuário não autorizado");
        }
    }

    // Adiciona novo usuário
    public void adduser(String nome, int id) {
        if (usuarioAtual != null) {
            Usuario admin = usuarioAtual;
            if (admin.possuiPermissao("admin")) {
                if (!usuarios.containsKey(nome)) {
                    Usuario novoUsuario = new Usuario(id, nome);
                    usuarios.put(nome, novoUsuario);
                    System.out.println("Usuário adicionado: " + nome);
                } else {
                    System.out.println("Nome de usuário já existe");
                }
            } else {
                System.out.println("Permissão negada. Apenas administradores podem adicionar usuários.");
            }
        } else {
            System.out.println("Usuário não autorizado");
        }
    }

    // Remove usuário
    public void rmuser(String nome) {
        if (usuarioAtual != null) {
            Usuario admin = usuarioAtual;
            if (admin.possuiPermissao("admin")) {
                if (usuarios.containsKey(nome)) {
                    usuarios.remove(nome);
                    System.out.println("Usuário removido: " + nome);
                } else {
                    System.out.println("Usuário não encontrado");
                }
            } else {
                System.out.println("Permissão negada. Apenas administradores podem remover usuários.");
            }
        } else {
            System.out.println("Usuário não autorizado");
        }
    }

    // Lista usuários existentes
    public void lsuser() {
        if (usuarioAtual != null) {
            Usuario admin = usuarioAtual;
            if (admin.possuiPermissao("admin")) {
                System.out.println("Lista de usuários:");

                for (Map.Entry<String, Usuario> entry : usuarios.entrySet()) {
                    System.out.println("Nome: " + entry.getKey() + ", ID: " + entry.getValue().getId());
                }
            } else {
                System.out.println("Permissão negada. Apenas administradores podem listar usuários.");
            }
        } else {
            System.out.println("Usuário não autorizado");
        }
    }
    
    // Troca de usuário    
    public void su(String nomeUsuario) {
        if (usuarios.containsKey(nomeUsuario)) {
            usuarioAtual = usuarios.get(nomeUsuario);
            System.out.println("Troca de usuário para: " + nomeUsuario);
        } else {
            System.out.println("Usuário não encontrado");
        }
    }
    
    // Exibe as datas referentes ao arquivo
    public void exibirInformacoesArquivo(String nome) {
        if (usuarioAtual != null) {
            Arquivo arquivo = diretorioAtual.getArquivos().get(nome);
            if (arquivo != null) {
                Inode inode = arquivo.getInode();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                System.out.println("Informações sobre o arquivo " + nome + ":");
                System.out.println("Criado em: " + sdf.format(inode.getDataCriacao()));
                System.out.println("Última modificação em: " + sdf.format(inode.getDataUltimaAtualizacao()));
                System.out.println("Último acesso em: " + sdf.format(inode.getDataUltimoAcesso()));
            } else {
                System.out.println("Arquivo não encontrado");
            }
        } else {
            System.out.println("Usuário não autorizado");
        }
    }

    // Cria usuário admin
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

    // Cria diretório home para o usuário
    private void criarDiretorioHome(String usuario) {
        Inode inode = new Inode(1, usuarios.get(usuario).getId(), true);
        Diretorio home = new Diretorio("home", inode);
        home.setParent(diretorioAtual);
        diretorioAtual.adicionarSubdiretorio(home);
        diretorioAtual = home;
    }
}