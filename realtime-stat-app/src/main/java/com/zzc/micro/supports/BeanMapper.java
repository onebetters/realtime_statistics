package com.zzc.micro.supports;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.metadata.Type;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 映射工具
 * <p>Filename: com.zzc.micro.supports.BeanMapper.java</p>
 * <p>Date: 2022-06-27, 周一, 19:36:14.</p>
 *
 * @author zhichuanzhang
 * @version 0.1.0
 */
@Component
public class BeanMapper implements ApplicationContextAware {

    /**
     * 映射器
     */
    private static MapperFacade mapperFacade;

    /**
     * 映射
     * @param src
     * @param destClass
     * @param <TSrc>
     * @param <TDest>
     * @return
     */
    public static <TSrc, TDest> TDest map(TSrc src, Class<TDest> destClass) {
        return mapperFacade.map(src, destClass);
    }

    /**
     * 映射
     * @param src
     * @param dest
     * @param <TSrc>
     * @param <TDest>
     */
    public static <TSrc, TDest> void map(TSrc src, TDest dest) {
        mapperFacade.map(src, dest);
    }

    /**
     * 映射
     * @param srcs
     * @param destClass
     * @param <TSrc>
     * @param <TDest>
     * @return
     */
    public static <TSrc, TDest> List<TDest> mapAsList(Iterable<TSrc> srcs, Class<TDest> destClass) {
        return mapperFacade.mapAsList(srcs, destClass);
    }

    public static <Sk, Sv, Dk, Dv> Map<Dk, Dv> mapAsMap(final Map<Sk, Sv> source, final Type<? extends Map<Sk, Sv>> sourceType,
                                                 final Type<? extends Map<Dk, Dv>> destinationType) {
        return mapperFacade.mapAsMap(source, sourceType, destinationType);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        mapperFacade = applicationContext.getBean(MapperFacade.class);
    }
}
