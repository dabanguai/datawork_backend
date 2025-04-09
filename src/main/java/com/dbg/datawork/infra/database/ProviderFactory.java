// package com.dbg.datawork.infra.database;
//
// import com.dbg.datawork.infra.constants.DatasourceType;
// import com.dbg.datawork.datasource.DatasourceProvider;
// import com.dbg.datawork.datasource.JdbcProvider;
// // import com.dbg.datawork.query.QueryProvider;
// import org.springframework.beans.BeansException;
// import org.springframework.context.ApplicationContext;
// import org.springframework.context.ApplicationContextAware;
// import org.springframework.stereotype.Component;
//
// /**
//  * @author 15968
//  * @version 1.0
//  * @description: TODO
//  * @date 2025/2/24 2:00
//  */
// @Component
// public class ProviderFactory implements ApplicationContextAware {
//
//     private static ApplicationContext context;
//
//     @Override
//     public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//         this.context = applicationContext;
//     }
//
//     // public static QueryProvider getQueryProvider(DatasourceType type) {
//     //     switch(type) {
//     //         case mysql:
//     //             return context.getBean("mysqlQuery", QueryProvider.class);
//     //         case PostgreSQL:
//     //             return context.getBean("pgQuery", QueryProvider.class);
//     //         default:
//     //             return context.getBean("mysqlQuery", QueryProvider.class);
//     //     }
//     // }
// }
