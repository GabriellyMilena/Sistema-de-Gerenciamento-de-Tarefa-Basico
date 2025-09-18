import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

// Classe que representa uma Tarefa
class Tarefa {
    private int id;
    private String descricao;
    private boolean concluida;

    public Tarefa(int id, String descricao, boolean concluida) {
        this.id = id;
        this.descricao = descricao;
        this.concluida = concluida;
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void concluir() {
        this.concluida = true;
    }

    @Override
    public String toString() {
        return id + " - " + descricao + (concluida ? " [Concluída]" : " [Pendente]");
    }

    public String formatarParaArquivo() {
        return id + "  " + descricao + "  " + concluida;
    }

    public static Tarefa fromString(String linha) {
        String[] partes = linha.split(";");
        int id = Integer.parseInt(partes[0]);
        String descricao = partes[1];
        boolean concluida = Boolean.parseBoolean(partes[2]);
        return new Tarefa(id, descricao, concluida);
    }
}

// Classe que gerencia as Tarefas
class GerenciadorDeTarefas {
    private ArrayList<Tarefa> tarefas = new ArrayList<>();
    private int proximoId = 1;
    private final String NOME_ARQUIVO = "tarefas.txt";

    public GerenciadorDeTarefas() {
        carregarTarefas();
    }

    public void adicionarTarefa(String descricao) {
        Tarefa nova = new Tarefa(proximoId++, descricao, false);
        tarefas.add(nova);
        salvarTarefas();
        System.out.println("Tarefa adicionada com sucesso!");
    }

    public void listarTarefas() {
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa cadastrada.");
        } else {
            for (Tarefa t : tarefas) {
                System.out.println(t);
            }
        }
    }

    public void concluirTarefa(int id) {
        for (Tarefa t : tarefas) {
            if (t.getId() == id) {
                t.concluir();
                salvarTarefas();
                System.out.println("Tarefa concluída!");
                return;
            }
        }
        System.out.println("Tarefa não encontrada.");
    }

    public void removerTarefa(int id) {
        for (Tarefa t : tarefas) {
            if (t.getId() == id) {
                tarefas.remove(t);
                salvarTarefas();
                System.out.println("Tarefa removida!");
                return;
            }
        }
        System.out.println("Tarefa não encontrada.");
    }

    private void salvarTarefas() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(NOME_ARQUIVO))) {
            for (Tarefa t : tarefas) {
                bw.write(t.formatarParaArquivo());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar tarefas: " + e.getMessage());
        }
    }

    private void carregarTarefas() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                Tarefa t = Tarefa.fromString(linha);
                tarefas.add(t);
                proximoId = Math.max(proximoId, t.getId() + 1);
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar tarefas: " + e.getMessage());
        }
    }
}

// Classe principal (menu interativo)
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GerenciadorDeTarefas gerenciador = new GerenciadorDeTarefas();
        int opcao;

        do {
            System.out.println("\n=== Gerenciador de Tarefas ===");
            System.out.println("1 - Adicionar tarefa");
            System.out.println("2 - Listar tarefas");
            System.out.println("3 - Concluir tarefa");
            System.out.println("4 - Remover tarefa");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine(); // limpar buffer

            switch (opcao) {
                case 1:
                    System.out.print("Digite a descrição da tarefa: ");
                    String desc = sc.nextLine();
                    gerenciador.adicionarTarefa(desc);
                    break;
                case 2:
                    gerenciador.listarTarefas();
                    break;
                case 3:
                    System.out.print("Digite o ID da tarefa a concluir: ");
                    int idConcluir = sc.nextInt();
                    gerenciador.concluirTarefa(idConcluir);
                    break;
                case 4:
                    System.out.print("Digite o ID da tarefa a remover: ");
                    int idRemover = sc.nextInt();
                    gerenciador.removerTarefa(idRemover);
                    break;
                case 0:
                    System.out.println("Encerrando...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);

        sc.close();
    }
}
