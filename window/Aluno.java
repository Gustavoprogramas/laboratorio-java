package janela;

public class Aluno {
	private String nomeAluno;
	private int[] notasAluno;
	
	
	public Aluno(String nomeInicial, int qtdeNotas) {
		this.nomeAluno = nomeInicial;
		this.notasAluno = new int[qtdeNotas];
	}
	public String getNomeAluno() {
		return this.nomeAluno;
	}
	public int[] getNotasAluno() {
		return this.notasAluno;
	}
	
	public void setNomeAluno(String novoNome) {
		this.nomeAluno = novoNome;
	}
	
	public void setNota(int indiceNota, int valorNota) {
		notasAluno[indiceNota] = valorNota;
	}
	
	public double calcularMedia() {
		double soma = 0;
		for (double nota : notasAluno) {
			soma += nota;
		}
		return soma / notasAluno.length;
	}


}
