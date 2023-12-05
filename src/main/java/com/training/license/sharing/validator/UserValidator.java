package com.training.license.sharing.validator;

import com.training.license.sharing.services.CredentialsService;
import com.training.license.sharing.services.UserService;
import com.training.license.sharing.util.CustomExceptions.IdNotValidException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;

import static com.training.license.sharing.validator.ErrorMessagesUtil.EMAIL_NOT_UNIQE;
import static com.training.license.sharing.validator.ErrorMessagesUtil.ID_NOT_EXIST_MESSAGE;
import static com.training.license.sharing.util.ErrorKeyUtil.ID_KEY;

@Component
@Log4j2
public class UserValidator {

    private final UserService userService;

    private final CredentialsService credentialsService;

    public UserValidator(UserService userService, CredentialsService credentialsService) {
        this.userService = userService;
        this.credentialsService = credentialsService;
    }

    public void areAllIdsExistingInDB(List<Long> usersIds) {
        final boolean areAllIdPresent = usersIds.stream().noneMatch(id -> userService.getUserById(id).isEmpty());
        if(!areAllIdPresent){
            log.error(ID_NOT_EXIST_MESSAGE);
            throw new IdNotValidException(Map.of(ID_KEY, ID_NOT_EXIST_MESSAGE));
        }
    }

    public void isUserEmailUnique(String email, BindingResult bindingResult) {
        if(credentialsService.findByUsername(email).isPresent()){
            bindingResult.addError(new FieldError("userDTO", "username", EMAIL_NOT_UNIQE));
        }
    }
}