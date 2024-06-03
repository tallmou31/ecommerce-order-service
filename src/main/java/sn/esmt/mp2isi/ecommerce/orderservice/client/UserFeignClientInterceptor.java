package sn.esmt.mp2isi.ecommerce.orderservice.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sn.esmt.mp2isi.ecommerce.orderservice.security.SecurityUtils;

@Component
public class UserFeignClientInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer";
    private static final Logger log = LoggerFactory.getLogger(UserFeignClientInterceptor.class);

    @Override
    public void apply(RequestTemplate template) {
        log.info("Security utils token : {}", SecurityUtils.getCurrentUserJWT().orElseThrow());
        SecurityUtils.getCurrentUserJWT().ifPresent(s -> template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER, s)));
    }
}
