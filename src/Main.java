import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Inicializa o sistema de arquivos UNIX
        SistemaArquivosUNIX sistemaArquivos = new SistemaArquivosUNIX();

        // Inicializa o scanner para entrada do usu�rio
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Exibe o prompt do shell indicando o usu�rio e diret�rio atual
            System.out.print(sistemaArquivos.getUsuarioAtual().getNome() + ":" + sistemaArquivos.getDiretorioAtual().getNome() + "> ");
            String comando = scanner.nextLine();

            // Divide o comando em partes para an�lise
            String[] partesComando = comando.split(" ");

            // Realiza a��es com base no comando fornecido
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
                        System.out.println("Comando inv�lido. Uso: touch <nome_arquivo>");
                    }
                    break;
                case "gravarConteudo":
                    // Comando para gravar conte�do em um arquivo
                    if (partesComando.length == 4) {
                        sistemaArquivos.gravarConteudo(partesComando[1], Integer.parseInt(partesComando[2]), partesComando[3]);
                    } else {
                        System.out.println("Comando inv�lido. Uso: gravarConteudo <arquivo> <posicao> <conteudo>");
                    }
                    break;
                case "ls":
                    // Comando para listar arquivos e diret�rios
                    sistemaArquivos.ls(null);
                    break;
                case "cd":
                    // Comando para mudar de diret�rio
                    if (partesComando.length == 2) {
                        sistemaArquivos.cd(partesComando[1]);
                    } else {
                        System.out.println("Comando inv�lido. Uso: cd <nome_diretorio>");
                    }
                    break;
                case "mkdir":
                    // Comando para criar um novo diret�rio
                    if (partesComando.length == 2) {
                        sistemaArquivos.mkdir(partesComando[1]);
                    } else {
                        System.out.println("Comando inv�lido. Uso: mkdir <nome_diretorio>");
                    }
                    break;
                case "rmdir":
                    // Comando para remover um diret�rio
                    if (partesComando.length == 2) {
                        sistemaArquivos.rmdir(partesComando[1]);
                    } else {
                        System.out.println("Comando inv�lido. Uso: rmdir <nome_diretorio>");
                    }
                    break;
                case "rm":
                    // Comando para remover um arquivo
                    if (partesComando.length == 2) {
                        sistemaArquivos.rm(partesComando[1]);
                    } else {
                        System.out.println("Comando inv�lido. Uso: rm <nome_arquivo>");
                    }
                    break;
                case "cat":
                    // Comando para exibir o conte�do de um arquivo
                    if (partesComando.length == 2) {
                        sistemaArquivos.cat(partesComando[1]);
                    } else {
                        System.out.println("Comando inv�lido. Uso: cat <nome_arquivo>");
                    }
                    break;
                case "chown":
                    // Comando para alterar o propriet�rio de um arquivo
                    if (partesComando.length == 4) {
                        sistemaArquivos.chown(partesComando[1], partesComando[2], partesComando[3]);
                    } else {
                        System.out.println("Comando inv�lido. Uso: chown <usuario_atual> <novo_usuario> <arquivo>");
                    }
                    break;
                case "adduser":
                    // Comando para adicionar um novo usu�rio
                    if (partesComando.length == 3) {
                        sistemaArquivos.adduser(partesComando[1], Integer.parseInt(partesComando[2]));
                    } else {
                        System.out.println("Comando inv�lido. Uso: adduser <nome> <id>");
                    }
                    break;
                case "rmuser":
                    // Comando para remover um usu�rio
                    if (partesComando.length == 2) {
                        sistemaArquivos.rmuser(partesComando[1]);
                    } else {
                        System.out.println("Comando inv�lido. Uso: rmuser <nome>");
                    }
                    break;
                case "lsuser":
                    // Comando para listar usu�rios
                    sistemaArquivos.lsuser();
                    break;
                case "su":
                    // Comando para trocar de usu�rio
                    if (partesComando.length == 2) {
                        sistemaArquivos.su(partesComando[1]);
                    } else {
                        System.out.println("Comando inv�lido. Uso: su <nome_usuario>");
                    }
                    break;
                case "dates":
                    // Comando para exibir informa��es sobre a �ltima atualiza��o de um arquivo
                    if (partesComando.length == 2) {
                        sistemaArquivos.exibirInformacoesArquivo(partesComando[1]);
                    } else {
                        System.out.println("Comando inv�lido. Uso: dates <nome_arquivo>");
                    }
                    break;
                case "exit":
                    // Comando para sair do sistema de arquivos
                    System.out.println("Saindo do sistema de arquivos. At� logo!");
                    System.exit(0);
                case "infoArquivo":
                    // Comando para exibir informa��es sobre um arquivo
                    if (partesComando.length == 2) {
                        sistemaArquivos.exibirInformacoesArquivo(partesComando[1]);
                    } else {
                        System.out.println("Comando inv�lido. Uso: infoArquivo <arquivo>");
                    }
                    break;
                default:
                    // Mensagem para comandos n�o reconhecidos
                    System.out.println("Comando n�o reconhecido. Tente novamente.");
            }
        }
    }
}