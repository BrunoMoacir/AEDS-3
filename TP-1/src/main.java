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
            System.out.println("0. Sair");
            System.out.print("Escolha uma opcao: ");
            
            opcao = scanner.nextInt();

            if (opcao == 1) {
                System.out.println("\nIniciando carga de dados...");
                ImportadorCSV.processarArquivo("dados/imdb_movies.csv", "dados/dados.bin");
            } 
            else if (opcao == 2) {
                System.out.print("\nDigite o ID do filme que deseja ler: ");
                int idBusca = scanner.nextInt();
                
                ArquivoBinario arqBin = new ArquivoBinario("dados/dados.bin");
                Filme encontrado = arqBin.ler(idBusca);
                
                if (encontrado != null) {
                    System.out.println("\n--- Filme Encontrado ---");
                    System.out.println(encontrado.toString());
                } else {
                    System.out.println("\nFilme com ID " + idBusca + " não encontrado ou foi deletado.");
                }
            } 
            else if (opcao == 3) {
                System.out.print("\nDigite o ID do filme que deseja atualizar: ");
                int idAtualiza = scanner.nextInt();
                scanner.nextLine(); // Limpa o buffer do teclado
                
                ArquivoBinario arqBin = new ArquivoBinario("dados/dados.bin");
                Filme filmeExistente = arqBin.ler(idAtualiza);
                
                if (filmeExistente != null) {
                    System.out.println("\nFilme atual: " + filmeExistente.getNome() + " | Score: " + filmeExistente.getScore());
                    
                    System.out.print("Digite o NOVO nome (ou aperte Enter para manter): ");
                    String novoNome = scanner.nextLine();
                    if (!novoNome.isEmpty()) {
                        filmeExistente.setNome(novoNome);
                    }
                    
                    System.out.print("Digite o NOVO score (use vírgula, ex: 85,5): ");
                    float novoScore = scanner.nextFloat();
                    filmeExistente.setScore(novoScore);
                    
                    boolean sucesso = arqBin.atualizar(filmeExistente);
                    if (sucesso) {
                        System.out.println("\nFilme atualizado com sucesso no arquivo binário!");
                    }
                } else {
                    System.out.println("\nFilme não encontrado para atualização.");
                }
            }
            else if (opcao == 4) {
                System.out.print("\nDigite o ID do filme que deseja deletar: ");
                int idDeleta = scanner.nextInt();
                
                ArquivoBinario arqBin = new ArquivoBinario("dados/dados.bin");
                boolean sucesso = arqBin.deletar(idDeleta);
                
                if (sucesso) {
                    System.out.println("\nFilme deletado com sucesso!");
                } else {
                    System.out.println("\nFalha ao deletar: Filme não encontrado.");
                }
            }
            else if (opcao == 0) {
                System.out.println("\nSaindo do sistema...");
            } 
            else {
                System.out.println("\nOpção inválida.");
            }
            System.out.println();
        }
        scanner.close();
    }
}