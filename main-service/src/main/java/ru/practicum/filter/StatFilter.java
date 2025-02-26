package ru.practicum.filter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import ru.practicum.StatClient;
import ru.practicum.client.EventStatClient;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.regex.Pattern;

@Slf4j
@Component
public class StatFilter extends AbstractRequestLoggingFilter {

    private static Set<Pattern> PATTERNS = Set.of(
            Pattern.compile("^/events$"),
            Pattern.compile("^/events/\\d+$")
    );

    private final StatClient statClient;
    private final String app;

    public StatFilter(StatClient statClient, @Value("${spring.application.name}") String app) {
        this.statClient = statClient;
        this.app = app;
    }

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return PATTERNS.stream().anyMatch(pattern -> pattern.matcher(uri).find());
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        log.info("Обращение к эндпоинту {} с IP = {} будет отправлено в сервис статистики", request.getRequestURI(),
                request.getRemoteAddr());
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        statClient.hit(app, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
    }
}
