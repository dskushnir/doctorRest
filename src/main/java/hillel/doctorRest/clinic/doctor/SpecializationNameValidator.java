package hillel.doctorRest.clinic.doctor;
import hillel.doctorRest.clinic.SpecializationConfig;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SpecializationNameValidator implements ConstraintValidator<SpecializationName,String> {
    private SpecializationConfig specializationConfig;

    public SpecializationNameValidator(SpecializationConfig specializationConfig) {
        this.specializationConfig = specializationConfig;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value==null || doSpecializationNameCheck(value, specializationConfig);
    }
    public boolean doSpecializationNameCheck(String specializationName, SpecializationConfig specializationConfig){
        return specializationConfig.getSpecializationName().contains(specializationName);
    }
}
