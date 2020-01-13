package com.icss.mvp.entity.pageHelper;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.github.pagehelper.PageHelper;

@Aspect
public class PageHelperAspect {

    @Around("execution(* com.icss.mvp.dao.*.*(..,com.icss.mvp.entity.pageHelper.PageEntity))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] params = pjp.getArgs();
        for (Object object : params) {
            if (object instanceof PageEntity) {
                PageEntity page = (PageEntity) object;
                PageHelper.startPage(page.getPage(), page.getRows());
                break;
            }
        }
        return pjp.proceed();
    }
}
