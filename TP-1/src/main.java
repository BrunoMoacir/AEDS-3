import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("=====================================");
            System.out.println("  MENU PRINCIPAL - AEDS III (TP1)  ");
            System.out.println("=====================================");
            System.out.println("1. Realizar Carga da Base de Dados (CSV para Binário)");
            System.out.println("2. Ler um Registro (CRUD)");
            System.out.println("3. Atualizar um Registro (CRUD)");
            System.out.println("4. Deletar um Registro (CRUD)");
            System.out.println("5. Ordenação Externa");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opcao: ");
            
            // --- CORREÇÃO DO BUFFER AQUI ---
            String entradaOpcao = scanner.nextLine();
            try {
                if (entradaOpcao.length() > 0) {
                    opcao = Integer.parseInt(entradaOpcao);
                } else {
                    opcao = -1;
                }
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            if (opcao == 1) {
                System.out.println("\nIniciando carga de dados...");
                ImportadorCSV.processarArquivo("dados/imdb_movies.csv", "dados/dados.bin");
            } 
            else if (opcao == 2) {
                System.out.print("\nDigite o ID do filme que deseja ler: ");
                String idBuscaStr = scanner.nextLine();
                try {
                    int idBusca = Integer.parseInt(idBuscaStr);
                    ArquivoBinario arqBin = new ArquivoBinario("dados/dados.bin");
                    Filme encontrado = arqBin.ler(idBusca);
                    
                    if (encontrado != null) {
                        System.out.println("\n--- Filme Encontrado ---");
                        System.out.println(encontrado.toString());
                    } else {
                        System.out.println("\nFilme com ID " + idBusca + " não encontrado ou foi deletado.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\nErro: ID inválido.");
                }
            } 
            else if (opcao == 3) {
                System.out.print("\nDigite o ID do filme que deseja atualizar: ");
                String idStr = scanner.nextLine(); 
                
                try {
                    int idAtualiza = Integer.parseInt(idStr);
                    ArquivoBinario arqBin = new ArquivoBinario("dados/dados.bin");
                    Filme filmeExistente = arqBin.ler(idAtualiza);
                    
                    if (filmeExistente != null) {
                        System.out.println("\n--- Atualizando Filme (ID " + idAtualiza + ") ---");
                        System.out.println("Deixe em branco e aperte Enter para manter o valor atual.");
                        
                        System.out.print("Nome atual (" + filmeExistente.getNome() + "): ");
                        String novoNome = scanner.nextLine();
                        if (novoNome.length() > 0) filmeExistente.setNome(novoNome);
                        
                        System.out.print("Data atual (" + filmeExistente.getDataLancamento() + " ms) - Digite no formato MM/DD/YYYY: ");
                        String novaData = scanner.nextLine();
                        if (novaData.length() > 0) filmeExistente.setDataLancamento(ImportadorCSV.converterDataManual(novaData));
                        
                        System.out.print("Score atual (" + filmeExistente.getScore() + "): ");
                        String novoScoreStr = scanner.nextLine();
                        if (novoScoreStr.length() > 0) filmeExistente.setScore(Float.parseFloat(novoScoreStr));
                        
                        // --- CORREÇÃO: Formatando e mostrando os gêneros atuais ---
                        String strGen = "[";
                        String[] genAtuais = filmeExistente.getGeneros();
                        for (int i = 0; i < genAtuais.length; i++) {
                            strGen += genAtuais[i];
                            if (i < genAtuais.length - 1) strGen += ", ";
                        }
                        strGen += "]";
                        
                        System.out.print("Gêneros atuais " + strGen + " - Digite separados por vírgula: ");
                        String novosGeneros = scanner.nextLine();
                        if (novosGeneros.length() > 0) filmeExistente.setGeneros(ImportadorCSV.separarGenerosManual(novosGeneros));
                        // -----------------------------------------------------------
                        
                        System.out.print("País atual (" + filmeExistente.getPais() + ") - Sigla de 2 letras: ");
                        String novoPais = scanner.nextLine();
                        if (novoPais.length() > 0) filmeExistente.setPais(novoPais);
                        
                        boolean sucesso = arqBin.atualizar(filmeExistente);
                        if (sucesso) {
                            System.out.println("\nFilme atualizado com sucesso no arquivo binário!");
                            // --- CORREÇÃO: Mostrando como o registro ficou ---
                            System.out.println("Como ficou: " + filmeExistente.toString());
                        }
                    } else {
                        System.out.println("\nFilme não encontrado para atualização.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\nErro: Digite um ID numérico válido.");
                }
            }
            else if (opcao == 4) {
                System.out.print("\nDigite o ID do filme que deseja deletar: ");
                String idDeletaStr = scanner.nextLine();
                try {
                    int idDeleta = Integer.parseInt(idDeletaStr);
                    ArquivoBinario arqBin = new ArquivoBinario("dados/dados.bin");
                    boolean sucesso = arqBin.deletar(idDeleta);
                    
                    if (sucesso) {
                        System.out.println("\nFilme deletado com sucesso!");
                    } else {
                        System.out.println("\nFalha ao deletar: Filme não encontrado.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\nErro: ID inválido.");
                }
            }
            else if (opcao == 5) {
                System.out.println("\n--- Ordenação Externa ---");
                System.out.print("Digite o número de caminhos (arquivos temporários, ex: 2 ou 3): ");
                int caminhos = Integer.parseInt(scanner.nextLine());
                
                System.out.print("Digite o limite de registros em memória primária (ex: 1000): ");
                int registrosMemoria = Integer.parseInt(scanner.nextLine());
                
                OrdenacaoExterna ordenacao = new OrdenacaoExterna("dados/dados.bin", caminhos, registrosMemoria);
                
                System.out.println("Iniciando Fase 1: Distribuição (e limpeza de excluídos)...");
                int totalArquivos = ordenacao.distribuir();
                
                System.out.println("Distribuição concluída! " + totalArquivos + " arquivos temporários gerados.");
                System.out.println("(A Fase de Intercalação será implementada no próximo passo)");
            }
            else if (opcao == 0) {
                System.out.println("\nSaindo do sistema...");
            } 
            else {
                System.out.println("\nOpção inválida. Tente novamente.");
            }
            System.out.println();
        }
        scanner.close();
    }
}