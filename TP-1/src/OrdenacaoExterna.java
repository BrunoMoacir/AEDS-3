import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.File;

public class OrdenacaoExterna {
    private String arquivoOriginal;
    private int caminhos;
    private int limiteMemoria;

    public OrdenacaoExterna(String arquivoOriginal, int caminhos, int limiteMemoria) {
        this.arquivoOriginal = arquivoOriginal;
        this.caminhos = caminhos;
        this.limiteMemoria = limiteMemoria;
    }

    // FASE 1: Lê o arquivo original, limpa os excluídos, ordena blocos e distribui
    public int distribuir() {
        int arquivosGerados = 0;
        
        try {
            RandomAccessFile raf = new RandomAccessFile(this.arquivoOriginal, "r");
            raf.seek(4); // Pula o cabeçalho
            
            Filme[] memoria = new Filme[limiteMemoria];
            int qtdAtual = 0;
            int caminhoAtual = 0;

            while (raf.getFilePointer() < raf.length()) {
                byte lapide = raf.readByte();
                int tamanho = raf.readInt();
                
                byte[] ba = new byte[tamanho];
                raf.read(ba);

                // Só processa se NÃO for um registro deletado (isso remove os espaços em branco)
                if (lapide == ' ') {
                    Filme filme = new Filme();
                    filme.fromByteArray(ba);
                    
                    memoria[qtdAtual] = filme;
                    qtdAtual++;
                    
                    // Se a memória encheu, ordena e joga para o arquivo temporário
                    if (qtdAtual == limiteMemoria) {
                        ordenarVetorMemoria(memoria, qtdAtual);
                        gravarArquivoTemporario(memoria, qtdAtual, caminhoAtual);
                        
                        arquivosGerados++;
                        caminhoAtual = (caminhoAtual + 1) % caminhos; // Alterna entre os caminhos
                        qtdAtual = 0; // Limpa a memória
                    }
                }
            }
            
            // Grava os registros que sobraram na memória e não preencheram um bloco completo
            if (qtdAtual > 0) {
                ordenarVetorMemoria(memoria, qtdAtual);
                gravarArquivoTemporario(memoria, qtdAtual, caminhoAtual);
                arquivosGerados++;
            }
            
            raf.close();
            
        } catch (IOException e) {
            System.out.println("Erro na fase de distribuição: " + e.getMessage());
        }
        
        return arquivosGerados;
    }

    // Ordenação manual em memória primária (Selection Sort por ID)
    private void ordenarVetorMemoria(Filme[] memoria, int tamanho) {
        for (int i = 0; i < tamanho - 1; i++) {
            int indiceMenor = i;
            for (int j = i + 1; j < tamanho; j++) {
                if (memoria[j].getId() < memoria[indiceMenor].getId()) {
                    indiceMenor = j;
                }
            }
            Filme temp = memoria[indiceMenor];
            memoria[indiceMenor] = memoria[i];
            memoria[i] = temp;
        }
    }

    // Grava o bloco ordenado em um arquivo da rodada atual
    private void gravarArquivoTemporario(Filme[] memoria, int tamanho, int caminho) {
        // Nome padrão: temp_caminho.bin (ex: dados/temp_0.bin)
        String nomeArquivo = "dados/temp_0_" + caminho + ".bin"; 
        
        try {
            RandomAccessFile tempRaf = new RandomAccessFile(nomeArquivo, "rw");
            
            // Vai para o final do arquivo temporário (pois podemos escrever mais blocos nele)
            tempRaf.seek(tempRaf.length());
            
            for (int i = 0; i < tamanho; i++) {
                byte[] ba = memoria[i].toByteArray();
                tempRaf.writeByte(' ');
                tempRaf.writeInt(ba.length);
                tempRaf.write(ba);
            }
            
            tempRaf.close();
        } catch (IOException e) {
            System.out.println("Erro ao gravar arquivo temporário: " + e.getMessage());
        }
    }
}