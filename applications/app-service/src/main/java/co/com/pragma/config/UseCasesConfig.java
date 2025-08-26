package co.com.pragma.config;

import co.com.pragma.model.cliente.gateway.ClienteGateway;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.usecase.solicitud.SolicitudUseCase;
import co.com.pragma.usecase.solicitud.in.SolicitudUseCasePort;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = "co.com.pragma.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

    private final SolicitudRepository solicitudRepository;
    private final ClienteGateway clienteGateway;

    public UseCasesConfig(SolicitudRepository solicitudRepository, ClienteGateway clienteGateway) {
        this.solicitudRepository = solicitudRepository;
        this.clienteGateway = clienteGateway;
    }

    @Bean
    @Primary
    public SolicitudUseCasePort solicitudUseCasePort(){
        return new SolicitudUseCase(solicitudRepository, clienteGateway);
    }
}
