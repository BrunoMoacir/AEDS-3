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
                // Passa o caminho do CSV e onde o arquivo binário deve ser gerado
                ImportadorCSV.processarArquivo("dados/imdb_movies.csv", "dados/dados.bin");
            } 
            else if (opcao == 2) {
                System.out.print("\nDigite o ID do filme que deseja ler: ");
                int idBusca = scanner.nextInt();
                
                ArquivoBinario arqBin = new ArquivoBinario("dados/dados.bin");
                Filme encontrado = arqBin.ler(idBusca); // Chama o método de leitura
                
                if (encontrado != null) {
                    System.out.println("\n--- Filme Encontrado ---");
                    System.out.println(encontrado.toString());
                } else {
                    System.out.println("\nFilme com ID " + idBusca + " não encontrado ou foi deletado.");
                }
            } 
            else if (opcao == 4) {
                System.out.print("\nDigite o ID do filme que deseja deletar: ");
                int idDeleta = scanner.nextInt();
                
                ArquivoBinario arqBin = new ArquivoBinario("dados/dados.bin");
                boolean sucesso = arqBin.deletar(idDeleta); // Chama o método de exclusão
                
                if (sucesso) {
                    System.out.println("\nFilme deletado com sucesso!");
                } else {
                    System.out.println("\nFalha ao deletar: Filme não encontrado.");
                }
            }
            else if (opcao == 0) {
                System.out.println("Saindo do sistema...");
            } 
            else {
                System.out.println("\nFuncionalidade em desenvolvimento para as próximas etapas.");
            }
            System.out.println();
        }
        scanner.close();
    }
}