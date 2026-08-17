# Problema-dos-blocos

## Sobre o problema

O problema dos blocos se trata de:

- Uma mesa infinita;
- Blocos sobre a mesa que podem estar empilhados uns sobre os outros ou não;
- Um braço robótico que consegue empilhar ou desempilhar blocos;

O objetivo do problema é sair de uma configuração e chegar à outra com as ações do braço robótico.
Nesse código em específico, você empilha os blocos como quiser e pede para o braço robótico resolver o problema. Ao final das ações, todos os blocos estarão empilhados de forma ordenada.

## Sobre o código

O programa possui 5 blocos, mas consegue resolver para um número arbitrário de blocos.
Apesar do programar utilizar somente 1 busca, está implementada outra busca para utilização caso o desenvolvedor queira alterar.
Código escrito na linguagem __Java__ que utiliza a biblioteca `AIMA` para a implementação da Inteligência Artificial.
O código é divido em 2 pastas: Uma com o código da IA, que resolve o problema; uma com o código da parte visual.

Para compilar o código via terminal, basta ter a biblioteca `AIMA` baixada ou ter o `.jar` na pasta dos programas.
Se estiver com o `.jar` nas pastas, use o seguinte comando para compilar e executar:

```javac -d bin -cp "lib/aima-core.jar" roboBlocos/src/telasDoSistema/*.java roboBlocos/src/funcionamentoDoSistema/*.java```
```java -cp "lib/aima-core.jar" --class-path bin src.telasDoSistema.RoboBlocosTela```

Ao executar, vai aparecer a tela principal do programa. 
> Obs: O arquivo `.jar` da biblioteca `AIMA` deve estar nas pastas 

O arquivo `.jar` da biblioteca `AIMA` está disponível [aqui](https://github.com/aimacode/aima-java/releases)

## Sobre o desenvolvimento do código

O código foi criado com a intenção de obter nota na disciplina de Inteligência Computacional, do curso Engenharia de Computação da UEPG.
Modificado para demonstrar de forma visual o funcionamento da inteligência artifical.
