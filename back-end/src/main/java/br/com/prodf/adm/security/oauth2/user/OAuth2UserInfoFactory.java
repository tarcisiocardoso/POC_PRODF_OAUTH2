package br.com.prodf.adm.security.oauth2.user;

import br.com.prodf.adm.exception.OAuth2AuthenticationProcessingException;
import br.com.prodf.adm.model.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        System.out.println(">>>registrationId<<<"+ registrationId );
        if(registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.facebook.toString())) {
            return new FacebookOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.github.toString())) {
            return new GithubOAuth2UserInfo(attributes);
        } else if ( registrationId.equalsIgnoreCase(AuthProvider.customClient.toString())){
            attributes.forEach((k,v)->{
                System.out.println(k+": "+v);
            });
            return new CustomOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
