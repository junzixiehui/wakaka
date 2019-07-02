package com.junzixiehui.wakaka.infrastructure.common.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * 静态
 *
 * @author zhoutao
 * @date 2018/11/2
 */
@Configuration
public class StaticResourcesConfig extends WebMvcConfigurerAdapter {
    //静态资源统一后缀
    private static final String HTML_SUFFIX = "/**/*.html";
    private static final String JS_SUFFIX = "/**/*.js";
    private static final String CSS_SUFFIX = "/**/*.css";
    private static final String JPG_SUFFIX = "/**/*.jpg";
    private static final String JPEG_SUFFIX = "/**/*.jpeg";
    private static final String GIF_SUFFIX = "/**/*.gif";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST")
                .allowCredentials(false);   //不允许Cookie跨域
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //管理后台静态资源相对路径
        registry.addResourceHandler(this.genStaticPathPatterns("/admin")).addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/admin/");
        //文档静态资源相对路径
        registry.addResourceHandler(this.genStaticPathPatterns("/docs")).addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/docs/");
        super.addResourceHandlers(registry);
    }

    private String[] genStaticPathPatterns(String prefix) {
        List<String> staticPathPatternsList = Lists.newArrayList();
        staticPathPatternsList.add(prefix + HTML_SUFFIX);
        staticPathPatternsList.add(prefix + JS_SUFFIX);
        staticPathPatternsList.add(prefix + CSS_SUFFIX);
        staticPathPatternsList.add(prefix + JPG_SUFFIX);
        staticPathPatternsList.add(prefix + JPEG_SUFFIX);
        staticPathPatternsList.add(prefix + GIF_SUFFIX);
        return staticPathPatternsList.toArray(new String[staticPathPatternsList.size()]);
    }
}
