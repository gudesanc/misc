import name.velikodniy.vitaliy.fixedlength.Align;
import name.velikodniy.vitaliy.fixedlength.annotation.FixedField;
import name.velikodniy.vitaliy.fixedlength.annotation.FixedLine;


@FixedLine
public record TracciatoMCTCDTO (
        @FixedField(offset = 1, length = 2, align = Align.RIGHT)
        String tipoRecord,

        @FixedField(offset = 3, length = 9, align = Align.LEFT)
        String targa,

        @FixedField(offset = 12, length = 8, align = Align.RIGHT)
        String dataInfrazione,

        @FixedField(offset = 20, length = 6, align = Align.LEFT)
        String numeroVerbale,

        @FixedField(offset = 26, length = 8, align = Align.RIGHT)
        String dataRichiesta,

        @FixedField(offset = 34, length = 1, align = Align.LEFT)
        String indicatorePersonaSocieta,

        @FixedField(offset = 35, length = 70, align = Align.LEFT)
        String anagraficaProprietario,

        @FixedField(offset = 105, length = 22, align = Align.LEFT)
        String comuneNascita,

        @FixedField(offset = 127, length = 2, align = Align.LEFT)
        String provinciaNascita,

        @FixedField(offset = 129, length = 3, align = Align.LEFT)
        String siglaStatoEsteroNascita,

        @FixedField(offset = 132, length = 8, align = Align.RIGHT)
        String dataNascita,

        @FixedField(offset = 140, length = 22, align = Align.LEFT)
        String localitaResidenza,

        @FixedField(offset = 162, length = 2, align = Align.LEFT)
        String provinciaResidenza,

        @FixedField(offset = 164, length = 3, align = Align.LEFT)
        String statoEsteroResidenza,

        @FixedField(offset = 167, length = 5, align = Align.LEFT)
        String toponimoIndirizzoResidenza,

        @FixedField(offset = 172, length = 34, align = Align.LEFT)
        String indirizzoResidenza,

        @FixedField(offset = 206, length = 6, align = Align.RIGHT)
        String numeroCivicoResidenza,

        @FixedField(offset = 212, length = 5, align = Align.RIGHT)
        String codiceAvviamentoPostale,

        @FixedField(offset = 217, length = 40, align = Align.LEFT)
        String fabbricaETipo,

        @FixedField(offset = 257, length = 42, align = Align.LEFT)
        String marcaModello,

        @FixedField(offset = 299, length = 40, align = Align.LEFT)
        String denominazioneCommerciale,

        @FixedField(offset = 339, length = 10, align = Align.LEFT)
        String tipoModello,

        @FixedField(offset = 349, length = 25, align = Align.LEFT)
        String varianteModello,

        @FixedField(offset = 374, length = 35, align = Align.LEFT)
        String versioneModello,

        @FixedField(offset = 409, length = 3, align = Align.RIGHT)
        String potenzaFiscale,

        @FixedField(offset = 412, length = 5, align = Align.RIGHT)
        String portataUtile,

        @FixedField(offset = 417, length = 8, align = Align.RIGHT)
        String dataInizioProprieta,

        @FixedField(offset = 425, length = 1, align = Align.LEFT)
        String indicatoreTrasportoMerci,

        @FixedField(offset = 426, length = 8, align = Align.RIGHT)
        String dataConseguimentoGuida,

        @FixedField(offset = 434, length = 1, align = Align.LEFT)
        String indicatoreLocatario,

        @FixedField(offset = 435, length = 10, align = Align.LEFT)
        String patente,

        @FixedField(offset = 445, length = 6, align = Align.RIGHT)
        String massaComplessiva,

        @FixedField(offset = 451, length = 2, align = Align.LEFT)
        String categoriaUsoVeicolo,

        @FixedField(offset = 453, length = 16, align = Align.LEFT)
        String codiceFiscaleIntestatario,

        @FixedField(offset = 469, length = 2, align = Align.LEFT)
        String carrozzeriaVeicolo,

        @FixedField(offset = 471, length = 5, align = Align.LEFT)
        String categoriaEuro,

        @FixedField(offset = 476, length = 30, align = Align.LEFT)
        String alimentazioneVeicolo,

        @FixedField(offset = 506, length = 6, align = Align.RIGHT)
        String emissioniNox,

        @FixedField(offset = 512, length = 6, align = Align.RIGHT)
        String emissioniParticolato,

        @FixedField(offset = 518, length = 5, align = Align.RIGHT)
        String emissioniCO2,

        @FixedField(offset = 523, length = 4, align = Align.RIGHT)
        String coefficienteAssorbimento,

        @FixedField(offset = 527, length = 3, align = Align.LEFT)
        String codiceDirettivaCEE,

        @FixedField(offset = 530, length = 8, align = Align.RIGHT)
        String dataUltimaRevisione,

        @FixedField(offset = 538, length = 1, align = Align.LEFT)
        String esitoUltimaRevisione,

        @FixedField(offset = 539, length = 12, align = Align.LEFT)
        String codiceAntifalsificazione,

        @FixedField(offset = 551, length = 10, align = Align.LEFT)
        String spazioADisposizione
) {}

