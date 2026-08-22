import java.io.RandomAccessFile;
import java.io.IOException;

public class ArquivoBinario {
    private String nomeArquivo;

    public ArquivoBinario(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    // Inicializa o arquivo criando o cabeçalho se ele não existir
    public void inicializar() {
        try {
            RandomAccessFile raf = new RandomAccessFile(this.nomeArquivo, "rw");
            if (raf.length() == 0) {
                raf.writeInt(0); // Escreve 0 como o último ID utilizado no cabeçalho
            }
            raf.close();
        } catch (IOException e) {
            System.out.println("Erro ao inicializar o arquivo binário: " + e.getMessage());
        }
    }

    // Insere um novo filme no final do arquivo e atualiza o cabeçalho
    public void inserir(Filme filme) {
        try {
            RandomAccessFile raf = new RandomAccessFile(this.nomeArquivo, "rw");
            
            // 1. Atualiza o cabeçalho com o novo ID
            raf.seek(0); 
            raf.writeInt(filme.getId());

            // 2. Vai para o final do arquivo para inserir o novo registro
            raf.seek(raf.length());

            // Converte o objeto para vetor de bytes
            byte[] ba = filme.toByteArray();

            // 3. Escreve a Lápide (espaço em branco ' ' significa válido, asterisco '*' significa excluído)
            raf.writeByte(' '); 

            // 4. Escreve o Indicador de Tamanho do registro
            raf.writeInt(ba.length);

            // 5. Escreve o Vetor de Bytes com os dados
            raf.write(ba);

            raf.close();
        } catch (IOException e) {
            System.out.println("Erro ao inserir registro: " + e.getMessage());
        }
    }
}