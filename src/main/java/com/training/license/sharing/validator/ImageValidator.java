package com.training.license.sharing.validator;

import com.training.license.sharing.util.CustomExceptions.ImageHasIllegalTypeException;
import com.training.license.sharing.util.CustomExceptions.ImageSizeOutOfBoundsException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Map;
import java.util.function.Predicate;

import static com.training.license.sharing.validator.ErrorMessagesUtil.IMAGE_INCORRECT_SIZE_MESSAGE;
import static com.training.license.sharing.validator.ErrorMessagesUtil.IMAGE_INCORRECT_TYPE_MESSAGE;
import static java.util.Objects.isNull;

@Component
@Log4j2
public class ImageValidator {
    private static final Map<String, Predicate<byte[]>> IMAGE_TYPES = Map.of(
            "PNG", imageData -> matchesSignature(imageData, new byte[]{(byte) 0x89, 80, 78, 71, 13, 10, 26, 10}),
            "JPEG", imageData -> matchesSignature(imageData, new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF})
    );

    public void logoImageTypeValidation(String logo) {
        byte[] imageBytes = Base64.getDecoder().decode(logo);
        String type = getImageType(imageBytes);
        if (isNull(type)){
            log.error(IMAGE_INCORRECT_TYPE_MESSAGE);
            throw new ImageHasIllegalTypeException(IMAGE_INCORRECT_TYPE_MESSAGE);
        }
    }

    public void logoImageSizeValidation(String logo) {
        byte[] imageBytes = Base64.getDecoder().decode(logo);
        double sizeOfImageInMB = (double) imageBytes.length / (1024 * 1024);
        if (sizeOfImageInMB < 2 || sizeOfImageInMB > 10){
            log.error(IMAGE_INCORRECT_SIZE_MESSAGE);
            throw new ImageSizeOutOfBoundsException(IMAGE_INCORRECT_SIZE_MESSAGE);
        }
    }

    private String getImageType(byte[] imageData) {
        return IMAGE_TYPES.entrySet().stream()
                .filter(entry -> entry.getValue().test(imageData))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    private static boolean matchesSignature(byte[] imageData, byte[] signature) {
        if (imageData.length >= signature.length) {
            for (int i = 0; i < signature.length; i++) {
                if (imageData[i] != signature[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
