package co.com.pragma.config;

import co.com.pragma.model.cliente.gateway.ClienteGateway;
import co.com.pragma.model.messagesender.gateways.MessageSenderRepository;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.model.tipoprestamo.gateways.TipoPrestamoRepository;
import co.com.pragma.usecase.messagesender.MessageSenderUseCase;
import co.com.pragma.usecase.messagesender.in.MessageSenderUseCasePort;
import co.com.pragma.usecase.solicitud.SolicitudUseCase;
import co.com.pragma.usecase.solicitud.in.SolicitudUseCasePort;
import co.com.pragma.usecase.tipoprestamo.TipoPrestamoUseCase;
import co.com.pragma.usecase.tipoprestamo.in.TipoPrestamoUseCasePort;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final MessageSenderRepository messageSenderRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;

    public UseCasesConfig(SolicitudRepository solicitudRepository,
                          ClienteGateway clienteGateway,
                          TipoPrestamoUseCasePort tipoPrestamoUseCasePort,
                          MessageSenderRepository messageSenderRepository,
                          TipoPrestamoRepository tipoPrestamoRepository) {
        this.messageSenderRepository = messageSenderRepository;
        this.solicitudRepository = solicitudRepository;
        this.clienteGateway = clienteGateway;
        this.tipoPrestamoUseCasePort = tipoPrestamoUseCasePort;
        this.tipoPrestamoRepository = tipoPrestamoRepository;
    }

    @Bean
    @Primary
    public SolicitudUseCasePort solicitudUseCasePort(){
        return new SolicitudUseCase(solicitudRepository, clienteGateway,
                tipoPrestamoUseCasePort,
                messageSenderRepository);
    }


    @Bean
    @Primary
    public MessageSenderUseCasePort messageSenderUseCasePort(){
        return new MessageSenderUseCase(messageSenderRepository);
    }

    @Bean
    @Primary
    public TipoPrestamoUseCasePort tipoPrestamoUseCasePort(){
        return new  TipoPrestamoUseCase(tipoPrestamoRepository);
    }

}
