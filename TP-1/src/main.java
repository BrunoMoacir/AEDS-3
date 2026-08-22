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
                // Chama o importador passando o caminho relativo da pasta dados
                ImportadorCSV.processarArquivo("dados/imdb_movies.csv");
            } else if (opcao == 0) {
                System.out.println("Saindo do sistema...");
            } else {
                System.out.println("\nFuncionalidade em desenvolvimento para as próximas etapas.");
            }
            System.out.println();
        }
        scanner.close();
    }
}