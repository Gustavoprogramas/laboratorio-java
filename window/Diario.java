

package janela;

public class Diario {
	private Aluno[] listaDeAlunos;
	private int quantidadedeNotasPorAluno;
	private int contadorDeAlunosCadastrados;
	
	public Diario (int capacidadeMaximaAlunos, int qtdeNotas) {
		listaDeAlunos = new Aluno[capacidadeMaximaAlunos];
		quantidadedeNotasPorAluno = qtdeNotas;
		contadorDeAlunosCadastrados = 0;
	}
	
	public void adicionarAluno(String nomeNovoAluno) {
		if (contadorDeAlunosCadastrados < listaDeAlunos.length) {
			Aluno novoAluno = new Aluno(nomeNovoAluno, quantidadedeNotasPorAluno);
			listaDeAlunos[contadorDeAlunosCadastrados] = novoAluno;
			contadorDeAlunosCadastrados++;
		} else {
			System.out.println("Diario cheio.");
		}
	}
	
	public void modificarNota(String nomeAluno, int numeroDaNota, int novaNota) {
		for (int cont = 0; cont < contadorDeAlunosCadastrados; cont++) {
			if (listaDeAlunos[cont].getNomeAluno().equals(nomeAluno)) {
				listaDeAlunos[cont].setNota(numeroDaNota - 1, novaNota);
				return;
			}
		}
	}
	public Aluno[] getListaDeAlunos() {
    return this.listaDeAlunos;
    }

	public int getQuantidadeCadastrados() {
    return this.contadorDeAlunosCadastrados;
	}
	public int getQuantidadeNotas() {
    return this.quantidadedeNotasPorAluno;
	}
	
	
	public static void imprimirCabecalhoRelatorio() {
        System.out.println("========================================");
        System.out.println("            RELATORIO DE NOTAS           ");
        System.out.println("========================================");
	}
	
	public void gerarRelatorioDeNotas() {
		imprimirCabecalhoRelatorio();
		for (int cont = 0; cont < contadorDeAlunosCadastrados; cont++) {
			Aluno a = listaDeAlunos[cont];
		    System.out.print("Aluno " + a.getNomeAluno() + " | Notas: ");
		    int[] notas = a.getNotasAluno();
		    for (int j = 0; j < notas.length; j++) {
		    	System.out.print(notas[j] + " | ");
		    }
		    System.out.printf(" Média: %.2f\n", a.calcularMedia());
		}
	}
	public void relatorioMaiorNota() {
		double maior = 0;
		double menor = 10;
		for (int i = 0; i < contadorDeAlunosCadastrados; i++) {
			int[] notas = listaDeAlunos[i].getNotasAluno();
			for (int j = 0; j < notas.length; j++) {
				if (notas[j] > maior) maior = notas[j];
				if (notas[j] < menor) menor = notas[j];
			}
		}
		System.out.println("\nMaior nota: " + maior);
		System.out.println("\nMenor nota: "+ menor);
	}
	
	/*public void gerarHistograma() {
		int intervalo[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	
		
		String[] quadrado = {"▇"};
		for (int i=0; i<contadorDeAlunosCadastrados; i++) {
			int[] notas = listaDeAlunos[i].getNotasAluno();
			for (int j = 0; j<notas.length; j++) {
				if (notas[j] == 100) {
					intervalo[9]++;
				}else
				intervalo[notas[j]/10]++;
				
			}
		} //fim for alunos.lenght
		System.out.printf("Frequencia de notas de 0 a 9: ");
		for (int i=0; i<intervalo[0]; i++) {
			System.out.printf("%s", quadrado);
		}
		System.out.printf(" %d notas", intervalo[0]);
		System.out.println();
		System.out.printf("Frequencia de notas de 10 a 19: ");
		for (int i=0; i<intervalo[1]; i++) {
			System.out.printf("%s", quadrado);
		}
		System.out.printf(" %d notas", intervalo[1]);
		System.out.println();
		System.out.printf("Frequencia de notas de 20 a 29: ");
		for (int i=0; i<intervalo[2]; i++) {
			System.out.printf("%s", quadrado);
		}
		System.out.printf(" %d notas", intervalo[2]);
		System.out.println();
		System.out.printf("Frequencia de notas de 30 a 39: ");
		for (int i=0; i<intervalo[3]; i++) {
			System.out.printf("%s", quadrado);
		}
		System.out.printf(" %d notas", intervalo[3]);
		System.out.println();
		System.out.printf("Frequencia de notas de 40 a 49: ");
		for (int i=0; i<intervalo[4]; i++) {
			System.out.printf("%s", quadrado);
		}
		System.out.printf(" %d notas", intervalo[4]);
		System.out.println();
		System.out.printf("Frequencia de notas de 50 a 59: ");
		for (int i=0; i<intervalo[5]; i++) {
			System.out.printf("%s", quadrado);
		}
		System.out.printf(" %d notas", intervalo[5]);
		System.out.println();
		System.out.printf("Frequencia de notas de 60 a 69: ");
		for (int i=0; i<intervalo[6]; i++) {
			System.out.printf("%s", quadrado);
		}
		System.out.printf(" %d notas", intervalo[6]);
		System.out.println();
		System.out.printf("Frequencia de notas de 70 a 79: ");
		for (int i=0; i<intervalo[7]; i++) {
			System.out.printf("%s", quadrado);
		}
		System.out.printf(" %d notas", intervalo[7]);
		System.out.println();
		System.out.printf("Frequencia de notas de 80 a 89: ");
		for (int i=0; i<intervalo[8]; i++) {
			System.out.printf("%s", quadrado);
		}
		System.out.printf(" %d notas", intervalo[8]);
		System.out.println();
		System.out.printf("Frequencia de notas de 90 a 100: ");
		for (int i=0; i<intervalo[9]; i++) {
		System.out.printf("%s", quadrado);
		}
		System.out.printf(" %d notas", intervalo[9]);
		System.out.println();
		
		
		
		
		
		
	}
}*/
public String gerarHistograma() {
    int intervalo[] = new int[10]; 
    
  
    for (int i = 0; i < contadorDeAlunosCadastrados; i++) {
      
        int[] notas = listaDeAlunos[i].getNotasAluno(); 
        
        for (int j = 0; j < notas.length; j++) {
            if (notas[j] == 100) {
                intervalo[9]++;
            } else {
                intervalo[(int) (notas[j] / 10)]++;
            }
        }
    } 

    
    StringBuilder textoGrafico = new StringBuilder();
    textoGrafico.append("Frequência de Notas:\n\n");

   
    for (int i = 0; i < intervalo.length; i++) {
        
       
        if (i == 9) {
            textoGrafico.append("90 a 100: ");
        } else {
           
            textoGrafico.append(String.format("%02d a %02d: ", i * 10, i * 10 + 9));
        }

      
        for (int j = 0; j < intervalo[i]; j++) {
            textoGrafico.append("▇");
        }
        
     
        textoGrafico.append("").append(intervalo[i]).append("\n");
    }

  
    	return textoGrafico.toString();
	}
}
