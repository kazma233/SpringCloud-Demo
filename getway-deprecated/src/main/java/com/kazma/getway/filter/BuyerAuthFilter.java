package com.kazma.getway.filter;

import com.kazma.getway.constants.CookieCons;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 权限拦截 区分买家和卖家
 */
@Component
public class BuyerAuthFilter extends ZuulFilter {

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

        /* /order/create 只能买家访问(cookie 里有openid)
         * /product/list 都可以访问
         */

        Cookie[] cookies = request.getCookies();

        if ("/order/order/create".equals(request.getRequestURI())) {
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
        }
        return null;
    }
}
