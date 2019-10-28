package com.mykid.platform.endpoint;

import com.mykid.platform.common.annotation.PlatformEndPoint;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;

import java.util.List;

/**
 * @author MrBird
 */
@PlatformEndPoint
public class PlatformHttpTraceEndpoint {

    private final HttpTraceRepository repository;

    public PlatformHttpTraceEndpoint(HttpTraceRepository repository) {
        this.repository = repository;
    }

    public PlatformHttpTraceDescriptor traces() {
        return new PlatformHttpTraceDescriptor(this.repository.findAll());
    }

    public static final class PlatformHttpTraceDescriptor {

        private final List<HttpTrace> traces;

        private PlatformHttpTraceDescriptor(List<HttpTrace> traces) {
            this.traces = traces;
        }

        public List<HttpTrace> getTraces() {
            return this.traces;
        }
    }
}
