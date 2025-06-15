package com.demoqa.properties.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

public class SystemPropertiesTests {

    @Test
    @Tag("property")
    void systemPropertiesTest() {
        String browser = System.getProperty("browser", "mozilla");
        System.out.println(browser);
        //gradle property_test -Dbrowser=opera
    }
}
