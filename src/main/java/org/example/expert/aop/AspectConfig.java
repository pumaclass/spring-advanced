package org.example.expert.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class AspectConfig {
    @Pointcut("execution(public * org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..))")
    private void deleteCommentPointCut() {}

    @Pointcut("execution(public * org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    private void changeUserRolePointCut() {}

    @Around("deleteCommentPointCut() || changeUserRolePointCut()")
    public Object deleteCommentTest(ProceedingJoinPoint joinPoint) throws Throwable{
        String requestId = UUID.randomUUID().toString();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String requestUri = request.getRequestURI();
        Long userId = (Long) request.getAttribute("userId");
        String time = LocalDateTime.now().toString();
        try{
            log.info("BEFORE [{}]: 로깅 시간: {}, URI: {}, 유저ID: {}", requestId, time, requestUri, userId);
            Object result = joinPoint.proceed();
            return result;
        }catch (Exception e){
            log.error("EXCEPTION [{}]: 로깅 시간: {}, URI: {}, 유저ID: {}, 에러: {}", requestId, time, requestUri, userId, e.getMessage());
            throw e;
        }finally {
            log.info("AFTER : " + "로깅 시간 : " + time + " URI : " + requestUri + " 유저ID : " + userId);
        }
    }
}
