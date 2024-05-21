package br.com.conversor;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Scanner;

public class CurrencyConverter {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.00");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CurrencyService currencyService = new CurrencyService();

        System.out.println("Bem-vindo ao Conversor de Moedas!");

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.print("Digite a moeda base (ex: USD) ou 'sair' para encerrar: ");
            String baseCurrency = scanner.nextLine().toUpperCase();

            if (baseCurrency.equalsIgnoreCase("sair")) {
                continueRunning = false;
                break;
            }

            try {
                Map<String, Currency> rates = currencyService.getExchangeRates(baseCurrency);
                System.out.println("Taxas de câmbio para " + baseCurrency + ":");
                rates.forEach((code, currency) -> {
                    System.out.println(code + ": " + decimalFormat.format(currency.getRate()));
                });

                System.out.print("Digite a moeda de destino: ");
                String targetCurrency = scanner.nextLine().toUpperCase();

                System.out.print("Digite o valor a ser convertido: ");
                double amount = scanner.nextDouble();
                scanner.nextLine(); // Consume the newline

                if (rates.containsKey(targetCurrency)) {
                    double rate = rates.get(targetCurrency).getRate();
                    double convertedAmount = amount * rate;
                    System.out.println(decimalFormat.format(amount) + " " + baseCurrency + " = " + decimalFormat.format(convertedAmount) + " " + targetCurrency);
                } else {
                    System.out.println("Moeda de destino não encontrada.");
                }

            } catch (IOException e) {
                System.err.println("Erro ao obter taxas de câmbio: " + e.getMessage());
            }

            System.out.println();
        }

        System.out.println("Programa encerrado. Obrigado por usar o Conversor de Moedas!");
    }
}