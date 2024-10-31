package backend.academy.analyzer.log;

import java.time.Instant;
import lombok.Builder;

@Builder
public record LogRecord(

    String remoteAddr,
    String remoteUser,
    Instant timeLocal,
    String request,
    int status,
    long bodyBytesSent,
    String httpReferer,
    String httpUserAgent

) {
}
