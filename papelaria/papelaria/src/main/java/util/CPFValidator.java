package util;

public class CPFValidator {

    public static boolean isValidCPF(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^\\d]", "");

        // Verifica se possui 11 dígitos ou se todos os dígitos são iguais
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int sum = 0;
            // Primeiro dígito verificador
            for (int i = 0; i < 9; i++) {
                sum += (cpf.charAt(i) - '0') * (10 - i);
            }
            int firstDigit = 11 - (sum % 11);
            firstDigit = (firstDigit > 9) ? 0 : firstDigit;
            if (firstDigit != (cpf.charAt(9) - '0')) {
                return false;
            }

            // Segundo dígito verificador
            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += (cpf.charAt(i) - '0') * (11 - i);
            }
            int secondDigit = 11 - (sum % 11);
            secondDigit = (secondDigit > 9) ? 0 : secondDigit;

            return secondDigit == (cpf.charAt(10) - '0');
        } catch (Exception e) {
            return false;
        }
    }
}

