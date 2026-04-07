package ro.unibuc.prodeng.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ro.unibuc.prodeng.IntegrationTestBase;
import ro.unibuc.prodeng.model.CarEntity;
import ro.unibuc.prodeng.model.ClientEntity;
import ro.unibuc.prodeng.model.OrderStatus;
import ro.unibuc.prodeng.model.PartEntity;
import ro.unibuc.prodeng.model.ServiceOrderEntity;
import ro.unibuc.prodeng.model.SupplierEntity;
import ro.unibuc.prodeng.model.MechanicEntity;
import ro.unibuc.prodeng.repository.CarRepository;
import ro.unibuc.prodeng.repository.ClientRepository;
import ro.unibuc.prodeng.repository.MechanicRepository;
import ro.unibuc.prodeng.repository.PartRepository;
import ro.unibuc.prodeng.repository.ServiceAppointmentRepository;
import ro.unibuc.prodeng.repository.ServiceOrderRepository;
import ro.unibuc.prodeng.repository.SupplierRepository;

@DisplayName("Auto Service Integration Tests")
class AutoServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private MechanicRepository mechanicRepository;

    @Autowired
    private SupplierRepository supplierRepository;

        @Autowired
        private ServiceAppointmentRepository serviceAppointmentRepository;

    @BeforeEach
    void cleanUp() {
                serviceAppointmentRepository.deleteAll();
        serviceOrderRepository.deleteAll();
        partRepository.deleteAll();
        carRepository.deleteAll();
        clientRepository.deleteAll();
        mechanicRepository.deleteAll();
        supplierRepository.deleteAll();
    }

    @Test
    void createServiceOrder_shouldDeductPartStockAndPersistOrder() throws Exception {
        seedCatalog(10);

        String createOrderPayload = """
                {
                  "carId": "car-1",
                  "mechanicId": "mechanic-1",
                  "serviceName": "Revizie completa",
                  "description": "Schimb ulei si filtru",
                  "laborCost": 200.0,
                  "scheduledAt": "2026-03-05T10:00:00",
                  "requiredParts": [
                    {
                      "partId": "part-1",
                      "quantity": 2
                    }
                  ]
                }
                """;

        String responseBody = mockMvc.perform(post("/api/service-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createOrderPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.partsCost").value(90))
                .andExpect(jsonPath("$.totalCost").value(290))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String orderId = objectMapper.readTree(responseBody).get("id").asText();

        PartEntity updatedPart = partRepository.findById("part-1").orElseThrow();
        assertEquals(8, updatedPart.availableStock());

        ServiceOrderEntity savedOrder = serviceOrderRepository.findById(orderId).orElseThrow();
        assertEquals(OrderStatus.IN_PROGRESS, savedOrder.status());
        assertEquals(BigDecimal.valueOf(290.0), savedOrder.totalCost());
        assertEquals(1, savedOrder.requiredParts().size());
        assertEquals("part-1", savedOrder.requiredParts().getFirst().partId());
    }

    @Test
    void receiveDelivery_shouldIncreasePartStockAndPersist() throws Exception {
        seedCatalog(5);

        String deliveryPayload = """
                {
                  "partId": "part-1",
                  "supplierId": "supplier-1",
                  "quantity": 7,
                  "deliveredAt": "2026-03-05T08:30:00"
                }
                """;

        mockMvc.perform(patch("/api/deliveries/receive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deliveryPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("part-1"))
                .andExpect(jsonPath("$.availableStock").value(12));

        PartEntity updatedPart = partRepository.findById("part-1").orElseThrow();
        assertEquals(12, updatedPart.availableStock());
    }

    @Test
    void createServiceOrder_whenStockInsufficient_shouldReturnBadRequestWithoutChangingStock() throws Exception {
        seedCatalog(1);

        String createOrderPayload = """
                {
                  "carId": "car-1",
                  "mechanicId": "mechanic-1",
                  "serviceName": "Revizie completa",
                  "description": "Schimb ulei si filtru",
                  "laborCost": 200.0,
                  "scheduledAt": "2026-03-05T10:00:00",
                  "requiredParts": [
                    {
                      "partId": "part-1",
                      "quantity": 2
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/api/service-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createOrderPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());

        PartEntity unchangedPart = partRepository.findById("part-1").orElseThrow();
        assertEquals(1, unchangedPart.availableStock());
        assertTrue(serviceOrderRepository.findAll().isEmpty());
    }

    @Test
    void createAppointment_andGetByMechanic_shouldWork() throws Exception {
        seedCatalog(10);

        String appointmentPayload = """
                {
                  "carId": "car-1",
                  "mechanicId": "mechanic-1",
                  "scheduledAt": "2027-01-10T10:30:00",
                                                                        "reasons": ["ENGINE_DIAGNOSTIC", "OIL_CHANGE"]
                }
                """;

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(appointmentPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.carId").value("car-1"))
                .andExpect(jsonPath("$.mechanicId").value("mechanic-1"))
                .andExpect(jsonPath("$.reasons[0]").value("ENGINE_DIAGNOSTIC"))
                .andExpect(jsonPath("$.reasons[1]").value("OIL_CHANGE"))
                .andExpect(jsonPath("$.estimatedDurationMinutes").value(105))
                .andExpect(jsonPath("$.estimatedEndAt").value("2027-01-10T12:15:00"));

        mockMvc.perform(get("/api/appointments/by-mechanic/mechanic-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].carId").value("car-1"))
                .andExpect(jsonPath("$[0].mechanicId").value("mechanic-1"));
    }

    private void seedCatalog(int partStock) {
        clientRepository.save(new ClientEntity(
                "client-1",
                "Mihai",
                "Ionescu",
                "0722111222",
                "mihai@example.com",
                "Bucuresti"
        ));

        carRepository.save(new CarEntity(
                "car-1",
                "Dacia",
                "Duster",
                2021,
                "B-42-XYZ",
                "client-1"
        ));

        mechanicRepository.save(new MechanicEntity(
                "mechanic-1",
                "Andrei",
                "Pop",
                "0733222111"
        ));

        supplierRepository.save(new SupplierEntity(
                "supplier-1",
                "Auto Parts Distribution",
                "Bd. Timisoara 8",
                "0215552233"
        ));

        partRepository.save(new PartEntity(
                "part-1",
                "Filtru ulei",
                partStock,
                BigDecimal.valueOf(45.0),
                "supplier-1"
        ));
    }
}
