package org.example.repository;

import org.example.model.Game;
import org.junit.*;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.Instant;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class GameRepositoryImplTest {

    private static Connection connection;
    private static  GameRepositoryImpl gameRepository;

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Set up the H2 in-memory database
        connection = DriverManager.getConnection("jdbc:h2:mem:test;MODE=MySQL;DB_CLOSE_DELAY=-1");
        executeScript("init_db");
        gameRepository = new GameRepositoryImpl(connection);
    }

    private static void executeScript(String script) throws Exception {
        URI uri = GameRepositoryImplTest.class.getClassLoader().getResource("sql/" + script + ".sql").toURI();
        String content = Files.lines(Paths.get(uri)).collect(Collectors.joining("\n"));
        System.out.println("Executing script:");
        System.out.println(content);
        try (Statement statement = connection.createStatement()) {
            statement.execute(content);
        }
    }

    @Before
    public void setUp() throws Exception {
        executeScript("init_data");
    }

    @After
    public void tearDown() throws Exception {
        executeScript("delete_data");
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        // Close the database connection
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void saveAndGetById() {
        Game newGame = Game.builder()
                .name("name")
                .releaseDate(Date.valueOf("2000-11-12"))
                .rating(2.4)
                .cost(31.99)
                .description("some desc")
                .build();

        int savedId = gameRepository.save(newGame).getId();

        Game fromDb = gameRepository.get(savedId);

        assertEquals(newGame, fromDb);

    }

    @Test
    public void buyGame() {
        // todo
    }

}