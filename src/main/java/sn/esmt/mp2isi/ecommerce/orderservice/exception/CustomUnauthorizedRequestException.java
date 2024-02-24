package sn.esmt.mp2isi.ecommerce.orderservice.exception;

import lombok.Getter;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomUnauthorizedRequestException extends AbstractThrowableProblem {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String PROBLEM_BASE_URL = "https://www.jhipster.tech/problem";

    private final String entityName;

    private final String errorKey;

    public CustomUnauthorizedRequestException(String title, String detail) {
        super(
            URI.create(PROBLEM_BASE_URL + "/problem-with-message"),
            title == null ? "Requête non autorisée " : title,
            Status.UNAUTHORIZED,
            detail,
            null,
            null,
            getAlertParameters(detail)
        );
        this.entityName = "ORDER_SERVICE";
        this.errorKey = "UNAUTHORIZED";
    }

    public CustomUnauthorizedRequestException(String detail) {
        this(null, detail);
    }

    private static Map<String, Object> getAlertParameters(String detail) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", detail);
        parameters.put("params", "ORDER_SERVICE");
        return parameters;
    }
}
