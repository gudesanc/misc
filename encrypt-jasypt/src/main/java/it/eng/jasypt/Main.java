package it.eng.jasypt;

import org.jasypt.util.text.BasicTextEncryptor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static final String SECRET_KEY_CRYPT = "CoNfIgCrYpTkEy";
    private static final List<Integer> orarioInzioTurnoSecondario = new ArrayList<>();

    static {
        orarioInzioTurnoSecondario.add(0);
        orarioInzioTurnoSecondario.add(6);
        orarioInzioTurnoSecondario.add(7);
        orarioInzioTurnoSecondario.add(8);
        orarioInzioTurnoSecondario.add(9);
        orarioInzioTurnoSecondario.add(10);
        orarioInzioTurnoSecondario.add(11);
        orarioInzioTurnoSecondario.add(12);
        orarioInzioTurnoSecondario.add(13);
        orarioInzioTurnoSecondario.add(14);
        orarioInzioTurnoSecondario.add(15);
        orarioInzioTurnoSecondario.add(16);
        orarioInzioTurnoSecondario.add(17);
        orarioInzioTurnoSecondario.add(18);
        orarioInzioTurnoSecondario.add(19);
        orarioInzioTurnoSecondario.add(20);
        orarioInzioTurnoSecondario.add(21);
        orarioInzioTurnoSecondario.add(22);
        orarioInzioTurnoSecondario.add(23);
    }

    public static void main(String[] args) {
//        new Main().decrypt("zM6v816MdMoZBf+j0T8i3DJe13shLzl+");
        for (int i = 0; i< orarioInzioTurnoSecondario.size(); i++) {
            int start = orarioInzioTurnoSecondario.get(i);
            getOrariInizio(start, start + 9);
        }
    }

    private static List<Integer> getOrariInizio(Integer oraInzioTurnoOrdinario, Integer oraFineTurnoOrdinario) {
        final int oraLimteInzioSecondario = normalizzaOrario(oraFineTurnoOrdinario + 2);
        final int oraLimiteFineSecondario = normalizzaOrario(oraInzioTurnoOrdinario - 6);
        System.out.print("Turno Ordinario [" + oraInzioTurnoOrdinario + "-" + oraFineTurnoOrdinario + "]");
        System.out.print(" - Limite Secondario [" + oraLimteInzioSecondario + "-" + oraLimiteFineSecondario + "] - ");
        List<Integer> orari = orarioInzioTurnoSecondario.stream().filter(o -> isInRange(o, oraLimteInzioSecondario, oraLimiteFineSecondario))
                .collect(Collectors.toList());
        System.out.println(orari);
        return orari;
    }

    private static Integer normalizzaOrario(int orario) {
        if (orario > 23) {
            return orario - 24;
        }
        if (orario < 0) {
            return orario + 24;
        }
        return orario;
    }

    private static boolean isInRange(int oraTest, int oraInizioLimite, int oraFineLimite) {
        oraTest = normalizzaOrario(oraTest);
        if (oraInizioLimite < oraFineLimite) {
            return oraTest >= oraInizioLimite && oraTest <= oraFineLimite;
        }
        return oraTest >= oraInizioLimite || oraTest <= oraFineLimite;
    }

    private void decrypt(String what) {
        BasicTextEncryptor criptator = new BasicTextEncryptor();
        criptator.setPasswordCharArray(SECRET_KEY_CRYPT.toCharArray());
        String deCryptedPwd = criptator.decrypt(what);
        System.out.println(deCryptedPwd);

    }

}



