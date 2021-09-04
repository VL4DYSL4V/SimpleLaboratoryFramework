package framework.option;

import framework.enums.PropertyNames;
import framework.exception.LaboratoryFrameworkException;
import framework.option.dto.OptionDto;
import framework.utils.ValidationUtils;
import lombok.Data;
import lombok.Getter;

import javax.annotation.concurrent.ThreadSafe;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@ThreadSafe
@Getter
public final class OptionHolder {

    private final Map<String, OptionDto> optionDtos;

    public OptionHolder(Properties applicationProperties) {
        Map<String, List<String>> optionNameToListOfKeysForThisOption =
                applicationProperties.stringPropertyNames()
                        .stream()
                        .filter(key -> key.startsWith(PropertyNames.OPTION_PREFIX.getName()))
                        .collect(Collectors.groupingBy(PropertyNames::extractOptionName));
        this.optionDtos = fromOptionNameToListOfKeysForThisOption(optionNameToListOfKeysForThisOption, applicationProperties);
    }

    private Map<String, OptionDto> fromOptionNameToListOfKeysForThisOption(
            Map<String, List<String>> optionNameToListOfKeysForThisOption,
            Properties applicationProperties) {
        Map<String, MutableOptionDto> container = new HashMap<>();
        for (String optionName : optionNameToListOfKeysForThisOption.keySet()) {
            MutableOptionDto dto = new MutableOptionDto();
            container.putIfAbsent(optionName, dto);
            for (String optionKey : optionNameToListOfKeysForThisOption.get(optionName)) {
                String value = applicationProperties.getProperty(optionKey);
                if (optionKey.endsWith(PropertyNames.OPTION_SUFFIX_NAME.getName())) {
                    ValidationUtils.requireNotEmpty(value, "Name must be specified");
                    dto.setName(value);
                } else if (optionKey.endsWith(PropertyNames.OPTION_SUFFIX_ARITY.getName())) {
                    int arity = Integer.parseInt(value);
                    ValidationUtils.requireGreaterOrEqualThanZero(arity, "Arity must be >= 0");
                    dto.setArity(Integer.parseInt(value));
                } else if (optionKey.endsWith(PropertyNames.OPTION_SUFFIX_DESCRIPTION.getName())) {
                    ValidationUtils.requireNotEmpty(value, "Description must be provided");
                    dto.setDescription(value);
                } else if (optionKey.endsWith(PropertyNames.OPTION_SUFFIX_CONSTRAINT_VIOLATION_MESSAGE.getName())) {
                    dto.setConstraintViolationMessage(value);
                } else {
                    throw new LaboratoryFrameworkException(String.format("Unknown key: %s", optionKey));
                }
            }
        }
        Map<String, OptionDto> out = new ConcurrentHashMap<>();
        container.forEach((key, value) -> out.put(key,
                new OptionDto(
                        value.getName(),
                        value.getArity(),
                        value.getDescription(),
                        value.getConstraintViolationMessage())));
        return Collections.unmodifiableMap(out);
    }

    @Data
    private static final class MutableOptionDto {

        private String name;

        private int arity;

        private String description;

        private String constraintViolationMessage;

    }

}
