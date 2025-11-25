package com.mercadolibre.mutant.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DnaValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDna {
    String message() default "La secuencia de ADN no es válida (debe ser NxN, no nula y solo caracteres A, T, C, G)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}