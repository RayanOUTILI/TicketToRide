package fr.cotedazur.univ.polytech.ttr.equipeb.stats;

class GameResultDataWriterTest {
/*
    private GameResultDataWriter gameResultDataWriter;
    private ObjectMapper objectMapper;
    private File file;

    @BeforeEach
    void setUp() {
        gameResultDataWriter = new GameResultDataWriter();
        objectMapper = mock(ObjectMapper.class);
        GameResultDataWriter.objectMapper = objectMapper;
        file = mock(File.class);
    }

    @Test
    void saveGameResultSuccessfully() throws IOException {
        List<GameResultWrapper> gameResults = new ArrayList<>();
        when(file.exists()).thenReturn(true);
        when(file.length()).thenReturn(100L);
        when(objectMapper.readValue(file, new TypeReference<List<GameResultWrapper>>() {})).thenReturn(gameResults);

        gameResultDataWriter.saveGameResult(gameResult);

        gameResults.add(gameResult);
        verify(objectMapper).writeValue(file, gameResults);
    }

    @Test
    void saveGameResultFileNotFound() throws IOException {
        GameResultWrapper gameResult = new GameResultWrapper();
        when(file.exists()).thenReturn(false);

        gameResultDataWriter.saveGameResult(gameResult);

        List<GameResultWrapper> gameResults = new ArrayList<>();
        gameResults.add(gameResult);
        verify(objectMapper).writeValue(file, gameResults);
    }

    @Test
    void saveGameResultWithSingleResult() throws IOException {
        GameResultWrapper gameResult = new GameResultWrapper();
        GameResultWrapper singleResult = new GameResultWrapper();
        when(file.exists()).thenReturn(true);
        when(file.length()).thenReturn(100L);
        when(objectMapper.readValue(file, new TypeReference<List<GameResultWrapper>>() {})).thenThrow(new IOException());
        when(objectMapper.readValue(file, GameResultWrapper.class)).thenReturn(singleResult);

        gameResultDataWriter.saveGameResult(gameResult);

        List<GameResultWrapper> gameResults = new ArrayList<>();
        gameResults.add(singleResult);
        gameResults.add(gameResult);
        verify(objectMapper).writeValue(file, gameResults);
    }

    @Test
    void saveGameResultIOException() throws IOException {
        GameResultWrapper gameResult = new GameResultWrapper();
        when(file.exists()).thenReturn(true);
        when(file.length()).thenReturn(100L);
        when(objectMapper.readValue(file, new TypeReference<List<GameResultWrapper>>() {})).thenThrow(new IOException());

        gameResultDataWriter.saveGameResult(gameResult);

        List<GameResultWrapper> gameResults = new ArrayList<>();
        gameResults.add(gameResult);
        verify(objectMapper).writeValue(file, gameResults);
    }*/
}

