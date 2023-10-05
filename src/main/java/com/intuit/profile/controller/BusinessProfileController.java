package com.intuit.profile.controller;

import com.intuit.common.entity.ProfileUpdateRequest;
import com.intuit.common.entity.ProfileUpdateResponse;
import com.intuit.profile.entity.BusinessProfile;
import com.intuit.profile.exception.BusinessProfileNotValidException;
import com.intuit.profile.exception.UserNotSubscribedToProductsException;
import com.intuit.profile.service.BusinessProfileService;
import com.intuit.profile.service.ValidateBusinessProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/businessProfile")
@CrossOrigin(origins = "http://localhost:4200")
public class BusinessProfileController {

    private static final Logger logger = LoggerFactory.getLogger(BusinessProfileController.class);

    @Autowired
    private BusinessProfileService businessProfileService;

    @Autowired
    private ValidateBusinessProfileService validateBusinessProfileService;


    /**
     * Accepts Profile Update Request, validate it , generate request Id
     * @return ProfileUpdateResponse
     */
    @PostMapping("/acceptRequest/user/{userId}")
    public ResponseEntity<ProfileUpdateResponse> acceptRequest(@PathVariable("userId") String userId,
                                @RequestBody BusinessProfile businessProfile) {

        logger.info("Profile Update Request received for user {}", userId);
        if (validateBusinessProfileService.validate(businessProfile)) {
            String requestId = businessProfileService.acceptRequest(userId,businessProfile);
            ProfileUpdateResponse response = new ProfileUpdateResponse(requestId);

            logger.info("Profile Update Request Id for user {} is {}", userId, response.getRequestId());
            return ResponseEntity.ok(response);
        } else {
            //Although it wont reach here
            ProfileUpdateResponse response = new ProfileUpdateResponse("");
            return ResponseEntity.ok(response);
        }

    }

    /**
     * Retrieves BusinessProfile for user Id and company Name
     * @return BusinessProfile
     */
    @GetMapping("/user/{userId}/company/{companyName}")
    public ResponseEntity<BusinessProfile> getBusinessProfile(@PathVariable("userId") String userId,
                                              @PathVariable("companyName") String companyName) {

        logger.info("Profile Retrieve Request received for user {}", userId);
        return ResponseEntity.ok(businessProfileService.getBusinessProfile(userId, companyName));
    }

    /**
     * Retrieves status of request id and user id
     * @return ProfileUpdateRequest
     */
    @GetMapping("getStatus/user/{userId}/request/{requestId}")
    public ResponseEntity<ProfileUpdateRequest> getStatus(@PathVariable("userId") String userId ,
                                          @PathVariable("requestId") String requestId) {

        logger.info("Get Status Request received for user {} and request id {}", userId, requestId);
        return ResponseEntity.ok(businessProfileService.getStatus(userId, requestId));
    }

    /**
     * Retrieves status of All request for user id
     * @return List<ProfileUpdateRequest>
     */
    @GetMapping("getAllRequestStatus/user/{userId}")
    public ResponseEntity<List<ProfileUpdateRequest>> getAllRequestStatus(@PathVariable("userId") String userId) {

        logger.info("Get All request status request received for user {}", userId);
        return ResponseEntity.ok(businessProfileService.getAllRequestStatus(userId));
    }

    /**
     * Exception Handler for UserNotSubscribedToProductsException
     * @return String
     */
    @ExceptionHandler(UserNotSubscribedToProductsException.class)
    public ResponseEntity<String> handleUserNotSubscribedToProductsException(UserNotSubscribedToProductsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Exception Handler for BusinessProfileNotValidException
     * @return String
     */
    @ExceptionHandler(BusinessProfileNotValidException.class)
    public ResponseEntity<String> handleBusinessProfileNotValidException(BusinessProfileNotValidException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
