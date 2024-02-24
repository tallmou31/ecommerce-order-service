package sn.esmt.mp2isi.ecommerce.orderservice.exception;

import lombok.Getter;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ItemNotFoundException extends AbstractThrowableProblem {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final String PROBLEM_BASE_URL = "https://www.jhipster.tech/problem";

    private final String entityName;

    public ItemNotFoundException(String defaultMessage, String entityName) {
        super(
            URI.create(PROBLEM_BASE_URL + "/problem-with-message"),
            defaultMessage,
            Status.NOT_FOUND,
            null,
            null,
            null,
            getAlertParameters(entityName)
        );
        this.entityName = entityName;
    }

    private static Map<String, Object> getAlertParameters(String entityName) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", "error.NOTFOUND");
        parameters.put("params", entityName);
        return parameters;
    }
}
