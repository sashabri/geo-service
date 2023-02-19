package ru.netology.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.netology.entity.Country;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public class LocalizationServiceImplTests {

    @ParameterizedTest
    @EnumSource
    public void localeTest(Country country) {

        LocalizationService localizationService = new LocalizationServiceImpl();

        String expectedRu = "Добро пожаловать";
        String expectedEng = "Welcome";

        String actualMessage = localizationService.locale(country);

        if (country == Country.RUSSIA) {
            assertThat(expectedRu,equalToIgnoringCase(actualMessage));
        } else {
            assertThat(expectedEng,equalToIgnoringCase(actualMessage));
        }
    }
}
