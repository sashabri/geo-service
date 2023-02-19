package ru.netology.sender;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageSenderImplTests {

   private static final String MESSAGE_FOR_RU = "RU";
   private static final String MESSAGE_FOR_USA = "USA";
   private static final String MESSAGE_FOR_GE = "GE";
   private static final String MESSAGE_FOR_BR = "BR";

   static  LocalizationService localizationService = Mockito.mock(LocalizationService.class);

   @BeforeAll
   public static void before() {
      Mockito.when(localizationService.locale(Mockito.<Country>eq(Country.RUSSIA)))
              .thenReturn(MESSAGE_FOR_RU);
      Mockito.when(localizationService.locale(Mockito.<Country>eq(Country.BRAZIL)))
              .thenReturn(MESSAGE_FOR_BR);
      Mockito.when(localizationService.locale(Mockito.<Country>eq(Country.USA)))
              .thenReturn(MESSAGE_FOR_USA);
      Mockito.when(localizationService.locale(Mockito.<Country>eq(Country.GERMANY)))
              .thenReturn(MESSAGE_FOR_GE);
   }

   static class TestData {

      String ip;
      Country country;
      String expected;

      private TestData(String ip, Country country, String expected) {
         this.ip = ip;
         this.country = country;
         this.expected = expected;
      }
   }

   public static List<TestData> source() {
      List<TestData> result = new ArrayList<TestData>();

      result.add(
        new TestData("226.156.2.56", Country.RUSSIA, MESSAGE_FOR_RU)
      );
      result.add(
              new TestData("226.156.2.56", Country.USA, MESSAGE_FOR_USA)
      );
      result.add(
              new TestData("226.156.2.56", Country.BRAZIL, MESSAGE_FOR_BR)
      );
      result.add(
              new TestData("226.156.2.56", Country.GERMANY, MESSAGE_FOR_GE)
      );
      result.add(
              new TestData("", Country.GERMANY, MESSAGE_FOR_USA)
      );

      return result;
   }



   @ParameterizedTest
   @MethodSource("source")
   public void sendTests(TestData testData) {
      GeoService geoService = Mockito.mock(GeoService.class);
      Mockito.when(geoService.byIp(Mockito.<String>any()))
              .thenReturn(new Location(null, testData.country, null, 0));

      MessageSender sut = new MessageSenderImpl(geoService,localizationService);

      Map<String, String> headers = new HashMap<String, String>();
      headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, testData.ip);

      String actual =  sut.send(headers);

      if (testData.ip != null && !testData.ip.isEmpty()) {
         Mockito.verify(geoService, Mockito.times(1)).byIp(testData.ip);
      } else {
         Mockito.verify(geoService, Mockito.times(0)).byIp(Mockito.<String>any());
      }

      Assertions.assertEquals(testData.expected, actual);
   }

}
