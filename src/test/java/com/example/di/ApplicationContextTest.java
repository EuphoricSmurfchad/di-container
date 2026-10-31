package com.example.di;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.example.ApplicationContext;
import com.example.Component;
import com.example.Inject;

public class ApplicationContextTest {

    @Component
    public static class SimpleService {

    }

    @Test
    public void shouldCreateBeanWithoutDependencies() {
        ApplicationContext context = new ApplicationContext();
        context.scan(SimpleService.class);

        SimpleService bean = context.getBean(SimpleService.class);

        Assertions.assertNotNull(bean);
    }

    @Component
    public static class ServiceA {

    }

    @Component
    public static class ServiceB {

        @Inject
        public ServiceA a;
    }

    @Test
    public void shouldCreateBeanWithOneDependency() {

        ApplicationContext context = new ApplicationContext();
        context.scan(ServiceA.class, ServiceB.class);
        ServiceB b = context.getBean(ServiceB.class);
        Assertions.assertNotNull(b.a);
    }

    @Component
    public static class NeedA {

        @Inject 
        public MissingA a;

    }

    public static class MissingA {

    }

    @Test 
    public void shouldThrowWhenBeanNotFound() {
        ApplicationContext context = new ApplicationContext();
        Assertions.assertThrows(RuntimeException.class, () -> context.scan(NeedA.class));
    }

    @Component 
    public static class A {

        @Inject 
        public B b;
    }

    @Component 
    public static class B {

        @Inject 
        public C c;
    }

    @Component 
    public static class C {}

    @Test 
    public void shouldCreateTripleDependencies(){
        ApplicationContext context = new ApplicationContext();
        context.scan(A.class, B.class, C.class);
        A a = context.getBean(A.class);
        Assertions.assertNotNull(a.b);
        Assertions.assertNotNull(a.b.c);
    }

    @Component
    public static class MultiInject{
        @Inject 
        public AA a;

        @Inject 
        public BB b;
    }

    @Component
    public static class AA {
        
    }
    
    @Component
    public static class BB {
        
    }

    @Test 
    public void shouldCreateMultiInject() {
        ApplicationContext context = new ApplicationContext();
        context.scan(MultiInject.class, AA.class, BB.class);
        MultiInject m = context.getBean(MultiInject.class);
        Assertions.assertNotNull(m.a);
        Assertions.assertNotNull(m.b);
    }

    @Component
    public static class FirstBean {
    }

    @Component
    public static class SecondBean {
    }

    @Test
    public void shouldAddBeansOnMultipleScans() {
        ApplicationContext context = new ApplicationContext();
        context.scan(FirstBean.class);
        context.scan(SecondBean.class);

        Assertions.assertNotNull(context.getBean(FirstBean.class));
        Assertions.assertNotNull(context.getBean(SecondBean.class));
    }
}
