package ljl.bilibili.chat.config;

import org.springframework.context.annotation.Configuration;
/**
 * 异步线程池配置类
*/
@Configuration
//@EnableAsync(proxyTargetClass = true)
public class AsyncConfig {
//    @Override
//    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
//        return new MyAsyncUncaughtExceptionHandler();
//    }

//    static class MyAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {
//        @Override
//        public void handleUncaughtException(Throwable ex, Method method, Object... params) {
//            System.err.println("Exception in async method: " + ex.getMessage());
//        }
//    }
//    implements AsyncConfigurer
//    @Bean(name = "customAsyncExecutor")
//    public Executor customAsyncExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(5);
//        executor.setMaxPoolSize(10);
//        executor.setQueueCapacity(25);
//        executor.setThreadNamePrefix("Custom-Async-");
//        executor.initialize();
//        return executor;
//}

//public class CustomAsyncExceptionHandler implements Thread.UncaughtExceptionHandler {
//    @Override
//    public void uncaughtException(Thread t, Throwable e) {

//        System.err.println("Uncaught exception in thread '" + t.getName() + "': " + e.getMessage());
//    }
//}


//executor.setRejectedExecutionHandler(new CustomAsyncExceptionHandler());
//@Async("customAsyncExecutor")
//public void asyncMethod() {

//}
//@Autowired
//private AutowireCapableBeanFactory beanFactory;
//
//    public MyObject createMyObject() {
//        MyObject myObject = new MyObject();
//        beanFactory.autowireBean(myObject);
//        return myObject;
//    }
    //    import org.springframework.context.ApplicationContext;
    //import org.springframework.context.ApplicationContextAware;
    //
    //    public class MyObject implements ApplicationContextAware {
    //        private ApplicationEventPublisher applicationEventPublisher;
    //
    //        @Override
    //        public void setApplicationContext(ApplicationContext applicationContext) {
    //            this.applicationEventPublisher = applicationContext;
    //        }
    //
    //
    //    }
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;

//    public class MyObject implements ApplicationContextAware {
//        private ApplicationEventPublisher applicationEventPublisher;
//
//        @Override
//        public void setApplicationContext(ApplicationContext applicationContext) {
//            this.applicationEventPublisher = applicationContext;
//        }
//
//
//    }

}
