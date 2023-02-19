package ru.netology.geo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static ru.netology.geo.GeoServiceImpl.*;

public class GeoServiceImplTests {

        GeoService geoService = new GeoServiceImpl();

    private static class TestData {

        Location locationExpected;
        String ip;

        public TestData(Location locationExpected, String ip) {
            this.locationExpected = locationExpected;
            this.ip = ip;
        }
    }

    public static List<TestData> source() {
        List<TestData> result = new ArrayList<TestData>();

        result.add(
                new TestData(new Location(null, null, null, 0), LOCALHOST)
        );
        result.add(
                new TestData(new Location("Moscow", Country.RUSSIA, "Lenina", 15), MOSCOW_IP)
        );
        result.add(
                new TestData(new Location("New York", Country.USA, " 10th Avenue", 32), NEW_YORK_IP)
        );
        result.add(
                new TestData(new Location("Moscow", Country.RUSSIA, null, 0), "172.230.65.98")
        );
        result.add(
                new TestData(new Location("New York", Country.USA, null,  0), "96.16.179.45")
        );

        result.add(
                new TestData(null, "56.45.89.215")
        );

        return result;
    }

    @ParameterizedTest
    @MethodSource("source")
    public void byIpTest(TestData testData) {

        Location actualLocation = geoService.byIp(testData.ip);

       if (testData.locationExpected != null) {
           assertThat(testData.locationExpected,samePropertyValuesAs(actualLocation));
       } else {
           Assertions.assertNull(actualLocation);
       }
    }

    @Test
    public void byCoordinatesTest() {

        Class<RuntimeException> expected = RuntimeException.class;

        Executable executable = () -> geoService.byCoordinates(2.45, 3.15);

        Assertions.assertThrowsExactly(expected, executable);
    }
}
