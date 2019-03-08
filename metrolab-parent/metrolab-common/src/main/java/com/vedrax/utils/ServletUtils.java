package com.vedrax.utils;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.Validate;

/**
 *
 * @author remypenchenat
 */
public class ServletUtils {

    /**
     * Get header from request
     *
     * @param request
     * @param headerName
     * @return
     */
    public static Optional<String> getHeader(HttpServletRequest request, String headerName) {
        Validate.notNull(request, "A HttpServletRequest must be provided");
        Validate.notNull(headerName, "A headerName must be provided");
        
        String header = request.getHeader(headerName);
        return Optional
                .ofNullable(header);
    }

}
