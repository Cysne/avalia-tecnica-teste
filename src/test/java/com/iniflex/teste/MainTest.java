package com.iniflex.teste;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private List<Funcionario> listaBase() {
        return new ArrayList<>(Arrays.asList(
                new Funcionario("Maria",   LocalDate.of(2000,10,18), new BigDecimal("2009.44"),  "Operador"),
                new Funcionario("João",    LocalDate.of(1990, 5,12), new BigDecimal("2284.38"),  "Operador"),
                new Funcionario("Caio",    LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"),  "Coordenador"),
                new Funcionario("Miguel",  LocalDate.of(1988,10,14), new BigDecimal("19119.88"), "Diretor"),
                new Funcionario("Alice",   LocalDate.of(1995, 1, 5), new BigDecimal("2234.68"),  "Recepcionista"),
                new Funcionario("Heitor",  LocalDate.of(1999,11,19), new BigDecimal("1582.72"),  "Operador"),
                new Funcionario("Arthur",  LocalDate.of(1993, 3,31), new BigDecimal("4071.84"),  "Contador"),
                new Funcionario("Laura",   LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"),  "Gerente"),
                new Funcionario("Heloísa", LocalDate.of(2003, 5,24), new BigDecimal("1606.85"),  "Eletricista"),
                new Funcionario("Helena",  LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"),  "Gerente")
        ));
    }

    private List<Funcionario> semJoao(List<Funcionario> funcionarios) {
        List<Funcionario> copia = new ArrayList<>(funcionarios);
        for (int i = 0; i < copia.size(); i++) {
            if (copia.get(i).getNome().equals("João")) {
                copia.remove(i);
                break;
            }
        }
        return copia;
    }

    private void aplicarAumento10(List<Funcionario> funcionarios) {
        for (Funcionario f : funcionarios) {
            BigDecimal novo = f.getSalario().multiply(new BigDecimal("1.10")).setScale(2, RoundingMode.HALF_UP);
            f.setSalario(novo);
        }
    }

    @Test
    void removeJoaoDaLista() {
        List<Funcionario> sem = semJoao(listaBase());
        assertEquals(9, sem.size());
        assertTrue(sem.stream().noneMatch(f -> f.getNome().equalsIgnoreCase("João")));
    }

    @Test
    void aumentoDezPorCento_Maria221038() {
        List<Funcionario> sem = semJoao(listaBase());
        aplicarAumento10(sem);
        BigDecimal salarioMaria = sem.stream()
                .filter(f -> f.getNome().equals("Maria"))
                .findFirst().orElseThrow().getSalario();
        assertEquals(new BigDecimal("2210.38"), salarioMaria);
    }

    @Test
    void aniversariantesMes10e12_somenteMariaEMiguel() {
        List<Funcionario> sem = semJoao(listaBase());
        Set<String> nomes = new HashSet<>();
        for (Funcionario f : sem) {
            int mes = f.getDataNascimento().getMonthValue();
            if (mes == 10 || mes == 12) nomes.add(f.getNome());
        }
        assertEquals(Set.of("Maria", "Miguel"), nomes);
    }

    @Test
    void maisVelho_eOCaio() {
        List<Funcionario> sem = semJoao(listaBase());
        Funcionario maisVelho = sem.get(0);
        for (Funcionario f : sem) {
            if (f.getDataNascimento().isBefore(maisVelho.getDataNascimento())) {
                maisVelho = f;
            }
        }
        assertEquals("Caio", maisVelho.getNome());
    }

    @Test
    void totalSalarios_aposAumentoERemocao() {
        List<Funcionario> sem = semJoao(listaBase());
        aplicarAumento10(sem);
        BigDecimal total = BigDecimal.ZERO;
        for (Funcionario f : sem) total = total.add(f.getSalario());
        assertEquals(new BigDecimal("50906.82"), total);
    }

    @Test
    void salariosMinimos_MiguelAposAumento17_35() {
        List<Funcionario> sem = semJoao(listaBase());
        aplicarAumento10(sem);
        BigDecimal minimo = new BigDecimal("1212.00");
        Funcionario miguel = sem.stream().filter(f -> f.getNome().equals("Miguel")).findFirst().orElseThrow();
        BigDecimal qtd = miguel.getSalario().divide(minimo, 2, RoundingMode.HALF_UP);
        assertEquals(new BigDecimal("17.35"), qtd);
    }
}
