package com.demo.pteam.authentication.validator;

public interface UniqueCheckStrategy {
    boolean isUnique(String value);
    boolean isValidFormat(String value);
}
