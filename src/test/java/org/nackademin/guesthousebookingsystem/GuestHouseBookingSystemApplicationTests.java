package org.nackademin.guesthousebookingsystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"test", "local"})
class GuestHouseBookingSystemApplicationTests {

    @Test
    void contextLoads() {
    }

}
