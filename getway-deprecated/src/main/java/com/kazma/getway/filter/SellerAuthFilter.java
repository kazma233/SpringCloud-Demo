package com.kazma.getway.filter;

import com.kazma.getway.constants.CookieCons;
import com.kazma.getway.constants.RedisCons;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 权限拦截 区分买家和卖家
 */
@Component
public class SellerAuthFilter extends ZuulFilter {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE; // 前置拦截器
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER; // 越小越靠前
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();

        /*
         * /order/finish 只能卖家访问(cookie 理由token, 并且对应的redis有值)
         * /product/list 都可以访问
         */

        Cookie[] cookies = request.getCookies();

        if ("/order/order/finish".equals(request.getRequestURI())) {
            if (cookies == null) {
                currentContext.setSendZuulResponse(false);
                currentContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
                return null;
            }
            String value = null;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(CookieCons.OPENID)) {
                    value = cookie.getValue();
                    break;
                }
            }
            if (StringUtils.isBlank(value)) {
                currentContext.setSendZuulResponse(false);
                currentContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
                return null;
            }

            String key = String.format(RedisCons.TOKEN_TEMPLATE, value);
            if (StringUtils.isBlank(redisTemplate.opsForValue().get(key))) {
                currentContext.setSendZuulResponse(false);
                currentContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
                return null;
            }
        }

        return null;
    }
}
