package com.dzd.phonebook.aop;



import com.dzd.phonebook.util.DzdPageParam;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author dlluo
 * @ClassName: AspectPagination
 * @Description: TODO
 * @date 2016年9月5日
 */
@Aspect
@Component
public class AspectPaginationLj {
    private Logger logger = LoggerFactory.getLogger(AspectPaginationLj.class);
   
    @Around(value = "execution(* com.dzd.base.dao.BaseDao+.*Page(..))")
    public Object aroundMethod(ProceedingJoinPoint pjd) throws Throwable {
        logger.info("<<<<<<<<<<<<<<Aspect page start");
        Object[] parames = pjd.getArgs();
        if (ArrayUtils.isEmpty(parames)) {
            return null;
        }
        DzdPageParam pageParam = (DzdPageParam) parames[0];
        PageHelper.startPage(pageParam.getStart(), pageParam.getLimit());
        logger.info("<<<<<<<<<<<<<<Aspect page end");
        return pjd.proceed();

    }
}
