import name.velikodniy.vitaliy.fixedlength.FixedLength;

import java.io.Reader;
import java.io.StringReader;

public class Main {
    public static void main(String[] args)  throws Exception{
        String gino = "90DGA236RW 05042023      26042023 ZOMPATORI*GINO                                                        BOVILLE ERNICA        FR   04031968BOVILLE ERNICA        FR   VIA  ANTICA                            23    03022AUDI 8E ABRDF1 FM6XH0R8E707GG                                                                                                                                                                   0200047527042007 03051986 U13K42588P002060A0ZMPGNI68C04A720ZACEURO4GASOL + DISPOSITIVO F.A.P.          0.0010154.00.50Z7 02082021PFRAO9BQ3JBD          0";
        Reader reader = new StringReader(gino);
        TracciatoMCTCDTO result = new FixedLength<TracciatoMCTCDTO>()
                .registerLineType(TracciatoMCTCDTO.class)
                .parse(reader).get(0);
        System.out.println(result);

    }
}
