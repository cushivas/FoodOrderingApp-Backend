package com.upgrad.FoodOrderingApp.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import com.upgrad.FoodOrderingApp.api.model.ErrorResponse;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.ItemNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.PaymentMethodNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import com.upgrad.FoodOrderingApp.service.exception.SaveOrderException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;

@ControllerAdvice
public class RestExceptionHandler extends Exception {

	/**
	 *
	 *	 Exception handler for SignUpRestricted
	 * @param exc object contains error code and error message.
	 * @param request The web request object gives access to all the request parameters.
	 * @return ResponseEntity<ErrorResponse> type object
	 */
	@ExceptionHandler(SignUpRestrictedException.class)
	public ResponseEntity<ErrorResponse> signUpRestrictedException(SignUpRestrictedException exc, WebRequest request) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
				HttpStatus.BAD_REQUEST);
	}

	/**
	 * Exception handler for authenticationFailed
	 * @param exc containing error code and error message
	 * @param request web request object
	 * @return ErrorResponse type object
	 */
	@ExceptionHandler(AuthenticationFailedException.class)
	public ResponseEntity<ErrorResponse> authenticationFailedException(AuthenticationFailedException exc,
			WebRequest request) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
				HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Exception handler for AuthorizationFailed
	 * @param exc containing error code and error message
	 * @param request web request object
	 * @return ErrorResponse type object
	 */
	@ExceptionHandler(AuthorizationFailedException.class)
	public ResponseEntity<ErrorResponse> authorizationFailedException(AuthorizationFailedException exc,
			WebRequest request) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
				HttpStatus.FORBIDDEN);
	}

	/**
	 * Exception handler for AddressNotFound
	 * @param exc containing error code and error message
	 * @param request web request object
	 *@return ErrorResponse type object
	 */
	@ExceptionHandler(AddressNotFoundException.class)
	public ResponseEntity<ErrorResponse> addressNotFoundException(AddressNotFoundException exc, WebRequest request) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
				HttpStatus.NOT_FOUND);
	}

	/**
	 * Exception handler for UpdateCustomer
	 * @param exc containing error code and error message
	 * @param request web request object
	 *@return ErrorResponse type object
	 */
	@ExceptionHandler(UpdateCustomerException.class)
	public ResponseEntity<ErrorResponse> updateCustomerException(UpdateCustomerException exc, WebRequest request) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
				HttpStatus.BAD_REQUEST);
	}

	/**
	 * Exception handler for saveAddress
	 * @param exc containing error code and error message
	 * @param request web request object
	 *@return ErrorResponse type object
	 */
	@ExceptionHandler(SaveAddressException.class)
	public ResponseEntity<ErrorResponse> saveAddressException(SaveAddressException exc, WebRequest request) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
				HttpStatus.BAD_REQUEST);
	}

	/**
	 * Exception handler for saveOrder
	 * @param exc containing error code and error message
	 * @param request web request object
	 *@return ErrorResponse type object
	 */
	@ExceptionHandler(SaveOrderException.class)
	public ResponseEntity<ErrorResponse> saveOrderException(SaveOrderException exc, WebRequest request) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
				HttpStatus.BAD_REQUEST);
	}


	/**
	 * Exception  handler for CategoryNotFound
	 * @param exc containing error code and error message
	 * @param request web request object
	 *@return ErrorResponse type object
	 */
	@ExceptionHandler(CategoryNotFoundException.class)
	public ResponseEntity<ErrorResponse> categoryNotFoundException(CategoryNotFoundException exc, WebRequest request) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
				HttpStatus.NOT_FOUND);
	}

	/**
	 * Exception handler for RestaurantNotFound
	 * @param exc containing error code and error message
	 * @param request web request object
	 *@return ErrorResponse type object
	 */

	@ExceptionHandler(RestaurantNotFoundException.class)
	public ResponseEntity<ErrorResponse> restaurantNotFoundException(RestaurantNotFoundException exc,
			WebRequest request) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
				HttpStatus.NOT_FOUND);
	}

	/**
	 * Exception handler for InvalidRating
	 * @param exc containing error code and error message
	 * @param request web request object
	 *@return ErrorResponse type object
	 */

	@ExceptionHandler(InvalidRatingException.class)
	public ResponseEntity<ErrorResponse> invalidRatingException(InvalidRatingException exc, WebRequest request) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
				HttpStatus.BAD_REQUEST);
	}

	/**
	 * Exception handler for CouponNotFound
	 * @param exc containing error code and error message
	 * @param request web request object
	 *@return ErrorResponse type object
	 */

	@ExceptionHandler(CouponNotFoundException.class)
	public ResponseEntity<ErrorResponse> couponNotFoundException(CouponNotFoundException exc, WebRequest request) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
				HttpStatus.NOT_FOUND);
	}

	/**
	 * Exception handler for PaymentMethodNotFound
	 * @param exc containing error code and error message
	 * @param request web request object
	 *@return ErrorResponse type object
	 */

	@ExceptionHandler(PaymentMethodNotFoundException.class)
	public ResponseEntity<ErrorResponse> paymentMethodNotFoundException(PaymentMethodNotFoundException exc,
			WebRequest request) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
				HttpStatus.NOT_FOUND);
	}

	/**
	 * Exception handler for ItemNotFound
	 * @param exc containing error code and error message
	 * @param request web request object
	 *@return ErrorResponse type object
	 */
	@ExceptionHandler(ItemNotFoundException.class)
	public ResponseEntity<ErrorResponse> itemNotFoundException(ItemNotFoundException exc, WebRequest request) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
				HttpStatus.NOT_FOUND);
	}
}