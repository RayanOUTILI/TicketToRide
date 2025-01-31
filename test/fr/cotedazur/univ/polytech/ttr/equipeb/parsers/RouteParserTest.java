package fr.cotedazur.univ.polytech.ttr.equipeb.parsers;

import com.fasterxml.jackson.core.type.TypeReference;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.JsonParseException;
import fr.cotedazur.univ.polytech.ttr.equipeb.jsonparsers.BaseParser;
import fr.cotedazur.univ.polytech.ttr.equipeb.jsonparsers.RouteParser;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RouteParserTest {
    private BaseParser baseParser;
    private RouteParser routeParser;

    @BeforeEach
    void setUp() {
        baseParser = mock(BaseParser.class);
        routeParser = new RouteParser();
    }

    @Test
    void parseRoutesSuccessfullyParsesValidJson() throws JsonParseException {
        String filePath = "data-europe/routes.json";
        List<Route> routes = routeParser.parseRoutes(filePath);
        assertEquals(routes.size(), 101);
    }

    @Test
    void parseRoutesThrowsExceptionForInvalidJson() throws JsonParseException {
        String filePath = "invalidRoutes.json";
        when(baseParser.parseJsonFile(filePath, new TypeReference<>() {})).thenThrow(new JsonParseException("Invalid JSON"));

        assertThrows(JsonParseException.class, () -> routeParser.parseRoutes(filePath));
    }

    @Test
    void parseRoutesHandlesEmptyJsonFile() throws JsonParseException {
        String filePath = "emptyRoutes.json";
        List<Route> routes;
        assertThrows(JsonParseException.class, () -> routeParser.parseRoutes(filePath));
    }

    @Test
    void parseRoutesHandlesMissingFieldsInJson() throws JsonParseException {
        String filePath = "missingFieldsRoutes.json";
        List<Map<String, Object>> routeDataList = List.of(
                Map.of("start", "CityA", "end", "CityB", "size", 5, "type", "NORMAL")
        );
        when(baseParser.parseJsonFile(filePath, new TypeReference<>() {})).thenReturn(routeDataList);

        assertThrows(JsonParseException.class, () -> routeParser.parseRoutes(filePath));
    }
}