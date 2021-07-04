package test.thiamath.user.springserver.config

import com.thiamath.user.service.UserService
import io.grpc.ServerBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Configuration
class Configuration {

    @Bean
    fun userService() = UserService()

    @Bean
    fun grpcServer() = ServerBuilder.forPort(8980).addService(userService()).build()!!

    @PostConstruct
    fun startup() {
        grpcServer().start()
        print("Server started ${grpcServer()}")
    }

    @PreDestroy
    fun cleanUp() {
        grpcServer().shutdown().awaitTermination(30, TimeUnit.SECONDS)
    }
}