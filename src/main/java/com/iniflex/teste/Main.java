package com.iniflex.teste;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    private static final DateTimeFormatter DATA_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DecimalFormatSymbols BR_SYMBOLS = new DecimalFormatSymbols(new Locale("pt", "BR"));
    private static final DecimalFormat NUM_BR;
    private static final BigDecimal SALARIO_MINIMO = new BigDecimal("1212.00");

    static {
        BR_SYMBOLS.setDecimalSeparator(',');
        BR_SYMBOLS.setGroupingSeparator('.');
        NUM_BR = new DecimalFormat("#,##0.00", BR_SYMBOLS);
        NUM_BR.setRoundingMode(RoundingMode.HALF_UP);
    }

    public static void main(String[] args) {
        List<Funcionario> funcionarios = inserirFuncionarios();

        funcionarios.removeIf(f -> f.getNome().equals("João"));

        System.out.println("Funcionários:");
        imprimirFuncionarios(funcionarios);

        funcionarios.forEach(f -> {
            BigDecimal novoSalario = f.getSalario().multiply(new BigDecimal("1.10")).setScale(2, RoundingMode.HALF_UP);
            f.setSalario(novoSalario);
        });
        
        System.out.println("\nFuncionários Depois do aumento de 10%:");
        imprimirFuncionarios(funcionarios);

        Map<String, List<Funcionario>> funcionariosPorFuncao = funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao, LinkedHashMap::new, Collectors.toList()));

        System.out.println("\nFuncionários por função:");
        funcionariosPorFuncao.forEach((funcao, lista) -> {
            System.out.println("Função: " + funcao);
            imprimirFuncionarios(lista);
        });

        System.out.println("\nAniversariantes dos meses Outubro e Dezembro:");
        funcionarios.stream()
                .filter(f -> f.getDataNascimento().getMonthValue() == 10 || f.getDataNascimento().getMonthValue() == 12)
                .forEach(f -> System.out.println("Nome: " + f.getNome() + " | Data de Nascimento: " + f.getDataNascimento().format(DATA_BR)));

        System.out.println("\nFuncionário mais velho:");
        funcionarios.stream()
                .min(Comparator.comparing(Funcionario::getDataNascimento))
                .ifPresent(maisVelho -> {
                    int idade = Period.between(maisVelho.getDataNascimento(), LocalDate.now()).getYears();
                    System.out.println("Nome: " + maisVelho.getNome() + " | Idade: " + idade + " anos");
                });

        System.out.println("\nFuncionários por ordem alfabética:");
        funcionarios.stream()
                .sorted(Comparator.comparing(Funcionario::getNome, String.CASE_INSENSITIVE_ORDER))
                .forEach(f -> System.out.println(f.getNome()));

        BigDecimal totalSalarios = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("\nTotal dos salários: " + NUM_BR.format(totalSalarios));

        System.out.println("\nQuantidade de salários mínimos:");
        funcionarios.forEach(f -> {
            BigDecimal qtdSalariosMinimos = f.getSalario().divide(SALARIO_MINIMO, 2, RoundingMode.HALF_UP);
            System.out.println(f.getNome() + " ganha " + NUM_BR.format(qtdSalariosMinimos) + " salários mínimos.");
        });
    }

    private static List<Funcionario> inserirFuncionarios() {
        List<Funcionario> lista = new ArrayList<>();
        lista.add(new Funcionario("Maria", LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"));
        lista.add(new Funcionario("João", LocalDate.of(1990, 5, 12), new BigDecimal("2284.38"), "Operador"));
        lista.add(new Funcionario("Caio", LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"), "Coordenador"));
        lista.add(new Funcionario("Miguel", LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"));
        lista.add(new Funcionario("Alice", LocalDate.of(1995, 1, 5), new BigDecimal("2234.68"), "Recepcionista"));
        lista.add(new Funcionario("Heitor", LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"));
        lista.add(new Funcionario("Arthur", LocalDate.of(1993, 3, 31), new BigDecimal("4071.84"), "Contador"));
        lista.add(new Funcionario("Laura", LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"), "Gerente"));
        lista.add(new Funcionario("Heloísa", LocalDate.of(2003, 5, 24), new BigDecimal("1606.85"), "Eletricista"));
        lista.add(new Funcionario("Helena", LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"), "Gerente"));
        return lista;
    }

    private static void imprimirFuncionarios(List<Funcionario> lista) {
        for (Funcionario f : lista) {
            System.out.println("Nome: " + f.getNome() +
                    " | Data: " + f.getDataNascimento().format(DATA_BR) +
                    " | Salário: " + NUM_BR.format(f.getSalario()) +
                    " | Função: " + f.getFuncao());
        }
    }
}