package co.com.pragma.config;

import co.com.pragma.model.cliente.gateway.ClienteGateway;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.usecase.solicitud.SolicitudUseCase;
import co.com.pragma.usecase.solicitud.in.SolicitudUseCasePort;
import co.com.pragma.usecase.tipoprestamo.in.TipoPrestamoUseCasePort;
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
    private final TipoPrestamoUseCasePort tipoPrestamoUseCasePort;

    public UseCasesConfig(SolicitudRepository solicitudRepository, ClienteGateway clienteGateway, TipoPrestamoUseCasePort tipoPrestamoUseCasePort) {
        this.solicitudRepository = solicitudRepository;
        this.clienteGateway = clienteGateway;
        this.tipoPrestamoUseCasePort = tipoPrestamoUseCasePort;
    }

    @Bean
    @Primary
    public SolicitudUseCasePort solicitudUseCasePort(){
        return new SolicitudUseCase(solicitudRepository, clienteGateway, tipoPrestamoUseCasePort);
    }
}
