package eu.herble.herbleapi.errors;

import org.springframework.http.HttpStatus;

public interface ApiErrorType {
    ApiError USER_NOT_FOUND = new ApiError("User not found", HttpStatus.NOT_FOUND);

}
