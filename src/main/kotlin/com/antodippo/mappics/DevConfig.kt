import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import io.github.cdimascio.dotenv.dotenv

@Configuration
@Profile("dev")
class DevConfiguration {

    @Bean
    fun loadDotenv() {
        dotenv().entries().map { System.setProperty(it.key, it.value) }
    }
}