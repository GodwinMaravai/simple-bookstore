package be.kata.api;

import be.kata.api.model.User;
import be.kata.persistence.user.UserEntity;
import be.kata.persistence.user.UserRepository;
import be.kata.persistence.user.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.datasource.url=jdbc:h2:mem:testdb",
                "spring.jpa.hibernate.ddl-auto=create-drop"
        })
class UserControllerTest {

    private MockMvc mvc;
    private UserEntity userEntity;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method
        userEntity = new UserEntity();
        userEntity.setName("user1");
        userEntity.setNrn("12345678902");
        userEntity.setPassword("password");
        userEntity.setRole(UserRole.ADMIN);
        userRepository.save(userEntity);

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        userRepository.delete(userEntity);
    }

    @Test
    void givenNoUser_whenLogin_thenReturnWithStatusCode401() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/login"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user1", roles = "ADMIN")
    void givenAdminUser_whenLogin_thenReturnWithStatusCode200() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/login"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void givenUser_whenRegister_thenReturnWithStatusCode201() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User("user2", "pass", "12345678901", UserRole.USER);
        mvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void givenAdmin_whenRegister_thenReturnWithStatusCode201() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User("adminuser", "pass", "12345678901", UserRole.ADMIN);
        mvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}
