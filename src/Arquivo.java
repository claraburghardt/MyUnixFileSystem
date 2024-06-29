public class Arquivo {
    private int fileID;
    private String nome;
    private Inode inode;
    private String content;
    
    // Construtor para inicializar um arquivo com ID, nome e inode associado
    public Arquivo(int fileID, String nome, Inode inode) {
        this.fileID = fileID;
        this.nome = nome;
        this.inode = inode;
        this.content = ""; 
    }

    public int getFileID() {
        return fileID;
    }

    public String getNome() {
        return nome;
    }

    public Inode getInode() {
        return inode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}