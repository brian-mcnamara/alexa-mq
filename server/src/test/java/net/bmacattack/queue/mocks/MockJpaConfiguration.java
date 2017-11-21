package net.bmacattack.queue.mocks;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class MockJpaConfiguration {
    @Bean
    @Primary
    public DataSource getMockDatasource() {
        return Mockito.mock(DataSource.class);
    }
}
