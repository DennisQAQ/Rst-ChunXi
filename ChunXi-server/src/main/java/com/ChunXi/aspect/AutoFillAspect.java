package com.ChunXi.aspect;

import com.ChunXi.annotation.AutoFill;
import com.ChunXi.constant.AutoFillConstant;
import com.ChunXi.context.BaseContext;
import com.ChunXi.enumeration.OperationType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
public class AutoFillAspect {


    @Pointcut("execution(* com.ChunXi.mapper.*.*(..)) && @annotation(com.ChunXi.annotation.AutoFill) ")
    public void autoFillPointCut(){}

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        //1，获取到当前拦截方法的数据库操作类型
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();
        //2,获取当前被拦截的方法的参数---实体对象
        Object[] args = joinPoint.getArgs();
        if (args==null&& args.length==0){
            return;
        }

        Object entity  = args[0];
         //3,准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long id = BaseContext.getCurrentId();

        if (operationType==OperationType.INSERT) {
            try {
                Method CREATE_TIME = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME,LocalDateTime.class);
                Method CREATE_USER = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER,Long.class);
                Method UPDATE_TIME = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
                Method UPDATE_USER = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);

                //通过反射为对象赋值
                CREATE_TIME.invoke(entity, now);
                CREATE_USER.invoke(entity,id);
                UPDATE_TIME.invoke(entity, now);
                UPDATE_USER.invoke(entity,id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else if(operationType == OperationType.UPDATE){
                try {
                    Method  UPDATE_TIME= entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
                    Method  UPDATE_USER= entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);

                    //通过反射为对象赋值
                    UPDATE_TIME.invoke(entity,now);
                    UPDATE_USER.invoke(entity,id);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }


        }
    }

