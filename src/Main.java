import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Inicializa o sistema de arquivos UNIX
        SistemaArquivosUNIX sistemaArquivos = new SistemaArquivosUNIX();

        // Inicializa o scanner para entrada do usuário
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Exibe o prompt do shell indicando o usuário e diretório atual
            System.out.print(sistemaArquivos.getUsuarioAtual().getNome() + ":" + sistemaArquivos.getDiretorioAtual().getNome() + "> ");
            String comando = scanner.nextLine();

            // Divide o comando em partes para análise
            String[] partesComando = comando.split(" ");

            // Realiza ações com base no comando fornecido
            switch (partesComando[0]) {
                case "formata":
                    // Comando para formatar o sistema de arquivos
                    sistemaArquivos.formata();
                    break;
                case "touch":
                    // Comando para criar um novo arquivo
                    if (partesComando.length == 2) {
                        sistemaArquivos.touch(partesComando[1]);
                    } else {
                        System.out.println("Comando inválido. Uso: touch <nome_arquivo>");
                    }
                    break;
                case "gravarConteudo":
                    // Comando para gravar conteúdo em um arquivo
                    if (partesComando.length == 4) {
                        sistemaArquivos.gravarConteudo(partesComando[1], Integer.parseInt(partesComando[2]), partesComando[3]);
                    } else {
                        System.out.println("Comando inválido. Uso: gravarConteudo <arquivo> <posicao> <conteudo>");
                    }
                    break;
                case "ls":
                    // Comando para listar arquivos e diretórios
                    sistemaArquivos.ls(null);
                    break;
                case "cd":
                    // Comando para mudar de diretório
                    if (partesComando.length == 2) {
                        sistemaArquivos.cd(partesComando[1]);
                    } else {
                        System.out.println("Comando inválido. Uso: cd <nome_diretorio>");
                    }
                    break;
                case "mkdir":
                    // Comando para criar um novo diretório
                    if (partesComando.length == 2) {
                        sistemaArquivos.mkdir(partesComando[1]);
                    } else {
                        System.out.println("Comando inválido. Uso: mkdir <nome_diretorio>");
                    }
                    break;
                case "rmdir":
                    // Comando para remover um diretório
                    if (partesComando.length == 2) {
                        sistemaArquivos.rmdir(partesComando[1]);
                    } else {
                        System.out.println("Comando inválido. Uso: rmdir <nome_diretorio>");
                    }
                    break;
                case "rm":
                    // Comando para remover um arquivo
                    if (partesComando.length == 2) {
                        sistemaArquivos.rm(partesComando[1]);
                    } else {
                        System.out.println("Comando inválido. Uso: rm <nome_arquivo>");
                    }
                    break;
                case "cat":
                    // Comando para exibir o conteúdo de um arquivo
                    if (partesComando.length == 2) {
                        sistemaArquivos.cat(partesComando[1]);
                    } else {
                        System.out.println("Comando inválido. Uso: cat <nome_arquivo>");
                    }
                    break;
                case "chown":
                    // Comando para alterar o proprietário de um arquivo
                    if (partesComando.length == 4) {
                        sistemaArquivos.chown(partesComando[1], partesComando[2], partesComando[3]);
                    } else {
                        System.out.println("Comando inválido. Uso: chown <usuario_atual> <novo_usuario> <arquivo>");
                    }
                    break;
                case "adduser":
                    // Comando para adicionar um novo usuário
                    if (partesComando.length == 3) {
                        sistemaArquivos.adduser(partesComando[1], Integer.parseInt(partesComando[2]));
                    } else {
                        System.out.println("Comando inválido. Uso: adduser <nome> <id>");
                    }
                    break;
                case "rmuser":
                    // Comando para remover um usuário
                    if (partesComando.length == 2) {
                        sistemaArquivos.rmuser(partesComando[1]);
                    } else {
                        System.out.println("Comando inválido. Uso: rmuser <nome>");
                    }
                    break;
                case "lsuser":
                    // Comando para listar usuários
                    sistemaArquivos.lsuser();
                    break;
                case "su":
                    // Comando para trocar de usuário
                    if (partesComando.length == 2) {
                        sistemaArquivos.su(partesComando[1]);
                    } else {
                        System.out.println("Comando inválido. Uso: su <nome_usuario>");
                    }
                    break;
                case "dates":
                    // Comando para exibir informações sobre a última atualização de um arquivo
                    if (partesComando.length == 2) {
                        sistemaArquivos.exibirInformacoesArquivo(partesComando[1]);
                    } else {
                        System.out.println("Comando inválido. Uso: dates <nome_arquivo>");
                    }
                    break;
                case "exit":
                    // Comando para sair do sistema de arquivos
                    System.out.println("Saindo do sistema de arquivos. Até logo!");
                    System.exit(0);
                case "infoArquivo":
                    // Comando para exibir informações sobre um arquivo
                    if (partesComando.length == 2) {
                        sistemaArquivos.exibirInformacoesArquivo(partesComando[1]);
                    } else {
                        System.out.println("Comando inválido. Uso: infoArquivo <arquivo>");
                    }
                    break;
                default:
                    // Mensagem para comandos não reconhecidos
                    System.out.println("Comando não reconhecido. Tente novamente.");
            }
        }
    }
}