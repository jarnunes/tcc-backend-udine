package com.pucminas.udinetour;

import com.pucminas.udinetour.functions.ConcatFunction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;
import java.util.function.UnaryOperator;

@SpringBootApplication
public class UdineTourApplication {

    public static void main(String[] args) {
        SpringApplication.run(UdineTourApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext context){
        return args -> {
            FunctionCatalog catalog  = context.getBean(FunctionCatalog.class);
//            Function<String, String> reverse = catalog.lookup(Function.class, "reverseString");
//            System.out.println("Reverse: " +reverse.apply("Hello World"));

            Function<String, String> concat = catalog.lookup( "concatFunction");
            Function<String, String> conc =  catalog.lookup(ConcatFunction.class, null);

            System.out.println("Concat: " +concat.apply("Hello World"));
        };
    }

//    @Bean
//    public Function<String, String> reverseString() {
//        return value -> new StringBuilder(value).reverse().toString();
//    }
}
