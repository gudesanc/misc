package org.gds.misc.jks;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class Main {

    private Collection<JWKProvierConfig> jwkProvidersConfig = new ArrayList<>();

    public static void main(String[] args) throws MalformedURLException {
//        UrlJwkProvider jwkProvider;
//        if(proxy!=null){
//            jwkProvider = new UrlJwkProvider(new URL(jwkProviderUrl), null, null, proxy);
//        }else{
//            jwkProvider = new UrlJwkProvider(new URL(jwkProviderUrl));
//        }
//        Jwk jwk = jwkProvider.get(keyId);
        Main me = new Main();

        me.initJwkProvidersConfig();
        System.out.println(me.getVerifier("lm22r/8nqwjEdpkwR+449xKg7z0g="));
    }

    private JWTVerifier getVerifier(final String keyId){
//        for(JWKProvierConfig conf: jwkProvidersConfig){
//            JWTVerifier candidate = getVerifier(keyId, conf);
//            if(candidate!=null){
//                return candidate;
//            }
//        }
////return default
//        return null;
        Optional<JWTVerifier> candidate = jwkProvidersConfig.stream().map(p -> p.loadKey(keyId)).filter(k -> k!=null).findFirst();
        return candidate.isPresent()?candidate.get():null;
    }
//
//    private JWTVerifier getVerifier(String keyId, JWKProvierConfig config ){
//        try {
//            UrlJwkProvider candidate = config.getProvier();
//            Jwk jwk = candidate.get(keyId);
//            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
//            return JWT.require(algorithm).build();
//        }catch (Throwable t){
//            t.printStackTrace();
//            return null;
//        }
//    }

    private void initJwkProvidersConfig() throws MalformedURLException {
        jwkProvidersConfig.add(new JWKProvierConfig("https://identity.ws-comune.roma.it/oauth2/jwks","proxy.eng.it","3128"));
        jwkProvidersConfig.add(new JWKProvierConfig("https://sso.comune.roma.it/ssoservice/oauth2/realms/root/realms/public/connect/jwk_uri",null,null));
    }
}
class JWKProvierConfig{
    private final UrlJwkProvider jwkProvider;

    public JWKProvierConfig(String url, String proxyHost, String proxyPort) throws MalformedURLException {
        URL providerUrl = new URL(url);
        if(proxyHost!=null && proxyHost.trim().length()>0 && proxyPort!=null && proxyPort.trim().length()>0){
            Proxy  proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress(proxyHost,Integer.valueOf(proxyPort)));
            jwkProvider = new UrlJwkProvider(providerUrl, null, null, proxy);
        }else{
            jwkProvider = new UrlJwkProvider(providerUrl);
        }
    }

    public JWTVerifier loadKey(String keyId){
        try {
            Jwk jwk = jwkProvider.get(keyId);
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
            return JWT.require(algorithm).build();
        }catch (Throwable t){
            System.out.println(t.getMessage());
            return null;
        }

    }

}
